package edu.cooper.ece366.euphoria.store.model;

import edu.cooper.ece366.euphoria.model.Application;

import java.util.List;

public interface ApplicationStore {

    Application getApplication(String applicationId);

    List<Application> getApplicationsForPosting(String postingId);

    List<Application> createApplication(String postingId, String userId, String resume, String coverLetter);

}
