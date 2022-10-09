package com.major.ewallet.user.service;

import com.major.ewallet.user.entity.UserInfo;
import com.major.ewallet.user.repository.UserInfoRepository;
import com.major.ewallet.user.request.CreateUserRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

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
}
