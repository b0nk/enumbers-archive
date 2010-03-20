package org.uaraven.e;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class ECodeDbHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "enumbers";
	private static final int DB_VERSION = 1;
	private Context context;

	public ECodeDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/*Toast t = Toast.makeText(context, context.getText(R.string.db),
				Toast.LENGTH_SHORT);
		t.show();*/
		execScript(db, "e.sql");
	}

	private void execScript(SQLiteDatabase db, String script) {
		AssetManager mgr = context.getAssets();
		BufferedReader rd;
		try {
			rd = new BufferedReader(new InputStreamReader(mgr.open(script)));
			String sql = null;
			db.beginTransaction();
			while ((sql = rd.readLine()) != null) {
				Log.d("CREATE_DB", sql);
				if (!"".equals(sql))
					db.execSQL(sql);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.endTransaction();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Toast t = Toast.makeText(context, context.getText(R.string.db),
				Toast.LENGTH_SHORT);
		t.show();		
		execScript(db, "e_clear.sql");
		execScript(db, "e.sql");
	}

}
