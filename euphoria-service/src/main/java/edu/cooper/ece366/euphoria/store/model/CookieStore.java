package edu.cooper.ece366.euphoria.store.model;

import edu.cooper.ece366.euphoria.model.Cookie;

public interface CookieStore {

    Cookie getCookie(String cookieCheck);

    Cookie createCookie(String userName, String passwordHash);

}