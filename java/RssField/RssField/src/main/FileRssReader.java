package main;

import model.ConfigRss;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

public class FileRssReader extends AbsRssBase {

    public void exec(ConfigRss config, String filePath, Boolean isCutter, boolean isConvert) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(filePath)))) {
            String text;
            while ((text = br.readLine()) != null) {

                if (text.contains(_titletag) == false && text.contains(_bodytag) == false) {
                    continue;
                }

                String tag = _titletag;
                int maxLen = config.maxTitleLength;
                if (text.contains(_bodytag)) {
                    tag = _bodytag;
                    maxLen = config.maxBodyLength;
                }

                text = text.replace(tag, "").trim();
                text = EditString(text, isCutter, maxLen, isConvert, config.Replaces);
                System.out.println(tag + text);
            }
        }
    }
}
