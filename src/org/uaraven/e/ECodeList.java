/*
 * This file is distributed under GPL v3 license 
 * http://www.gnu.org/licenses/gpl-3.0.html      
 *                                               
 * raven@acute-angle.net                         
 */

package org.uaraven.e;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ECodeList extends ArrayList<ECode> {
    private static final long serialVersionUID = 1L;

    public ECodeList() {
    }

    public void load(Context ctx) {
        InputStream is = null;
        this.clear();
        try {
            AssetManager mgr = ctx.getAssets();
            is = mgr.open("e.xml");
            loadFromStream(is);
        } catch (Exception e) {
            Log.e("E", "Failed to load data", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private void loadFromStream(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(is, new ECodeHandler(this));
    }

    public ECode find(String code) {
        final String lcode = code.toLowerCase();
        for (ECode ecode : this) {
            if (ecode.eCode.contains(code)) {
                return ecode;
            }
            if (ecode.name != null) {
                if (ecode.name.toLowerCase().contains(lcode))
                    return ecode;
            }
        }
        return null;
    }

    public ECodeList filter(Collection<String> codes) {
        ECodeList result = new ECodeList();
        for (ECode code : this) {
            for (String token : codes)
                if (code.eCode.contains(token)) {
                    result.add(code);
                    break;
                }
        }
        return result;
    }

    public List<String> textSearch(String text) {
        List<String> result = new LinkedList<String>();
        for (ECode code : this) {
            /*
             * int d = LevenshteinDistance.possibleMatch(text.toLowerCase(),
             * code.name.toLowerCase()); if (d <= 2) result.add(code.eCode);
             */
            if (text != null && code != null && code.name != null)
                if (code.name.toLowerCase().contains(text.toLowerCase()))
                    result.add(code.eCode);
        }
        return result;
    }

}
