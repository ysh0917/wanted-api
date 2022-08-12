package com.draka.wantedapi.controller;

import com.draka.wantedapi.entity.Company;
import com.draka.wantedapi.entity.Language;
import com.draka.wantedapi.model.response.ListResult;
import com.draka.wantedapi.repository.CompanyJpaRepository;
import com.draka.wantedapi.repository.LanguageJpaRepository;
import com.draka.wantedapi.service.ResponseService;
import com.draka.wantedapi.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class SearchController {
    private final CompanyJpaRepository companyJpaRepository;
    private final LanguageJpaRepository languageJpaRepository;

    private final SearchService searchService;
    private final ResponseService responseService;

    @GetMapping("/search")
    public ListResult<List<Map<String, Object>>> search(@RequestParam String query, @RequestHeader(value="x-wanted-language") String header) {
        Language language = languageJpaRepository.findLanguageByName(header);
        List<Company> companies = companyJpaRepository.findByNameContains(query);

        return searchService.getSearchListResult(language, companies, searchService, responseService);
    }



}
