package app.taplock.sapo.add_itap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import app.taplock.sapo.R;
import app.taplock.sapo.RetrofitAPI;
import app.taplock.sapo.menu.Menu;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddItap extends AppCompatActivity {

    TextView cancel_add_itap;

    String email;
    String password;

    EditText name, address, activation_key;
    TextView error_text;

    Button button;

    LinearLayout layout_background;
    RelativeLayout loading_layout;

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
        setContentView(R.layout.activity_add_itap);

        cancel_add_itap = (TextView) findViewById(R.id.cancel_add_itap);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        activation_key = findViewById(R.id.activation_key);
        error_text = (TextView) findViewById(R.id.error_text);
        button =  (Button) findViewById(R.id.go_to_step_two);

        layout_background =findViewById(R.id.layout_background);
        loading_layout =findViewById(R.id.loading_layout);

        password = getPassword();
        email = getEmail();

        cancel_add_itap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddItap.this, Menu.class);
                startActivity(intent);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(fieldValidation())
                {
                    postData();
                }
            }
        });
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
        //AddItapModel modal = new AddItapModel("naimgomezcn@gmail.com", "19401940n", "name", "address", "PC0Z58C");

        AddItapModel modal = new AddItapModel(email, password, name.getText().toString(), address.getText().toString(), activation_key.getText().toString());

        // calling a method to create a post and passing our modal class.
        Call<AddItapModel> call = retrofitAPI.createPost(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<AddItapModel>() {
            @Override
            public void onResponse(Call<AddItapModel> call, Response<AddItapModel> response) {

                focusBackgroundAfterSendData();

                if(response.code() == 200)
                {
                    AddItapModel responseFromAPI = response.body();
                    Toast.makeText(AddItap.this, "RESPONSE: "+ responseFromAPI.getMessage(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddItap.this, Menu.class);
                    //intent.putExtra("email", email.getText().toString());
                    //intent.putExtra("password", password.getText().toString());
                    startActivity(intent);
                }
                else
                {
                    try {

                        JSONObject obj = new JSONObject(response.errorBody().string());

                        showError(obj.getString("message"));

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<AddItapModel> call, Throwable t) {

                Toast.makeText(AddItap.this, "Ha ocurrido un error.", Toast.LENGTH_SHORT).show();
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
        loading_layout.setVisibility(View.VISIBLE);
        layout_background.setAlpha((float) 0.78);
        button.setClickable(false);

        name.clearFocus();
        name.setFocusableInTouchMode(false);
        activation_key.clearFocus();
        activation_key.setFocusableInTouchMode(false);
        address.clearFocus();
        address.setFocusableInTouchMode(false);
    }

    private void focusBackgroundAfterSendData()
    {
        loading_layout.setVisibility(View.GONE);
        layout_background.setAlpha((float) 1);
        button.setClickable(true);
        name.setFocusableInTouchMode(true);
        activation_key.setFocusableInTouchMode(true);
        address.setFocusableInTouchMode(true);
    }

    private boolean fieldValidation()
    {
        if(activation_key.getText().toString().isEmpty())
        {
            showError("Debe introducir el PIN.");
            return false;
        } else {clearError();}

        if(name.getText().toString().isEmpty())
        {
            showError("Debe introducir el nombre.");
            return false;
        } else {clearError();}


        if(address.getText().toString().isEmpty())
        {
            showError("Debe introducir la dirección.");
            return false;
        } else {clearError();}

        return true;
    }
}