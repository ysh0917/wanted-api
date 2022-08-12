package com.draka.wantedapi.controller;

import com.draka.wantedapi.entity.*;
import com.draka.wantedapi.exception.CompanyDuplicateException;
import com.draka.wantedapi.exception.CompanyNotFoundException;
import com.draka.wantedapi.model.response.SingleResult;
import com.draka.wantedapi.repository.*;
import com.draka.wantedapi.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.*;

@RequiredArgsConstructor
@RestController
public class CompanyController {
    private final CompanyJpaRepository companyJpaRepository;
    private final LanguageJpaRepository languageJpaRepository;
    private final CompanyLanguageJpaRepository companyLanguageJpaRepository;
    private final CompanyCodeJpaRepository companyCodeJpaRepository;
    private final TagJpaRepository tagJpaRepository;
    private final TagLanguageJpaRepository tagLanguageJpaRepository;
    private final CompanyTagJpaRepository companyTagJpaRepository;

    private final LanguageService languageService;
    private final CompanyCodeService companyCodeService;
    private final TagService tagService;
    private final CompanyService companyService;
    private final ResponseService responseService;

    @PostMapping(value = "/companies")
    public SingleResult<Map<String, Object>> companySave(
           @RequestBody Map<String, Object> paramMap, @RequestHeader(value="x-wanted-language") String paramLang) {

        String resultCompany = null;

        //Company Process
        Map<String, Object> companies = (Map<String, Object>) paramMap.get("company_name");
        List<Company_Language> company_languages = new ArrayList<>(); //CompanyTag 처리 용도 배열

        int company_loop_count = 0;
        Company_Code companyCode = new Company_Code();


        for (Map.Entry<String, Object> pair : companies.entrySet()) {

            //language 추가 처리
            Language language = languageService.save(pair.getKey());

            //company insert
            Company company = companyJpaRepository.findCompanyByName(pair.getValue().toString());
            if (company == null) {
                company = companyJpaRepository.save(
                        Company.builder()
                                .name(pair.getValue().toString())
                                .build());
            } else {
                continue;
            }

            //결과값 반환용 변수에 할당한다.
            resultCompany = company.getName();

            //company_language insert
            Company_Language company_language = companyLanguageJpaRepository.findCompany_LanguageByCompany(company);
            if (company_language == null) {
                company_language = companyLanguageJpaRepository.save(
                        Company_Language.builder()
                                .company(company)
                                .language(language)
                                .build());
            }

            //company_code grouping
            if (company_loop_count == 0) {
                companyCode = companyCodeService.getCompanyCode(company);
            } else {
                companyCodeJpaRepository.save(
                        Company_Code.builder()
                                .company(company)
                                .codeNo(companyCode.getCodeNo())
                                .build()
                );
            }

            company_languages.add(company_language);

            company_loop_count++;
        }

        Map<String, Object> result_map = new HashMap<>();

        if (resultCompany == null) {
            Optional<Map<String, Object>> optional = Optional.empty();
            return responseService.getSingleResult(optional.orElseThrow(CompanyDuplicateException::new));
        }

        //Tag Process
        List<Map<String, Object>> tags = (List<Map<String, Object>>) paramMap.get("tags");

        return tagInsertAfterGetSingleResult(resultCompany, paramLang, tags, company_languages);
    }

    @GetMapping("/companies/{name}")
    public SingleResult<Map<String, Object>> searchCompany(@PathVariable(name="name") String name, @RequestHeader(value = "x-wanted-language") String header) {
        Map<String, Object> map = companyService.getCompanyInfo(name, header);

        Optional<Map<String, Object>> optionalMap = Optional.ofNullable(map);
        return responseService.getSingleResult(optionalMap.orElseThrow(CompanyNotFoundException::new));
    }

    @PutMapping("/companies/{name}/tags")
    public SingleResult<Map<String, Object>> addCompanyTags(@PathVariable(name = "name") String name,
                                              @RequestHeader(value = "x-wanted-language") String header,
                                              @RequestBody List<Map<String, Object>> paramMap) {
        Language language = languageJpaRepository.findLanguageByName(header);
        Company company = companyJpaRepository.findCompanyByName(name);

        if (company == null) {
            Optional<Map<String, Object>> optional = Optional.empty();
            return responseService.getSingleResult(optional.orElseThrow(CompanyNotFoundException::new));
        }

        Company_Code company_code = companyCodeJpaRepository.findCompany_CodeByCompany(company);

        List<Company_Code> company_codes = companyCodeJpaRepository.findCompany_CodesByCodeNo(company_code.getCodeNo());
        List<Company> allCompanies = new ArrayList<>();
        for (Company_Code cc : company_codes) {
            allCompanies.add(cc.getCompany());
        }

        List<Company_Language> company_languages = companyLanguageJpaRepository.findCompany_LanguagesByLanguageAndCompanyIn(language, allCompanies);

        return tagInsertAfterGetSingleResult(name, header, paramMap, company_languages);

    }

    private SingleResult<Map<String, Object>> tagInsertAfterGetSingleResult(@PathVariable(name = "name") String name, @RequestHeader("x-wanted-language") String header, @RequestBody List<Map<String, Object>> paramMap, List<Company_Language> company_languages) {
        tagService.insertTags(company_languages, paramMap);

        Map<String, Object> map = companyService.getCompanyInfo(name, header);
        Optional<Map<String, Object>> optionalMap = Optional.ofNullable(map);

        return responseService.getSingleResult(optionalMap.orElseThrow(CompanyNotFoundException::new));
    }

    @DeleteMapping("/companies/{company_name}/tags/{tag_name}")
    public SingleResult<Map<String, Object>> deleteTag(@PathVariable(name="company_name") String company_name,
                                         @PathVariable(name="tag_name") String tag_name,
                                         @RequestHeader(value = "x-wanted-language") String header) {
        Company company = companyJpaRepository.findCompanyByName(company_name);
        Tag tag = tagJpaRepository.findTagByName(tag_name);
        Company_Tag company_tag = companyTagJpaRepository.findCompany_TagByCompanyAndTag(company, tag);
        companyTagJpaRepository.deleteCompany_TagById(company_tag.getId());

        Map<String, Object> map = companyService.getCompanyInfo(company_name, header);
        Optional<Map<String, Object>> optionalMap = Optional.ofNullable(map);

        return responseService.getSingleResult(optionalMap.orElseThrow(CompanyNotFoundException::new));
    }



}
