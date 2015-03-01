package de.stetro.domradio.service.event;


public class StopRadioEvent {
    private boolean updateNotification;

    public StopRadioEvent(boolean updateNotification) {
        this.updateNotification = updateNotification;
    }

    public boolean isUpdateNotification() {
        return updateNotification;
    }
}
