package edu.cooper.ece366.euphoria.store.model;

import edu.cooper.ece366.euphoria.model.Posting;
import edu.cooper.ece366.euphoria.utils.Industry;
import edu.cooper.ece366.euphoria.utils.Location;
import edu.cooper.ece366.euphoria.utils.SkillLevel;

import java.util.List;

public interface PostingStore {

    Posting getPosting(String postingId);

    List<Posting> searchPostings(String location, String industry, String skillLevel);

    List<Posting> getAllPostings();

    List<Posting> getRandomPostings();

    List<Posting> getPostingsForCompany(String companyId);

    List<Posting> createPosting(String companyId, String jobTitle, String description, Location location, Industry industry, SkillLevel skillLevel);

    List<Posting> editPosting(String postingId, String jobTitle, String description, Location location, Industry industry, SkillLevel skillLevel);

    List<Posting> deletePosting(String postingId);
}
