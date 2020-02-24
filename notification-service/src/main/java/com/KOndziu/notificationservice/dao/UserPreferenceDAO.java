package com.KOndziu.notificationservice.dao;

import com.KOndziu.notificationservice.modules.UserPreference;
import com.KOndziu.notificationservice.specifications.SearchCriteria;
import com.KOndziu.notificationservice.specifications.UserPreferenceQueryCriteriaConsumer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserPreferenceDAO implements IUserPreferenceDAO {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<UserPreference> searchUserPreference(List<SearchCriteria> params) {
        CriteriaBuilder builder=entityManager.getCriteriaBuilder();
        CriteriaQuery<UserPreference> query=builder.createQuery(UserPreference.class);
        Root r=query.from(UserPreference.class);

        Predicate predicate=builder.conjunction();

        UserPreferenceQueryCriteriaConsumer searchConsumer=new UserPreferenceQueryCriteriaConsumer(predicate,builder,r);
        params.stream().forEach(searchConsumer);
        predicate=searchConsumer.getPredicate();
        query.where(predicate);

        List<UserPreference> result=entityManager.createQuery(query).getResultList();
        return result;


    }
}
