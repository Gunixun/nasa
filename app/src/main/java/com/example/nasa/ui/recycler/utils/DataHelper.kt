package com.example.nasa.ui.recycler.utils

import com.example.nasa.ui.recycler.Data
import com.example.nasa.ui.recycler.TypeItem

fun generateData(name: String, description: String, type: TypeItem): Pair<Data, Boolean> {
    return Pair(Data(name, description, type), false)
}