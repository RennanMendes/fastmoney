package fastmoney.atm.fastmoney.domain.dto.user;

import fastmoney.atm.fastmoney.domain.dto.account.AccountResponseDto;
import fastmoney.atm.fastmoney.domain.model.User;

public record UserResponseDto(
        Long id,
        String name,
        String cpf,
        String email,
        AccountResponseDto accountDto,
        boolean active
) {
    public UserResponseDto(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getCpf(),
                user.getEmail(),
                new AccountResponseDto(user.getAccount()),
                user.isActive()
        );
    }
}
