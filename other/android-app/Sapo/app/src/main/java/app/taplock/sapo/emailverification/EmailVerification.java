package app.taplock.sapo.emailverification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import app.taplock.sapo.R;
import app.taplock.sapo.RetrofitAPI;
import app.taplock.sapo.menu.Menu;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmailVerification extends AppCompatActivity {

    Button btn_verificate_email;
    TextView text_01, text_02;
    TextView error_text;

    String password;
    String email;

    Button button;

    LinearLayout background;
    RelativeLayout loadingBT;

    private String PREFS_NAME = "app.taplock.auth";
    private String getEmail()
    {
        String PREF_EMAIL = "email";
        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        email = pref.getString(PREF_EMAIL, null);

        return email;
    }

    private String getPassword()
    {
        String PREF_PASSWORD = "password";
        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        password = pref.getString(PREF_PASSWORD, null);

        return password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        password = getPassword();
        email = getEmail();

        Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), password, Toast.LENGTH_SHORT).show();

        btn_verificate_email = findViewById(R.id.btn_verificate_email);
        text_01 = findViewById(R.id.text_01);
        String text = "Le hemos enviado un enlace de activación al email, " + email.toString();
        text_01.setText(text);

        text_02 = findViewById(R.id.text_02);
        text_02.setText(Html.fromHtml("Despues de activar su cuenta vuelva aquí y pulse en <b>" + "\"Verificar\"" + "</b>"));


        error_text = findViewById(R.id.error_text);
        loadingBT = findViewById(R.id.loadingBT);
        background = findViewById(R.id.layout_background);

        button = findViewById(R.id.btn_verificate_email);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating the data
                postData();
            }
        });

        findViewById(R.id.resend_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating the data
                resendEmail();
            }
        });
    }


    private void resendEmail() {

        clearError();
        blurBackgroundOnSendData();

        // on below line we are creating a retrofit
        // builder and passing our base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://taplock.app/app/")
                // as we are sending data in json format so
                // we have to add Gson converter factory
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();

        // below line is to create an instance for our retrofit api class.
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        // passing data from our text fields to our modal class.
        ResendVerificationModel modal = new ResendVerificationModel(email);

        // calling a method to create a post and passing our modal class.
        Call<ResendVerificationModel> call = retrofitAPI.createPost(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<ResendVerificationModel>() {
            @Override
            public void onResponse(Call<ResendVerificationModel> call, Response<ResendVerificationModel> response) {

                focusBackgroundAfterSendData();

                if(response.code() == 200)
                {
                    ResendVerificationModel responseFromAPI = response.body();

                    Toast.makeText(EmailVerification.this, responseFromAPI.message, Toast.LENGTH_LONG).show();

                }
                else
                {
                    try {

                        JSONObject obj = new JSONObject(response.errorBody().string());

                        if(obj.getString("code").equals("signIn-2002"))
                        {
                            showError("Ha ocurrido un error durante la autentificación, inténtelo más tarde.");
                        }
                        else
                        {
                            showError(obj.getString("message"));
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResendVerificationModel> call, Throwable t) {

                Toast.makeText(EmailVerification.this, "Ha ocurrido un error general, inténtelo más tarde.", Toast.LENGTH_LONG).show();
                showError(t.getMessage());
            }
        });
    }

    private void postData() {

        clearError();
        blurBackgroundOnSendData();

        // on below line we are creating a retrofit
        // builder and passing our base url
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://taplock.app/app/")
                // as we are sending data in json format so
                // we have to add Gson converter factory
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();

        // below line is to create an instance for our retrofit api class.
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        // passing data from our text fields to our modal class.
        EmailVerificationModel modal = new EmailVerificationModel(email, password);

        // calling a method to create a post and passing our modal class.
        Call<EmailVerificationModel> call = retrofitAPI.createPost(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<EmailVerificationModel>() {
            @Override
            public void onResponse(Call<EmailVerificationModel> call, Response<EmailVerificationModel> response) {
                EmailVerificationModel responseFromAPI = response.body();

                focusBackgroundAfterSendData();

                if(response.code() == 200)
                {
                    if(responseFromAPI.getSYSTEM_CODE().equals("SUCCESS")) {
                        // Toast.makeText(EmailVerification.this, responseFromAPI.getMessage(), Toast.LENGTH_SHORT).show();

                        if (responseFromAPI.getIs_active() == true) {
                            Toast.makeText(EmailVerification.this, "Su cuenta ha sido activada!\n token: " + responseFromAPI.getToken(), Toast.LENGTH_LONG).show();
                        }

                        Intent intent = new Intent(EmailVerification.this, Menu.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (responseFromAPI.getSYSTEM_CODE().equals("EMAIL_VERIFICATION"))
                    {
                        showError(responseFromAPI.getMessage());
                    }

                }
                else
                {
                    try {

                        JSONObject obj = new JSONObject(response.errorBody().string());

                        if(obj.getString("code").equals("signIn-2002"))
                        {
                            showError("Ha ocurrido un error durante la autentificación, inténtelo más tarde.");
                        }
                        else
                        {
                            showError(obj.getString("message"));
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<EmailVerificationModel> call, Throwable t) {

                Toast.makeText(EmailVerification.this, "Ha ocurrido un error general, inténtelo más tarde.", Toast.LENGTH_LONG).show();
                showError(t.getMessage());
            }
        });
    }

    private void showError(String s)
    {
        error_text.setText(s);
    }

    private void clearError()
    {
        error_text.setText("");
    }

    private void blurBackgroundOnSendData()
    {
        loadingBT.setVisibility(View.VISIBLE);
        background.setAlpha((float) 0.78);
    }

    private void focusBackgroundAfterSendData()
    {
        loadingBT.setVisibility(View.GONE);
        background.setAlpha((float) 1);
    }
}