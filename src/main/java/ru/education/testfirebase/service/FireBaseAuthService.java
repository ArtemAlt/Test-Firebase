package ru.education.testfirebase.service;


import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.*;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.education.testfirebase.FireBaseInitializer;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class FireBaseAuthService {


    public String createNewAuthUser(String number,String email) throws FirebaseAuthException {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPhoneNumber(number)
                .setDisplayName("John Doe")
                .setDisabled(false);
        UserRecord userRecord = auth.createUser(request);


//        Message message = Message.builder().setToken(userRecord.getTenantId()).build();
//        messaging.send(message);
        log.info("Successfully created new user: " + userRecord.getUid());
        return userRecord.getUid();
    }
    public boolean verificationToken(String token){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        try {
            return auth.verifyIdToken(token) != null;
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }return false;
    }

    public String checkToken (String token) throws FirebaseAuthException{
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseToken response=  auth.verifyIdToken(token);
       return response.getUid();

    }

    public String createToken(String uid) throws FirebaseAuthException {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        UserRecord record=  auth.getUser(uid);
        log.info("Create token for uid - "+ record.toString());
        return auth.createCustomToken(uid);
    }

    public String sendPushMessage(String token){
        FirebaseMessaging messaging = FirebaseMessaging.getInstance();
      Notification n = Notification.builder().setTitle("Test title").setBody("Test message body").build();
      Message m = Message.builder().setToken(token).setNotification(n).build();
        try {
            messaging.send(m);

        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return "Message send to uid";
    }

    public List<String> getAll() throws FirebaseAuthException {
        ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
        List<String> result = new ArrayList<>();
        while (page != null) {
            for (ExportedUserRecord user : page.getValues()) {
                System.out.println("User: " + user.getUid());
                result.add(user.getUid());
            }
            page = page.getNextPage();
        }

// Iterate through all users. This will still retrieve users in batches,
// buffering no more than 1000 users in memory at a time.
        page = FirebaseAuth.getInstance().listUsers(null);
        for (ExportedUserRecord user : page.iterateAll()) {
            System.out.println("User: " + user.getUid());
            result.add(user.getUid());
        }
        return result;
    }
}
