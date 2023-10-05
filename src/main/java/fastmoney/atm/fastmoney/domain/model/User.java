package fastmoney.atm.fastmoney.domain.model;

import fastmoney.atm.fastmoney.domain.dto.user.UserRequestDto;
import fastmoney.atm.fastmoney.domain.dto.user.UserUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(unique = true)
    private String cpf;
    private String email;
    private String password;

    @Embedded
    private Account account;
    private boolean active;

    public User(UserRequestDto userData) {
        this.name = userData.name();
        this.cpf = userData.cpf();
        this.email = userData.email();
        this.password = userData.password();
        this.account = new Account(userData.accountBranch(), userData.pin());
        this.active = true;
    }

    public void update(UserUpdateDto userData) {
        this.name = userData.name();
        this.password = userData.password();
        account.update(userData.pin());
    }

    public void delete() {
        this.active = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return cpf;
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

    public void setPassword(String password) {
        this.password = password;
    }
}
