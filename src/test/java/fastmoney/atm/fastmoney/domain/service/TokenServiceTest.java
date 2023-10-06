package fastmoney.atm.fastmoney.domain.service;

import fastmoney.atm.fastmoney.domain.model.Account;
import fastmoney.atm.fastmoney.domain.model.User;
import fastmoney.atm.fastmoney.infra.dto.TokenJwtDTO;
import fastmoney.atm.fastmoney.infra.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldReturnTokenDtoValid_WhenReceiveUserValid() {
        User user = createUser();
        tokenService.setSecret("test-secret");
        TokenJwtDTO tokenResult = new TokenJwtDTO(tokenService.generateToken(user));

        assertNotNull(tokenResult);
    }

    private User createUser() {
        return new User(1L,
                "Rennan",
                "152.656.530-74",
                "rennan@email.com",
                "1234",
                new Account("100",
                        2562,
                        BigDecimal.ZERO,
                        "1234"),
                true);
    }
}
