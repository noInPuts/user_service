package cphbusiness.noInPuts.userService.service;

import cphbusiness.noInPuts.userService.dto.UserDTO;
import cphbusiness.noInPuts.userService.exception.UserNotFoundException;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUser(Long id) throws UserNotFoundException;
}
