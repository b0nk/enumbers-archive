/*
 * This file is distributed under GPL v3 license 
 * http://www.gnu.org/licenses/gpl-3.0.html      
 *                                               
 * raven@acute-angle.net                         
 */
package org.uaraven.e;


public class LevenshteinDistance {
	public static int calculate(String s, String t) {

		int m = s.length() + 1;
		int n = t.length() + 1;
		// d is a table with m+1 rows and n+1 columns
		int[][] d = new int[m][n];

		for (int i = 0; i < m; i++)
			d[i][0] = i;
		for (int j = 0; j < n; j++)
			d[0][j] = j;

		for (int j = 1; j < n; j++) {
			for (int i = 1; i < m; i++) {
				if (s.charAt(i - 1) == t.charAt(j - 1)) {
					d[i][j] = d[i - 1][j - 1];
				} else {
					d[i][j] = Math.min(d[i - 1][j] + 1, Math.min(
							d[i][j - 1] + 1, d[i - 1][j - 1] + 1));
				}
			}
		}
		return d[m-1][n-1];
	}
	
	public static int possibleMatch(String word, String commaSeparated) {
		int d = -1;
		String[] wordList = commaSeparated.split("[\\ ,]");
		for (String w: wordList) {
			int i = calculate(word, w);
			d = Math.max(i, d);
		}
		return d;
	}
}
