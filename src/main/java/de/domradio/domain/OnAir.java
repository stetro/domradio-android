package de.domradio.domain;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class OnAir {
    @Element
    public String title;
    @Element
    public String artist;
    @Element
    public String type;
    @Element
    public String presenter;
    @Element
    public String date;
    @Element
    public String duration;
    @Element
    public String start;
    @Element
    public String cover;
    @Element
    public String TitelID;
    @Element
    public String ElementID;
    @Element
    public String ArchivNr;
}
