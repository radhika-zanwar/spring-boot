package com.appsdeveloper.mobileappws.service.implementation;

import com.appsdeveloper.mobileappws.Entity.UserEntity;
import com.appsdeveloper.mobileappws.UserRespository;
import com.appsdeveloper.mobileappws.exceptions.UserServiceException;
import com.appsdeveloper.mobileappws.service.UserService;
import com.appsdeveloper.mobileappws.shared.dto.UserDto;
import com.appsdeveloper.mobileappws.shared.dto.Utils;
import com.appsdeveloper.mobileappws.ui.model.response.ErrorMessages;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRespository userRespository;
    @Autowired
    Utils utils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public UserDto createUser(UserDto user) {

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user,userEntity);

        String publicUserId = utils.generateUserId(30);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setUserId(publicUserId);

        UserEntity storedUserDetails = userRespository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails,returnValue);
        return returnValue;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRespository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity,returnValue);
        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRespository.findByUserId(userId);
        if (userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto user) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRespository.findByUserId(userId);
        if (userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());

        UserEntity updatedUserDetails = userRespository.save(userEntity);
        BeanUtils.copyProperties(updatedUserDetails,returnValue);

        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRespository.findByUserId(userId);
        if(userEntity == null) throw new UsernameNotFoundException(userId);
        userRespository.delete(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRespository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),new ArrayList<>());
    }
}
