package cphbusiness.noInPuts.userService.controller;


import cphbusiness.noInPuts.userService.dto.UserDTO;
import cphbusiness.noInPuts.userService.exception.UserNotFoundException;
import cphbusiness.noInPuts.userService.service.JwtService;
import cphbusiness.noInPuts.userService.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/user/create", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO postUserDTO, @Valid @CookieValue("jwt-token") String jwtToken) {
        Long userIdFromToken = jwtService.getUserIdFromToken(jwtToken);

        if(!userIdFromToken.equals(postUserDTO.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        UserDTO userDTO = userService.createUser(postUserDTO);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/user", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> getUser(@Valid @CookieValue("jwt-token") String jwtToken) {
        Long userIdFromToken = jwtService.getUserIdFromToken(jwtToken);

        try {
            UserDTO userDTO = userService.getUser(userIdFromToken);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
