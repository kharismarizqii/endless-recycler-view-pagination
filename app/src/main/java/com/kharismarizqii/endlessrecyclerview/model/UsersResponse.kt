package com.kharismarizqii.endlessrecyclerview.model

data class UsersResponse(
    val data: ArrayList<Data>,
    val page: Int,
    val per_page: Int,
    val total: Int,
    val total_pages: Int
)