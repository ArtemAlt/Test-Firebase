package ru.education.testfirebase.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.internal.FirebaseCustomAuthToken;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.education.testfirebase.entity.Person;
import ru.education.testfirebase.service.FireBaseAuthService;
import ru.education.testfirebase.service.FireBaseService;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class ApplicationController {
    private final FireBaseService service;
    private final FireBaseAuthService authService;


    @GetMapping("/checkToken")
    public String checkToken(@RequestHeader() String token) throws FirebaseAuthException, ExecutionException, InterruptedException {
        return authService.checkToken(token);
    }

    @GetMapping("/createToken")
    public String createToken(@RequestHeader() String uid) throws FirebaseAuthException {
        return authService.createToken(uid);
    }

    @GetMapping("/createUser")
    public String createUserByPhoneNumber(@RequestHeader() String phone, @RequestHeader() String email) throws FirebaseAuthException, FirebaseMessagingException {
        return authService.createNewAuthUser(phone,email);

    }


    @GetMapping("/getUserDetails")
    public Person getExample(@RequestHeader() String name) throws InterruptedException, ExecutionException {
        return service.getPersonDetails(name);
    }

    @PostMapping("/createUser")
    public String postExample(@RequestBody Person person) throws InterruptedException, ExecutionException {
        return service.saveUser(person);
    }

    @PutMapping("/updateUser")
    public String putExample(@RequestBody Person person) throws InterruptedException, ExecutionException {
        return service.updatePersonDetails(person);
    }

    @DeleteMapping("/deleteUser")
    public String deleteExample(@RequestHeader String name) throws ExecutionException, InterruptedException {
        return service.deleteUser(name);
    }
}
