package com.example.demo.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;

import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InsertMyContents {

    @Autowired
    EntityManager entityManager;

    @Transactional
    public void InsertContents(String userId, String companyId, Long contentsId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" INSERT INTO m001_mycontents_info ( ");
        sb.append("     company_id, ");
        sb.append("     user_id, ");
        sb.append("     contents_id, ");
        sb.append("     show_auth ");
        sb.append(" ) VALUES ( ");
        sb.append("     :companyId, ");
        sb.append("     :userId, ");
        sb.append("     :contentsId, ");
        sb.append("     '1' ");
        sb.append(" ) ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userId", userId);
        query.setParameter("companyId", companyId);
        query.setParameter("contentsId", contentsId);

        query.executeUpdate();
    }
}