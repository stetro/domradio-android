package de.domradio.api.data

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "onair")
data class Onair(
    @PropertyElement(name = "title")
    var title: String? = null,
    @PropertyElement(name = "artist")
    var artist: String? = null,
    @PropertyElement(name = "type")
    var type: String? = null,
    @PropertyElement(name = "presenter")
    var presenter: String? = null,
    @PropertyElement(name = "date")
    var date: String? = null,
    @PropertyElement(name = "duration")
    var duration: String? = null,
    @PropertyElement(name = "start")
    var start: String? = null,
    @PropertyElement(name = "cover")
    var cover: String? = null,
    @PropertyElement(name = "TitelID")
    var TitelID: String? = null,
    @PropertyElement(name = "ElementID")
    var ElementID: String? = null,
    @PropertyElement(name = "ArchivNr")
    var ArchivNr: String? = null
)