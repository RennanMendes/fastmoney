package fastmoney.atm.fastmoney.api.controller;

import fastmoney.atm.fastmoney.domain.dto.user.LoginDTO;
import fastmoney.atm.fastmoney.domain.model.User;
import fastmoney.atm.fastmoney.infra.dto.TokenJwtDTO;
import fastmoney.atm.fastmoney.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    @Operation(summary = "Faz login na aplicação")
    public ResponseEntity login(@RequestBody @Valid LoginDTO loginDTO) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.login(), loginDTO.password());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenJwtDTO(tokenJWT));
    }

}
