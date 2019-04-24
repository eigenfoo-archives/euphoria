package club.euphoria_recruiting.model;

import club.euphoria_recruiting.utils.Industry;
import club.euphoria_recruiting.utils.Location;
import club.euphoria_recruiting.utils.SkillLevel;
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