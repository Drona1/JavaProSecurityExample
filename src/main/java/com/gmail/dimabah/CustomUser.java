package com.gmail.dimabah;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data @NoArgsConstructor
public class CustomUser {
    @Id
    @GeneratedValue
    private Long id;
    private String login;
    private String pass;
    @Enumerated (EnumType.STRING)
    private  UserRole role;
    private String email;
    private String phone;
    private String address;
    private String avatar;

    public CustomUser(String login, String pass, UserRole role, String email, String phone, String address) {
        this.login = login;
        this.pass = pass;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}
