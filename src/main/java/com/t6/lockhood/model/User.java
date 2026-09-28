package com.t6.lockhood.model;

import lombok.*;
import org.springframework.lang.NonNull;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends SuperEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String userName;
    @NotNull
    private String password;

    @NotNull
    private String privilege;

    @NonNull
    //@Email(regexp = ".+@.+\\..+")
    private String email;


}
