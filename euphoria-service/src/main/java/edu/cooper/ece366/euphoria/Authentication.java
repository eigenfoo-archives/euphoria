package edu.cooper.ece366.euphoria;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface Authentication {
    String username();

    String passwordHash();

    Boolean isUser();
}