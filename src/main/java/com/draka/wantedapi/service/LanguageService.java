package com.draka.wantedapi.service;

import com.draka.wantedapi.entity.Language;
import com.draka.wantedapi.repository.LanguageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageJpaRepository languageJpaRepository;

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Language save(String name) {
        Language language;

        if (languageJpaRepository.existsLanguageByName(name)) {
            language = languageJpaRepository.findLanguageByName(name);
        } else {
            language = languageJpaRepository.save(Language.builder().name(name).build());
        }

        return language;
    }
}
