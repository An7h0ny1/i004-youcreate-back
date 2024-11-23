package com.igrowker.miniproject.User.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.igrowker.miniproject.Collaborator.Model.Collaborator;

import com.igrowker.miniproject.Income.Model.Income;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "last_name")
    private String lastName;

    private String password;

    @Column(name = "confirm_password")
    @Transient
    private String confirmPassword;

    @Column(unique = true)
    @JsonProperty("email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String role;

    private String profilePhotoPath;


    private String country;

    private Double taxRate;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Collaborator> collaborators;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "account_no_expired")
    private boolean accountNoExpired;

    @Column(name = "account_no_locked")
    private boolean accountNoLocked;

    @Column(name = "credentials_no_expired")
    private boolean credentialsNoExpired;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Income> incomes;

    /*@ManyToMany(fetch = FetchType.EAGER , targetEntity = Role.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();*/
}
