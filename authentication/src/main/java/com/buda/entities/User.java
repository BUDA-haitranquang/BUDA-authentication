package com.buda.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.buda.entities.enumeration.PlanType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user", indexes = {
        @Index(columnList = "email", name = "user_email_index"),
        @Index(columnList = "phoneNumber", name = "user_phone_number_index"),
        @Index(columnList = "userName", name = "user_user_name_index")

})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userID;

    @Column(columnDefinition = "varchar(50) default (uuid())", name = "user_uuid")
    private String userUUID = UUID.randomUUID().toString();
    @Column(length = 30)
    private String userName;
    @Column(length = 128)
    private String password;
    @Column(length = 50)
    private String email;
    @Column(length = 15)
    private String phoneNumber;
    @Column(length = 20)
    private String lastName;
    @Column(length = 20)
    private String firstName;
    @Column(name = "enabled")
    private Boolean enabled = false;
    @Column(name = "plan_type", columnDefinition = "varchar(255) default 'BASIC'")
    @Enumerated(EnumType.STRING)
    private PlanType planType = PlanType.BASIC;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role",
               joinColumns = @JoinColumn(name = "user_id"), 
               inverseJoinColumns = @JoinColumn(name = "role_id"),
               indexes = {
                   @Index(name = "user_role_user_id", columnList = "user_id")
               }
    )
    private Collection<Role> roles = new ArrayList<Role>();
}
