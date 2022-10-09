package com.major.ewallet.user.repository;

import com.major.ewallet.user.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {


    Optional<UserInfo> findByEmail(String email);
}
