package com.youngtechie.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.youngtechie.todoapp.modelResponse.BasicResponse;
import com.youngtechie.todoapp.networkClient.RetrofitService;
import com.youngtechie.todoapp.storage.SharedPrefConstantKey;
import com.youngtechie.todoapp.storage.SharedPreferenceClass;
import com.youngtechie.todoapp.utilsService.UtilsService;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailCtr, passwordCtr;
    ProgressBar progressBar;
    private String email, password;
    UtilsService utilsService;
    SharedPreferenceClass sharedPreferenceClass;


//    @Override
//    protected void onStart(){
//        super.onStart();
//        SharedPreferences todo_pref = getSharedPreferences("user_todo" ,MODE_PRIVATE);
//        if(todo_pref.contains("token")){
//            startActivity(new Intent(LoginActivity.this , MainActivity.class));
//            finish();
//        };
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailCtr = findViewById(R.id.login_email_controller);
        passwordCtr = findViewById(R.id.login_password_controller);
        progressBar = findViewById(R.id.login_progress_bar);
        sharedPreferenceClass = new SharedPreferenceClass(this);
        Button createAccountButton = findViewById(R.id.go_create_account_button);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        Button  loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                utilsService.hideKeyboard(v, LoginActivity.this);
                Log.d("we" , "Click ");
                email = emailCtr.getText().toString();
                password = passwordCtr.getText().toString();

                if (validate(v)) {
                    loginUser(v);
                }
            }
        });
    }

    private void loginUser(View v) {
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> body = new HashMap<>();

        body.put("email" ,email);
        body.put("password" , password);
        Call<BasicResponse> call = RetrofitService.getInstance(this).getApi().userLogin(body);

        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(@NonNull Call<BasicResponse> call, @NonNull Response<BasicResponse> response) {
                progressBar.setVisibility(View.GONE);
                BasicResponse resp = response.body();
                if(response.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    assert resp != null;
                    sharedPreferenceClass.setValue_string("token" , resp.getData().getToken());
                    Toast.makeText(LoginActivity.this, resp.getMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BasicResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public boolean validate(View v) {
        boolean isValid = false;
        if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (!TextUtils.isEmpty(password)) {
                isValid = true;
            } else {
                utilsService.showSnackBar(v, "Please enter password");
            }
        } else {
            utilsService.showSnackBar(v, "Please enter email");
        }

        return isValid;
    }
}