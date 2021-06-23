package org.launchcode.mentalhealthcareaccess.models.data.dto;

import org.launchcode.mentalhealthcareaccess.models.Languages;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class RegisterFormDTO extends LoginFormDTO {
    @NotBlank
    private String verifyPassword;
    private String firstName;
    private String lastName;
    private String companyName;
    private String displayName;
    private Languages lang;
    @NotBlank
    @Pattern(regexp = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$", message = "please enter a valid phone number")
    private String phoneNumber;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getDisplayName() {
        if (firstName != "" && lastName != "") {
             displayName = lastName + ", " + firstName;
        }
        else {
             displayName = companyName;
        }
        return displayName;
    }


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

}
