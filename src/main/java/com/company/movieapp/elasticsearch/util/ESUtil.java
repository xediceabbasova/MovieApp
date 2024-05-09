package com.company.movieapp.elasticsearch.util;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import com.company.movieapp.elasticsearch.dto.SearchRequestDto;

import java.util.function.Supplier;

public class ESUtil {

    public static Query createMatchAllQuery() {
        return Query.of(q -> q.matchAll(new MatchAllQuery.Builder().build()));
    }

    public static Supplier<Query> buildQueryForFieldAndValue(String fieldName, String searchValue) {
        return () -> Query.of(q -> q.match(new MatchQuery.Builder()
                .field(fieldName)
                .query(searchValue)
                .build()));
    }

    public static Supplier<Query> createBoolQuery(SearchRequestDto request) {
        return () -> {
            String fieldName1 = request.fieldName().get(0);
            String searchValue1 = request.searchValue().get(0);
            String fieldName2 = request.fieldName().get(1);
            String searchValue2 = request.searchValue().get(1);

            return Query.of(q -> q.bool(new BoolQuery.Builder()
                    .filter(matchQuery(fieldName1, searchValue1))
                    .must(termQuery(fieldName2, searchValue2))
                    .build()));
        };
    }

    public static Query matchQuery(String field, String value) {
        return Query.of(q -> q.match(new MatchQuery.Builder()
                .field(field)
                .query(value)
                .build()));
    }

    public static Query termQuery(String field, String value) {
        return Query.of(q -> q.term(new TermQuery.Builder()
                .field(field)
                .value(value)
                .build()));
    }

    public static Supplier<Query> createRangeQuery(int startYear, int endYear) {
        return () -> Query.of(q -> q.range(new RangeQuery.Builder()
                .field("releaseYear")
                .gte(JsonData.of(startYear))
                .lt(JsonData.of(endYear))
                .build()));
    }

    public static Query buildAutoSuggestQuery(String title) {
        return Query.of(q -> q.match(new MatchQuery.Builder()
                .field("title")
                .query(title)
                .analyzer("custom_index")
                .build()));
    }

}
