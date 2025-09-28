package de.splayfer.web_backend.authentication;

import com.mongodb.lang.Nullable;
import de.splayfer.web_backend.MongoDBDatabase;
import org.bson.Document;

public class AuthenticationUser {

    private String username;
    private String method = "password";
    private String value;
    private String discordUserId;

    private final static MongoDBDatabase mongoDBDatabase = MongoDBDatabase.getDatabase("authentication");

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDiscordUserId() {
        return discordUserId;
    }

    public void setDiscordUserId(String discordUserId) {
        this.discordUserId = discordUserId;
    }

    public AuthenticationUser(String username, String method, String value, @Nullable String discordUserId) {
        this.username = username;
        this.method = method;
        this.value = value;
        this.discordUserId = discordUserId;
    }

    public void syncWithDatabase() {
        Document document = new Document()
                .append("username", username.toLowerCase())
                .append("method", method)
                .append("value", value);
        if (discordUserId != null)
            document.append("discordUserId", discordUserId);
        if (mongoDBDatabase.exists("accounts", "username", username))
            mongoDBDatabase.update("accounts", mongoDBDatabase.find("accounts", "username", username).first(), document);
        else
            mongoDBDatabase.insert("accounts", document);
    }

    public static AuthenticationUser fromUsername(String username) {
        if (mongoDBDatabase.exists("accounts", "username", username)) {
            Document doc = mongoDBDatabase.find("accounts", "username", username).first();
            return new AuthenticationUser(username, doc.getString("method"), doc.getString("value"), doc.getString("discordUserId") != null ? doc.getString("discordUserId") : null);
        } else
            return null;
    }

    public static AuthenticationUser fromJwt(String token) {
        return fromUsername(new JwtService().extractUsername(token));
    }
}
