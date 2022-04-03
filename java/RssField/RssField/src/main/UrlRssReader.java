package main;

import java.util.List;
import model.ConfigRss;
import rss.Rssad;
import model.RssItemModel;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.File;

public class UrlRssReader extends AbsRssBase {

    public void exec(ConfigRss config, String url, String filePath, Boolean isCutter, boolean isConvert) {

        try {
            List<RssItemModel> rssItemList = new Rssad().getRssItemModelList(url);

            File myObj = CreateFile(filePath);
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(myObj)))) {
                for (RssItemModel rssItemModel : rssItemList) {
                    writer.println(_titletag
                            + EditString(rssItemModel._title, isCutter, config.maxTitleLength, isConvert,
                                    config.Replaces));
                    writer.println(_bodytag
                            + EditString(rssItemModel._description, isCutter, config.maxBodyLength, isConvert,
                                    config.Replaces));
                    writer.println("");
                }
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private File CreateFile(String filePath) throws IOException {
        File filebj = new File(filePath);
        if (filebj.createNewFile()) {
            System.out.println("File created: " + filebj.getName());
        } else {
            System.out.println("File already exists.");
        }
        return filebj;
    }
}
