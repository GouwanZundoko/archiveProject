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
public class SelectMyContents {

    @Autowired
    EntityManager entityManager;

    public List<MyContentsDto> GetAllMyContents(String UserId, String CompanyId) {

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
        sb.append("     m001_mycontents_info m001 ");
        sb.append(" LEFT JOIN ");
        sb.append("     c001_contents_info c001 ");
        sb.append(" ON ");
        sb.append("     c001.contents_id = m001.contents_id ");
        sb.append(" WHERE ");
        sb.append("     m001.company_id = :company_id ");
        sb.append("     AND m001.user_id = :user_id ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("user_id", UserId);
        query.setParameter("company_id", CompanyId);

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
            dto.setShowPublicAuth(String.valueOf(row[12]));

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Transactional
    public int UpdateMyContents(String userId, String companyId, String contentsId, String releaseFlg) {
        StringBuilder sb = new StringBuilder();
        sb.append(" UPDATE c001_contents_info SET ");
        sb.append("     show_auth = :show_auth ");
        sb.append(" WHERE contents_id = :contentsId ");
        sb.append("   AND user_id = :userId ");
        sb.append("   AND company_id = :companyId ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("contentsId", contentsId);
        query.setParameter("userId", userId);
        query.setParameter("companyId", companyId);
        query.setParameter("show_auth", releaseFlg);

        return query.executeUpdate();
    }

    @Transactional
    public int DeleteMyContents(String userId, String companyId, String contentsId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" DELETE FROM ");
        sb.append("     m001_mycontents_info ");
        sb.append(" WHERE contents_id = :contentsId ");
        sb.append("   AND user_id = :userId ");
        sb.append("   AND company_id = :companyId ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("contentsId", contentsId);
        query.setParameter("userId", userId);
        query.setParameter("companyId", companyId);
        return query.executeUpdate();
    }

    @Transactional
    public int DeletePublicContents(String userId, String companyId, String contentsId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" DELETE FROM ");
        sb.append("     c001_contents_info c001 ");
        sb.append(" WHERE ");
        sb.append("     c001.contents_id = :contents_Id ");
        sb.append("     AND c001.user_id = :user_id ");
        sb.append("     AND c001.company_id = :company_id ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("contents_Id", contentsId);
        query.setParameter("user_id", userId);
        query.setParameter("company_id", companyId);
        return query.executeUpdate();
    }
}