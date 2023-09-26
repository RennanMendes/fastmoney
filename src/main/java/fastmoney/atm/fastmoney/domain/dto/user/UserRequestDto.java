package fastmoney.atm.fastmoney.domain.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.br.CPF;

public record UserRequestDto(
        @NotBlank
        String name,
        @CPF
        String cpf,
        @Email
        String email,
        @NotBlank
        String password,
        @NotBlank
        String accountBranch,
        @NotBlank
        String pin
) {

}
