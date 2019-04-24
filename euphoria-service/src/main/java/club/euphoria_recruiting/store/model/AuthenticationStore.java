package club.euphoria_recruiting.store.model;

import club.euphoria_recruiting.model.Authentication;

import java.util.List;

public interface AuthenticationStore {

    Authentication getAuthentication(String userName, String passwordHash);

    List<Authentication> createAuthentication(Integer id, String username, String passwordHash, Boolean isUser);

}
