package org.uaraven.e;

import java.util.Collections;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ECodeDb  {


	private String caption_column = "caption";
	private String appl_column = "application";
	private String extra_column = "extra_text";

	private String sql_all = "select e_number, danger, vegan, children, allergic, ap.{3}, "
			+ "cap.{1}, ex.{2} "
			+ "from e_numbers enu left join "
			+ "application ap on (enu.application = ap.id) left join "
			+ "extra ex on (ex.e_id = enu.id) left join "
			+ "captions cap on (cap.e_id = enu.id)";

	private String sql_all_q;

	private ECodeDbHelper helper;
	private SQLiteDatabase db;
	private ECodeList codeList;

	public ECodeDb(Context context) {
		setLocale();
		
		helper = new ECodeDbHelper(context);
		
		db = helper.getReadableDatabase();
		
		codeList = null;

	}

	public void setList(ECodeList list) {
		codeList = list;
	}

	private void setLocale() {
		Locale loc = Locale.getDefault();
		if (loc.getCountry().equals("RU")) {
			caption_column = "caption_ru";
			appl_column = "application_ru";
			extra_column = "extra_text_ru";
		}
		String s = sql_all.replaceAll("\\{1\\}", caption_column);
		s = s.replaceAll("\\{2\\}", extra_column);
		s = s.replaceAll("\\{3\\}", appl_column);
		sql_all = s;
		sql_all_q = sql_all + " where enu.e_number like ?";
	}
	
	public void clear() {
		codeList.clear();
	}

	public ECodeList getEcodes(boolean clear, String mask) {
		if (clear)
			codeList.clear();
		if (mask == null || "".equals(mask))
			return populateList(db.rawQuery(sql_all, null));
		else
			return populateList(db.rawQuery(sql_all_q, new String[] { "%" + mask + "%"}));
	}

	private ECodeList populateList(Cursor c) {
		while (c.moveToNext()) {
			ECode code = new ECode();
			code.eCode = c.getString(0); // name
			code.setDanger(c.getInt(1)); // danger
			code.vegan = c.getInt(2);
			code.children = c.getInt(3);
			code.allergic = c.getInt(4);
			code.purpose = c.getString(5);
			code.name = c.getString(6);
			code.comment = c.getString(7);
			codeList.add(code);
		};
		Collections.sort(codeList);
		return codeList;
	}
}
