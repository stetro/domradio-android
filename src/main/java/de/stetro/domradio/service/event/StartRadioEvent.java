package de.stetro.domradio.service.event;


public class StartRadioEvent {
    private String url;

    public StartRadioEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
