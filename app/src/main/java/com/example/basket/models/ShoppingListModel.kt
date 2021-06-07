package com.example.basket.models

import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*

class ShoppingListModel : Serializable {
    var shoppingListId: String? = null
        private set
    var shoppingListName: String? = null
        private set
    var createdBy: String? = null
        private set

    @ServerTimestamp
    val date: Date? = null

    constructor() {}
    constructor(shoppingListId: String?, shoppingListName: String?, createdBy: String?) {
        this.shoppingListId = shoppingListId
        this.shoppingListName = shoppingListName
        this.createdBy = createdBy
    }
}