package com.app.noteit.feature_note.domain.util

/**
 * This class tells the order of notes like
 * do you want the notes in ascending or descending order
 *
* */

sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}


