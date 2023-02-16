package com.example.mymusic.domain.util

sealed class SongOrder(val orderType: OrderType){
    class Title(orderType: OrderType): SongOrder(orderType)
    class Color(orderType: OrderType): SongOrder(orderType)
    class Date(orderType: OrderType): SongOrder(orderType)

    fun copy(orderType: OrderType): SongOrder{
        return when(this){
            is Title-> Title(orderType)
            is Color-> Color(orderType)
            is Date-> Date(orderType)
        }
    }
}
