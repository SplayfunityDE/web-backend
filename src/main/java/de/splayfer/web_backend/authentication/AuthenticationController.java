package de.splayfer.web_backend.authentication;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;

@RestController
@RequestMapping("/authentication/accounts")
public class AuthenticationController {

    WebClient oauthClient = WebClient.builder()
            .baseUrl("https://discord.com/api/v10/oauth2/token")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
            .build();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationUser authenticationUser, @RequestParam boolean remember) {
        String username = authenticationUser.getUsername().toLowerCase();
        String value = authenticationUser.getValue();
        if (AuthenticationUser.fromUsername(username) != null && AuthenticationUser.fromUsername(username).getValue().equals(hashToSHA256(value))) {
            return ResponseEntity.ok(Collections.singletonMap("token", new JwtService().generateToken(username, remember)));
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            new JwtService().invalidateToken(token.split(" ")[1]);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/{token}")
    public AuthenticationUser getAuthenticationUser(@PathVariable String token) {
        return AuthenticationUser.fromJwt(token);
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

    @PostMapping("/oauth")
    public void oauth(@RequestParam String code, @RequestParam String state) {
        oauthClient.post()
                .bodyValue(new HashMap<String, String>(){{
                    put("grant_type", "authorization_code");
                    put("code", code);
                    put("redirect_uri", "https://splayfer.de/ticket/dashboard");
                }})
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
