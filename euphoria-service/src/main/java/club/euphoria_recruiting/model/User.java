package club.euphoria_recruiting.model;

import club.euphoria_recruiting.utils.EducationLevel;
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
