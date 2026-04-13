package com.example.demo.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import com.example.demo.dto.MyContentsDto;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SelectPublicContents {

    @Autowired
    EntityManager entityManager;

    public List<MyContentsDto> GetAllPublicContents(String UserId, String CompanyId, String ShowAuth) {

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
        sb.append("     ,c001.show_auth ");
        sb.append(" FROM ");
        sb.append("     c001_contents_info c001 ");
        sb.append(" LEFT JOIN ");
        sb.append("     m001_mycontents_info m001 ");
        sb.append(" ON ");
        sb.append("     m001.contents_id = c001.contents_id ");
        sb.append("     AND m001.user_id = :user_id ");
        sb.append("     AND m001.company_id = :company_id ");
        sb.append(" WHERE ");
        sb.append("     c001.company_id = :company_id ");
        sb.append("     AND c001.show_auth = :show_auth ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("user_id", UserId);
        query.setParameter("company_id", CompanyId);
        query.setParameter("show_auth", ShowAuth);

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
            dto.setCreateDate(sdf.format(row[10]));
            dto.setUpdateDate(sdf.format(row[11]));
            dto.setShowPublicAuth(String.valueOf(row[12]));

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Transactional
    public void ImportPublicContents(String userId, String companyId, String contentsId) {
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
        sb.append("     '0' ");
        sb.append(" ) ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userId", userId);
        query.setParameter("companyId", companyId);
        query.setParameter("contentsId", contentsId);

        query.executeUpdate();
    }
}