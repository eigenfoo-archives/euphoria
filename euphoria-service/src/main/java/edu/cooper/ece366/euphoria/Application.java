package edu.cooper.ece366.euphoria;

import io.norberg.automatter.AutoMatter;

import java.sql.Blob;

@AutoMatter
public interface Application {
    Integer applicationId();

    Integer postingId();

    Integer userId();

    Blob resume();

    Blob coverLetter();
}
