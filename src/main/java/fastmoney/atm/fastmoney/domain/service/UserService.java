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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public UserResponseDto create(UserRequestDto userData) {
        User user = repository.save(new User(userData));
        return new UserResponseDto(user);
    }

    public UserResponseDto findById(Long id) {
        User user = this.findByIdAndActiveTrue(id);
        return new UserResponseDto(user);
    }

    public Page<UserResponseDto> findAll(Pageable page) {
        Page<User> users = repository.findByActiveTrue(page);
        return users.map(user -> new UserResponseDto(user));
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
        User user = repository.findByIdAndActiveTrue(id).orElseThrow(() -> new UserNotFoundException());
        return user;
    }
}
