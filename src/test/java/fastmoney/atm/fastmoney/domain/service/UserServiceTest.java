package fastmoney.atm.fastmoney.domain.service;

import fastmoney.atm.fastmoney.domain.dto.user.UserRequestDto;
import fastmoney.atm.fastmoney.domain.dto.user.UserResponseDto;
import fastmoney.atm.fastmoney.domain.dto.user.UserUpdateDto;
import fastmoney.atm.fastmoney.domain.exception.UserNotFoundException;
import fastmoney.atm.fastmoney.domain.model.Account;
import fastmoney.atm.fastmoney.domain.model.User;
import fastmoney.atm.fastmoney.domain.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    private static final Long ID = 1L;

    @Mock
    private UserRepository repository;

    @InjectMocks
    UserService userService;

    @Test
    void shouldReturnUserResponseDto_whenCreateCorrectly() {
        UserRequestDto userRequest = createUserRequestDto();
        User user = createUser();
        UserResponseDto expectedResponse = createUserResponseDto(user);

        when(repository.save(any())).thenReturn(user);

        UserResponseDto userResponse = userService.create(userRequest);

        assertEquals(expectedResponse, userResponse);
    }

    @Test
    void shouldReturnUserResponseDto_whenFoundByValidId() {
        User user = createUser();
        UserResponseDto expectedResponse = createUserResponseDto(user);

        when(repository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(user));

        UserResponseDto userResponse = userService.findById(ID);

        assertEquals(expectedResponse, userResponse);
    }

    @Test
    void shouldReturnUserNotFound_whenFoundByInvalidId() {
        UserNotFoundException error = Assertions.assertThrows(UserNotFoundException.class, () -> userService.findById(ID));

        Assertions.assertTrue(error instanceof UserNotFoundException);
    }

    @Test
    void shouldReturnUserResponseDto_whenFoundAll() {
        Pageable pageable = PageRequest.of(0, 20);

        User user = createUser();
        Page<User> pageUsers = new PageImpl<>(List.of(user));
        Page<UserResponseDto> ExpectedPage = new PageImpl<>(List.of(new UserResponseDto(user)));

        Mockito.when(repository.findByActiveTrue(pageable)).thenReturn(pageUsers);

        Page<UserResponseDto> pageResponse = userService.findAll(pageable);

        Assertions.assertEquals(ExpectedPage, pageResponse);
    }

    @Test
    void shouldReturnUserResponseDto_whenUpdatedUser() {
        UserUpdateDto userUpdateDto = new UserUpdateDto("Rennan", "1234", "1234");
        User user = createUser();
        UserResponseDto expectedResponse = createUserResponseDto(user);

        when(repository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(user));

        UserResponseDto response = userService.update(ID, userUpdateDto);

        Assertions.assertEquals(expectedResponse, response);
    }

    private UserRequestDto createUserRequestDto() {
        return new UserRequestDto("Rennan", "152.656.530-74", "rennan@email.com", "1234", "100", "1234");
    }

    private User createUser() {
        return new User(ID,
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

    private UserResponseDto createUserResponseDto(User user) {
        return new UserResponseDto(user);
    }

}