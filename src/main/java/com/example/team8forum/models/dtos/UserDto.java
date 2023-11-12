package com.example.team8forum.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDto {

    @NotNull(message = "First name can't be empty")
    @Size(min = 4, max = 32)
    private String firstName;

    @NotNull(message = "Last name can't be empty")
    @Size(min = 4, max = 32)
    private String lastName;

    @NotNull(message = "Email can't be empty")
    private String email;

    public UserDto(){
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
