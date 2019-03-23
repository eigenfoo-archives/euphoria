package edu.cooper.ece366.euphoria;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface Application {
    Integer applicationId();

    Integer postingId();

    Integer userId();

    byte[] resume();

    byte[] coverLetter();
}
