package fastmoney.atm.fastmoney.domain.dto.user;

import fastmoney.atm.fastmoney.domain.model.User;

public record UserTransactionDto(Long id, String name, String accountBranch, int accountNumber) {

    public UserTransactionDto(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getAccount().getBranch(),
                user.getAccount().getNumber());
    }
}
