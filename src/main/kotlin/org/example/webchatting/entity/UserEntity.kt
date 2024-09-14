package org.example.webchatting.entity

import jakarta.persistence.*

@Entity
@Table(name = "user")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val oauthId: String,
    val name: String
) {
    constructor(oauthId: String, name: String) : this(null, oauthId, name)
}