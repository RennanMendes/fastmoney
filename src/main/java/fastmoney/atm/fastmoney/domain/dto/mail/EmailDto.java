package fastmoney.atm.fastmoney.domain.dto.mail;

import java.math.BigDecimal;

public record EmailDto(
        String name,
        BigDecimal amount,
        String to
) {
}
