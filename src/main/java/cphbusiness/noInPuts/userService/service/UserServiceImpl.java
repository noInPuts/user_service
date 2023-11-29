package cphbusiness.noInPuts.userService.service;

import cphbusiness.noInPuts.userService.dto.UserDTO;
import cphbusiness.noInPuts.userService.exception.UserNotFoundException;
import cphbusiness.noInPuts.userService.model.User;
import cphbusiness.noInPuts.userService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = new User(userDTO.getId(), userDTO.getName(), userDTO.getEmail(), userDTO.getPhoneNumber(), userDTO.getAddress());
        User userEntity = userRepository.save(user);

        return new UserDTO(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), userEntity.getPhoneNumber(), userEntity.getAddress());
    }

    public UserDTO getUser(Long id) throws UserNotFoundException {
        Optional<User> userOptional =  userRepository.findById(id);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPhoneNumber(), user.getAddress());
        } else {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
    }
}
