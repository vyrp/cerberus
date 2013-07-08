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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		sharedPref = getPreferences(Context.MODE_PRIVATE);
		//updateBorrowState();

		Button borrowReturn = (Button) findViewById(R.id.button_borrow);

		borrowReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent openBorrowReturn = new Intent(MenuActivity.this,
						BorrowReturnActivity.class);
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

	private void updateBorrowState() {
		borrowState = getResources().getInteger(R.string.saved_borrow_state);
		SharedPreferences.Editor editor = sharedPref.edit();
		if(borrowState==null){
			editor.putInt(getString(R.string.saved_borrow_state), AVAIABLE);
			editor.commit();
		} else {
			if (borrowState.equals(AVAIABLE)) {
				editor.putInt(getString(R.string.saved_borrow_state), BORROWED);
			} else {
				editor.putInt(getString(R.string.saved_borrow_state), AVAIABLE);
			}
			editor.commit();
		}

		return;
	}
}
