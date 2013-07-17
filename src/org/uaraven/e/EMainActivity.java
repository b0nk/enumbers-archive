/*
 * This file is distributed under GPL v3 license 
 * http://www.gnu.org/licenses/gpl-3.0.html      
 *                                               
 * raven@acute-angle.net                         
 */


package org.uaraven.e;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;

import java.util.HashSet;
import java.util.Set;

public class EMainActivity extends SherlockListActivity implements TextWatcher {
    private EditText searchText;

    private final Handler textChangeHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            searchForECodes();
            return true;
        }
    });

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.search_view);
        searchText = (EditText) actionBar.getCustomView().findViewById(R.id.text_search);

        searchText.addTextChangedListener(this);

        if (Consts.ACTION_VIEW_E_CODE.equals(getIntent().getAction())) {
            Intent i = new Intent(this, ECodeViewActivity.class);
            i.putExtra(SearchManager.EXTRA_DATA_KEY, getIntent().getStringExtra(SearchManager.EXTRA_DATA_KEY));
            startActivity(i);
            finish();
        }

        GlobalCodeList.init(getApplicationContext());

        this.setListAdapter(new ECodeAdapter(this, GlobalCodeList.getInstance()));
    }

    private Set<String> createCodeList(String searchString) {
        Set<String> result = new HashSet<String>();
        String[] tokens = searchString.split(" ");
        for (String token : tokens) {
            if (TextUtils.isDigitsOnly(token)) {
                result.add(token);
            } else {
                result.addAll(GlobalCodeList.getInstance().textSearch(token));
            }
        }
        return result;
    }

    private void searchForECodes() {
        String text = searchText.getText().toString().trim();
        Set<String> codes = createCodeList(text);

        this.setListAdapter(new ECodeAdapter(this, GlobalCodeList.getInstance().filter(codes)));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ECode code = (ECode) getListAdapter().getItem(position);
        Intent intent = new Intent(this, ECodeViewActivity.class);
        intent.putExtra("ecode", code);
        startActivity(intent);
    }

    public void afterTextChanged(Editable s) {
        textChangeHandler.removeMessages(0);
        textChangeHandler.sendEmptyMessageDelayed(0, 200);
    }

    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

}