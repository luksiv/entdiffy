package com.luksiv.entdiffy.entities

import com.luksiv.entdiffy.annotations.DiffEntity

@DiffEntity
data class Person(
    val firstName: String,
    val lastName: String,
    val address: String,
    val country: String,
    val age: String,
)