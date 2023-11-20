package com.example.universityapp.model.dataUser

import com.fasterxml.jackson.annotation.JsonProperty

data class Role(
    @JsonProperty("Name") val name: String?,
    @JsonProperty("Description") val description: String?)