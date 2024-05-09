package com.company.movieapp.elasticsearch.model

import com.company.movieapp.model.Genre
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.Setting

@Document(indexName = "movies")
@Setting(settingPath = "static/es-settings.json")
@JsonIgnoreProperties(ignoreUnknown = true)
data class MovieDocument(

    @Id
    val id: String? = null,

    @Field(name = "title", type = FieldType.Text, analyzer = "custom_index", searchAnalyzer = "custom_search")
    val title: String,

    @Field(name = "director", type = FieldType.Text, analyzer = "custom_index", searchAnalyzer = "custom_search")
    val director: String,

    @Field(name = "releaseYear", type = FieldType.Integer)
    val releaseYear: Int,

    @Field(name = "genre", type = FieldType.Keyword)
    val genre: Genre,

    @Field(name = "actors", type = FieldType.Text, analyzer = "custom_index", searchAnalyzer = "custom_search")
    val actors: List<String>
)
