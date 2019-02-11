package com.example.dflet.scripttanklogindemo;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//db write service. this operates in the background for any long writes
//currently no error handling and it will only write the created user profile to
//db.
public class DatabaseWriteService extends IntentService {


    public DatabaseWriteService() {
        super("DatabaseWriteService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                Bundle extra = intent.getExtras();
                FirebaseDatabase fb = FirebaseDatabase.getInstance("https://scripttankdemo.firebaseio.com/");
                DatabaseReference myRef = fb.getReference("/Users/");
                User newUser = new User(extra.getString("email"),
                        extra.getString("number"),
                        extra.getString("name"),
                        extra.getString("type")); //create user object
                DatabaseReference pushRef = myRef.push(); //push new object to db
                pushRef.setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            System.out.println("The Write was processed");
                        } else {
                            System.err.println(task.getException().getMessage());
                        }
                    }
                });
                newUser.setKey(pushRef.getKey()); //grab unique identifier created from push operation
                //this value in the future will be cached in a file on the device, along with the rest of the
                //user object as well
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}