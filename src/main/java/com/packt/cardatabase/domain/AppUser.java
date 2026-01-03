package com.packt.cardatabase.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;

@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)

    // update=false: 나중에 update불가능, 한번 설정되면 변경 불가(pk인 ID에는 당연히 설정) => SQL "UPDATE"로 변경 불가능
    @Column(nullable=false, updatable=false)
    private Long id;

    // unique=true: no two rows can have the same value.
    // username이 고유하도록 설정(하지만 update는 가능 => SQL "UPDATE"로 변경 가능)
    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String role;

    public AppUser() {

    }

    public AppUser(String username, String password, String role) {
        super(); //
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
