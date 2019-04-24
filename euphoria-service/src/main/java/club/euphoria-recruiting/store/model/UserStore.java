package edu.cooper.ece366.euphoria.store.model;

import edu.cooper.ece366.euphoria.model.User;
import edu.cooper.ece366.euphoria.utils.EducationLevel;

public interface UserStore {

    User getUser(String userId);

    User createUser(String name, String email, String phoneNumber, EducationLevel educationLevel, String description);

}
