package com.example.team8forum.models.dtos;

import java.util.Date;

public class FilterPostDto {

    private String title;
    private Integer minLikes;
    private Integer maxLike;
    private Date creationDate;
    private String sortBy;
    private String sortOrder;

    public FilterPostDto(){
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMinLikes() {
        return minLikes;
    }

    public void setMinLikes(int minLikes) {
        this.minLikes = minLikes;
    }

    public int getMaxLike() {
        return maxLike;
    }

    public void setMaxLike(int maxLike) {
        this.maxLike = maxLike;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
