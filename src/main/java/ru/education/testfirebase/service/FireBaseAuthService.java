package ru.education.testfirebase.service;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.internal.FirebaseCustomAuthToken;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.remoteconfig.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class FireBaseAuthService {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseMessaging messaging = FirebaseMessaging.getInstance();

    public String createNewAuthUser(String number,String email) throws FirebaseAuthException, FirebaseMessagingException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPhoneNumber(number)
                .setDisplayName("John Doe")
                .setDisabled(false);
        UserRecord userRecord = auth.createUser(request);

//        Message message = Message.builder().setToken(userRecord.getTenantId()).build();
//        messaging.send(message);
        return "Successfully created new user: " + userRecord.getUid();
    }
    public boolean verificationToken(String token){
        try {
            return auth.verifyIdToken(token) != null;
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }return false;
    }

    public String checkToken (String token) throws FirebaseAuthException, ExecutionException, InterruptedException {
        FirebaseToken response=  auth.verifyIdToken(token);
       return response.getUid();

    }

    public String createToken(String uid) throws FirebaseAuthException {
        UserRecord record=  auth.getUser(uid);
        System.out.println(record.toString());
        record.getPhoneNumber();
        JsonWebSignature.Header header = new JsonWebSignature.Header();

        FirebaseCustomAuthToken.Payload payload = new FirebaseCustomAuthToken.Payload();
        payload.setUid(uid);
        byte[] signatureBytes= {1,1,1,1,1,1};
        byte[] signedContentBytes = {2,2,2,2,2,2};
        FirebaseCustomAuthToken token = new FirebaseCustomAuthToken(header,payload, signatureBytes,signedContentBytes);

        return token.toString();
    }
}
