package com.example.basket.models

class UserModel {
    var userEmail: String? = null
        private set
    var tokenId: String? = null
        private set

    constructor() {}
    constructor(userEmail: String?, tokenId: String?) {
        this.userEmail = userEmail
        this.tokenId = tokenId
    }
}