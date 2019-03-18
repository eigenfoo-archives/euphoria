package edu.cooper.ece366.euphoria;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface User {
    Integer applicationId();

    Integer postingId();

    Integer userId();

    String resume();  //FIXME need to change this to blob.

    String coverLetter();  //FIXME need to change this to blob.
}
