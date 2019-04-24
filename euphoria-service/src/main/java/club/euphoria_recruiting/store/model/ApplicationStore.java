package club.euphoria_recruiting.store.model;

import club.euphoria_recruiting.model.Application;

import java.util.List;

public interface ApplicationStore {

    Application getApplication(String applicationId);

    List<Application> getApplicationsForPosting(String postingId);

    List<Application> createApplication(String postingId, String userId, String resume, String coverLetter);

}
