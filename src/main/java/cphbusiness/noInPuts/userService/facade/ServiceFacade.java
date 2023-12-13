package cphbusiness.noInPuts.userService.facade;

import cphbusiness.noInPuts.userService.dto.UserDTO;
import cphbusiness.noInPuts.userService.exception.NotAllowedException;
import cphbusiness.noInPuts.userService.exception.UserNotFoundException;

public interface ServiceFacade {
    UserDTO createUser(UserDTO userDTO, String jwtToken) throws NotAllowedException;
    UserDTO getUser(String jwtToken) throws UserNotFoundException;;
}
