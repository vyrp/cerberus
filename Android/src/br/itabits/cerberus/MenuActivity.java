package br.itabits.cerberus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import br.itabits.cerberus.borrow.BorrowReturnActivity;
import br.itabits.cerberus.borrow.BorrowTableActivity;
import br.itabits.cerberus.database.DataBaseManager;
import br.itabits.cerberus.login.LoginActivity;

public class MenuActivity extends Activity {

	private static final int AVAIABLE = 0;
	private static final int BORROWED = 1;
	private static final int UNREGISTERED = 0;
	private static final int REGISTERED = 1;
	private SharedPreferences sharedPref;
	private Integer borrowState;
	private Integer registerState;
	private Button borrowReturn;

	private String mEmail;
	private static final String SERVER = "http://itabitscerberus.appspot.com/";
	public static final String DEVICE_NAME = "ITAbits_" + android.os.Build.MODEL + "_" + android.os.Build.MANUFACTURER;
	private static final String DEVICE_REGISTERED_STATE = "br.itabits.cerberus.registered";
	DataBaseManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		mEmail = getIntent().getStringExtra(LoginActivity.EXTRA_EMAIL);

		sharedPref = getPreferences(Context.MODE_PRIVATE);
		
		TextView title = (TextView) findViewById(R.id.greetings);
		title.setText(title.getText().toString().replace("username", mEmail));

		borrowReturn = (Button) findViewById(R.id.button_borrow);
		loadStates();

		borrowReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				updateBorrowState();
				
				ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
				if (networkInfo != null && networkInfo.isConnected()) {
					new createUpdateTask().execute(mEmail);
				} else {
					// TODO
				}
			}
		});

		Button viewPastButton = (Button) findViewById(R.id.button_view_past);
		viewPastButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent openViewPast = new Intent(MenuActivity.this, BorrowTableActivity.class);
				startActivity(openViewPast);
			}
		});
		
		Button exitButton = (Button) findViewById(R.id.button_quit);
		exitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
                System.exit(0);
            }
        });
	}

	private void loadStates() {
		borrowState = sharedPref.getInt(getString(R.string.saved_borrow_state), AVAIABLE);
		if (borrowState.equals(AVAIABLE)) {
			borrowReturn.setText(R.string.borrow_button);
		} else {
			borrowReturn.setText(R.string.return_button);
		}
		registerState = sharedPref.getInt(DEVICE_REGISTERED_STATE, UNREGISTERED);
		if (registerState.equals(UNREGISTERED)) {
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				new createDeviceTask().execute();
			}
		}
	}

	private void updateBorrowState() {
		if (borrowState.equals(AVAIABLE)) {
			borrowState = BORROWED;
			borrowReturn.setText(R.string.return_button);
		} else {
			borrowState = AVAIABLE;
			borrowReturn.setText(R.string.borrow_button);
		}
		return;
	}

	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferences.Editor editor = sharedPref.edit();
		if (borrowState.equals(AVAIABLE)) {
			editor.putInt(getString(R.string.saved_borrow_state), AVAIABLE);
		} else {
			editor.putInt(getString(R.string.saved_borrow_state), BORROWED);
		}
		editor.commit();
	}

	// Uses AsyncTask to create a task away from the main UI thread.
	// This task makes a put on the server depending on the state of the tablet
	private class createUpdateTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... name) {
			try {
				DataBaseManager manager = new DataBaseManager(SERVER, DEVICE_NAME);
				
				if(borrowState.equals(BORROWED)){
					try {
			            manager.put(name[0]);
			            System.out.println("Created.");
			            return true;
			        } catch(ConnectException e){
			            System.out.println("\n" + e.getMessage());
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
				} else {
					String result = null;
			        
			        try {
			            result = manager.update();
			            if(result!=null)
			            	return true;
			        } catch(ConnectException e){
			            System.out.println("\n" + e.getMessage());
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			        
			        return false;
				}
				return false;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(final Boolean success) {
			if(success){
				Intent openBorrowReturn = new Intent(MenuActivity.this, BorrowReturnActivity.class);
				String title = (borrowState.equals(AVAIABLE)) ? "Returned" : "Borrowed";
				openBorrowReturn.putExtra(getString(R.string.title_activity_borrow_return), title);
				openBorrowReturn.putExtra(LoginActivity.EXTRA_EMAIL, mEmail);
				startActivity(openBorrowReturn);	
			}
		}
	}
	
	// Uses AsyncTask to create a task away from the main UI thread.
	// This task makes a put on the server depending on the state of the tablet
	private class createDeviceTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				DataBaseManager manager = new DataBaseManager(SERVER, DEVICE_NAME);
				
				try {
		            manager.createDevice(DEVICE_NAME);
		        } catch(ConnectException e){
		            System.out.println("\n" + e.getMessage());
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				
				return true;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(final Boolean success) {
			if(success){
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putInt(DEVICE_REGISTERED_STATE, REGISTERED);
				editor.commit();
			}
		}
	}
}
