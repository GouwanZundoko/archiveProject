package com.example.demo.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.MyContentsListDto;
import com.example.demo.dto.MyListsDto;

@Component
public class SelectMyLists {

    @Autowired
    EntityManager entityManager;

    public List<MyListsDto> GetAllMyLists(String UserId, String CompanyId) {

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
        sb.append("     m002_mylists_info m002 ");
        sb.append("     LEFT JOIN c002_lists_info c002 ");
        sb.append("         ON c002.lists_id = m002.lists_id ");
        sb.append(" WHERE ");
        sb.append("     m002.company_id = :company_id ");
        sb.append("     AND m002.user_id = :user_id ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("user_id", UserId);
        query.setParameter("company_id", CompanyId);

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

    public List<MyListsDto> GetOneMyLists(String UserId, String CompanyId, String ListId) {

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
        sb.append("     m002_mylists_info m002 ");
        sb.append("     LEFT JOIN c002_lists_info c002 ");
        sb.append("         ON c002.lists_id = m002.lists_id ");
        sb.append(" WHERE ");
        sb.append("     m002.company_id = :company_id ");
        sb.append("     AND m002.user_id = :user_id ");
        sb.append("     AND m002.lists_id = :lists_id ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("user_id", UserId);
        query.setParameter("company_id", CompanyId);
        query.setParameter("lists_id", ListId);

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

    public List<MyContentsListDto> getContentsByListId(String listId) {

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append("     c003.contents_id ");
        sb.append("   , c001.contents_title ");
        sb.append(" FROM ");
        sb.append("     c003_lists_contents c003 ");
        sb.append("     LEFT JOIN c001_contents_info c001 ");
        sb.append("       ON c003.contents_id = c001.contents_id ");
        sb.append(" WHERE ");
        sb.append("     c003.lists_id = :lists_id ");
        sb.append(" ORDER BY ");
        sb.append("     c003.lists_order ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("lists_id", listId);

        List<Object[]> result = query.getResultList();
        List<MyContentsListDto> list = new ArrayList<>();

        for (Object[] row : result) {
            MyContentsListDto dto = new MyContentsListDto();
            dto.setContentsId(String.valueOf(row[0]));
            dto.setContentsTitle((String) row[1]);
            list.add(dto);
        }

        return list;
    }

    @Transactional
    public void DeleteMyContentsList(String userId, String companyId, String listsId) {

        StringBuilder sb = new StringBuilder();
        sb.append(" DELETE FROM");
        sb.append("     c003_lists_contents ");
        sb.append(" WHERE ");
        sb.append("     company_id = :company_Id ");
        sb.append("     AND user_id = :user_Id ");
        sb.append("     AND lists_id = :lists_id ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("user_Id", userId);
        query.setParameter("company_Id", companyId);
        query.setParameter("lists_id", listsId);

        query.executeUpdate();
    }

    @Transactional
    public Long InsertAllPublicLists(String UserId, String CompanyId, String listsTitle, String listsText) {

        StringBuilder sb = new StringBuilder();
        sb.append(" INSERT INTO c002_lists_info ( ");
        sb.append("     company_id, ");
        sb.append("     user_id, ");
        sb.append("     lists_title, ");
        sb.append("     lists_text, ");
        sb.append("     show_auth, ");
        sb.append("     created_at, ");
        sb.append("     updated_at ");
        sb.append(" ) VALUES ( ");
        sb.append("     :companyId, ");
        sb.append("     :userId, ");
        sb.append("     :lists_title, ");
        sb.append("     :lists_text, ");
        sb.append("     '0', ");
        sb.append("     CURRENT_TIMESTAMP, ");
        sb.append("     CURRENT_TIMESTAMP ");
        sb.append(" ) ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userId", UserId);
        query.setParameter("companyId", CompanyId);
        query.setParameter("lists_title", listsTitle);
        query.setParameter("lists_text", listsText);

        query.executeUpdate();

        Object id = entityManager.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult();

        return ((Number) id).longValue();
    }

    @Transactional
    public void InsertMyLists(String userId, String companyId, Long listsId) {
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
        sb.append("     '1' ");
        sb.append(" ) ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("userId", userId);
        query.setParameter("companyId", companyId);
        query.setParameter("lists_id", listsId);

        query.executeUpdate();
    }

    @Transactional
    public void InsertPublicList(String listId, String userId, String companyId, List<String> contentsIds) {

        StringBuilder sb = new StringBuilder();
        sb.append(" INSERT INTO c003_lists_contents ( ");
        sb.append("     lists_id, ");
        sb.append("     contents_id, ");
        sb.append("     company_id, ");
        sb.append("     user_id, ");
        sb.append("     lists_order, ");
        sb.append("     show_auth, ");
        sb.append("     created_at, ");
        sb.append("     updated_at ");
        sb.append(" ) VALUES ( ");
        sb.append("     :lists_id, ");
        sb.append("     :contents_id, ");
        sb.append("     :company_id, ");
        sb.append("     :user_id, ");
        sb.append("     :lists_order, ");
        sb.append("     :show_auth, ");
        sb.append("     CURRENT_TIMESTAMP, ");
        sb.append("     CURRENT_TIMESTAMP ");
        sb.append(" ) ");

        for (int i = 0; i < contentsIds.size(); i++) {

            Query query = entityManager.createNativeQuery(sb.toString());

            query.setParameter("lists_id", listId);
            query.setParameter("contents_id", contentsIds.get(i));
            query.setParameter("company_id", companyId);
            query.setParameter("user_id", userId);
            query.setParameter("lists_order", String.valueOf(i + 1)); // char(6)なので文字列化
            query.setParameter("show_auth", "1");

            query.executeUpdate();
        }
    }

    @Transactional
    public int DeleteMyLists(String userId, String companyId, String listsId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" DELETE FROM ");
        sb.append("     m002_mylists_info ");
        sb.append(" WHERE lists_id = :lists_id ");
        sb.append("   AND user_id = :user_Id ");
        sb.append("   AND company_id = :company_Id ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("lists_id", listsId);
        query.setParameter("user_Id", userId);
        query.setParameter("company_Id", companyId);
        return query.executeUpdate();
    }

    @Transactional
    public int DeletePublicLists(String userId, String companyId, String listsId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" DELETE FROM ");
        sb.append("     c002_lists_info ");
        sb.append(" WHERE ");
        sb.append("     lists_id = :lists_id ");
        sb.append("     AND user_id = :user_id ");
        sb.append("     AND company_id = :company_id ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("lists_id", listsId);
        query.setParameter("user_id", userId);
        query.setParameter("company_id", companyId);
        return query.executeUpdate();
    }

    @Transactional
    public int DeletePublicContentsList(String userId, String companyId, String listsId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" DELETE FROM ");
        sb.append("     c003_lists_contents ");
        sb.append(" WHERE ");
        sb.append("     lists_id = :lists_id ");
        sb.append("     AND user_id = :user_id ");
        sb.append("     AND company_id = :company_id ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("lists_id", listsId);
        query.setParameter("user_id", userId);
        query.setParameter("company_id", companyId);
        return query.executeUpdate();
    }

    @Transactional
    public int UpdateMyLists(String userId, String companyId, String listsId, String releaseFlg) {
        StringBuilder sb = new StringBuilder();
        sb.append(" UPDATE c002_lists_info SET ");
        sb.append("     show_auth = :show_auth ");
        sb.append(" WHERE lists_id = :listsId ");
        sb.append("   AND user_id = :userId ");
        sb.append("   AND company_id = :companyId ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("listsId", listsId);
        query.setParameter("userId", userId);
        query.setParameter("companyId", companyId);
        query.setParameter("show_auth", releaseFlg);

        return query.executeUpdate();
    }
}