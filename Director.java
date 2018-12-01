package com.tinhat.ClubView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tinhat.ClubView.PersonalGroupList;
import com.tinhat.ClubView.StartUpPage;

public class Director extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences personalInfo = getSharedPreferences("PersonalInformation", MODE_PRIVATE);

        if (personalInfo.getString("personalEmail", "").equals("")) { // The user doesn't have any saved data
            //setContentView(R.layout.activity_start_up_page);
            Intent intent = new Intent(this, StartUpPage.class);
            startActivity(intent);
        }
        else { // The user has data of a previous account
            //setContentView(R.layout.activity_personal_group_list);
            Intent intent = new Intent(this, PersonalGroupList.class);
            startActivity(intent);
        }
    }

}
