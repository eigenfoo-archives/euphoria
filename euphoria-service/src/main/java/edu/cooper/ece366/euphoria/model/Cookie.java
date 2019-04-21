package edu.cooper.ece366.euphoria.model;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface Cookie {

    Integer id();

    Boolean isUser();

    String cookie();

}
