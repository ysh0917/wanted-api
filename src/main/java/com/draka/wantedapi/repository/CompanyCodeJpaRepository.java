package com.draka.wantedapi.repository;

import com.draka.wantedapi.entity.Company;
import com.draka.wantedapi.entity.Company_Code;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyCodeJpaRepository extends JpaRepository<Company_Code, Long> {
    public boolean existsCompany_CodeByCompany(Company company);
    public Company_Code findCompany_CodeByCompany(Company company);
    public Company_Code findFirstByOrderByIdDesc();
    public List<Company_Code> findCompany_CodesByCodeNo(int codeNo);
    public List<Company_Code> findCompany_CodesByCodeNoIn(List<Integer> codeNos);
    public List<Company_Code> findCompany_CodesByCompanyIn(List<Company> companies);
}
