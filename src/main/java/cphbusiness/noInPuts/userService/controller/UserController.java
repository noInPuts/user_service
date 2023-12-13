package cphbusiness.noInPuts.userService.controller;


import cphbusiness.noInPuts.userService.dto.UserDTO;
import cphbusiness.noInPuts.userService.exception.NotAllowedException;
import cphbusiness.noInPuts.userService.exception.UserNotFoundException;
import cphbusiness.noInPuts.userService.facade.ServiceFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final ServiceFacade serviceFacade;

    @Autowired
    public UserController(ServiceFacade serviceFacade) {
        this.serviceFacade = serviceFacade;
    }

    @PostMapping(value = "/user/create", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO postUserDTO, @Valid @CookieValue("jwt-token") String jwtToken) {
        try {
            UserDTO userDTO = serviceFacade.createUser(postUserDTO, jwtToken);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NotAllowedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(value = "/user", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> getUser(@Valid @CookieValue("jwt-token") String jwtToken) {
        try {
            UserDTO userDTO = serviceFacade.getUser(jwtToken);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
