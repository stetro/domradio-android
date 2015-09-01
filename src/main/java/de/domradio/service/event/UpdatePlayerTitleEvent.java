package de.domradio.service.event;

import de.domradio.domain.Station;

public class UpdatePlayerTitleEvent {

    private Station station;

    public UpdatePlayerTitleEvent(Station station) {
        this.station = station;
    }

    public Station getStation() {
        return station;
    }
}
