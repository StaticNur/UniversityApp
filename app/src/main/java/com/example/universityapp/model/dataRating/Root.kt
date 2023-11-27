package com.example.universityapp.model.dataRating

import com.fasterxml.jackson.annotation.JsonProperty

data class Root(
    @JsonProperty("MarkZeroSession")
    val markZeroSession: MarkZeroSession?,
    @JsonProperty("Sections")
    val sections: ArrayList<Section>
)