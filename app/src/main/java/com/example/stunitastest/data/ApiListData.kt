package com.example.stunitastest.data

data class ApiListData<T> constructor (
    val documents: ArrayList<T> = ArrayList(),
    val meta: Meta
)