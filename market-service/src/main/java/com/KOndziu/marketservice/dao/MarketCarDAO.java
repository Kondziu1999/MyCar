package com.KOndziu.marketservice.dao;

import com.KOndziu.marketservice.modules.MarketCar;
import com.KOndziu.marketservice.specifications.MarketCarQueryCriteriaConsumer;
import com.KOndziu.marketservice.specifications.SearchCriteria;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class MarketCarDAO implements IMarketCarDAO {

    @PersistenceContext
    private EntityManager entityManager;

    //basic searching
    public List<MarketCar> searchMarketCar(List<SearchCriteria> params){
        CriteriaBuilder builder=entityManager.getCriteriaBuilder();
        CriteriaQuery<MarketCar> query=builder.createQuery(MarketCar.class);
        Root r=query.from(MarketCar.class);

        Predicate predicate=builder.conjunction();

        MarketCarQueryCriteriaConsumer searchConsumer=new MarketCarQueryCriteriaConsumer(predicate,builder,r);
        params.stream().forEach(searchConsumer);
        predicate=searchConsumer.getPredicate();
        query.where(predicate);

        List<MarketCar> result=entityManager.createQuery(query).getResultList();
        return result;
    }

    @Override
    public void save(MarketCar entity) {
        entityManager.persist(entity);
    }
}
