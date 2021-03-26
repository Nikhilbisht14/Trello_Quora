package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private QuestionDao questionDao;


    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(final QuestionEntity questionEntity, final String authorization) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthToken = userDao.getUserAuthToken(authorization);

        // Validate if user is signed in or not
        if(userAuthToken == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        // Validate if user has signed out
        if(userAuthToken.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        }

        questionEntity.setUser(userAuthToken.getUser());
        QuestionEntity question = questionDao.createQuestion(questionEntity);

        return question;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestion(final String authorization) throws AuthorizationFailedException {

        UserAuthTokenEntity userAuthToken = userDao.getUserAuthToken(authorization);

        // Validate if user is signed in or not
        if(userAuthToken == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        // Validate if user has signed out
        if(userAuthToken.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        }

        return questionDao.getAllQuestion();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestionDetails(final QuestionEntity questionEntity, final String authorization) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthTokenEntity userAuthToken = userDao.getUserAuthToken(authorization);

        // Validate if user is signed in or not
        if(userAuthToken == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        // Validate if user has signed out or not
        if(userAuthToken.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        }

        //validate questions exists or not
        QuestionEntity questionByUuid = questionDao.getQuestionByUuid(questionEntity.getUuid());
        if(questionByUuid == null){
            throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist");
        }

        if(questionByUuid.getUser() != userAuthToken.getUser()){
            throw new AuthorizationFailedException("ATHR-003","Only the question owner can edit the question");
        }

        questionEntity.setUser(questionByUuid.getUser());
        questionByUuid.setId(questionByUuid.getId());
        questionEntity.setDate(questionByUuid.getDate());

        return questionDao.editQuestionDetails(questionEntity);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserQuestion(final String authorization, final String questionId) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthTokenEntity userAuthToken = userDao.getUserAuthToken(authorization);

        // Validate if user is signed in or not
        if(userAuthToken == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        // Validate if user has signed out
        if(userAuthToken.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        }

        QuestionEntity questionByUuid = questionDao.getQuestionByUuid(questionId);

        //validate if user exists or not
        if(questionByUuid == null){
            throw new InvalidQuestionException("USR-001","Entered question uuid does not exist");
        }

        if(userAuthToken.getUser() != questionByUuid.getUser()){
            if(userAuthToken.getUser().getRole().equals("nonadmin")){
                throw new AuthorizationFailedException("ATHR-003", "Oly the question owner or admin can delete the question");
            }
        }

        questionDao.deleteQuestion(questionId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestionsByUser(final String userId, final String authorization) throws AuthorizationFailedException, UserNotFoundException {

        UserAuthTokenEntity userAuthToken = userDao.getUserAuthToken(authorization);

        // Validate if user is signed in or not
        if(userAuthToken == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        // Validate if user has signed out
        if(userAuthToken.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        }

        UserEntity userEntity = userDao.getUserByUuid(userId);

        //validate if user exists or not
        if(userEntity == null){
            throw new UserNotFoundException("USR-001","User with entered uuid whose question details are to be seen does not exist");
        }

        return questionDao.getAllQuestionsByUser(userId);
    }
}
