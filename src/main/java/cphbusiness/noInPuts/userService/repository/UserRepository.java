package cphbusiness.noInPuts.userService.repository;

import cphbusiness.noInPuts.userService.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
