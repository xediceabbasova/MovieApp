package com.company.movieapp.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import org.hibernate.annotations.GenericGenerator

@Entity
data class MovieDetails @JvmOverloads constructor(

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    val id: String? = "",

    val description: String?,
    val durationMinutes: Int?,
    val imageUrl: String?,

    @ElementCollection
    val actors: List<String>? = ArrayList(),

    @OneToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    @JsonBackReference
    val movie: Movie


) {
    constructor(description: String, durationMinutes: Int, imageUrl: String, actors: List<String>, movie: Movie) :
            this("", description, durationMinutes, imageUrl, actors, movie)
}