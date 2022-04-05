package com.example.nasa.ui.recycler.diffUtils

import com.example.nasa.ui.recycler.Data

data class Change<out T> (
    val oldData: T,
    val newData: T
)

fun <T> createCombinePayloads(payloads: List<Change<T>>): Change<T>{
    val firs = payloads.first()
    val last = payloads.last()
    return Change(firs.oldData, last.newData)
}