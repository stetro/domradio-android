package de.domradio.dialog;


import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import de.domradio.R;
import de.domradio.service.event.SetNewsFeedEvent;
import de.greenrobot.event.EventBus;

public class RssChooserDialog implements Dialog, MaterialDialog.ListCallback {
    private final MaterialDialog dialog;

    public RssChooserDialog(Context context) {
        dialog = new MaterialDialog.Builder(context)
                .title(R.string.rss_feed_topic)
                .items(FeedTopic.getTitles())
                .itemsCallback(this)
                .negativeText(R.string.cancel)
                .build();
    }

    @Override
    public void show() {
        dialog.show();
    }

    @Override
    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
        this.dialog.hide();
        FeedTopic feedTopic = FeedTopic.resolve(charSequence);
        if (feedTopic != null) {
            EventBus.getDefault().post(new SetNewsFeedEvent(feedTopic));
        }
    }

    public enum FeedTopic {
        ALL("http://www.domradio.de/rss-feeds/domradio-rss.xml", "Alle Nachrichten"),
        ADVENIAT("http://www.domradio.de/rss-feeds/themen/157957/domradio-rss.xml", "Adveniat"),
        BENEDIKT("http://www.domradio.de/rss-feeds/themen/49/domradio-rss.xml", "Benedikt XVI"),
        BIBEL("http://www.domradio.de/rss-feeds/themen/28/domradio-rss.xml", "Bibel"),
        BISCHOFSKONFERENZ("http://www.domradio.de/rss-feeds/themen/27/domradio-rss.xml", "Bischofskonferenz"),
        BISCHOFSSYNODE("http://www.domradio.de/rss-feeds/themen/486/domradio-rss.xml", "Bischofssynode"),
        BISTUEMER("http://www.domradio.de/rss-feeds/themen/29/domradio-rss.xml", "Bistümer"),
        BONIFATIUSWERK("http://www.domradio.de/rss-feeds/themen/160509/domradio-rss.xml", "Bonifatiuswerk"),
        CARITAS("http://www.domradio.de/rss-feeds/themen/30/domradio-rss.xml", "Caritas"),
        CHRISTENVERFOLGUNG("http://www.domradio.de/rss-feeds/themen/31/domradio-rss.xml", "Christenverfolgung"),
        EHE("http://www.domradio.de/rss-feeds/themen/20/domradio-rss.xml", "Ehe und Familie"),
        ERZBISTUM("http://www.domradio.de/rss-feeds/themen/165314/domradio-rss.xml", "Erzbistum Köln"),
        ETHIK("http://www.domradio.de/rss-feeds/themen/607/domradio-rss.xml", "Ethik und Moral"),
        EUCHARISTISCHER("http://www.domradio.de/rss-feeds/themen/33/domradio-rss.xml", "Eucharistischer Kongress"),
        EVANGELII("http://www.domradio.de/rss-feeds/themen/168419/domradio-rss.xml", "Evangelii gaudium"),
        FASTENZEIT("http://www.domradio.de/rss-feeds/themen/34/domradio-rss.xml", "Fastenzeit"),
        FLUECHTLINGSHILFE("http://www.domradio.de/rss-feeds/themen/182985/domradio-rss.xml", "Flüchtlingshilfe"),
        HILDEGARD("http://www.domradio.de/rss-feeds/themen/489/domradio-rss.xml", "Hildegard von Bingen"),
        HOCHFESTE("http://www.domradio.de/rss-feeds/themen/36/domradio-rss.xml", "Hochfeste"),
        INTERRELIGIOESER("http://www.domradio.de/rss-feeds/themen/37/domradio-rss.xml", "Interreligiöser Dialog"),
        ISLAM("http://www.domradio.de/rss-feeds/themen/38/domradio-rss.xml", "Islam und Kirche"),
        JOACHIM("http://www.domradio.de/rss-feeds/themen/39/domradio-rss.xml", "Joachim Kardinal Meisner"),
        JUDENTUM("http://www.domradio.de/rss-feeds/themen/168443/domradio-rss.xml", "Judentum"),
        JUGEND("http://www.domradio.de/rss-feeds/themen/171083/domradio-rss.xml", "Jugend und Spiritualität"),
        KARNEVAL("http://www.domradio.de/rss-feeds/themen/40/domradio-rss.xml", "Karneval"),
        KARWOCHE("http://www.domradio.de/rss-feeds/themen/41/domradio-rss.xml", "Karwoche"),
        KATHOLIKENTAG("http://www.domradio.de/rss-feeds/themen/42/domradio-rss.xml", "Katholikentag"),
        KIRCHE("http://www.domradio.de/rss-feeds/themen/168418/domradio-rss.xml", "Kirche und Politik"),
        KIRCHENJAHR("http://www.domradio.de/rss-feeds/themen/477/domradio-rss.xml", "Kirchenjahr"),
        KIRCHENTAG("http://www.domradio.de/rss-feeds/themen/43/domradio-rss.xml", "Kirchentag"),
        KOELNER("http://www.domradio.de/rss-feeds/themen/32/domradio-rss.xml", "Kölner Dom"),
        KOLPING("http://www.domradio.de/rss-feeds/themen/159180/domradio-rss.xml", "Kolping International"),
        KONKLAVE("http://www.domradio.de/rss-feeds/themen/155604/domradio-rss.xml", "Konklave"),
        KULTUR("http://www.domradio.de/rss-feeds/themen/45/domradio-rss.xml", "Kultur"),
        LAIEN("http://www.domradio.de/rss-feeds/themen/46/domradio-rss.xml", "Laien"),
        MENSCHENRECHTE("http://www.domradio.de/rss-feeds/themen/168448/domradio-rss.xml", "Menschenrechte"),
        MINISTRANTENWALLFAHRT("http://www.domradio.de/rss-feeds/themen/166700/domradio-rss.xml", "Ministrantenwallfahrt 2013"),
        OEKUMENE("http://www.domradio.de/rss-feeds/themen/47/domradio-rss.xml", "Ökumene"),
        OEKUMENISCHER("http://www.domradio.de/rss-feeds/themen/480/domradio-rss.xml", "Ökumenischer Kirchentag"),
        OSTERN("http://www.domradio.de/rss-feeds/themen/48/domradio-rss.xml", "Ostern"),
        PAPST("http://www.domradio.de/rss-feeds/themen/156334/domradio-rss.xml", "Papst Franziskus"),
        PAPSTSTRASSBURG("http://www.domradio.de/rss-feeds/themen/183514/domradio-rss.xml", "Papst Franziskus in Straßburg"),
        RAINER("http://www.domradio.de/rss-feeds/themen/177841/domradio-rss.xml", "Rainer Maria Kardinal Woelki"),
        REFORMEN("http://www.domradio.de/rss-feeds/themen/51/domradio-rss.xml", "Reformen"),
        RENOVABIS("http://www.domradio.de/rss-feeds/themen/159672/domradio-rss.xml", "Renovabis"),
        SCHOEPFUNG("http://www.domradio.de/rss-feeds/themen/52/domradio-rss.xml", "Schöpfung"),
        SEELSORGE("http://www.domradio.de/rss-feeds/themen/53/domradio-rss.xml", "Seelsorge"),
        SOLDATEN("http://www.domradio.de/rss-feeds/themen/54/domradio-rss.xml", "Soldaten und Kirche"),
        SOZIALES("http://www.domradio.de/rss-feeds/themen/155951/domradio-rss.xml", "Soziales"),
        SPORT("http://www.domradio.de/rss-feeds/themen/474/domradio-rss.xml", "Sport und Kirche"),
        TAIZE("http://www.domradio.de/rss-feeds/themen/55/domradio-rss.xml", "Taizé"),
        VATIKAN("http://www.domradio.de/rss-feeds/themen/128235/domradio-rss.xml", "Vatikan"),
        WEIHBISCHOFPUFF("http://www.domradio.de/rss-feeds/themen/161071/domradio-rss.xml", "Weihbischof Ansgar Puff"),
        WEIHBISCHOFSCHWADERLAPP("http://www.domradio.de/rss-feeds/themen/62/domradio-rss.xml", "Weihbischof Dominikus Schwaderlapp"),
        WEIHBISCHOF("http://www.domradio.de/rss-feeds/themen/57/domradio-rss.xml", "Weihbischof Manfred Melzer"),
        WEIHNACHTEN("http://www.domradio.de/rss-feeds/themen/63/domradio-rss.xml", "Weihnachten"),
        WELTJUGENDTAGE("http://www.domradio.de/rss-feeds/themen/64/domradio-rss.xml", "Weltjugendtage"),
        WELTKIRCHE("http://www.domradio.de/rss-feeds/themen/65/domradio-rss.xml", "Weltkirche"),
        ZWEITES("http://www.domradio.de/rss-feeds/themen/483/domradio-rss.xml", "Zweites Vatikanisches Konzil");

        private final String url;
        private final String title;

        FeedTopic(String url, String title) {
            this.url = url;
            this.title = title;
        }

        public static CharSequence[] getTitles() {
            CharSequence[] titles = new CharSequence[FeedTopic.values().length];
            for (int i = 0; i < FeedTopic.values().length; i++) {
                titles[i] = FeedTopic.values()[i].title;
            }
            return titles;
        }

        public static FeedTopic resolve(CharSequence charSequence) {
            for (FeedTopic ft : FeedTopic.values()) {
                if (ft.title.equals(charSequence)) {
                    return ft;
                }
            }
            return null;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }
    }
}
