package com.draka.wantedapi.repository;

import com.draka.wantedapi.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyJpaRepository extends JpaRepository<Company, Long> {
    public Company findTop1ByNameOrderByIdDesc(String name);
    public List<Company> findCompanyByIdIn(List<Long> ids);
    public List<Company> findByNameContains(String name);

    public Company findCompanyByName(String name);
}
