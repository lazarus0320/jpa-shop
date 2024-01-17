package com.example.jpashop.entity;

import com.example.jpashop.constant.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(Long id, String name, String email, String password, String address, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
    }
}
