package ru.education.testfirebase.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.education.testfirebase.entity.Person;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class FireBaseService {
    private static final String NAME_COLLECTION = "users";
    private final Firestore dbFirestore = FirestoreClient.getFirestore();

    public String saveUser(Person person) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionApi = dbFirestore.collection(NAME_COLLECTION).document(person.getName()).set(person);
        return collectionApi.get().getUpdateTime().toString();
    }

    public Person getPersonDetails(String name) throws ExecutionException, InterruptedException {
        DocumentReference dr = dbFirestore.collection(NAME_COLLECTION).document(name);
        ApiFuture<DocumentSnapshot> future = dr.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
           return document.toObject(Person.class);
        } else {
            return null;
        }
    }
    public String updatePersonDetails (Person person) throws ExecutionException, InterruptedException {
        DocumentReference dr = dbFirestore.collection(NAME_COLLECTION).document(person.getName());
        ApiFuture<DocumentSnapshot> future = dr.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            dr.set(person);
            return "Information for - "+person.getName()+" updated";
        } else {
            return "No such user";
        }
    }

    public String deleteUser(String name) throws ExecutionException, InterruptedException {
        DocumentReference dr = dbFirestore.collection(NAME_COLLECTION).document(name);
        ApiFuture<DocumentSnapshot> future = dr.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            dr.delete();
            return "Information for - "+name+" deleted";
        } else {
            return "No such user";
        }
    }
}
