package ru.education.testfirebase.controller;

import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.education.testfirebase.entity.Person;
import ru.education.testfirebase.service.FireBaseAuthService;
import ru.education.testfirebase.service.FireBaseService;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Api(value = "TestController", tags = {"Test"})
@SwaggerDefinition(tags = {@Tag(name = "Test Controller", description = "Тестовый контроллер")})
@RestController
@RequiredArgsConstructor
public class ApplicationController {
    private final FireBaseService service;
    private final FireBaseAuthService authService;


    @ApiOperation(value = "Get Hello", httpMethod = "GET", notes = "Получить привет", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    @GetMapping("/")
    public String sayHello(){
        return "Hello";
    }


    @ApiOperation(value = "Send push message", httpMethod = "GET", notes = "Отправить push уведомление", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    @GetMapping("/sendMsg")
    public String sendMsg(@RequestHeader(value = "token") String token)  {
        return authService.sendPushMessage(token);
    }

    @ApiOperation(value = "Check custom token validation", httpMethod = "GET", notes = "Проверить токен", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    @GetMapping("/checkToken")
    public String checkToken(@RequestHeader(value = "token") String token) throws FirebaseAuthException {
        return authService.checkToken(token);
    }

    @ApiOperation(value = "Create custom token", httpMethod = "GET", notes = "Создать токен", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    @GetMapping("/createToken")
    public String createToken(@RequestHeader(value = "uid") String uid) throws FirebaseAuthException {
        return authService.createToken(uid);
    }
    @ApiOperation(value = "Create user", httpMethod = "GET", notes = "Создать пользователя", response = String.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    @GetMapping("/createUser")
    public String createUserByPhoneNumber(@RequestHeader(value = "phone") String phone, @RequestHeader() String email) throws FirebaseAuthException {
        return authService.createNewAuthUser(phone,email);

    }

    @GetMapping("/getAll")
    public List<String> getAllUser() throws FirebaseAuthException {
        return authService.getAll();
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
