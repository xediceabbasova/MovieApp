package com.company.movieapp.model

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity
data class Movie @JvmOverloads constructor(

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    val id: String? = "",

    val title: String,
    val director: String,
    val releaseYear: Int,

    @Enumerated(EnumType.STRING)
    val genre: Genre,
)