package com.appsdeveloper.mobileappws.ui.controller;

import com.appsdeveloper.mobileappws.exceptions.UserServiceException;
import com.appsdeveloper.mobileappws.service.UserService;
import com.appsdeveloper.mobileappws.shared.dto.UserDto;
import com.appsdeveloper.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloper.mobileappws.ui.model.response.ErrorMessages;
import com.appsdeveloper.mobileappws.ui.model.response.OperationStatusModel;
import com.appsdeveloper.mobileappws.ui.model.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping(path = "/{id}")
    public UserRest getUser(@PathVariable String id){
        UserRest resultValue = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto,resultValue);
        return resultValue;
    }
    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception{
        UserRest returnValue = new UserRest();
        if(userDetails.getFirstName().isEmpty()) throw
               new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetails,userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser,returnValue);

        return returnValue;
    }
    @PutMapping(path = "/{id}")
    public UserRest updateUser(@RequestBody UserDetailsRequestModel userDetails, @PathVariable String id) {
        UserRest returnValue = new UserRest();
        if(userDetails.getFirstName().isEmpty()) throw
                new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetails,userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser,returnValue);

        return returnValue;
    }
    @DeleteMapping(path = "/{id}")
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel resultValue = new OperationStatusModel();
        userService.deleteUser(id);
        resultValue.setOperationResult("Success");
        resultValue.setOperationName("Delete");
        return resultValue;
    }
}
