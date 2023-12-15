package cphbusiness.noInPuts.userService.unit.controller;

import cphbusiness.noInPuts.userService.controller.UserController;
import cphbusiness.noInPuts.userService.dto.UserDTO;
import cphbusiness.noInPuts.userService.exception.NotAllowedException;
import cphbusiness.noInPuts.userService.facade.ServiceFacade;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import net.datafaker.Faker;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceFacade serviceFacade;

    @Value("${jwt.secret}")
    private String pKey;

    @Test
    public void createUser() throws Exception {
        // Arrange
        // Creating faker object
        Faker faker = new Faker();

        // Creating a map with the data we want to send in the request
        Map<String, Object> jsonRequestMap = new HashMap<>();
        jsonRequestMap.put("username", "my_test_user");
        jsonRequestMap.put("id", 1L);
        jsonRequestMap.put("name", faker.name().fullName());
        jsonRequestMap.put("address", faker.address().fullAddress());
        jsonRequestMap.put("phoneNumber", faker.phoneNumber().cellPhone());
        jsonRequestMap.put("email", faker.internet().emailAddress());

        // Mocking the userService.createUser method to return a UserDTO object
        UserDTO userMockObject = new UserDTO(Long.parseLong(jsonRequestMap.get("id").toString()), jsonRequestMap.get("name").toString(), jsonRequestMap.get("email").toString(), jsonRequestMap.get("phoneNumber").toString(), jsonRequestMap.get("address").toString());
        when(serviceFacade.createUser(any(UserDTO.class), any(String.class))).thenReturn(userMockObject);

        // Converting the map to a JSON object
        JSONObject jsonObject = new JSONObject(jsonRequestMap);
        String jsonRequestData = jsonObject.toString();

        Cookie cookie = getCookie(1L);

        // Act and Assert
        MvcResult result = this.mockMvc.perform(post("/user/create").content(jsonRequestData).contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), "{\"id\":1,\"name\":\"" + jsonRequestMap.get("name").toString() + "\",\"email\":\"" + jsonRequestMap.get("email").toString() + "\",\"phoneNumber\":\"" + jsonRequestMap.get("phoneNumber").toString() + "\",\"address\":\"" + jsonRequestMap.get("address").toString() + "\"}");
    }

    @Test
    public void createUserShouldReturn400BadRequestWhenParsingBadRequest() throws Exception {
        // Act and Assert
        this.mockMvc.perform(post("/user/create").content("{ \"password\": \"Password1!\" }").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserShouldReturn415UnsupportedeMediaTypeWhenParsingInvalidJson() throws Exception {
        // Act and Assert
        this.mockMvc.perform(post("/user/create").content("not json").characterEncoding("UTF-8"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void createUserShouldReturn403ForbiddenWhenParsingInvalidJwtToken() throws Exception {
        // Arrange
        // Creating faker object
        Faker faker = new Faker();

        // Creating a map with the data we want to send in the request
        Map<String, Object> jsonRequestMap = new HashMap<>();
        jsonRequestMap.put("username", "my_test_user");
        jsonRequestMap.put("id", 1L);
        jsonRequestMap.put("name", faker.name().fullName());
        jsonRequestMap.put("address", faker.address().fullAddress());
        jsonRequestMap.put("phoneNumber", faker.phoneNumber().cellPhone());
        jsonRequestMap.put("email", faker.internet().emailAddress());

        // Converting the map to a JSON object
        JSONObject jsonObject = new JSONObject(jsonRequestMap);
        String jsonRequestData = jsonObject.toString();

        Cookie cookie = getCookie(2L);
        when(serviceFacade.createUser(any(UserDTO.class), any(String.class))).thenThrow(new NotAllowedException("You are not allowed to create a user with this id"));

        // Act and Assert
        this.mockMvc.perform(post("/user/create").content(jsonRequestData).contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8").cookie(cookie))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getUser() throws Exception {
        // Arrange
        when(serviceFacade.getUser(any(String.class))).thenReturn(new UserDTO(1L, "name", "email", "phoneNumber", "address"));
        Cookie cookie = getCookie(1L);

        // Act and Assert
        this.mockMvc.perform(get("/user").cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":1,\"name\":\"name\",\"email\":\"email\",\"phoneNumber\":\"phoneNumber\",\"address\":\"address\"}"));
    }

    private Cookie getCookie(Long id) {
        // Arrange
        // Creating a cookie with a jwt token
        SecretKey key = Keys.hmacShaKeyFor(pKey.getBytes());
        String jwtToken = Jwts.builder()
                .header()
                .add("id", id)
                .add("username", "user")
                .add("role", "admin")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                // Expires after 2 days of issue
                .expiration(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000))
                .signWith(key)
                .compact();
        return new Cookie("jwt-token", jwtToken);
    }
}
