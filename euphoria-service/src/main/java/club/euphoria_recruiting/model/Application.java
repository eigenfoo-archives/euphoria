package club.euphoria_recruiting.model;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface Application {
    Integer applicationId();

    Integer postingId();

    Integer userId();

    byte[] resume();

    byte[] coverLetter();

    String dateCreated();
}
