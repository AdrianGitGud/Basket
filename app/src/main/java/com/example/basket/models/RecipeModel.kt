package com.example.basket.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class RecipeModel {
    var recipeId: String? = null
        private set
    var recipeName: String? = null
        private set
    var recipeDesc: String? = null
        private set
    var recipeIngredientes: String? = null
        private set

    @ServerTimestamp
    val date: Date? = null

    constructor() {}
    constructor(recipeId: String?, recipeName: String?, recipeDesc: String?, recipeIngredientes: String?) {
        this.recipeId = recipeId
        this.recipeName = recipeName
        this.recipeDesc = recipeDesc
        this.recipeIngredientes = recipeIngredientes
    }
}