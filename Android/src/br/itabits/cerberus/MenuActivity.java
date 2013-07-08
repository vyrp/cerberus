package br.itabits.cerberus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.itabits.cerberus.borrow.BorrowReturnActivity;
import br.itabits.cerberus.borrow.BorrowTableActivity;

public class MenuActivity extends Activity {

	private static final int AVAIABLE = 0;
	private static final int BORROWED = 1;
	private SharedPreferences sharedPref;
	private Integer borrowState;
	private Button borrowReturn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		sharedPref = getPreferences(Context.MODE_PRIVATE);

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
				startActivity(openBorrowReturn);
			}
		});

		findViewById(R.id.button_view_past).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent openBorrowReturn = new Intent(MenuActivity.this,
								BorrowTableActivity.class);
						startActivity(openBorrowReturn);
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
