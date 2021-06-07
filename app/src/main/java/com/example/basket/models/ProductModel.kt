package com.example.basket.models

class ProductModel {
    var productId: String? = null
        private set
    var productName: String? = null
        private set
    var izInShoppingList: Boolean? = null
        private set

    constructor() {}
    constructor(productId: String?, productName: String?, izInShoppingList: Boolean?) {
        this.productId = productId
        this.productName = productName
        this.izInShoppingList = izInShoppingList
    }
}