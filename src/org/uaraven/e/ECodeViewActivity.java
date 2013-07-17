/*
 * This file is distributed under GPL v3 license 
 * http://www.gnu.org/licenses/gpl-3.0.html      
 *                                               
 * raven@acute-angle.net                         
 */


package org.uaraven.e;

import android.app.Activity;
import android.app.SearchManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        ImageView imVegan = (ImageView) findViewById(R.id.img_v);
        ImageView imChild = (ImageView) findViewById(R.id.img_ch);
        ImageView imAller = (ImageView) findViewById(R.id.img_al);

        TextView tvVegan = (TextView) findViewById(R.id.tv_v);
        TextView tvChild = (TextView) findViewById(R.id.tv_ch);
        TextView tvAller = (TextView) findViewById(R.id.tv_al);

        LinearLayout layExtra = (LinearLayout) findViewById(R.id.layExtra);

        Bundle extras = getIntent().getExtras();
        ECode code;
        if (extras.containsKey(SearchManager.EXTRA_DATA_KEY)) {
            code = GlobalCodeList.getInstance().find(extras.getString(SearchManager.EXTRA_DATA_KEY));
        } else {
            code = this.getIntent().getParcelableExtra("ecode");
        }
        if (code == null) {
            Toast.makeText(this, "Fock", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        tvCode.setBackgroundColor(code.getColor());
        tvCode.setText("E" + code.eCode);
        tvName.setText(code.name);
        tvPurpose.setText(code.purpose);
        tvExtra.setText(code.comment);

        layExtra.setVisibility(code.hasExtra() ? View.VISIBLE : View.GONE);
        imVegan.setImageResource(code.vegan == 0 ? R.drawable.vegan :
                (code.vegan == 2 ? R.drawable.vegan_y : R.drawable.vegan_r));
        imChild.setImageResource(code.children == 0 ? R.drawable.child : R.drawable.child_r);
        imAller.setImageResource(code.allergic ? R.drawable.allergic_r : R.drawable.allergic);

        tvVegan.setText(code.vegan == 0 ? R.string.vegan2 :
                (code.vegan == 2 ? R.string.v2 : R.string.v1));
        tvChild.setText(code.children == 0 ? R.string.child2 :
                (code.children == 1 ? R.string.c1 : R.string.c2));
        tvAller.setText(code.allergic ? R.string.a1 : R.string.allerg2);

    }

}
