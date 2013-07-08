package br.itabits.cerberus.borrow;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import br.itabits.cerberus.MenuActivity;
import br.itabits.cerberus.R;
import br.itabits.cerberus.database.DataBaseManager;
import br.itabits.cerberus.util.Transaction;

public class BorrowTableActivity extends Activity {
	private static final int pad = 5;
	public static final String SERVER = "http://itabitscerberus.appspot.com/";
	
	
	private View mTableView;
	private View mTableStatusView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrow_table);
		
		mTableView = findViewById(R.id.borrow_return_table);
		mTableStatusView = findViewById(R.id.borrow_table_status);
		
		findViewById(R.id.button_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		showProgress(true);
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new DownloadDataTask().execute();
		} else {
			// TODO
		}
	}

	private void printTable(ArrayList<Transaction> transactions) {
		TableLayout mTableLayout = (TableLayout) findViewById(R.id.borrow_table);

		for (Transaction transaction : transactions) {
			TableRow mRow = new TableRow(this);
			TextView borrowed = new TextView(this);
			TextView name = new TextView(this);
			TextView returned = new TextView(this);

			TableRow.LayoutParams params = new TableRow.LayoutParams();
			params.span = 1;

			mRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			borrowed.setBackgroundResource(R.drawable.cell_shape);
			borrowed.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			borrowed.setPadding(pad, pad, pad, pad);
			borrowed.setText(transaction.getStart());
			mRow.addView(borrowed, params);

			name.setBackgroundResource(R.drawable.cell_shape);
			name.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			name.setPadding(pad, pad, pad, pad);
			name.setText(transaction.getName());
			mRow.addView(name, params);

			returned.setBackgroundResource(R.drawable.cell_shape);
			returned.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			returned.setPadding(pad, pad, pad, pad);
			returned.setText(transaction.getEnd());
			mRow.addView(returned, params);

			mTableLayout.addView(mRow);
		}
	}

	// Uses AsyncTask to create a task away from the main UI thread. This task takes a
	// URL string and uses it to create an HttpUrlConnection. Once the connection
	// has been established, the AsyncTask downloads the contents of the dataserver as
	// an InputStream. Finally, the InputStream is converted into a string, which is
	// modeled into a table in the UI by the AsyncTask's onPostExecute method.
	private class DownloadDataTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			try {
				DataBaseManager manager = new DataBaseManager(SERVER,MenuActivity.DEVICE_NAME);
				String result = null;
				try {
					result = manager.getAll();
				} catch (ConnectException e) {
					System.out.println("\n" + e.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return result;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			showProgress(false);
			
			if(result != null){
				ArrayList<Transaction> transactions = Transaction.fromString(result);
				printTable(transactions);	
			}
		}
	}
	
	/**
	 * Shows the progress UI and hides the table.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mTableStatusView.setVisibility(View.VISIBLE);
			mTableStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mTableStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mTableView.setVisibility(View.VISIBLE);
			mTableView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mTableView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mTableStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mTableView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}
