package com.youngtechie.todoapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.youngtechie.todoapp.modelResponse.BasicResponse;
import com.youngtechie.todoapp.networkClient.RetrofitService;
import com.youngtechie.todoapp.storage.SharedPreferenceClass;
import com.youngtechie.todoapp.utilsService.UtilsService;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameCtr, emailCtr, passwordCtr;
    ProgressBar progressBar;
    Button registerButton;
    private String name, password, email;
    UtilsService utilsService;
    SharedPreferenceClass sharedPreferenceClass;


//    @Override
//    protected void onStart(){
//        super.onStart();
//        SharedPreferences todo_pref = getSharedPreferences("user_todo" ,MODE_PRIVATE);
//        if(todo_pref.contains("token")){
//            startActivity(new Intent( RegisterActivity.this , MainActivity.class));
//            finish();
//        };
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameCtr = findViewById(R.id.name_controller);
        emailCtr = findViewById(R.id.email_controller);
        passwordCtr = findViewById(R.id.password_controller);
        registerButton = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.login_progress_bar);
        Button loginButton = findViewById(R.id.go_login);
        utilsService = new UtilsService();
        sharedPreferenceClass = new SharedPreferenceClass(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilsService.hideKeyboard(v, RegisterActivity.this);
                name = nameCtr.getText().toString();
                email = emailCtr.getText().toString();
                password = passwordCtr.getText().toString();
                if (validate(v)) {
                    registerUser(v);
                }
            }
        });
    }

    private void registerUser(View v) {
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> body = new HashMap<>();
        body.put("username", name);
        body.put("email", email);
        body.put("password", password);
        Call<BasicResponse> call = RetrofitService.getInstance(this).getApi().userRegister(body);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(@NonNull Call<BasicResponse> call, @NonNull Response<BasicResponse> response) {
                BasicResponse resp = response.body();
                progressBar.setVisibility(View.INVISIBLE);
//                getIntent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                if(response.isSuccessful()){
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    assert resp != null;
                    sharedPreferenceClass.setValue_string("token" , resp.getData().getToken());
                    Toast.makeText(RegisterActivity.this, resp.getMsg(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<BasicResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public boolean validate(View v) {
        boolean isValid = false;
        if (!TextUtils.isEmpty(name)) {
            if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (!TextUtils.isEmpty(password)) {
                    isValid = true;
                } else {
                    utilsService.showSnackBar(v, "Please enter password");
                }
            } else {
                utilsService.showSnackBar(v, "Please enter email");
            }
        } else {
            utilsService.showSnackBar(v, "Please enter name");
        }
        return isValid;
    }
}