package de.fsmpi.repository;

import de.fsmpi.model.document.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.util.*;

@Repository
public class DocumentRepositoryImpl implements DocumentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Document> search(String partialName, Lecturer lecturer, Lecture lecture, ExamForm examForm, ExamType examType) {
        Pageable allPages = new PageRequest(0, -1);
        Page<Document> tmp = this.search(partialName, lecturer, lecture, examForm, examType, allPages);
        return tmp.getContent();
    }

    @Override
    public Page<Document> search(String keyword,
                                 Lecturer lecturer,
                                 Lecture lecture,
                                 ExamForm examForm,
                                 ExamType examType,
                                 Pageable pageable) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();

        CriteriaQuery<Document> criteriaQuery = criteriaBuilder.createQuery(Document.class);
        Root<Document> document = criteriaQuery.from(Document.class);
        criteriaQuery.select(document);
        criteriaQuery.distinct(true);
        criteriaQuery.where(getRestrictions(keyword, lecturer, lecture, examForm, examType, criteriaBuilder, document));

        // total pages
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Document> countRoot = countQuery.from(criteriaQuery.getResultType());
        countQuery.select(criteriaBuilder.count(countRoot));
        countQuery.where(getRestrictions(keyword, lecturer, lecture, examForm, examType, criteriaBuilder, countRoot));
        Long count = this.entityManager.createQuery(countQuery).getSingleResult();

        // return max page, if we are on a page, that is out of bounds
        int maxPage = Math.max((int) Math.ceil(count / (double)pageable.getPageSize()) - 1, 0);
        int page = Math.max(pageable.getPageNumber(), 0);
        if(pageable.getPageNumber() > maxPage) {
            pageable = new PageRequest(maxPage, pageable.getPageSize());
        }

        // pagination
        TypedQuery<Document> query = this.entityManager.createQuery(criteriaQuery);
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(query.getResultList(), pageable, count);
    }

    private Predicate getEqualsPredicate(String parameterName,
                                         Object value,
                                         Root<?> root,
                                         CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get(parameterName), value);
    }

    private Predicate getRestrictions(String partialName, Lecturer lecturer, Lecture lecture, ExamForm examForm, ExamType examType, CriteriaBuilder criteriaBuilder, Root<?> root) {
        Predicate andPredicate = criteriaBuilder.conjunction();
        if(partialName != null && partialName.trim().length() > 0) {
            String fuzzyName = MessageFormat.format("%{0}%", partialName);
            List<Predicate> fuzzyPredicates = new ArrayList<>();
            Predicate orPredicate = criteriaBuilder.disjunction();
            orPredicate = criteriaBuilder.or(orPredicate, criteriaBuilder.like(root.get("name"), fuzzyName));
            orPredicate = criteriaBuilder.or(orPredicate, criteriaBuilder.like(root.get("lecturer").get("name"), fuzzyName));
            orPredicate = criteriaBuilder.or(orPredicate, criteriaBuilder.like(root.get("lecture").get("name"), fuzzyName));
            andPredicate = criteriaBuilder.and(andPredicate, orPredicate);
        }
        if(lecturer != null) {
            andPredicate = criteriaBuilder.and(andPredicate, getEqualsPredicate("lecturer", lecturer.getId(), root, criteriaBuilder));
        }
        if(lecture != null) {
            andPredicate = criteriaBuilder.and(andPredicate, getEqualsPredicate("lecture", lecture.getId(), root, criteriaBuilder));
        }
        if(examForm != null) {
            andPredicate = criteriaBuilder.and(andPredicate, getEqualsPredicate("examForm", examForm, root, criteriaBuilder));
        }
        if(examType != null) {
            andPredicate = criteriaBuilder.and(andPredicate, getEqualsPredicate("examType", examType, root, criteriaBuilder));
        }

        return andPredicate;
    }
}
