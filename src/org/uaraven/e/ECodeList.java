package org.uaraven.e;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import android.util.Log;

public class ECodeList extends ArrayList<ECode> {
	private static final long serialVersionUID = 1L;
	private ECodeListObserver observer = null;
	
	public ECodeList() {
	}

	public void load() {
		InputStream is = null;
		this.clear();				
		try {
			is = ECodeList.class.getResourceAsStream("e_en.dat");
		} catch (Exception e) {
			onChanged();
			return;
		}
		loadFromStream(is);		
	}

	private void onChanged() {
		if (observer != null) {
			observer.dataChanged();
		}
	}
	
	public void load(Locale locale) {
		InputStream is = null;		
		try {
			is = ECodeList.class.getResourceAsStream("e_" 
					+ locale.getLanguage() + ".dat");
			if (is == null) {
				load();
				return;
			}
		} catch (Exception e) {
			load();
			return;
		}
		this.clear();		
		loadFromStream(is);
	}
	
	private void loadFromStream(InputStream is) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String ln = null;
		try {
			while ((ln = br.readLine()) != null) {
				String[] row = ln.split("\t");
				ECode code = new ECode();
				code.init(row);
				this.add(code);
			}
			br.close();
		} catch (IOException e) {
			Log.e(this.getClass().getName(), e.getMessage(), e);
		}
		onChanged();
	}

	public void filter(String[] codes, ECodeList selectedECodes) {
		selectedECodes.clear();
		for (ECode code: this) {
			for (String userCode: codes)
				if (code.eCode.contains(userCode)) {
					selectedECodes.add(code);
					break;
				}
		}
		selectedECodes.onChanged();
	}
	
	public void setObserver(ECodeListObserver observer) {
		this.observer = observer;
	}
	
	public void reportChange() {
		onChanged();
	}
	
}
