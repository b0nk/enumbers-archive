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
import android.widget.*;

public class ECodeViewActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecodeview);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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


        TextView eCode = (TextView) findViewById(R.id.ecode);
        eCode.setText("E" + code.eCode);
        View colorBand = findViewById(R.id.color_band);
        colorBand.setBackgroundColor(code.getColor());

        tvName.setText(code.name);
        tvPurpose.setText(code.purpose);
        tvExtra.setText(code.comment);

        layExtra.setVisibility(code.hasExtra() ? View.VISIBLE : View.GONE);
        imVegan.setImageResource(code.vegan == 0 ? R.drawable.veg_green :
                (code.vegan == 2 ? R.drawable.veg_yellow : R.drawable.veg_red));
        imChild.setImageResource(code.children == 0 ? R.drawable.child_green : R.drawable.child_red);
        imAller.setImageResource(code.allergic ? R.drawable.allergic_red : R.drawable.allergic_green);

        tvVegan.setText(code.vegan == 0 ? R.string.vegan2 :
                (code.vegan == 2 ? R.string.v2 : R.string.v1));
        tvChild.setText(code.children == 0 ? R.string.child2 :
                (code.children == 1 ? R.string.c1 : R.string.c2));
        tvAller.setText(code.allergic ? R.string.a1 : R.string.allerg2);


        Button close = (Button) findViewById(R.id.close_btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
