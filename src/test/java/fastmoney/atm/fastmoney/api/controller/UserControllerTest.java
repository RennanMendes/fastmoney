package fastmoney.atm.fastmoney.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fastmoney.atm.fastmoney.domain.dto.account.AccountResponseDto;
import fastmoney.atm.fastmoney.domain.dto.user.UserRequestDto;
import fastmoney.atm.fastmoney.domain.dto.user.UserResponseDto;
import fastmoney.atm.fastmoney.domain.dto.user.UserUpdateDto;
import fastmoney.atm.fastmoney.domain.exception.UserNotFoundException;
import fastmoney.atm.fastmoney.domain.model.Account;
import fastmoney.atm.fastmoney.domain.model.User;
import fastmoney.atm.fastmoney.domain.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private static final String BASE_URL = "/users";
    private static final String NAME = "Rennan";
    private static final String CPF = "152.656.530-74";
    private static final String EMAIL = "rennan@email.com";

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Autowired
    UserControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void shouldReturnStatus200_WhenFindAllUsers() throws Exception {
        UserResponseDto user = createUserResponseDto(1L, NAME, CPF, EMAIL);
        Page<UserResponseDto> pagedResponse = new PageImpl<>(List.of(user));

        when(userService.findAll(any())).thenReturn(pagedResponse);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL));
        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
        Assertions.assertEquals(jsonResponse, objectMapper.writeValueAsString(pagedResponse));
    }

    @Test
    void shouldReturnStatus200_WhenFindUserById() throws Exception {
        Long id = 1L;
        UserResponseDto user = createUserResponseDto(id, NAME, CPF, EMAIL);

        when(userService.findById(id)).thenReturn(user);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/{id}", id));
        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
        Assertions.assertEquals(jsonResponse, objectMapper.writeValueAsString(user));
    }

    @Test
    void shouldReturnStatus404_WhenUserNotFound() throws Exception {
        Long id = 1L;
        when(userService.findById(id)).thenThrow(new UserNotFoundException());

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/{id}", id));

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus201_WhenUserCreated() throws Exception {
        UserRequestDto userRequest = createUserRequestDto(NAME, CPF, EMAIL);
        UserResponseDto userResponse = createUserResponseDto(1L, NAME, CPF, EMAIL);

        when(userService.create(userRequest)).thenReturn(userResponse);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(userResponse), jsonResponse);
        Assertions.assertEquals(HttpStatus.CREATED.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenUnnamedUser() throws Exception {
        UserRequestDto userRequest = createUserRequestDto("", CPF, EMAIL);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenUserWithoutEmail() throws Exception {
        UserRequestDto userRequest = createUserRequestDto(NAME, CPF, "");

        ResultActions resultActions = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenUserWithoutCPF() throws Exception {
        UserRequestDto userRequest = createUserRequestDto(NAME, "", EMAIL);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus200_WhenUserUpdated() throws Exception {
        Long id = 1L;
        UserUpdateDto userRequest = new UserUpdateDto(NAME, "1234", "1234");
        UserResponseDto userResponse = createUserResponseDto(id, NAME, CPF, EMAIL);

        when(userService.update(id, userRequest)).thenReturn(userResponse);

        ResultActions resultActions = mockMvc.perform(put(BASE_URL + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(userResponse), jsonResponse);
        Assertions.assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus204_WhenUserDeleted() throws Exception {
        Long id = 1L;

        ResultActions resultActions = mockMvc.perform(delete(BASE_URL + "/{id}", id));

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), resultActions.andReturn().getResponse().getStatus());
    }

    private UserRequestDto createUserRequestDto(String name, String cpf, String email) {
        return new UserRequestDto(name, cpf, email, "1234", "100", "1234");
    }


    private UserResponseDto createUserResponseDto(Long id, String name, String cpf, String email) {
        return new UserResponseDto(id,
                name,
                cpf,
                email,
                new AccountResponseDto("100", 8540, BigDecimal.ZERO),
                true);
    }

}