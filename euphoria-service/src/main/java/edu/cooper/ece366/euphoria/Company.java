package edu.cooper.ece366.euphoria;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface User {
    Integer companyId();

    String name();

    String website();

    String description();
}
