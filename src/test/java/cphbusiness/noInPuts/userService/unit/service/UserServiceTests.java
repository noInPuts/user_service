package cphbusiness.noInPuts.userService.unit.service;

import cphbusiness.noInPuts.userService.dto.UserDTO;
import cphbusiness.noInPuts.userService.model.User;
import cphbusiness.noInPuts.userService.repository.UserRepository;
import cphbusiness.noInPuts.userService.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void createUserShouldReturnUserDTO () {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(new User(1L, "name", "email", "phoneNumber", "address"));
        UserDTO userDTO = new UserDTO(1L, "name", "email", "phoneNumber", "address");

        // Act
        UserDTO userDTOPersisted = userService.createUser(new UserDTO(1L, "name", "email", "phoneNumber", "address"));

        // Assert
        assertEquals(userDTO.getId(), userDTOPersisted.getId());
        assertEquals(userDTO.getName(), userDTOPersisted.getName());
        assertEquals(userDTO.getEmail(), userDTOPersisted.getEmail());
        assertEquals(userDTO.getPhoneNumber(), userDTOPersisted.getPhoneNumber());
        assertEquals(userDTO.getAddress(), userDTOPersisted.getAddress());
    }

    @Test
    public void getUserShouldReturnUserDTO() throws Exception {
        // Arrange
        when(userRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(new User(1L, "name", "email", "phoneNumber", "address")));
        UserDTO userDTO = new UserDTO(1L, "name", "email", "phoneNumber", "address");

        // Act
        UserDTO userDTOPersisted = userService.getUser(1L);

        // Assert
        assertEquals(userDTO.getId(), userDTOPersisted.getId());
        assertEquals(userDTO.getName(), userDTOPersisted.getName());
        assertEquals(userDTO.getEmail(), userDTOPersisted.getEmail());
        assertEquals(userDTO.getPhoneNumber(), userDTOPersisted.getPhoneNumber());
        assertEquals(userDTO.getAddress(), userDTOPersisted.getAddress());
    }
}
