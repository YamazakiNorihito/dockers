package rss;

import java.util.ArrayList;
import java.util.List;

import java.net.URL;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import model.RssItemModel;

public class Rssad {

    public List<RssItemModel> getRssItemModelList(String url) throws Exception {
        try {
            List<RssItemModel> rssItemList = new ArrayList<RssItemModel>();
            URL feedUrl = new URL(url);
            SyndFeedInput input = new SyndFeedInput();

            SyndFeed feed = input.build(new XmlReader(feedUrl));
            for (Object obj : feed.getEntries()) {
                SyndEntry entry = (SyndEntry) obj;

                // item instance
                RssItemModel rssitem = new RssItemModel(
                        entry.getTitle(), // 記事タイトル
                        entry.getDescription().getValue(), // 記事の詳細
                        entry.getLink() // 記事のURL
                );
                // add
                rssItemList.add(rssitem);
            }
            return rssItemList;
        } finally {

        }
    }
}
