package com.example.universityapp.model.data

import com.fasterxml.jackson.annotation.JsonProperty

data class Role(
    @JsonProperty("Name") val name: String?,
    @JsonProperty("Description") val description: String?)