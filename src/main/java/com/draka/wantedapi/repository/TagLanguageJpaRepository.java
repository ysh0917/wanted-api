package com.draka.wantedapi.repository;

import com.draka.wantedapi.entity.Tag;
import com.draka.wantedapi.entity.Tag_Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagLanguageJpaRepository extends JpaRepository<Tag_Language, Long> {
    public boolean existsTag_LanguageByTag(Tag tag);
    public Tag_Language findTag_LanguageByTag(Tag tag);
}
