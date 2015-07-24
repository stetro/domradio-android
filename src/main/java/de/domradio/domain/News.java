package de.domradio.domain;


import android.net.Uri;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class News implements Serializable {
    private String title;
    private String date;
    private Uri link;
    private String description;

    public News(String title, String description, long date, Uri link) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        if (title != null) {
            this.title = title.trim();
        }
        if (description != null) {
            this.description = description.trim();
        }
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
