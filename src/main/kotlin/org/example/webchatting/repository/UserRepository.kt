package org.example.webchatting.repository

import org.example.webchatting.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByOauthId(oauthId: String): UserEntity?
}