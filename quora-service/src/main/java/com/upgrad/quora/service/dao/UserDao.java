package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity){
        entityManager.persist(userEntity);
        return userEntity;
    }

    public UserEntity getUserByUsername(final String userName){
        try {
            return entityManager.createNamedQuery("userByUserName", UserEntity.class)
                    .setParameter("userName", userName).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public UserEntity getUserByEmail(final String userEmail){
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class)
                    .setParameter("userEmail", userEmail).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public UserEntity getUserByUuid(final String userUuid){
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class)
                    .setParameter("uuid", userUuid)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public UserAuthTokenEntity getUserAuthToken(final String accessToken){
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken",UserAuthTokenEntity.class)
                    .setParameter("accessToken", accessToken).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public void updateUser(final UserEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }

    public void deleteUser(final String userUuid){
        UserEntity userEntity = getUserByUuid(userUuid);
        entityManager.remove(userEntity);
    }

    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthEntity) {
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }
}
