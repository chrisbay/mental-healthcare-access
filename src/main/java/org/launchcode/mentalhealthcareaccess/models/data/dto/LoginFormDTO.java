package org.launchcode.mentalhealthcareaccess.models.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginFormDTO {


        @NotNull
        @NotBlank
        @Size(min = 3, max = 18, message = "Make sure your username is between 3 and 18 characters.")
        private String email;

        @NotNull
        @NotBlank
        @Size(min = 8, max = 30, message = "Choose another password; it must be between 6 and 30 characters")
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

