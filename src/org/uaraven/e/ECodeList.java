/*
 * This file is distributed under GPL v3 license 
 * http://www.gnu.org/licenses/gpl-3.0.html      
 *                                               
 * raven@acute-angle.net                         
 */

package org.uaraven.e;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
		int codeCount = this.size();
		int userCodeCount = codes.length;		
		for (int i = 0; i < codeCount; i++) {
			ECode code = this.get(i);
			for (int j = 0; j < userCodeCount; j++)
				if (code.eCode.contains(codes[j])) {
					selectedECodes.add(code);
					break;
				}
		}
		selectedECodes.onChanged();
	}
	
	public List<String> textSearch(String text) {
		List<String> result = new LinkedList<String>();
		for (ECode code: this) {
			/*int d = LevenshteinDistance.possibleMatch(text.toLowerCase(), code.name.toLowerCase());
			if (d <= 2) 
				result.add(code.eCode);*/
			if (code.name.toLowerCase().indexOf(text.toLowerCase()) >= 0)
				result.add(code.eCode);
		}
		return result;
	}

	public void setObserver(ECodeListObserver observer) {
		this.observer = observer;
	}

	public void reportChange() {
		onChanged();
	}

}
