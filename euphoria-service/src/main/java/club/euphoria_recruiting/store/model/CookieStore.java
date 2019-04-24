package club.euphoria_recruiting.store.model;

import club.euphoria_recruiting.model.Cookie;

public interface CookieStore {

    Cookie getCookie(String cookieCheck);

    Cookie createCookie(String userName, String passwordHash);

}