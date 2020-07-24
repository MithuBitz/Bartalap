package com.example.bartalap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button mButton = findViewById(R.id.logout);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //signout the user also from Firebase so that the previus user data will clear
                FirebaseAuth.getInstance().signOut();
                //Create an intent to go to the Login page again
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //Clear all running activity and make the intent activity to root level
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //Start the intent activity
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}