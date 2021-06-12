package com.example.basket.models

class ProductModel {
    var productId: String? = null
        private set
    var productName: String? = null
        private set
    var shoppingListId: String? = null
        private set

    constructor() {}
    constructor(productId: String?, productName: String?, shoppingListId: String?) {
        this.productId = productId
        this.productName = productName
        this.shoppingListId = shoppingListId
    }
}