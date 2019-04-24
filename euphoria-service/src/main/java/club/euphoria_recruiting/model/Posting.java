package edu.cooper.ece366.euphoria.model;

import edu.cooper.ece366.euphoria.utils.Industry;
import edu.cooper.ece366.euphoria.utils.Location;
import edu.cooper.ece366.euphoria.utils.SkillLevel;
import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface Posting {
    Integer postingId();

    Integer companyId();

    String jobTitle();

    String description();

    Location location();

    Industry industry();

    SkillLevel skillLevel();

    String dateCreated();
}