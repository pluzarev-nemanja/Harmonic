package com.example.mymusic.domain.util

sealed class OrderType{
    object Ascending:OrderType()
    object Descending:OrderType()
}
