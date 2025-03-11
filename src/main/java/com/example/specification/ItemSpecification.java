package com.example.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import com.example.document.Item;

import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;


@Singleton
public class ItemSpecification {
    @PersistenceContext
    private final EntityManager entityManager;

    public ItemSpecification(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @ReadOnly
    public List<Item> findByCriteria(String name, Double priceSell, Double priceBuy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = criteriaBuilder.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null) {
            predicates.add(criteriaBuilder.equal(root.get("name"), name));
        }

        if (priceSell != null) {
            predicates.add(criteriaBuilder.equal(root.get("priceSell"), priceSell));
        }

        if (priceBuy != null) {
            predicates.add(criteriaBuilder.equal(root.get("priceBuy"), priceBuy));
        }

        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Item> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}
