package edu.cooper.ece366.euphoria;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface Authentication {
    Integer Id(); // Either userId or companyId

    String username();

    String passwordHash();

    Boolean isUser();
}