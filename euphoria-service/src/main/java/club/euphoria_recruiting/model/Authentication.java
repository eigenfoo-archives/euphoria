package edu.cooper.ece366.euphoria.model;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface Authentication {
    Integer id(); // Either userId or companyId

    String username();

    String passwordHash();

    Boolean isUser();
}