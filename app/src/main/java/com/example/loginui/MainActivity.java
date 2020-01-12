package com.example.loginui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    MaterialEditText email,password;
    Button login;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        email = findViewById(R.id.namee);
        password = findViewById(R.id.pass);
        login = findViewById(R.id.button);
        progressBar = findViewById(R.id.progress);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getEditData();
            }
        });


    }

    public void getEditData(){
        String name = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if(email.getText().toString().trim().isEmpty()){
            email.setError("Email Address required !!!");
            email.requestFocus();
            return;
        }

        if(password.getText().toString().trim().isEmpty()){
            email.setError("Password required !!!");
            email.requestFocus();
            return;
        }

        login(name,pass);
    }

    public void login(String name,String password){
        try {
            progressBar.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }

        RetrofitApiInterface ap = RetrofitClient.getRetrofit().create(RetrofitApiInterface.class);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("identity",name)
                .addFormDataPart("password",password)
                .build();

        ap.login(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            int status = jsonObject.optInt("status");
                            if (status == 1){

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i =0; i<jsonArray.length();i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String name = jsonObject1.optString("username");
                                    String id = jsonObject1.optString("id");
                                    System.out.println("Data : "+name+" "+id);
                                }

                                try {
                                    progressBar.setVisibility(View.GONE);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                startActivity(new Intent(getApplicationContext(),QR_Scan.class));
                            }else{
                                try {
                                    progressBar.setVisibility(View.GONE);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                Toast.makeText(MainActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                try {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Message: "+t.getMessage(),Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
