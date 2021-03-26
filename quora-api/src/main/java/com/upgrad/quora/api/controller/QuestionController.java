package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private QuestionBusinessService questionBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(final QuestionRequest questionRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        final QuestionEntity questionEntity = new QuestionEntity();

        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setDate(ZonedDateTime.now());

        QuestionEntity question = questionBusinessService.createQuestion(questionEntity, authorization);

        QuestionResponse question_created = new QuestionResponse().id(question.getUuid()).status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(question_created, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/question/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        List<QuestionEntity> allQuestion = questionBusinessService.getAllQuestion(authorization);

        List<QuestionDetailsResponse> allQuestionResponse = new ArrayList<>();

        for(int i = 0; i < allQuestion.size(); i++){
            QuestionDetailsResponse detailsResponse = new QuestionDetailsResponse()
                    .content(allQuestion.get(i).getContent())
                    .id(allQuestion.get(i).getUuid());

            allQuestionResponse.add(detailsResponse);
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(allQuestionResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent(final QuestionEditRequest questionEditRequest, @PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {

        final QuestionEntity questionEntity = new QuestionEntity();

        questionEntity.setContent(questionEditRequest.getContent());
        questionEntity.setUuid(questionId);

        QuestionEntity editQuestionDetails = questionBusinessService.editQuestionDetails(questionEntity, authorization);

        QuestionEditResponse question_edited = new QuestionEditResponse().id(editQuestionDetails.getUuid()).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(question_edited, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {

        //Delete Question
        questionBusinessService.deleteUserQuestion(authorization,questionId);

        //Sending Response
        QuestionDeleteResponse question_deleted = new QuestionDeleteResponse().id(questionId).status("QUESTION DELETED");

        return new ResponseEntity<QuestionDeleteResponse>(question_deleted, HttpStatus.OK);
    }

    @GetMapping(path = "/question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@PathVariable("userId") final String userId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {

        List<QuestionEntity> allQuestionsByUser = questionBusinessService.getAllQuestionsByUser(userId, authorization);

        List<QuestionDetailsResponse> allQuestionDetailsResponse = new ArrayList<>();

        for (int i = 0 ; i < allQuestionsByUser.size(); i++){
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse()
                    .content(allQuestionsByUser.get(i).getContent()).id(allQuestionsByUser.get(i).getUuid());

            allQuestionDetailsResponse.add(questionDetailsResponse);
        }

        return new ResponseEntity<List<QuestionDetailsResponse>>(allQuestionDetailsResponse, HttpStatus.FOUND);
    }
}
