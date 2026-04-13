package com.example.demo.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import java.util.ArrayList;

import com.example.demo.dto.MyListsDto;

@Component
public class SelectPublicLists {

    @Autowired
    EntityManager entityManager;

    public List<MyListsDto> GetAllPublicLists(String CompanyId, String ShowAuth) {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append("     c002.lists_id ");
        sb.append("     , c002.company_id ");
        sb.append("     , c002.user_id ");
        sb.append("     , c002.lists_title ");
        sb.append("     , c002.lists_text ");
        sb.append("     , c002.show_auth  ");
        sb.append(" FROM ");
        sb.append("     c002_lists_info c002  ");
        sb.append(" WHERE ");
        sb.append("     c002.company_id = :company_id  ");
        sb.append("     AND c002.show_auth = :show_auth ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("company_id", CompanyId);
        query.setParameter("show_auth", ShowAuth);

        List<Object[]> result = query.getResultList();

        // DTO変換
        List<MyListsDto> dtoList = new ArrayList<>();

        for (Object[] row : result) {
            MyListsDto dto = new MyListsDto();

            dto.setListsId(String.valueOf(row[0]));
            dto.setCompanyId(String.valueOf(row[1]));
            dto.setUserId(String.valueOf(row[2]));
            dto.setListsTitle((String) row[3]);
            dto.setListsText((String) row[4]);
            dto.setShowAuth(String.valueOf(row[5]));

            dtoList.add(dto);
        }

        return dtoList;
    }
}