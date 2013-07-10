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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
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
	
	private View tableWrapper;
	private View tableStatusView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrow_table);
		
		tableStatusView = findViewById(R.id.borrow_table_status);
		tableWrapper = findViewById(R.id.borrow_table_wrapper);
		View tableContainer = findViewById(R.id.borrow_table_container);
		
		if(tableWrapper.getWidth() > 800) {
		    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(800, LayoutParams.MATCH_PARENT);
		    params.gravity = Gravity.CENTER_HORIZONTAL;
		    tableContainer.setLayoutParams(params);
		}

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
		TableLayout borrowTable = (TableLayout) findViewById(R.id.borrow_table);

		for (Transaction transaction : transactions) {
			TableRow row = new TableRow(this);
			TextView borrowed = new TextView(this);
			TextView name = new TextView(this);
			TextView returned = new TextView(this);

			row.setLayoutParams(new LayoutParams(800, LayoutParams.WRAP_CONTENT));

			borrowed.setLayoutParams(new TableRow.LayoutParams(300, LayoutParams.WRAP_CONTENT));
			borrowed.setPadding(pad, pad, pad, pad);
			borrowed.setGravity(Gravity.CENTER_HORIZONTAL);
			borrowed.setWidth(300);
			borrowed.setText(transaction.getStart());
			row.addView(borrowed);

			name.setLayoutParams(new TableRow.LayoutParams(200, LayoutParams.WRAP_CONTENT));
			name.setPadding(pad, pad, pad, pad);
			name.setGravity(Gravity.CENTER_HORIZONTAL);
			borrowed.setWidth(200);
			name.setText(transaction.getName());
			row.addView(name);

			returned.setLayoutParams(new TableRow.LayoutParams(300, LayoutParams.WRAP_CONTENT));
			returned.setPadding(pad, pad, pad, pad);
			returned.setGravity(Gravity.CENTER_HORIZONTAL);
			borrowed.setWidth(300);
			returned.setText(transaction.getEnd());
			row.addView(returned);

			borrowTable.addView(row);
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
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			tableStatusView.setVisibility(View.VISIBLE);
			tableStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							tableStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			tableWrapper.setVisibility(View.VISIBLE);
			tableWrapper.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							tableWrapper.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			tableStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			tableWrapper.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}
