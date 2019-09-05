package de.domradio.api.data

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml


@Xml(name = "station")
data class StationInformation(
    @Attribute(name = "name")
    var name: String? = null,
    @Element(name = "onair")
    var onair: Onair? = null
)