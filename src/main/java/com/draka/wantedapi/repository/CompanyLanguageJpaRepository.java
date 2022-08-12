package com.draka.wantedapi.repository;

import com.draka.wantedapi.entity.Company;
import com.draka.wantedapi.entity.CompanyInterface;
import com.draka.wantedapi.entity.Company_Language;
import com.draka.wantedapi.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyLanguageJpaRepository  extends JpaRepository<Company_Language, Long> {
    public Company_Language findCompany_LanguageByLanguageAndCompanyIn(Language language, List<Company> companies);

    public List<Company_Language> findCompany_LanguagesByLanguageAndCompanyIn(Language language, List<Company> companies);

    public Company_Language findCompany_LanguageByCompany(Company company);

}
