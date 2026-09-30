package com.alagamb.petcompose.data.model.user

import com.alagamb.petcompose.data.db.table.user.UserTable

data class User(
    val id: Long = 0,
    val username: String,
    val email: String
)

fun UserTable.toDomain(): User = User(
    id = id,
    username = username,
    email = email
)

fun User.toEntity(password: String): UserTable = UserTable(
    id = id,
    username = username,
    email = email,
    password = password
)
