package de.domradio.api.data

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(strict = false, name = "station")
data class StationInformation(
    @field:Attribute(name = "name") var name: String? = null,
    @field:Element(name = "onair") var onair: Onair? = null
)