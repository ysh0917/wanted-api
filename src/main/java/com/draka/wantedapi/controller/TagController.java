package com.draka.wantedapi.controller;

import com.draka.wantedapi.entity.Company;
import com.draka.wantedapi.entity.Company_Tag;
import com.draka.wantedapi.entity.Language;
import com.draka.wantedapi.entity.Tag;
import com.draka.wantedapi.model.response.ListResult;
import com.draka.wantedapi.repository.CompanyCodeJpaRepository;
import com.draka.wantedapi.repository.CompanyTagJpaRepository;
import com.draka.wantedapi.repository.LanguageJpaRepository;
import com.draka.wantedapi.repository.TagJpaRepository;
import com.draka.wantedapi.service.ResponseService;
import com.draka.wantedapi.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class TagController {
    private final TagJpaRepository tagJpaRepository;
    private final CompanyTagJpaRepository companyTagJpaRepository;
    private final LanguageJpaRepository languageJpaRepository;
    private final CompanyCodeJpaRepository companyCodeJpaRepository;

    private final SearchService searchService;
    private final ResponseService responseService;

    @GetMapping("/tags")
    public ListResult<List<Map<String, Object>>> getCompaniesByTagName(@RequestParam String query, @RequestHeader(value = "x-wanted-language") String header) {
        Tag tag = tagJpaRepository.findTagByName(query);
        Language language = languageJpaRepository.findLanguageByName(header);
        List<Company_Tag> company_tags = companyTagJpaRepository.findCompany_TagsByTag(tag);
        List<Company> companies = new ArrayList<>();
        for (Company_Tag company_tag : company_tags) {
            companies.add(company_tag.getCompany());
        }

        return searchService.getSearchListResult(language, companies, searchService, responseService);
    }



}
