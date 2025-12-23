package com.hellFire.FoodOrderingSystemThinkify.controllers;


import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.AddAppUserRequest;
import com.hellFire.FoodOrderingSystemThinkify.services.AppUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/create")
    public ResponseEntity<?> addUser(@Valid @RequestBody AddAppUserRequest request){
        try{
            return new ResponseEntity<>(appUserService.createAppUser(request), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllAppUser(){
        return new ResponseEntity<>(appUserService.getAllAppUsers(), HttpStatus.OK);
    }
}
