package com.appsdeveloper.mobileappws;

import com.appsdeveloper.mobileappws.Entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRespository extends CrudRepository<UserEntity,Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);
}
