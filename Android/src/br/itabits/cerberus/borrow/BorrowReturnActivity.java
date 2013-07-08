package br.itabits.cerberus.borrow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import br.itabits.cerberus.R;

public class BorrowReturnActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrow_return);
		
		Intent intent = getIntent();
		String title = intent.getStringExtra(getString(R.string.title_activity_borrow_return));
		
		TextView titleView = (TextView) findViewById(R.id.borrow_return_title);
		titleView.setText(title);
		
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
