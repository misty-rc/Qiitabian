package org.misty.rc.Qiitabian;

import android.text.TextUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/09/26
 * Time: 15:47
 * To change this template use File | Settings | File Templates.
 */
public class HeaderParser {

    public static final String LINK = "Link";

    public static void parsePageNation(Map<String, String> header) {
        if(header.containsKey(LINK)) {
            String link = header.get(LINK);
            for(String v : link.split(",")) {
                if(v.contains("next")) {
                    String v3 = v.split(";")[0];
                    Pattern pattern = Pattern.compile("\\(<.+?>\\)");
                    Pattern p2 = Pattern.compile("page=.+?");
                    Matcher matcher = pattern.matcher(v3);

                }
            }
        }
    }
}
