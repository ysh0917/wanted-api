package com.draka.wantedapi.service;

import com.draka.wantedapi.entity.*;
import com.draka.wantedapi.repository.CompanyCodeJpaRepository;
import com.draka.wantedapi.repository.CompanyJpaRepository;
import com.draka.wantedapi.repository.CompanyLanguageJpaRepository;
import com.draka.wantedapi.repository.LanguageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyJpaRepository companyJpaRepository;
    private final CompanyCodeJpaRepository companyCodeJpaRepository;
    private final CompanyLanguageJpaRepository companyLanguageJpaRepository;
    private final LanguageJpaRepository languageJpaRepository;

    private final TagService tagService;

    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public Map<String, Object> getCompanyInfo(String name, String lang) {
        Company_Language company_language = getCompany_language(name, lang);

        if (company_language == null) {
            return null;
        }

        List<Tag> tags = tagService.getTagsInfoByCompany(company_language.getCompany());
        List<String> tag_list = new ArrayList<>();

        for (Tag tag : tags) {
            tag_list.add(tag.getName());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("company_name", company_language.getCompany().getName());
        result.put("tags", tag_list);

        return result;
    }


    private Company_Language getCompany_language(String name, String lang) {
        Company company = companyJpaRepository.findTop1ByNameOrderByIdDesc(name);

        if (company == null) {
            return null;
        }


        //같은 company_code group 을 가져온다.
        Company_Code company_code = companyCodeJpaRepository.findCompany_CodeByCompany(company);
        List<Company_Code> company_codes = companyCodeJpaRepository.findCompany_CodesByCodeNo(company_code.getCodeNo());

        //Language 코드 취득
        Language language = languageJpaRepository.findLanguageByName(lang);

        List<Long> companyIdList = new ArrayList<>();
        for (Company_Code companyCode : company_codes) {
            companyIdList.add(companyCode.getCompany().getId());
        }

        //Company 목록
        List<Company> companyList = companyJpaRepository.findCompanyByIdIn(companyIdList);
        Company_Language company_language = companyLanguageJpaRepository.findCompany_LanguageByLanguageAndCompanyIn(language, companyList);
        return company_language;
    }
}
