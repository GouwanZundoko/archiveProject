package com.example.demo.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;

@Component
public class SelectUserInfo {

    @Autowired
    EntityManager entityManager;

    public List<Object[]> GetUserInfoAll(String companyCode, String email, String password) {

        // Query型でSQLを作成
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT   ");
        sb.append("     company_id");
        sb.append("     ,user_id");
        sb.append(" FROM     ");
        sb.append("     a001_user_info");
        sb.append(" WHERE    ");
        sb.append("     company_id = :company_id");
        sb.append(" AND mail_address = :mail_address");
        sb.append(" AND user_password = :user_password");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("company_id", companyCode);
        query.setParameter("mail_address", email);
        query.setParameter("user_password", password);

        // SQLを実行
        List<Object[]> result = query.getResultList();

        return result;
    }
}
