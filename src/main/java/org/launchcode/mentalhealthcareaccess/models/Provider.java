package org.launchcode.mentalhealthcareaccess.models;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Provider extends AbstractUser {
    private String companyName;
    private String displayName;
    private Languages lang;
    private String phoneNumber;
    @ElementCollection
    private List<String> insurance;
    @ElementCollection(targetClass = Languages.class)
    @CollectionTable(name = "provider_languages",
            joinColumns = @JoinColumn(name = "provider_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "languages_supported")
    private final List<Languages> languages = new ArrayList<>();

    public Provider() {
    }

    public Provider(String displayName, String companyName, String firstName, String lastName, String email, String phoneNumber, String password, Languages lang) {
        this.companyName = companyName;
        this.firstName = firstName;
        this.email = email;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.pwHash = encoder.encode(password);
        this.displayName = displayName;
        this.lang = lang;
    }

    public Languages getLang() {
        return lang;
    }

    public void setLang(Languages lang) {
        this.lang = lang;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDisplayName() {
        return displayName;
    }


    public void setDisplayName(String displayName) {
        if (firstName != "" && lastName != "") {
            this.displayName = lastName + ", " + firstName;
        } else {

            this.displayName = companyName;
        }
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPwHash() {
        return pwHash;
    }

    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }

    public static BCryptPasswordEncoder getEncoder() {
        return encoder;
    }

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwHash);
    }

    public List<Languages> getLanguages() {
        return languages;
    }

    public void addLanguages(Languages languages) {

        this.languages.add(languages);
    }

    public List<String> getInsurance() {
        return insurance;
    }

    public void setInsurance(List<String> insurance) {
        this.insurance = insurance;
    }

    public void addInsurance(List<String> insurance) {

        this.insurance.add(String.valueOf(insurance));
    }
}



