package com.mobikasa.pagingsampleremote.models

data class ServiceResponse(
    var results: List<Results>,
    val page: Int,
    val total_pages: Int,
    val total_results: Int
)