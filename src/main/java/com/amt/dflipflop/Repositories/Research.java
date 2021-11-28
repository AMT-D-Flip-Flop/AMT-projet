package com.amt.dflipflop.Repositories;

import com.amt.dflipflop.Entities.Product;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class Research {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Product> searchDescription(String text) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        //Create query using Hibernate Search query DSL (not lucene directly)
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Product.class).get();

        //Lucene query
        org.apache.lucene.search.Query query = queryBuilder.keyword().onField("description").matching(text).createQuery();

        //Wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Product.class);

        return (List<Product>) jpaQuery.getResultList();
    }


}
