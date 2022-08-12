package com.draka.wantedapi.repository;

import com.draka.wantedapi.entity.Company;
import com.draka.wantedapi.entity.Company_Tag;
import com.draka.wantedapi.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CompanyTagJpaRepository extends JpaRepository<Company_Tag, Long> {
    public List<Company_Tag> findCompany_TagsByCompany(Company company);

    public List<Company_Tag> findCompany_TagsByTag(Tag tag);

    public Company_Tag findCompany_TagByCompanyAndTag(Company company, Tag tag);

    @Transactional
    public void deleteCompany_TagById(Long id);
}
