package com.pwc.todoworksapce.member.entity;

import com.pwc.todoworksapce.base.BaseEntity;
import com.pwc.todoworksapce.member.Role;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "members")
@Getter
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public Member(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = Role.USER;
    }

    protected Member() {

    }
}