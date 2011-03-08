/*
 * This file is distributed under GPL v3 license 
 * http://www.gnu.org/licenses/gpl-3.0.html      
 *                                               
 * raven@acute-angle.net                         
 */


package org.uaraven.e;

import java.util.LinkedList;
import java.util.List;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class EMainActivity extends ListActivity implements TextWatcher {
	private static final int MENU_HELP = 1;

	private EditText searchText;
	private ECodeList selectedECodes;
	private ECodeAdapter adapter;
	
	private Handler textChangeHandler;

	//private AdView adView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		searchText = (EditText) findViewById(R.id.searchString);

		searchText.addTextChangedListener(this);

		GlobalCodeList.init(this);
		
		if (Consts.ACTION_VIEW_E_CODE.equals(getIntent().getAction())) {
		    Intent i = new Intent(this, ECodeViewActivity.class);
		    i.putExtra(SearchManager.EXTRA_DATA_KEY, getIntent().getStringExtra(SearchManager.EXTRA_DATA_KEY));
		    startActivity(i);
		    finish();
		}

		selectedECodes = new ECodeList();
		selectedECodes.addAll(GlobalCodeList.getInstance());

		adapter = new ECodeAdapter(this, selectedECodes);
		this.setListAdapter(adapter);
		
		textChangeHandler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				searchForECodes();
				return true;
			}
		});
		
		if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
		    searchText.setVisibility(View.GONE);
		    String query = getIntent().getStringExtra(SearchManager.QUERY);
		    searchForECodes(query);
		}
		
		//installAdView();
	}

	private String[] createCodeList(String searchString) {
		List<String> result = new LinkedList<String>();
		String[] tokens = searchString.split(" ");
		for (String token: tokens) {
			try {
				Integer.parseInt(token);
				result.add(token);
			} catch (NumberFormatException e) {
				// not integer
				result.addAll(GlobalCodeList.getInstance().textSearch(token));
			}
		}
		String[] resarray = new String[result.size()];
		return result.toArray(resarray);
	}
	
	private void searchForECodes() {
	    String text = searchText.getText().toString().trim();
	    searchForECodes(text);
	}
	
	private void searchForECodes(String text) {
		String[] codes = createCodeList(text);

		GlobalCodeList.getInstance().filter(codes, selectedECodes);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		MenuItem help = menu.add(Menu.NONE, MENU_HELP, Menu.NONE,
				R.string.menu_help);
		help.setIcon(R.drawable.help);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == MENU_HELP) {
			startActivity(new Intent(this, EHelpActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		ECode code = selectedECodes.get(position);
		Intent intent = new Intent(this, ECodeViewActivity.class);
		intent.putExtra("ecode", code);
		startActivity(intent);
	}

	public void afterTextChanged(Editable s) {
		textChangeHandler.removeMessages(0);
		textChangeHandler.sendEmptyMessageDelayed(0, 200);
		//searchForECodes();
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	/*private void installAdView() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.mainlayout);			
		adView = new AdView(this);
		LayoutParams p = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		adView.setLayoutParams(p);
		if (adView != null) {
			layout.addView(adView);					
			adView.requestFreshAd();
		}

	}*/

}