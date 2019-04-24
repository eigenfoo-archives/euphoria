package club.euphoria_recruiting.store.model;

import club.euphoria_recruiting.model.Company;

public interface CompanyStore {

    Company getCompany(String companyId);

    Company createCompany(String name, String website, String description);
}
