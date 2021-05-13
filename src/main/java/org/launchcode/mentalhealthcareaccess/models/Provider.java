package org.launchcode.mentalhealthcareaccess.models;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
public class Provider extends AbstractEntity {
    private String companyName;
    private String displayName;
    private String lastName;

    private String phoneNumber;
    @NotBlank
    private String firstName;
    @NotNull
    private String email;
    @NotNull
    private String pwHash;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public Provider(){ }
    public Provider(String displayName, String companyName, String firstName, String lastName, String email, String phoneNumber, String password) {
        this.companyName = companyName;
        this.firstName = firstName;
        this.email = email;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.pwHash = encoder.encode(password);
        this.displayName = displayName;
        }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

}

