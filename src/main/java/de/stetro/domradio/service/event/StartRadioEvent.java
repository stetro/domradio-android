package de.stetro.domradio.service.event;


public class StartRadioEvent {

    private boolean updateNotification = false;

    public StartRadioEvent(boolean updateNotification) {

        this.updateNotification = updateNotification;
    }

    public boolean isUpdateNotification() {
        return updateNotification;
    }
}
