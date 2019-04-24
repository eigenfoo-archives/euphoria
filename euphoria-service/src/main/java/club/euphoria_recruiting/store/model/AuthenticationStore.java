package edu.cooper.ece366.euphoria.store.model;

import edu.cooper.ece366.euphoria.model.Authentication;

import java.util.List;

public interface AuthenticationStore {

    Authentication getAuthentication(String userName, String passwordHash);

    List<Authentication> createAuthentication(Integer id, String username, String passwordHash, Boolean isUser);

}
