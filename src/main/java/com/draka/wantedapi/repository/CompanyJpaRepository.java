package com.draka.wantedapi.repository;

import com.draka.wantedapi.entity.Company;
import com.draka.wantedapi.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface CompanyJpaRepository extends JpaRepository<Company, Long> {
    public Company findTop1ByNameOrderByIdDesc(String name);
    public List<Company> findCompanyByIdIn(List<Long> ids);
    public List<Company> findByNameContains(String name);

    public Company findCompanyByName(String name);
}
