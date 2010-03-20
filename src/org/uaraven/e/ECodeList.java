package org.uaraven.e;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.Context;
import android.content.res.AssetManager;

public class ECodeList extends ArrayList<ECode> {
	private static final long serialVersionUID = 1L;
	private ECodeListObserver observer = null;

	public ECodeList() {
	}

	public void load(Context ctx) {
		InputStream is = null;
		this.clear();
		try {
			AssetManager mgr = ctx.getAssets();
			is = mgr.open("e.xml");
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

	private void loadFromStream(InputStream is) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(is, new ECodeHandler(this));
			onChanged();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void filter(String[] codes, ECodeList selectedECodes) {
		selectedECodes.clear();
		for (ECode code : this) {
			for (String userCode : codes)
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
