package ru.itmentor.spring.boot_security.demo.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
@Table(name = "security")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "users_db_seq", allocationSize = 1)
    private Long id;
    @NotBlank(message = "Enter name")
    @Column(length = 20)
    private String name;
    @NotBlank(message = "Enter lastName")
    @Column(length = 30, name = "last_name")
    private String lastName;
    @NotBlank(message = "Enter e-mail")
    @Email(message = "Please enter a valid e-mail")
    @Column(length = 50, unique = true)
    private String email;
    @NotBlank(message = "Enter password")
    @Column(unique = true)
    @Size(min = 6, message = "Password must be more than 6 characters")
    private String password;

    public User(String lastName, String name, String email) {
        this.lastName = lastName;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); //роли
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
