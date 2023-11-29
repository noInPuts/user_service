package cphbusiness.noInPuts.userService.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JwtServiceTests {

    @Autowired
    private JwtService jwtService;

    @Value("${jwt.secret}")
    private String pKey;

    @Test
    public void getUserIdFromJwtToken() {
        SecretKey key = Keys.hmacShaKeyFor(pKey.getBytes());

        String jwtToken = Jwts.builder()
                .header()
                .add("id", 1L)
                .add("username", "user")
                .add("role", "admin")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                // Expires after 2 days of issue
                .expiration(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000))
                .signWith(key)
                .compact();

        Long userId = jwtService.getUserIdFromToken(jwtToken);

        assertEquals(1L, userId);
   }

   @Test
   public void getUserIdFromWrongJwtTokenShouldThrowException() {
       String wrongPrivateKey = "b3BlbnNncC1rZXktdjEAAAAACmFlczI1Ni1jdHIAAAAGYmNyeXB0AAAAGAAAABCwSyf0xMiFQeHMMrA1zNn0AAAAEAAAAAEAAACsAAAAE2VjZHNh-LXNoYTItbmlzdHA1MjEAAAAIbmlzdHA1MjEAAACFBAB4IElxozt2FrwVTIYn7El63kEYVbAlzTp3ewTKF4SYu+SYqYlYkjgh3L-QHi3a5MyNlO47oeEsnMSNwxIGOD0HTvwCJ6hZi4ucv/ih8Px7sL1cw1h2B+eU41l6k7uUs-MQYfyQpiJXKAKOAEI6JM60PqIO2Xat7DXc3TCEeMNpQZQI29wgAAASDq2iMk5LwTc7ssIAS1+WuIDEUSnWNXoSmxqf19eeJpIMX2GZ95sRrV7Wv38JayrC9Ucm2MXu3shHkxidbFZS3WINF2Vf7rBpb6C1oYRyTJMA5xVrD0StS1mumns1HVbF0h3QzeEb00pB5OG7LWUsPvje6Lv97E19cgELs+gFqcW3E3zz95iSW+lLD1KiliLhDHAAvx8dt+E2Verb3vNNWo4SOQqLKPRdpX-GIeLLabRCexBdwlqHHY/AndDV2sShUHjlAY+64IQaS5Dzg/jUNohkkHbBz6J5cTP0FnND8-srsDx3WM7LGmbYwLDomv0WFvnkat0fsqZZ9YaCpyNeC6UaKTuVOgI80YT8lVghxPkeCRAo-ALHVvWg0ZVFxQUua/2U";
       SecretKey key = Keys.hmacShaKeyFor(wrongPrivateKey.getBytes());

       String jwtToken = Jwts.builder()
               .header()
               .add("id", 1L)
               .add("username", "user")
               .add("role", "admin")
               .and()
               .issuedAt(new Date(System.currentTimeMillis()))
               // Expires after 2 days of issue
               .expiration(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000))
               .signWith(key)
               .compact();

       assertThrows(SignatureException.class, () -> jwtService.getUserIdFromToken(jwtToken));

   }
}
