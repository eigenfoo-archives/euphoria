package club.euphoria_recruiting.model;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface Company {
    Integer companyId();

    String name();

    String website();

    String description();
}
