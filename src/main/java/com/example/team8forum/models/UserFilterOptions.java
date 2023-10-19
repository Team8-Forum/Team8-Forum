package com.example.team8forum.models;

import java.util.Optional;

public class UserFilterOptions {

    private Optional<String> firstName;

    private Optional<String> lastName;

    private Optional<String> email;

    private Optional<String> username;
    private Optional<String> sortBy;

    private Optional<String> sortOrder;

    public UserFilterOptions(String firstName,
            String lastName,
            String email,
            String username,
            String sortBy,
            String sortOrder) {
        this.firstName = Optional.ofNullable(firstName);
        this.lastName = Optional.ofNullable(lastName);
        this.email = Optional.ofNullable(email);
        this.username = Optional.ofNullable(username);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public Optional<String> getLastName() {
        return lastName;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
