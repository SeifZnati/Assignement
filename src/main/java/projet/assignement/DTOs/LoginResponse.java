package projet.assignement.DTOs;



import projet.assignement.Entities.User;

import java.util.Map;

public class LoginResponse {
    public String token;
    public Map<String, String> user;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = Map.of("id", String.valueOf(user.getId()), "email", user.getEmail());
    }
}

