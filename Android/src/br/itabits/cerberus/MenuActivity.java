package br.itabits.cerberus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import br.itabits.cerberus.borrow.BorrowReturnActivity;
import br.itabits.cerberus.borrow.BorrowTableActivity;
import br.itabits.cerberus.login.LoginActivity;

public class MenuActivity extends Activity {

	private static final int AVAIABLE = 0;
	private static final int BORROWED = 1;
	private SharedPreferences sharedPref;
	private Integer borrowState;
	private Button borrowReturn;
	
	private String mEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		mEmail = getIntent().getStringExtra(LoginActivity.EXTRA_EMAIL);
		
		sharedPref = getPreferences(Context.MODE_PRIVATE);

		TextView title = (TextView) findViewById(R.id.greetings);
		title.setText(title.getText().toString().replace("username", mEmail));
		
		borrowReturn = (Button) findViewById(R.id.button_borrow);
		loadBorrowState();

		borrowReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				updateBorrowState();
				
				Intent openBorrowReturn = new Intent(MenuActivity.this,
						BorrowReturnActivity.class);
				String title = (borrowState.equals(AVAIABLE)) ? "Returned" : "Borrowed";
				openBorrowReturn.putExtra(getString(R.string.title_activity_borrow_return), title);
				openBorrowReturn.putExtra(LoginActivity.EXTRA_EMAIL, mEmail);				
				startActivity(openBorrowReturn);
			}
		});

		findViewById(R.id.button_view_past).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent openViewPast = new Intent(MenuActivity.this,
								BorrowTableActivity.class);
						startActivity(openViewPast);
					}
				});
	}

	private void loadBorrowState() {
		borrowState = sharedPref.getInt(getString(R.string.saved_borrow_state),
				AVAIABLE);
		if (borrowState.equals(AVAIABLE)) {
			borrowReturn.setText("borrow");
		} else {
			borrowReturn.setText("return");
		}
	}

	private void updateBorrowState() {
		if (borrowState.equals(AVAIABLE)) {
			borrowState = BORROWED;
			borrowReturn.setText("return");
		} else {
			borrowState = AVAIABLE;
			borrowReturn.setText("borrow");
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
}
