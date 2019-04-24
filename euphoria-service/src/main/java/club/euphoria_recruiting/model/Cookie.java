package club.euphoria_recruiting.model;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface Cookie {

    Integer id();

    Boolean isUser();

    String cookie();

}
