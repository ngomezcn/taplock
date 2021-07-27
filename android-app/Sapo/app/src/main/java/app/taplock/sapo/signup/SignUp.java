package app.taplock.sapo.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import app.taplock.sapo.DataModal;
import app.taplock.sapo.MainActivity;
import app.taplock.sapo.R;
import app.taplock.sapo.RetrofitAPI;
import app.taplock.sapo.emailverification.EmailVerification;
import app.taplock.sapo.sign_in.SignIn;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity implements View.OnTouchListener {

    EditText name;
    EditText email;
    EditText phone;
    EditText password;
    EditText confirmPassword;
    CheckBox terms_of_service;
    TextView error_text;
    Button button;
    LinearLayout layout_sign_up;

    RelativeLayout loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.full_name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone_number);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        error_text = findViewById(R.id.error_text);
        button = findViewById(R.id.SignUpBtn);
        terms_of_service = findViewById(R.id.terms_of_service);

        loadingPB = findViewById(R.id.loading_sign_up);
        layout_sign_up = findViewById(R.id.layout_sign_up);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating the data
                if(fieldValidation())
                {
                    clearError();
                    postData();
                }
            }
        });

        password.setOnTouchListener(SignUp.this);
        confirmPassword.setOnTouchListener(SignUp.this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;
        if (event.getAction() == MotionEvent.ACTION_UP) {

            if (event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                if (password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {

                    password.setInputType(InputType.TYPE_CLASS_TEXT |                           InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setCompoundDrawablesWithIntrinsicBounds(getApplicationContext().getResources().getDrawable(R.drawable.ic_lock), null, getApplicationContext().getResources().getDrawable(R.drawable.ic_pwd_visibility_off), null);
                    password.setSelection(password.getText().length());
                } else {

                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password.setCompoundDrawablesWithIntrinsicBounds(getApplicationContext().getResources().getDrawable(R.drawable.ic_lock), null, getApplicationContext().getResources().getDrawable(R.drawable.ic_pwd_visibility), null);
                    password.setSelection(password.getText().length());
                }
                return true;
            }
        }
        return false;
    }

    private void postData() {

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
        SignUpModel modal = new SignUpModel(email.getText().toString(), password.getText().toString(), name.getText().toString(), phone.getText().toString(), terms_of_service.isChecked());

        // calling a method to create a post and passing our modal class.
        Call<SignUpModel> call = retrofitAPI.createPost(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                SignUpModel responseFromAPI = response.body();

                focusBackgroundAfterSendData();

                if(response.code() == 200)
                {
                    if(responseFromAPI.getSYSTEM_CODE().equals("SUCCESS"))
                    {
                        Toast.makeText(SignUp.this, responseFromAPI.getMessage(), Toast.LENGTH_SHORT).show();

                        String PREFS_NAME = "app.taplock.auth";
                        String PREF_EMAIL = "email";
                        String PREF_PASSWORD = "password";
                        String PREF_REMEMBER = "remember";

                        getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                                .edit()
                                .putString(PREF_EMAIL, email.getText().toString())
                                .putString(PREF_PASSWORD, password.getText().toString())
                                .putBoolean(PREF_REMEMBER, false)
                                .apply();

                        Intent intent = new Intent(SignUp.this, EmailVerification.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(SignUp.this, responseFromAPI.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    try {

                        JSONObject obj = new JSONObject(response.errorBody().string());

                        showError(obj.getString("message"));



                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SignUp.this, "El servidor no se encuentra disponible, inténtelo más tarde.", Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t) {

                Toast.makeText(SignUp.this, "Ha ocurrido un error.", Toast.LENGTH_SHORT).show();
                showError(t.getMessage());
            }
        });
    }


    private boolean fieldValidation()
    {
        //TODO: confirmPassword.setBackgroundResource(R.drawable.edit_text_error);

        if(name.getText().toString().isEmpty())
        {
            showError("Debe introducir su nombre completo.");
            return false;
        } else {clearError();}


        if(phone.getText().toString().isEmpty())
        {
            showError("Debe introducir su número de móvil.");
            return false;
        } else {clearError();}

        if(!isValidMobile(phone.getText().toString()))
        {
            showError("El número de móvil introducido no es válido.");
            return false;
        } else {clearError();}

        if(email.getText().toString().isEmpty())
        {
            showError("Debe introducir su mejor email.");
            return false;
        } else {clearError();}

        if(!isEmailValid(email.getText().toString()))
        {
            showError("El email introducido no es válido.");
            return false;
        } else {clearError();}

        if(password.getText().toString().isEmpty())
        {
            showError("Debe introducir su contraseña");
            return false;
        } else {clearError();}

        if(confirmPassword.getText().toString().isEmpty())
        {
            showError("Debe verificar la contraseña");
            return false;
        } else {clearError();}

        if(!password.getText().toString().equals(confirmPassword.getText().toString()))
        {
            showError("Las contraseñas no coinciden.");
            return false;
        } else {clearError(); }

        if(password.getText().toString().length() < 6)
        {
            showError("La contraseña es demasiado corta");
            return false;
        } else {clearError(); }

        if(!terms_of_service.isChecked())
        {
            showError("Debe aceptar los términos del servicio para poder continuar.");
            return false;

        } else { clearError(); }

        return true;
    }

    private void showError(String s)
    {
        error_text.setText(s);
    }

    private void clearError()
    {
        error_text.setText("");
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }

    private void blurBackgroundOnSendData()
    {
        loadingPB.setVisibility(View.VISIBLE);
        layout_sign_up.setAlpha((float) 0.78);
        button.setClickable(false);

        name.clearFocus();
        email.clearFocus();
        phone.clearFocus();
        password.clearFocus();
        confirmPassword.clearFocus();

        name.setFocusableInTouchMode(false);
        email.setFocusableInTouchMode(false);
        phone.setFocusableInTouchMode(false);
        password.setFocusableInTouchMode(false);
        confirmPassword.setFocusableInTouchMode(false);
    }

    private void focusBackgroundAfterSendData()
    {
        loadingPB.setVisibility(View.GONE);
        layout_sign_up.setAlpha((float) 1);
        button.setClickable(true);
        name.setFocusableInTouchMode(true);
        email.setFocusableInTouchMode(true);
        phone.setFocusableInTouchMode(true);
        password.setFocusableInTouchMode(true);
        confirmPassword.setFocusableInTouchMode(true);
    }

    public void open_sign_in(View view)
    {

        Intent intent = new Intent(SignUp.this, SignIn.class);
        startActivity(intent);
    }
}