package main;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class AbsRssBase {

    protected static String _titletag = "title:";
    protected static String _bodytag = "body:";

    protected String EditString(String value, Boolean isCutter, int maxlength, boolean isConvert,
            HashMap<String, String> replaces) {

        String result = value;

        if (isCutter == true && 0 < maxlength) {
            result = result.substring(0, maxlength);
        }

        if (isConvert && replaces != null && 0 < replaces.size()) {
            for (Entry<String, String> entry : replaces.entrySet()) {
                String oldVal = entry.getKey();
                String newVal = entry.getValue();
                result = result.replace(oldVal, newVal);
            }
        }

        return result;
    }
}
