package de.domradio.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Station {

    @Attribute
    public String name;
    @Element
    public OnAir onair;
}
