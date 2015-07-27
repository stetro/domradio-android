package de.domradio.service.event;

import de.domradio.domain.FeedTopic;

public class SetNewsFeedEvent {

    private FeedTopic feedTopic;

    public SetNewsFeedEvent(FeedTopic feedTopic) {
        this.feedTopic = feedTopic;
    }

    public FeedTopic getFeedTopic() {
        return feedTopic;
    }
}
