package de.splayfer.web_backend;

import org.bson.Document;

public class AuthenticationUser {

    private String username;
    private String method = "password";
    private String value;

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

    public AuthenticationUser(String username, String method, String value) {
        this.username = username;
        this.method = method;
        this.value = value;
    }

    public void syncWithDatabase() {
        if (mongoDBDatabase.exists("accounts", "username", username))
            mongoDBDatabase.update("accounts", mongoDBDatabase.find("accounts", "username", username).first(), new Document()
                    .append("username", username.toLowerCase())
                    .append("method", method)
                    .append("value", value));
        else
            mongoDBDatabase.insert("accounts", new Document()
                    .append("username", username)
                    .append("method", method)
                    .append("value", value));
    }

    public static AuthenticationUser fromUsername(String username) {
        if (mongoDBDatabase.exists("accounts", "username", username)) {
            Document doc = mongoDBDatabase.find("accounts", "username", username).first();
            return new AuthenticationUser(username, doc.getString("method"), doc.getString("value"));
        } else
            return null;
    }
}
