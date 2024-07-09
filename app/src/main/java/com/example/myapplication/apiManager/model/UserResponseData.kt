package com.example.myapplication.apiManager.model

data class UserResponseData(val users: List<User>? = null, val total: Int , val skip: Int, val limit: Int)

data class User(var id: Int , var firstName: String? = null, var lastName: String? = null, var maidenName: String,
    var age: Int? = null, var gender: String? = null, var email: String? = null, var phone: String? = null, var username: String? = null,
    var password: String? = null, var birthDate: String? = null, var image: String? = null, var bloodGroup: String? = null,
    var height: Double? = null, var weight: Double? = null, var eyeColor: String? = null, var hair: Hair? = null, var ip: String? = null,
    var address: Address? = null, var macAddress: String? = null, var university: String? = null, var bank: Bank? = null,
    var company: Company? = null, var ein: String? = null, var ssn: String? = null, var userAgent: String? = null,
    var crypto: Crypto? = null, var role: String? = null)

data class Hair(var color: String, var type: String)

data class Crypto(var coin: String? = null, var wallet: String? = null, var network: String? = null)

data class Company(var department: String? = null, var name: String? = null, var title: String? = null, var address: Address? = null)

data class Bank(var cardExpire: String? = null, var cardNumber: String? = null, var cardType: String? = null, var currency: String? = null,
    var iban: String? = null)

data class Address(var address: String? = null, var city: String? = null, var state: String? = null, var stateCode: String? = null,
    var postalCode: String? = null, var coordinates: Coordinates? = null, var country: String? = null)

data class Coordinates(var lat: Double? = null, var lng: Double? = null)


