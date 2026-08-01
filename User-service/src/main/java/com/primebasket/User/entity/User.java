package com.primebasket.User.entity;

import com.primebasket.User.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;


    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String mobileNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Boolean isActive;

    private Boolean emailVerified;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY)
    private List<Address> addresses=new ArrayList<>();

}
