package de.domradio.service.event;

import de.domradio.dialog.RssChooserDialog;

public class SetNewsFeedEvent {

    private RssChooserDialog.FeedTopic feedTopic;

    public SetNewsFeedEvent(RssChooserDialog.FeedTopic feedTopic) {
        this.feedTopic = feedTopic;
    }

    public RssChooserDialog.FeedTopic getFeedTopic() {
        return feedTopic;
    }
}
