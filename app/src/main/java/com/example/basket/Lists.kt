package com.example.basket

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Lists (var shoppingListName: String? = null,
                  var createdBy : String ? = null,
                  @ServerTimestamp val date: Date? = null){
}