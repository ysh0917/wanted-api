package com.draka.wantedapi.repository;

import com.draka.wantedapi.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageJpaRepository extends JpaRepository<Language, Long> {
    public boolean existsLanguageByName(String name);
    public Language findLanguageByName(String name);
}
