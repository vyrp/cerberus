package br.itabits.cerberus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import br.itabits.cerberus.borrow.BorrowReturnActivity;
import br.itabits.cerberus.borrow.BorrowTableActivity;
import br.itabits.cerberus.database.DataBaseManager;
import br.itabits.cerberus.login.LoginActivity;
import br.itabits.cerberus.util.ErrorDialogMessage;

public class MenuActivity extends Activity {

	/* * Constants * */

	public static final String DEVICE_NAME = "ITAbits_" + android.os.Build.MODEL + "_" + android.os.Build.MANUFACTURER;
	public static final String SERVER = "http://itabitscerberus.appspot.com/";
	public static final String DEVICE_REGISTERED_STATE = "br.itabits.cerberus.registered";
	public static final String AVAILABLE_BORROWED_STATE = "br.itabits.cerberus.borrow";
	public static final String LAST_USER = "br.itabits.cerberus.last_user";

	public static final int AVAILABLE = 0;
	public static final int BORROWED = 1;
	private static final int UNREGISTERED = 0;
	private static final int REGISTERED = 1;

	/* * Fields * */

	private SharedPreferences sharedPref;
	private Integer borrowState;
	private Integer registerState;
	private Button borrowReturn;

	private View menuView;
	private View menuStatusView;

	private String userID;

	DataBaseManager manager;

	/* * Activity Methods * */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		sharedPref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

		menuStatusView = findViewById(R.id.borrow_menu_status);
		menuView = findViewById(R.id.borrow_return_menu);

		// The email is the user ID for the operations of borrow and
		// return
		// if the user connects with his facebook account the userID
		// will be his
		// name
		userID = getIntent().getStringExtra(LoginActivity.EXTRA_USER_ID);
		if (userID == null) {
			userID = sharedPref.getString(LAST_USER, "unknow");
		}

		TextView title = (TextView) findViewById(R.id.greetings);
		title.setText(title.getText().toString().replace("username", userID));

		// Registering listeners for the buttons
		borrowReturn = (Button) findViewById(R.id.button_borrow);
		borrowReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				borrowReturn.setClickable(false);
				showProgress(true);

				ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
				if (networkInfo != null && networkInfo.isConnected()) {
					new createUpdateTask().execute(userID);
				} else {
					ErrorDialogMessage.show(getParent(), "User not found");
				}
			}
		});
		Button viewPastButton = (Button) findViewById(R.id.button_view_past);
		viewPastButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// A table with all the past borrowings
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

	/**
	 * read from preferences file the borrow and registered state
	 */
	@Override
	protected void onStart() {
		super.onStart();
		borrowState = sharedPref.getInt(getString(R.string.saved_borrow_state), AVAILABLE);
		if (borrowState.equals(AVAILABLE)) {
			borrowReturn.setText(R.string.borrow_button);
		} else {
			borrowReturn.setText(R.string.return_button);
		}
		registerState = sharedPref.getInt(DEVICE_REGISTERED_STATE, UNREGISTERED);
		if (registerState.equals(UNREGISTERED)) {
			// and if it is the first time using the application,
			// creates a table at the server with this device name
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				new createDeviceTask().execute();
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		// every time the activity stops, save the states
		SharedPreferences.Editor editor = sharedPref.edit();
		if (borrowState.equals(AVAILABLE)) {
			editor.putInt(getString(R.string.saved_borrow_state), AVAILABLE);
		} else {
			editor.putInt(getString(R.string.saved_borrow_state), BORROWED);
			editor.putString(LAST_USER, userID);
		}
		editor.commit();
	}

	// Private Methods

	/**
	 * changes borrow state and the button UI action
	 */
	private void updateBorrowState() {
		if (borrowState.equals(AVAILABLE)) {
			borrowState = BORROWED;
			borrowReturn.setText("return");
		} else {
			borrowState = AVAILABLE;
			borrowReturn.setText("borrow");
		}
		return;
	}

	/**
	 * Shows the progress UI and hides the table.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs,
		// which allow
		// for very easy animations. If available, use these APIs to
		// fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			menuStatusView.setVisibility(View.VISIBLE);
			menuStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							menuStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});

			menuView.setVisibility(View.VISIBLE);
			menuView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							menuView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so
			// simply show
			// and hide the relevant UI components.
			menuStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			menuView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/* * * * AsyncTasks * * * */

	// Uses AsyncTask to create a task away from the main UI thread.
	// This task makes a put on the server depending on the state of
	// the device
	private class createUpdateTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... name) {
			// try a connection with de DB and
			// - says that someone borrowed the mobile or
			// - that the mobile was returned
			try {
				DataBaseManager manager = new DataBaseManager(SERVER, DEVICE_NAME);

				if (borrowState.equals(AVAILABLE)) {
					try {
						manager.put(name[0]);
						return true;
					} catch (ConnectException e) {
						ErrorDialogMessage.show(getParent(), "Connection failed. Check your network and try again.");
					} catch (IOException e) {
						ErrorDialogMessage.show(getParent(), "Connection failed.");
					}
				} else {
					String result = null;

					try {
						result = manager.update();
						if (result != null)
							return true;
					} catch (ConnectException e) {
						ErrorDialogMessage.show(getParent(), "Connection failed. Check your network and try again.");
					} catch (IOException e) {
						ErrorDialogMessage.show(getParent(),
								"Sorry, your message was not successful delivered. Try again.");
					}

					return false;
				}
				return false;
			} catch (UnsupportedEncodingException e) {
				ErrorDialogMessage.show(getParent(), "We're terrible sorry. This shouldn't have happened. :(");
			}
			return null;
		}

		// on success changes the button action
		// and leads to a confirmation screen
		@Override
		protected void onPostExecute(final Boolean success) {
			showProgress(false);
			borrowReturn.setClickable(true);

			if (success) {
				updateBorrowState();

				Intent openBorrowReturn = new Intent(MenuActivity.this, BorrowReturnActivity.class);
				String title = (borrowState.equals(AVAILABLE)) ? "Returned" : "Borrowed";
				openBorrowReturn.putExtra(AVAILABLE_BORROWED_STATE, title);
				openBorrowReturn.putExtra(LoginActivity.EXTRA_USER_ID, userID);
				startActivity(openBorrowReturn);
			}
		}
	}

	/**
	 * This task creates the device on the server when it is unregistered, in other words, at the first time using the
	 * app.
	 * 
	 * @author Marcelo
	 */
	private class createDeviceTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				DataBaseManager manager = new DataBaseManager(SERVER, DEVICE_NAME);

				try {
					manager.createDevice(DEVICE_NAME);
					return true;
				} catch (ConnectException e) {
					ErrorDialogMessage.show(getParent(), "Connection failed. Check your network and try again.");
				} catch (IOException e) {
					ErrorDialogMessage.show(getParent(), "An. Check your network and try again.");
				}
			} catch (UnsupportedEncodingException e) {
				ErrorDialogMessage.show(getParent(), "We're terrible sorry. This shouldn't have happened. :(");
			}

			return false;
		}

		// on success saves information that the device was registered
		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putInt(DEVICE_REGISTERED_STATE, REGISTERED);
				editor.commit();
			}
		}
	}
}
