package app.taplock.sapo.sign_in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import app.taplock.sapo.MainActivity;
import app.taplock.sapo.R;
import app.taplock.sapo.RetrofitAPI;
import app.taplock.sapo.emailverification.EmailVerification;
import app.taplock.sapo.menu.Menu;
import app.taplock.sapo.signup.SignUp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.GONE;

public class SignIn extends AppCompatActivity implements View.OnTouchListener {
    EditText old_pwd;
    CheckBox remember_me;

    EditText email, password;

    TextView error_text;

    RelativeLayout loading_layout;
    LinearLayout layout_background;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        remember_me = findViewById(R.id.remember_me);
        error_text =  (TextView) findViewById(R.id.error_text_sin);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        loading_layout = findViewById(R.id.loading_layout);
        layout_background = findViewById(R.id.layout_background);
        button = findViewById(R.id.login_btn);

        String PREFS_NAME = "app.taplock.auth";
        String PREF_EMAIL = "email";
        String PREF_PASSWORD = "password";
        String PREF_REMEMBER = "remember";

        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String _email = pref.getString(PREF_EMAIL, null);
        String _password = pref.getString(PREF_PASSWORD, null);
        boolean _remember = pref.getBoolean(PREF_REMEMBER, false);

        email.setText(_email);

        if(_remember)
        {
            remember_me.setChecked(true);
            password.setText(_password);

        }else
        {
            remember_me.setChecked(false);
            password.setText("");

        }

        // Hay internet???
        if(true)
        {
            getBanner();
        }
        else
        {
            loadBanner();
        }

        old_pwd  = (EditText)findViewById(R.id.password);
        old_pwd.setOnTouchListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBanner();
                 clearError();
                // validating the data
                if(fieldValidation())
                {
                    postData();
                }
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;
        if (event.getAction() == MotionEvent.ACTION_UP) {

            if (event.getRawX() >= (old_pwd.getRight() - old_pwd.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                if (old_pwd.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {

                    old_pwd.setInputType(InputType.TYPE_CLASS_TEXT |                           InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    old_pwd.setCompoundDrawablesWithIntrinsicBounds(getApplicationContext().getResources().getDrawable(R.drawable.ic_lock), null, getApplicationContext().getResources().getDrawable(R.drawable.ic_pwd_visibility_off), null);
                    old_pwd.setSelection(old_pwd.getText().length());
                } else {

                    old_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    old_pwd.setCompoundDrawablesWithIntrinsicBounds(getApplicationContext().getResources().getDrawable(R.drawable.ic_lock), null, getApplicationContext().getResources().getDrawable(R.drawable.ic_pwd_visibility), null);
                    old_pwd.setSelection(old_pwd.getText().length());
                }
                return true;
            }
        }
        return false;
    }

    public boolean remember_me()
    {
        if(remember_me.isChecked())
        {
            String PREFS_NAME = "app.taplock.auth";
            String PREF_EMAIL = "email";
            String PREF_PASSWORD = "password";
            String PREF_REMEMBER = "remember";

            getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                    .edit()
                    .putString(PREF_EMAIL, email.getText().toString())
                    .putString(PREF_PASSWORD, password.getText().toString())
                    .putBoolean(PREF_REMEMBER, true)
                    .apply();
        }
        else
        {
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
        }
        return true;
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
        //AddItapModel modal = new AddItapModel("naimgomezcn@gmail.com", "19401940n", "name", "address", "PC0Z58C");

        SignInModel modal = new SignInModel(email.getText().toString(), password.getText().toString());

        // calling a method to create a post and passing our modal class.
        Call<SignInModel> call = retrofitAPI.createPost(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<SignInModel>() {
            @Override
            public void onResponse(Call<SignInModel> call, Response<SignInModel> response) {
                SignInModel responseFromAPI = response.body();

                focusBackgroundAfterSendData();

                if(response.code() == 200)
                {

                    if(responseFromAPI.getSYSTEM_CODE().equals("SUCCESS"))
                    {
                        if(remember_me()) {

                            Toast.makeText(SignIn.this,  responseFromAPI.getMessage(), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignIn.this, Menu.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    if (responseFromAPI.getSYSTEM_CODE().equals("EMAIL_VERIFICATION"))
                    {
                        if(remember_me()) {

                            Toast.makeText(SignIn.this,  responseFromAPI.getMessage(), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignIn.this, EmailVerification.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else {

                        Toast.makeText(SignIn.this,  responseFromAPI.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(SignIn.this,  responseFromAPI.getSYSTEM_CODE(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    try {

                        JSONObject obj = new JSONObject(response.errorBody().string());

                        //showError(obj.getString("message"));

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SignIn.this, "El servidor no se encuentra disponible, inténtelo más tarde.", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<SignInModel> call, Throwable t) {

                Toast.makeText(SignIn.this, "Ha ocurrido un error, inténtelo más tarde.", Toast.LENGTH_SHORT).show();
                //showError(t.getMessage());
                focusBackgroundAfterSendData();
            }
        });
    }

    private void showError(String s)
    {

       //error_text.setText(s);
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
        //loading_layout.setVisibility(View.VISIBLE);
        layout_background.setAlpha((float) 0.78);
        button.setClickable(false);

        email.clearFocus();
        email.setFocusableInTouchMode(false);
        password.clearFocus();
        password.setFocusableInTouchMode(false);
    }

    private void focusBackgroundAfterSendData()
    {
        loading_layout.setVisibility(GONE);
        layout_background.setAlpha((float) 1);
        button.setClickable(true);
        email.setFocusableInTouchMode(true);
        password.setFocusableInTouchMode(true);
    }

    private boolean fieldValidation()
    {

        if(email.getText().toString().isEmpty())
        {
            showError("Debe introducir su email.");
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

        return true;
    }

    public void open_sign_up(View view)
    {
        Intent intent = new Intent(SignIn.this, SignUp.class);
        startActivity(intent);
    }


    private void getBanner() {

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
        //AddItapModel modal = new AddItapModel("naimgomezcn@gmail.com", "19401940n", "name", "address", "PC0Z58C");

        BannerModel modal = new BannerModel();

        // calling a method to create a post and passing our modal class.
        Call<BannerModel> call = retrofitAPI.createPost(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<BannerModel>() {
            @Override
            public void onResponse(Call<BannerModel> call, Response<BannerModel> response) {
                BannerModel responseFromAPI = response.body();

                focusBackgroundAfterSendData();

                if(response.code() == 200)
                {
                    String PREFS_NAME = "app.taplock.banner";
                    String PREF_MESSAGE = "message";
                    String PREF_TYPE = "type";
                    String PREF_URL = "url";

                    getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                            .edit()
                            .putString(PREF_MESSAGE, responseFromAPI.getMessage())
                            .putInt(PREF_TYPE, responseFromAPI.getType())
                            .putString(PREF_URL, responseFromAPI.getUrl())
                            .apply();

                    loadBanner();

                }
                else
                {
                    try {

                        JSONObject obj = new JSONObject(response.errorBody().string());
                        showError(obj.getString("message"));
                        loadErrorToBanner(obj.getString("message"));

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SignIn.this, "El servidor no se encuentra disponible, inténtelo más tarde.", Toast.LENGTH_SHORT).show();
                        loadErrorToBanner("El servidor no se encuentra disponible, cierre la aplicación e inténtelo más tarde. Disculpe las molestias.");
                    }
                }

            }

            @Override
            public void onFailure(Call<BannerModel> call, Throwable t) {

                Toast.makeText(SignIn.this, "Ha ocurrido un error.", Toast.LENGTH_SHORT).show();
                showError(t.getMessage());
            }


        });
    }

    private void loadBanner()
    {

        int NONE = 0;
        int WARNING = 1;
        int ERROR = 2;
        int UPDATE = 3;

        TextView text_banner = findViewById(R.id.text_banner);
        RelativeLayout banner = findViewById(R.id.banner);
        String PREFS_NAME = "app.taplock.banner";
        String PREF_MESSAGE = "message";
        String PREF_TYPE = "type";
        String PREF_URL = "url";

        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String message = pref.getString(PREF_MESSAGE, null);
        int type = pref.getInt(PREF_TYPE, 0);
        String url = pref.getString(PREF_URL, null);

        if(type == NONE)
        {
            banner.setVisibility(GONE);
            text_banner.setText("");
        }
        if(type == WARNING)
        {
            banner.setVisibility(View.VISIBLE);
            banner.setBackgroundColor(getResources().getColor(R.color.warningColor));
            text_banner.setText(message);
        }
        if(type == ERROR)
        {
            banner.setVisibility(View.VISIBLE);
            banner.setBackgroundColor(getResources().getColor(R.color.errorColor));
            text_banner.setText(message);
        }
        if(type == UPDATE)
        {
            banner.setVisibility(View.VISIBLE);
            banner.setBackgroundColor(getResources().getColor(R.color.updateColor));
            text_banner.setText(message);
        }
    }

    private void loadErrorToBanner(String message)
    {

        int NONE = 0;
        int WARNING = 1;
        int ERROR = 2;
        int UPDATE = 3;

        TextView text_banner = findViewById(R.id.text_banner);
        RelativeLayout banner = findViewById(R.id.banner);


        banner.setVisibility(View.VISIBLE);
        banner.setBackgroundColor(getResources().getColor(R.color.errorColor));
        text_banner.setText(message);

    }
}