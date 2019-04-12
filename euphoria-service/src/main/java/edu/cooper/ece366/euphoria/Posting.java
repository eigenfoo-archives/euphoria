package edu.cooper.ece366.euphoria;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface Posting {
    Integer postingId();

    String jobTitle();

    String description();

    Location location();

    Industry industry();

    SkillLevel skillLevel();

    String dateCreated();
}