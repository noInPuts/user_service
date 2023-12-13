package cphbusiness.noInPuts.userService.facade;

import cphbusiness.noInPuts.userService.dto.UserDTO;
import cphbusiness.noInPuts.userService.exception.NotAllowedException;
import cphbusiness.noInPuts.userService.exception.UserNotFoundException;
import cphbusiness.noInPuts.userService.service.JwtService;
import cphbusiness.noInPuts.userService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ServiceFacadeImpl implements ServiceFacade{

    private final JwtService jwtService;
    private final UserService userService;
    @Autowired
    public ServiceFacadeImpl(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO, String jwtToken) throws NotAllowedException {
        Long userIdFromToken = jwtService.getUserIdFromToken(jwtToken);

        if(!userIdFromToken.equals(userDTO.getId())) {
            throw new NotAllowedException("You are not allowed to create a user with this id");
        }

        return userService.createUser(userDTO);
    }

    @Override
    public UserDTO getUser(String jwtToken) throws UserNotFoundException {
        Long userIdFromToken = jwtService.getUserIdFromToken(jwtToken);

        return userService.getUser(userIdFromToken);
    }
}
