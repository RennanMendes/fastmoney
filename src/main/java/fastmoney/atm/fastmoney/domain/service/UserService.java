package fastmoney.atm.fastmoney.domain.service;

import fastmoney.atm.fastmoney.domain.dto.user.UserRequestDto;
import fastmoney.atm.fastmoney.domain.dto.user.UserResponseDto;
import fastmoney.atm.fastmoney.domain.dto.user.UserUpdateDto;
import fastmoney.atm.fastmoney.domain.exception.UserNotFoundException;
import fastmoney.atm.fastmoney.domain.model.User;
import fastmoney.atm.fastmoney.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {


    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponseDto create(UserRequestDto userData) {
        User user = new User(userData);
        String password = encryptPassword(userData.password());
        user.setPassword(password);

        return new UserResponseDto(repository.save(user));
    }

    public UserResponseDto findById(Long id) {
        User user = this.findByIdAndActiveTrue(id);
        return new UserResponseDto(user);
    }

    public Page<UserResponseDto> findAll(Pageable page) {
        Page<User> users = repository.findByActiveTrue(page);
        return users.map(UserResponseDto::new);
    }

    @Transactional
    public UserResponseDto update(Long id, UserUpdateDto userData) {
        User user = this.findByIdAndActiveTrue(id);
        user.update(userData);

        return new UserResponseDto(user);
    }

    @Transactional
    public void delete(Long id) {
        User user = this.findByIdAndActiveTrue(id);
        user.delete();
    }

    public User findByIdAndActiveTrue(Long id) {
        return  repository.findByIdAndActiveTrue(id).orElseThrow(UserNotFoundException::new);
    }

    public String encryptPassword(String password){
       return passwordEncoder.encode(password);
    }

}
