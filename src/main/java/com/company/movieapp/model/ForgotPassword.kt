package com.company.movieapp.model

import jakarta.persistence.*
import java.util.Date

@Entity
data class ForgotPassword @JvmOverloads constructor(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val fpid: Long? = null,

    @Column(nullable = false)
    val otp: Int,

    @Column(name = "expiration_time", nullable = false)
    val expirationTime: Date,

    @OneToOne
    val user: User

)
