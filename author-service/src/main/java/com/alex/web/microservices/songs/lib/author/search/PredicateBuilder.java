package com.alex.web.microservices.songs.lib.author.search;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PredicateBuilder {
    private List<Predicate> predicates = new ArrayList<>();

    public static PredicateBuilder builder() {

        return new PredicateBuilder();
    }

    public <T> PredicateBuilder add(T value, Function<T, Predicate> function) {
        if (value != null) {
            predicates.add(function.apply(value));
        }
        return this;
    }

    public Predicate buildAnd() {
        return Optional.ofNullable(ExpressionUtils.allOf(predicates)).orElse(Expressions.TRUE);
    }

    public Predicate buildOr() {
        return Optional.ofNullable(ExpressionUtils.anyOf(predicates)).orElse(Expressions.TRUE);
    }
}
