package com.example.demo.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import com.example.demo.dto.MyContentsDto;

@Component
public class SelectMyOneContents {

    @Autowired
    EntityManager entityManager;

    public List<MyContentsDto> GetMyContents(String UserId, String CompanyId, String contentsId) {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append("     c001.contents_id ");
        sb.append("     ,c001.company_id ");
        sb.append("     ,c001.user_id ");
        sb.append("     ,c001.contents_title ");
        sb.append("     ,c001.image_url ");
        sb.append("     ,c001.movie_url ");
        sb.append("     ,c001.file_url ");
        sb.append("     ,c001.contents_text ");
        sb.append("     ,m001.show_auth ");
        sb.append("     ,c001.contents_tag ");
        sb.append("     ,c001.created_at ");
        sb.append("     ,c001.updated_at ");
        sb.append(" FROM ");
        sb.append("     c001_contents_info c001 ");
        sb.append(" LEFT JOIN ");
        sb.append("     m001_mycontents_info m001 ");
        sb.append(" ON ");
        sb.append("     m001.contents_id = c001.contents_id ");
        sb.append(" WHERE ");
        sb.append("     c001.contents_id = :contents_id ");

        Query query = entityManager.createNativeQuery(sb.toString());
        // query.setParameter("user_id", UserId);
        // query.setParameter("company_id", CompanyId);
        query.setParameter("contents_id", contentsId);

        List<Object[]> result = query.getResultList();

        // DTO変換
        List<MyContentsDto> dtoList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        for (Object[] row : result) {
            MyContentsDto dto = new MyContentsDto();

            dto.setContentsId(String.valueOf(row[0]));
            dto.setCompanyId(String.valueOf(row[1]));
            dto.setUserId(String.valueOf(row[2]));
            dto.setContentsTitle((String) row[3]);
            dto.setImageUrl((String) row[4]);
            dto.setMovieUrl((String) row[5]);
            dto.setFileUrl((String) row[6]);
            dto.setContentsText((String) row[7]);
            dto.setShowAuth(String.valueOf(row[8]));
            dto.setTag((String) (row[9]));
            if (row[10] != null) {
                dto.setCreateDate(sdf.format(row[10]));
            }
            if (row[11] != null) {
                dto.setUpdateDate(sdf.format(row[11]));
            }

            dtoList.add(dto);
        }

        return dtoList;
    }
}