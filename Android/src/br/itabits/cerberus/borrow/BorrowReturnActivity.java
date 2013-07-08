package br.itabits.cerberus.borrow;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import br.itabits.cerberus.R;
import br.itabits.cerberus.login.LoginActivity;

public class BorrowReturnActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrow_return);
		
		Intent intent = getIntent();
		String title = intent.getStringExtra(getString(R.string.title_activity_borrow_return));
		String mEmail = intent.getStringExtra(LoginActivity.EXTRA_EMAIL);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
		
		TextView titleView = (TextView) findViewById(R.id.borrow_return_title);
		titleView.setText(title);
		
		TextView userView = (TextView) findViewById(R.id.borrow_return_name);
		String userName = getString(R.string.name) +"  "+ mEmail;
		userView.setText(userName);
		
		TextView dateView = (TextView) findViewById(R.id.borrow_return_date);
		String date = getString(R.string.date) +"  "+ dateFormat.format(c.getTime());
		dateView.setText(date);
		
		TextView timeView = (TextView) findViewById(R.id.borrow_return_time);
		String time = getString(R.string.time) +"  "+ timeFormat.format(c.getTime());
		timeView.setText(time);
		
		findViewById(R.id.button_OK).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent openViewPast = new Intent(BorrowReturnActivity.this, BorrowTableActivity.class);
				startActivity(openViewPast);
				finish();
			}
		});
	}
}
