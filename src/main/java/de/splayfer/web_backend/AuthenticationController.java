package de.splayfer.web_backend;

import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/authentication/accounts")
public class AuthenticationController {

    @PostMapping("/login")
    public boolean checkLoginValue(@RequestBody AuthenticationUser authenticationUser) {
        String username = authenticationUser.getUsername().toLowerCase();
        String value = authenticationUser.getValue();
        if (AuthenticationUser.fromUsername(username) != null && AuthenticationUser.fromUsername(username).getValue().equals(hashToSHA256(value)))
            return true;
        else
            return false;
    }

    @PostMapping("token")
    public boolean checkSessionToken() {
        return false;
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
