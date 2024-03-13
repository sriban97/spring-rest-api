package com.green.springrestapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

@Data
@Entity
@Table(name = "app_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotBlank
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @Size(max = 10, min = 10)
    private String phone;

    @Size(max = 8, min = 3)
    private String password;

    private String role;

    private char active = 'Y';


    @AssertTrue(message = "should not allow empty e-mail & phone!")
    private boolean isValidMailPhone() {
        return !ObjectUtils.isEmpty(email) || !ObjectUtils.isEmpty(phone);
    }
}
