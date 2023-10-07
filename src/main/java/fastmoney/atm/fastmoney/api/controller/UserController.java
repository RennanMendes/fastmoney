package fastmoney.atm.fastmoney.api.controller;

import fastmoney.atm.fastmoney.domain.dto.user.UserRequestDto;
import fastmoney.atm.fastmoney.domain.dto.user.UserResponseDto;
import fastmoney.atm.fastmoney.domain.dto.user.UserUpdateDto;
import fastmoney.atm.fastmoney.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Lista todos os usuários")
    public  ResponseEntity<Page<UserResponseDto>> findAll(Pageable page){
        Page<UserResponseDto> userResponse = userService.findAll(page);
        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Busca usuário por id")
    public  ResponseEntity<UserResponseDto> findById(@PathVariable Long id){
        UserResponseDto userResponse = userService.findById(id);
        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo usuário")
    public ResponseEntity<UserResponseDto> create(@RequestBody @Valid UserRequestDto userData, UriComponentsBuilder uriBuilder){
        UserResponseDto userResponse =  userService.create(userData);
        URI uri = uriBuilder.path("/user/{id}").buildAndExpand(userResponse.id()).toUri();
       return  ResponseEntity.created(uri).body(userResponse);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Atualiza dados de um usuário")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto){
        UserResponseDto userResponse = userService.update(id,userUpdateDto);
        return ResponseEntity.ok().body(userResponse);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    @Operation(summary = "Exclui usuário")
    public ResponseEntity delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
