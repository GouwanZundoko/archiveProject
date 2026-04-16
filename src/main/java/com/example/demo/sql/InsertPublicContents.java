package com.example.demo.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InsertPublicContents {

    @Autowired
    EntityManager entityManager;

    @Transactional
    public Long InsertContents(String userId, String companyId, String title, String description, String tags,
            String thumbnailPath, String videoPath, String documentPath) {
        StringBuilder sb = new StringBuilder();
        sb.append(" INSERT INTO c001_contents_info ( ");
        sb.append("     company_id, ");
        sb.append("     user_id, ");
        sb.append("     contents_title, ");
        sb.append("     contents_tag, ");
        sb.append("     image_url, ");
        sb.append("     movie_url, ");
        sb.append("     file_url, ");
        sb.append("     contents_text, ");
        sb.append("     show_auth, ");
        sb.append("     created_at, ");
        sb.append("     updated_at ");
        sb.append(" ) VALUES ( ");
        sb.append("     :CompanyId, ");
        sb.append("     :UserId, ");
        sb.append("     :title, ");
        sb.append("     :tags, ");
        sb.append("     :thumbnailPath, ");
        sb.append("     :videoPath, ");
        sb.append("     :documentPath, ");
        sb.append("     :description, ");
        sb.append("     0, ");
        sb.append("     CURRENT_TIMESTAMP, ");
        sb.append("     CURRENT_TIMESTAMP ");
        sb.append(" ) ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("CompanyId", companyId);
        query.setParameter("UserId", userId);
        query.setParameter("title", title);
        query.setParameter("tags", tags);
        query.setParameter("description", description);
        query.setParameter("thumbnailPath", thumbnailPath);
        query.setParameter("videoPath", videoPath);
        query.setParameter("documentPath", documentPath);

        query.executeUpdate();

        // idを取得
        Object id = entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult();

        return ((Number) id).longValue();
    }

    @Transactional
    public int UpdateContents(String contentsId, String userId, String companyId, String title, String description,
            String tags, String thumbnailPath, String videoPath, String documentPath) {

        StringBuilder sb = new StringBuilder();
        sb.append(" UPDATE c001_contents_info SET ");
        sb.append("     contents_title = :title, ");
        sb.append("     contents_tag = :tags, ");
        sb.append("     image_url = :thumbnailPath, ");
        sb.append("     movie_url = :videoPath, ");
        sb.append("     file_url = :documentPath, ");
        sb.append("     contents_text = :description, ");
        sb.append("     updated_at = CURRENT_TIMESTAMP ");
        sb.append(" WHERE contents_id = :contentsId ");
        sb.append("   AND user_id = :userId ");
        sb.append("   AND company_id = :companyId ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("contentsId", contentsId);
        query.setParameter("userId", userId);
        query.setParameter("companyId", companyId);
        query.setParameter("title", title);
        query.setParameter("tags", tags);
        query.setParameter("description", description);
        query.setParameter("thumbnailPath", thumbnailPath);
        query.setParameter("videoPath", videoPath);
        query.setParameter("documentPath", documentPath);

        return query.executeUpdate(); // 更新件数返す
    }
}