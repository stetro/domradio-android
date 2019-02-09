package de.domradio.api.data

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "onair")
data class Onair(
    @field:Element(name = "title")
    var title: String? = null,
    @field:Element(name = "artist")
    var artist: String? = null,
    @field:Element(name = "type")
    var type: String? = null,
    @field:Element(name = "presenter")
    var presenter: String? = null,
    @field:Element(name = "date")
    var date: String? = null,
    @field:Element(name = "duration")
    var duration: String? = null,
    @field:Element(name = "start")
    var start: String? = null,
    @field:Element(name = "cover")
    var cover: String? = null,
    @field:Element(name = "TitelID")
    var TitelID: String? = null,
    @field:Element(name = "ElementID")
    var ElementID: String? = null,
    @field:Element(name = "ArchivNr")
    var ArchivNr: String? = null
)