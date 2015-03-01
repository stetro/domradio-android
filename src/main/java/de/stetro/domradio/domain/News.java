package de.stetro.domradio.domain;


import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class News {
    private String title;
    private String date;
    private Uri link;
    private String description;

    public News(String title, String description, long date, Uri link) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        this.title = title.trim();
        this.description = description.trim();
        this.date = simpleDateFormat.format(new Date(date));
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public Uri getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }
}
