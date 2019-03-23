package edu.cooper.ece366.euphoria;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface Posting {
    //TODO: add location, industry and skill level (all are discrete attributes)
    Integer postingId();
    String jobTitle();
    String postingBlurb();
}