package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;


    public QuestionEntity createQuestion(QuestionEntity questionEntity){
        entityManager.persist(questionEntity);
        return questionEntity;
    }


    public List<QuestionEntity> getAllQuestion(){
        try {
            return entityManager.createNamedQuery("getAllQuestion", QuestionEntity.class).getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }

    public QuestionEntity getQuestionByUuid(final String uuid){
        try {
            return entityManager.createNamedQuery("questionByUuid", QuestionEntity.class)
                    .setParameter("userUuid", uuid)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public QuestionEntity editQuestionDetails(QuestionEntity questionEntity){
        entityManager.merge(questionEntity);
        return questionEntity;
    }

    public void deleteQuestion(final String questionId) {
        QuestionEntity questionByUuid = getQuestionByUuid(questionId);
        entityManager.remove(questionByUuid);
    }

    public List<QuestionEntity> getAllQuestionsByUser(final String uuid){
        try {
            return entityManager.createNamedQuery("questionByUserId", QuestionEntity.class).setParameter("uuid", uuid).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
