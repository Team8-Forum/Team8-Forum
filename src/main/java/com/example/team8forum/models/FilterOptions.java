package com.example.team8forum.models;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.Date;
import java.util.Optional;

public class FilterOptions {
    private Optional<String> title;
    private Optional<Integer> minLikes;
    private Optional<Integer> maxLikes;
    private Optional<Date> creationDate;

    private Optional<String> sortBy;

    private Optional<String> sortOrder;

    public FilterOptions() {
        this(null, null, null, null, null, null);
    }

    public FilterOptions(
            String title,
            Integer minLikes,
            Integer maxLikes,
            Date creationDate,
            String sortBy,
            String sortOrder) {
        this.title = Optional.ofNullable(title);
        this.minLikes = Optional.ofNullable(minLikes);
        this.maxLikes = Optional.ofNullable(maxLikes);
        this.creationDate = Optional.ofNullable(creationDate);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<Integer> getMinLikes() {
        return minLikes;
    }

    public Optional<Integer> getMaxLikes() {
        return maxLikes;
    }

    public Optional<Date> getCreationDate() {
        return creationDate;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
