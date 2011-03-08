package org.uaraven.e;

import android.content.Context;

public class GlobalCodeList extends ECodeList {

    private static ECodeList instance;
    
    public static void init(Context context) {
        instance = new ECodeList();
        instance.load(context);
    }
    
    public static ECodeList getInstance() {
        return instance;
    }
}
