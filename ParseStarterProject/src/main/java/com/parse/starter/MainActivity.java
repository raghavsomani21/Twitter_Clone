/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {

  public void signupLogin(View view) {
    final EditText usernameEditText = findViewById(R.id.usernameEditText);
    final EditText passwordEditText = findViewById(R.id.passwordEditText);

    ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        if (e == null) {
          Log.i("Login", "Success!");
          redirectUser();
        } else {
          AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
          builder.setTitle("This account doesn't exist!! Do you want to create this account?");
          builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              ParseUser newUser = new ParseUser();
              newUser.setUsername(usernameEditText.getText().toString());
              newUser.setPassword(passwordEditText.getText().toString());
              newUser.signUpInBackground(new SignUpCallback() {
              @Override
              public void done(ParseException e) {
                if (e == null) {
                  Log.i("Signup", "Success!");
                } else {
                  Toast.makeText(MainActivity.this, e.getMessage().substring(e.getMessage().indexOf(" ")),Toast.LENGTH_SHORT).show();
                }
              }
            });
            }
          });

          builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              Toast.makeText(MainActivity.this,"Try logging in with an existing account!!",Toast.LENGTH_SHORT);
              dialogInterface.cancel();
            }
          });
          builder.show();

        }
        redirectUser();
      }
    });
  }

  public void redirectUser() {
    if (ParseUser.getCurrentUser() != null) {
      Intent intent = new Intent(this, UsersActivity.class);
      startActivity(intent);
    }
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    redirectUser();
    setTitle("Twitter: Login");
    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}