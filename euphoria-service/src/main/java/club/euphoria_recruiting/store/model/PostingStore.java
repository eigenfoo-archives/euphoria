package club.euphoria_recruiting.store.model;

import club.euphoria_recruiting.model.Posting;
import club.euphoria_recruiting.utils.Industry;
import club.euphoria_recruiting.utils.Location;
import club.euphoria_recruiting.utils.SkillLevel;

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
