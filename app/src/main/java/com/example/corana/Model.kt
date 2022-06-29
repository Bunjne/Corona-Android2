package com.example.corana

data class Info(
    var id: Int,
    var userId: String,
    var code: String,
    var fullNameEn: String,
    var fullNameTh: String,
    var gender: String,
    var profileImageUrl: String
) {

}

data class QrCode(var qrCode: String, var status: String)

data class ErrorModel(var statusCode: String, var id: String, var description: String)