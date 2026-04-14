package com.example.demo.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import java.util.ArrayList;

import com.example.demo.dto.MyListsDto;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SelectPublicLists {

    @Autowired
    EntityManager entityManager;

    public List<MyListsDto> GetAllPublicLists(String UserId, String CompanyId, String ShowAuth) {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append("     c002.lists_id ");
        sb.append("   , c002.company_id ");
        sb.append("   , c002.user_id ");
        sb.append("   , c002.lists_title ");
        sb.append("   , c002.lists_text ");
        sb.append("   , c002.show_auth ");
        sb.append("   , c002.created_at ");
        sb.append("   , c002.updated_at ");
        sb.append("   , m002.show_auth ");
        sb.append(" FROM ");
        sb.append("     c002_lists_info c002 ");
        sb.append(" LEFT JOIN ");
        sb.append("     m002_mylists_info m002 ");
        sb.append(" ON ");
        sb.append("     m002.lists_id = c002.lists_id ");
        sb.append("     AND m002.user_id = :user_id ");
        sb.append("     AND m002.company_id = :company_id ");
        sb.append(" WHERE ");
        sb.append("     c002.company_id = :company_id ");
        sb.append("     AND c002.show_auth = :show_auth ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("user_id", UserId);
        query.setParameter("company_id", CompanyId);
        query.setParameter("show_auth", ShowAuth);

        List<Object[]> result = query.getResultList();

        List<MyListsDto> dtoList = new ArrayList<>();

        for (Object[] row : result) {
            MyListsDto dto = new MyListsDto();

            dto.setListsId(String.valueOf(row[0]));
            dto.setCompanyId(String.valueOf(row[1]));
            dto.setUserId(String.valueOf(row[2]));
            dto.setListsTitle((String) row[3]);
            dto.setListsText((String) row[4]);
            dto.setShowPublicAuth(String.valueOf(row[5]));
            dto.setCreatedAt(String.valueOf(row[6]));
            dto.setUpdatedAt(String.valueOf(row[7]));
            dto.setShowAuth(String.valueOf(row[8]));

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Transactional
    public void ImportPublicLists(String userId, String companyId, String contentsId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" INSERT INTO m002_mylists_info ( ");
        sb.append("     company_id, ");
        sb.append("     user_id, ");
        sb.append("     lists_id, ");
        sb.append("     show_auth ");
        sb.append(" ) VALUES ( ");
        sb.append("     :companyId, ");
        sb.append("     :userId, ");
        sb.append("     :lists_id, ");
        sb.append("     '0' ");
        sb.append(" ) ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userId", userId);
        query.setParameter("companyId", companyId);
        query.setParameter("lists_id", contentsId);

        query.executeUpdate();
    }
}