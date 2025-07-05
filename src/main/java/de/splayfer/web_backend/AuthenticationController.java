package de.splayfer.web_backend;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Date;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/authentication/accounts")
public class AuthenticationController {

//    @PostMapping("/login")
//    public boolean checkLoginValue(@RequestBody AuthenticationUser authenticationUser) {
//        String username = authenticationUser.getUsername().toLowerCase();
//        String value = authenticationUser.getValue();
//        if (AuthenticationUser.fromUsername(username) != null && AuthenticationUser.fromUsername(username).getValue().equals(hashToSHA256(value)))
//            return true;
//        else
//            return false;
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationUser authenticationUser) {
        String username = authenticationUser.getUsername().toLowerCase();
        String value = authenticationUser.getValue();
        if (AuthenticationUser.fromUsername(username) != null && AuthenticationUser.fromUsername(username).getValue().equals(hashToSHA256(value))) {
            String jwt = Jwts.builder()
                    .setSubject(authenticationUser.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 Tag gültig
                    .signWith(SignatureAlgorithm.HS512, System.getenv("JWT_KEY"))
                    .compact();
            return ResponseEntity.ok(Collections.singletonMap("token", jwt));
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private static String hashToSHA256(String value) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] encodedhash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


}
