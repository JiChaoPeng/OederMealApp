package com.android.oedermealapp.bean

class FoodBean {
    var name: String
    var content: String
    var price: Int
    var image_url: String
    var address: String
    var room_id: Int
    var isChecked = false
    var num = 0
    var id = 0

    constructor(
        name: String,
        content: String,
        price: Int,
        image_url: String,
        address: String,
        room_id: Int,
        num: Int,
        id: Int
    ) {
        this.name = name
        this.content = content
        this.price = price
        this.image_url = image_url
        this.address = address
        this.room_id = room_id
        this.num = num
        this.id = id
    }

    constructor(
        name: String,
        content: String,
        price: Int,
        imageUrl: String,
        address: String,
        roomId: Int
    ) {
        this.name = name
        this.content = content
        this.price = price
        image_url = imageUrl
        this.address = address
        room_id = roomId
    }

}