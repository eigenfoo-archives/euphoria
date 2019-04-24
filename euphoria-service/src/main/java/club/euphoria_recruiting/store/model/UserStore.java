package club.euphoria_recruiting.store.model;

import club.euphoria_recruiting.model.User;
import club.euphoria_recruiting.utils.EducationLevel;

public interface UserStore {

    User getUser(String userId);

    User createUser(String name, String email, String phoneNumber, EducationLevel educationLevel, String description);

}
