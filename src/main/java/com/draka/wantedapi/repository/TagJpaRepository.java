package com.draka.wantedapi.repository;

import com.draka.wantedapi.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagJpaRepository extends JpaRepository<Tag, Long> {
    public boolean existsTagByName(String name);
    public Tag findTagByName(String name);
    @Query(value =
            "SELECT * FROM TAG " +
            "WHERE ID IN (" +
                    "SELECT TL.TAG_ID FROM TAG_LANGUAGE TL JOIN LANGUAGE L ON TL.LANGUAGE_ID=L.ID AND L.NAME=:languageName" +
                    ") AND NAME=:tagName"
            , nativeQuery = true)
    public Tag selectTagInTagLanguage(String languageName, String tagName);
    public List<Tag> findTagsByIdIn(List<Long> ids);
}
