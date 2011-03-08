/*
 * This file is distributed under GPL v3 license 
 * http://www.gnu.org/licenses/gpl-3.0.html      
 *                                               
 * raven@acute-angle.net                         
 */

package org.uaraven.e;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;

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

    public ECode find(String code) {
        final String lcode = code.toLowerCase();
        for (ECode ecode: this) {
            if (ecode.eCode.contains(code)) {
                return ecode;
            }
            if (ecode.name != null) {
                if (ecode.name.toLowerCase().indexOf(lcode) >= 0)
                    return ecode;
            }
        }
        return null;
    }

    public void filter(Collection<String> codes, ECodeList selectedECodes) {
        selectedECodes.clear();
        for (ECode code: this) {
            for (String token: codes)
                if (code.eCode.contains(token)) {
                    selectedECodes.add(code);
                    break;
                }
        }
        selectedECodes.onChanged();
    }

    public List<String> textSearch(String text) {
        List<String> result = new LinkedList<String>();
        for (ECode code : this) {
            /*
             * int d = LevenshteinDistance.possibleMatch(text.toLowerCase(),
             * code.name.toLowerCase()); if (d <= 2) result.add(code.eCode);
             */
            if (text != null && code != null && code.name != null)
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
    
    public ECodeList filterList(String codesOrTexts) {
        Set<String> codeIds = new HashSet<String>(20);
        String[] tokens = codesOrTexts.split("[\\ ,]");
        for (String token: tokens) {
            try {
                Integer.parseInt(token);
                codeIds.add(token);
            } catch (NumberFormatException e) {
                // not integer
                codeIds.addAll(GlobalCodeList.getInstance().textSearch(token));
            }
        }
        ECodeList result = new ECodeList();
        filter(codeIds, result);
        return result;
    }
    
    public Cursor getSearchCursor(String query) {
        ECodeList codes = filterList(query);
        MatrixCursor c = new MatrixCursor(new String[] {
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_TEXT_2,
                        SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA
        });
        final int len = codes.size();
        for (int i = 0; i < len; i++) {
            ECode code = codes.get(i);
            c.addRow(new Object[] {
               new Integer(i+1),
               "E" + code.eCode,
               code.name,
               code.eCode
            });
        }
        return c;
    }

    public void filter(String[] codes, ECodeList selectedECodes) {
        List<String> lcodes = Arrays.asList(codes);
        filter(lcodes, selectedECodes);
    }
}
