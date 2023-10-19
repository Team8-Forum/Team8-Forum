package com.example.team8forum.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import org.springframework.data.annotation.Persistent;

import java.util.*;


@Entity
@Table(name = "posts")

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private int id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @OneToMany
    @JoinColumn(name = "post_id")
    private List<Comment> comments;

    @Column(name = "creation_date")
    private Date creationDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "posts_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likes;
    public Post() {
    }

    public Post(User createdBy, String title, String content) {
        this.createdBy = createdBy;
        this.title = title;
        this.content = content;
        this.comments = new ArrayList<>();
        this.creationDate = new Date();
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Set<User> getLikes() {
        return likes;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id && likes == post.likes && Objects.equals(createdBy, post.createdBy) && Objects.equals(title, post.title) && Objects.equals(content, post.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdBy, title, content, likes);
    }
}
