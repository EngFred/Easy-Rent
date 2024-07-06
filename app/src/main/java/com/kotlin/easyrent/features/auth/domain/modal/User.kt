package com.kotlin.easyrent.features.auth.domain.modal

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val contactNumber: String,
    val address: String? = null,
    val profileImage: String? = null,
    val dateOfBirth: String? = null,
    val joinDate: Long = System.currentTimeMillis(),
    val about: String? = null
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        null,
        null,
        null
    )
}
