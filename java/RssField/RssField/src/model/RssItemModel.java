package model;

public class RssItemModel {
    public String _title;
    public String _description;
    public String _link;

    public RssItemModel(String title, String description, String link) {
        this._title = title;
        this._description = description;
        this._link = link;
    }

    public String getTitle() {
        return _title;
    }

    public String getDescription() {
        return _description;
    }

    public String getLink() {
        return _link;
    }

}
