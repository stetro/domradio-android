package de.domradio.api

import de.domradio.api.data.StationInformation
import retrofit2.Call
import retrofit2.http.GET

interface StationService {

    @GET("/sites/all/themes/domradio/playlist/Export.xml")
    fun getStationInformation(): Call<StationInformation>

}


