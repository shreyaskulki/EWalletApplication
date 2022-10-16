package com.major.ewallet.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.major.ewallet.user.entity.UserInfo;
import com.major.ewallet.user.repository.UserInfoRepository;
import com.major.ewallet.user.request.CreateUserRequestDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    private static final String USER_CREATED = "USER_CREATED";

    @Autowired
    ObjectMapper objectMapper;

    @Transactional(rollbackOn=Exception.class)
    public UserInfo createANewUser(CreateUserRequestDto createUserRequestDto){
            UserInfo userInfo = createUserRequestDto.toUserInfo();
            Optional<UserInfo> userInfoByEmail = userInfoRepository.findByEmail(userInfo.getEmail());

            if(userInfoByEmail.isPresent()){
                log.info("**********----DUPLICATE EMAIL, USER ALREADY HAS ACCOUNT----*********");
            }

            return saveOrUpdate(userInfo);
    }

    private UserInfo saveOrUpdate(UserInfo userInfo){
        return  userInfoRepository.save(userInfo);
    }

    @SneakyThrows
    public void sendMessage(UserInfo userInfo){
        log.info("*** SENDING MESSAGE TO USER_CREATED ***");
        objectMapper.registerModule(new JavaTimeModule());
        String message = objectMapper.writeValueAsString(userInfo);
        addCallBack(message,kafkaTemplate.send(USER_CREATED,message));
    }

    public void addCallBack(String message, ListenableFuture<SendResult<String, String>> send){
        send.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("****** FAILURE TO SEND MESSAGE "+message);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("****** SUCCESS TO SEND MESSAGE "+message+" WITH PARTITION "+result.getRecordMetadata().partition()
                +" WITH OFFSET "+result.getRecordMetadata().offset());
            }
        });
    }

    public UserInfo fetchUserById(Long id) {
        Optional<UserInfo> user = userInfoRepository.findById(id);
        if(user.isEmpty()){
            throw  new RuntimeException();
        }

        return user.get();
    }
}
