package edu.cooper.ece366.euphoria;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface User {
    Integer userId();

    String name();

    String email();

    String phoneNumber();

    EducationLevel educationLevel();

    String description();
}
