package fastmoney.atm.fastmoney.domain.repository;

import fastmoney.atm.fastmoney.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndActiveTrue(Long id);

    Page<User> findByActiveTrue(Pageable page);

    UserDetails findByCpf(String cpf);
}
