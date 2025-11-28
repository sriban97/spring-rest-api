package com.green.springrestapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

@Data
@Entity
@Table(name = "app_user", uniqueConstraints = @UniqueConstraint(name = "fName_lName", columnNames = {"first_name", "last_name"}))
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @NotBlank
    @NotNull
    @Column(name = "first_name")
    @JsonProperty("firstName")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    @JsonProperty("lastName")
    private String lastName;


    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Column(name = "email")
    private String email;

    @Size(max = 10, min = 10)
    @Column(name = "phone")
    private String phone;

    @Size(max = 8, min = 3)
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "active")
    @JsonIgnore
    private char active = 'Y';

    @AssertTrue(message = "should not allow empty e-mail & phone!")
    private boolean isValidMailPhone() {
        return !ObjectUtils.isEmpty(email) || !ObjectUtils.isEmpty(phone);
    }
}
