package com.example.nasa.ui.recycler

import java.util.*


enum class TypeItem(val value: Int) {
    MOON(0), MARS(1);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}

data class Data(
    val name: String = "Text",
    val description: String = "",
    val type: TypeItem = TypeItem.MOON,
    val id: UUID = UUID.randomUUID()
)