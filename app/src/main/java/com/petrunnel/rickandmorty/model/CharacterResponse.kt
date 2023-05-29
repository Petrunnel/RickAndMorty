package com.petrunnel.rickandmorty.model

import com.squareup.moshi.Json

data class CharacterResponse (
    @field:Json(name = "info") val info: Info,
    @field:Json(name = "results") val results: List<Character>,
)