package com.draka.wantedapi.service;

import com.draka.wantedapi.entity.Company;
import com.draka.wantedapi.entity.Company_Code;
import com.draka.wantedapi.entity.Company_Language;
import com.draka.wantedapi.entity.Language;
import com.draka.wantedapi.exception.CompanyNotFoundException;
import com.draka.wantedapi.model.response.ListResult;
import com.draka.wantedapi.repository.CompanyCodeJpaRepository;
import com.draka.wantedapi.repository.CompanyLanguageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final CompanyCodeJpaRepository companyCodeJpaRepository;
    private final CompanyLanguageJpaRepository companyLanguageJpaRepository;

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public List<Map<String, Object>> getCompanyList(Language language, List<Company> companies) {
        //검색된 회사의 코드값들을 가져온다.
        List<Company_Code> company_codes = companyCodeJpaRepository.findCompany_CodesByCompanyIn(companies);
        List<Integer> codeNos = new ArrayList<>();
        for (Company_Code company_code : company_codes) {
            codeNos.add(company_code.getCodeNo());
        }
        //해당 코드값에 속한 전체 Company_Code 데이터를 가져온다.
        List<Company_Code> allCompanyCodes = companyCodeJpaRepository.findCompany_CodesByCodeNoIn(codeNos);
        List<Company> allCompanies = new ArrayList<>();
        for (Company_Code company_code : allCompanyCodes) {
            allCompanies.add(company_code.getCompany());
        }

        List<Company_Language> company_languages = companyLanguageJpaRepository.findCompany_LanguagesByLanguageAndCompanyIn(language, allCompanies);

        List<Map<String, Object>> maps = new ArrayList<>();

        for (Company_Language company_language : company_languages) {
            Map<String, Object> map = new HashMap<>();
            map.put("company_name", company_language.getCompany().getName());
            maps.add(map);
        }
        return maps;
    }

    public ListResult<List<Map<String, Object>>> getSearchListResult(Language language, List<Company> companies, SearchService searchService, ResponseService responseService) {
        List<Map<String, Object>> maps = searchService.getCompanyList(language, companies);
        Optional<List<Map<String, Object>>> optionalMap = Optional.ofNullable(maps);

        return responseService.getListResult(Collections.singletonList(optionalMap.orElseThrow(CompanyNotFoundException::new)));
    }
}
