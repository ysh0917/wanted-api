package com.draka.wantedapi.service;

import com.draka.wantedapi.entity.*;
import com.draka.wantedapi.repository.CompanyTagJpaRepository;
import com.draka.wantedapi.repository.TagJpaRepository;
import com.draka.wantedapi.repository.TagLanguageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagJpaRepository tagJpaRepository;
    private final CompanyTagJpaRepository companyTagJpaRepository;
    private final TagLanguageJpaRepository tagLanguageJpaRepository;

    private final LanguageService languageService;



    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Tag save(String languageName, String tagName) {

        Tag tag = tagJpaRepository.selectTagInTagLanguage(languageName, tagName);

        if (tag == null) {
            tag = tagJpaRepository.save(Tag.builder().name(tagName).build());
        }

        return tag;
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public List<Tag> getTagsInfoByCompany(Company company) {
        List<Company_Tag> company_tags = companyTagJpaRepository.findCompany_TagsByCompany(company);
        List<Long> tagIds = new ArrayList<>();

        for (Company_Tag company_tag : company_tags) {
            tagIds.add(company_tag.getTag().getId());
        }

        List<Tag> tags = tagJpaRepository.findTagsByIdIn(tagIds);

        return tags;

    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void insertTags(List<Company_Language> company_languages, List<Map<String, Object>> tags) {

        for (Map<String, Object> tagMap : tags) {
            Map<String, Object> tag_pair = (Map<String, Object>) tagMap.get("tag_name");
            for (Map.Entry<String, Object> pair : tag_pair.entrySet()) {
                //Tag 추가
                Tag tag = save(pair.getKey(), pair.getValue().toString());

                //Tag Language Process
                Tag_Language tag_language = tagLanguageJpaRepository.findTag_LanguageByTag(tag);
                if (tag_language == null) {
                    Language language = languageService.save(pair.getKey());
//                    Language language = languageJpaRepository.findLanguageByName(pair.getKey());
                    tag_language = tagLanguageJpaRepository.save(
                            Tag_Language.builder()
                                    .language(language)
                                    .tag(tag)
                                    .build()
                    );
                }

                //company_tag
                for (Company_Language cl : company_languages) {
                    if (cl.getLanguage().getId() == tag_language.getLanguage().getId()) {
                        Company_Tag company_tag = companyTagJpaRepository.save(
                                Company_Tag.builder()
                                        .company(cl.getCompany())
                                        .tag(tag_language.getTag())
                                        .build()
                        );
                    }
                }
            }
        }
    }
}
