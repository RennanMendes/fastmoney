package fastmoney.atm.fastmoney.domain.model;

import fastmoney.atm.fastmoney.domain.dto.user.UserRequestDto;
import fastmoney.atm.fastmoney.domain.dto.user.UserUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
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
        this.email= userData.email();
        this.password = userData.password();
        this.account  = new Account(userData.accountBranch(), userData.pin());
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
}
