package edu.cooper.ece366.euphoria.store.model;

import edu.cooper.ece366.euphoria.model.Company;

public interface CompanyStore {

    Company getCompany(String companyId);

    Company createCompany(String name, String website, String description);
}
