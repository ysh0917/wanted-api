package com.draka.wantedapi.service;

import com.draka.wantedapi.entity.Company;
import com.draka.wantedapi.entity.Company_Code;
import com.draka.wantedapi.repository.CompanyCodeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyCodeService {
    private final CompanyCodeJpaRepository companyCodeJpaRepository;

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Company_Code getCompanyCode(Company company) {
        Company_Code companyCode;

        if (companyCodeJpaRepository.existsCompany_CodeByCompany(company)) {
            //존재하는 회사의 경우 객체 리턴
            companyCode = companyCodeJpaRepository.findCompany_CodeByCompany(company);
        } else {
            //code 값 생성을 위해 마지막 데이터를 받아온다.
            Company_Code lastCompanyCode = companyCodeJpaRepository.findFirstByOrderByIdDesc();
            int codeNo = 0;
            if (lastCompanyCode != null) {
                codeNo = lastCompanyCode.getCodeNo() + 1;
            }

            //Company_Code 저장
            companyCode = companyCodeJpaRepository.save(
                    Company_Code
                            .builder()
                            .company(company)
                            .codeNo(codeNo).build()
            );
        }

        return companyCode;
    }
}
