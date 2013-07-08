package br.itabits.cerberus.borrow;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import br.itabits.cerberus.R;

public class BorrowTableActivity extends Activity {
	private static final int pad = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrow_table);
		
		findViewById(R.id.button_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		TableLayout mTableLayout = (TableLayout) findViewById(R.id.borrow_table);
		
		
		for(int i = 0 ; i  < 3 ; i++){
			TableRow mRow = new TableRow(this);
			TextView borrowed = new TextView(this);
			TextView name = new TextView(this);
			TextView returned = new TextView(this);
			
			TableRow.LayoutParams params = new TableRow.LayoutParams();  
		    params.span = 1;    
			
			mRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			
			borrowed.setBackgroundResource(R.drawable.cell_shape);
			borrowed.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			borrowed.setPadding(pad,pad,pad,pad);
			borrowed.setText("10 de julho de 2012 "+i);
			mRow.addView(borrowed,params);
			
			name.setBackgroundResource(R.drawable.cell_shape);
			name.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			name.setPadding(pad,pad,pad,pad);
			name.setText("Marcelo "+i);
			mRow.addView(name,params);
			
			returned.setBackgroundResource(R.drawable.cell_shape);
			returned.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			returned.setPadding(pad,pad,pad,pad);
			returned.setText("05 de julho de 2013 "+i);
			mRow.addView(returned,params);
			
			
			mTableLayout.addView(mRow);
		}
	}

}
