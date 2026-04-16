package com.example.demo.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Component
public class InsertRegistSQL {
    @Autowired
    EntityManager entityManager;

    @Transactional
    public void InsertUserInfo(String mailAddress, String userPassword) {
        StringBuilder sb = new StringBuilder();
        sb.append(" INSERT INTO a001_user_info ( ");
        sb.append("     company_id ");
        sb.append("     ,mail_address ");
        sb.append("     ,user_password ");
        sb.append("     ,user_auth ");
        sb.append("     ,created_at ");
        sb.append("     ,updated_at ");
        sb.append(" ) VALUES ( ");
        sb.append("     :company_id, ");
        sb.append("     :mail_address, ");
        sb.append("     :user_password, ");
        sb.append("     :user_auth, ");
        sb.append("     CURRENT_TIMESTAMP, ");
        sb.append("     CURRENT_TIMESTAMP ");
        sb.append(" ) ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("company_id", "");
        query.setParameter("mail_address", mailAddress);
        query.setParameter("user_password", userPassword);
        query.setParameter("user_auth", "1");

        query.executeUpdate();
    }

    public int GetUserInfo(String mailAddress) {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append("     count(*) ");
        sb.append(" FROM ");
        sb.append("     a001_user_info a001 ");
        sb.append(" WHERE ");
        sb.append("     a001.mail_address = :mail_address ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("mail_address", mailAddress);

        Number count = (Number) query.getSingleResult();

        return count.intValue();
    }

    @Transactional
    public int UpdateUserInfo(String mailAddress, String userPassword) {
        StringBuilder sb = new StringBuilder();
        sb.append(" UPDATE a001_user_info SET ");
        sb.append("     user_password = :user_password, ");
        sb.append("     updated_at = CURRENT_TIMESTAMP ");
        sb.append(" WHERE ");
        sb.append("     mail_address = :mail_address ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("mail_address", mailAddress);
        query.setParameter("user_password", userPassword);

        return query.executeUpdate(); // 更新件数返す
    }
}
