package com.newbie.handycraftsshop.Notifications;

/**
 * Created by wahyu_septiadi on 26, April 2020.
 * Visit My GitHub --> https://github.com/WahyuSeptiadi
 */

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        String refreshToken = FirebaseInstanceId.getInstance().getToken();

        if (firebaseUser != null){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
            Token tokens = new Token(token);
            reference.child(firebaseUser.getUid()).setValue(tokens);
        }
    }
}
