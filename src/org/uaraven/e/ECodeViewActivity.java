package org.uaraven.e;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ECodeViewActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ecodeview);

		TextView tvCode = (TextView) findViewById(R.id.textCode);
		TextView tvName = (TextView) findViewById(R.id.textName);
		TextView tvPurpose = (TextView) findViewById(R.id.textPurpose);
		TextView tvExtra = (TextView) findViewById(R.id.textExtra);
		LinearLayout layExtra = (LinearLayout) findViewById(R.id.layExtra);
		LinearLayout layVegan = (LinearLayout) findViewById(R.id.layVegan);
		LinearLayout layChild = (LinearLayout) findViewById(R.id.layChild);
		LinearLayout layAller = (LinearLayout) findViewById(R.id.layAller);

		ECode code = this.getIntent().getParcelableExtra("ecode");
		tvCode.setBackgroundColor(code.getColor());
		tvCode.setText("E" + code.eCode);
		tvName.setText(code.name);
		tvPurpose.setText(code.purpose);
		tvExtra.setText(code.comment);

		layExtra.setVisibility(code.hasExtra() ? View.VISIBLE : View.GONE);
		layVegan.setVisibility(code.safeForVegans() ? View.VISIBLE : View.GONE);
		layChild.setVisibility(code.safeForChildren() ? View.VISIBLE : View.GONE);
		layAller.setVisibility(code.safeForAllergic() ? View.VISIBLE : View.GONE);
	}

}
