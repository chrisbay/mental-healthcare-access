package org.launchcode.mentalhealthcareaccess.models;

import javax.persistence.Entity;

@Entity
public class User extends AbstractUser {

    public User() {};

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pwHash = encoder.encode(password);
    }

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwHash);
    }
}