package app.taplock.sapo.add_user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import app.taplock.sapo.R;
import app.taplock.sapo.RetrofitAPI;
import app.taplock.sapo.menu.Menu;
import app.taplock.sapo.sign_in.SignIn;
import app.taplock.sapo.users_list.UsersList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddUser extends AppCompatActivity {
    private String PREFS_NAME = "app.taplock.auth";

    // User Profile
    String email, password, itap_identifier, message;

    // Itap
    String token, rawToken;

    // New User
    String user_name, user_phone;
    TextView view_user_name, view_user_phone;

    String rawID;

    // Common
    Dialog myDialog;
    TextView go_bacK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        email = getEmail(getApplicationContext());
        password = getPassword(getApplicationContext());

        rawID = getIntent().getStringExtra("key");
        itap_identifier = rawID.substring(1, rawID.length() - 1);

        myDialog = new Dialog(this);
        view_user_name = findViewById(R.id.user_name);
        view_user_phone = findViewById(R.id.user_phone);


        Button mButton = findViewById(R.id.follow_add_user_01);

        mButton.setOnClickListener(new View.OnClickListener() {
            RelativeLayout loading;
            Button _continue;
            TextView info_text, info_text2, view_token;
            Button yes, no;
            Button send_playstore_link;


            @Override
            public void onClick(View v) {

                if(validateData())
                {
                    String text1 = "Accede al siguiente enlace para obtener acceso remoto desde Taplock. ";
                    String text2 = "                                                            ¡MUY-IMPORTANTE! Cuando le pregunte como quiere abrir el enlace debe indicar Taplock";
                    myDialog.setContentView(R.layout.add_user_step1);
                    TextView uPhone, uName;
                    uPhone = myDialog.findViewById(R.id.phone_add_user);
                    uName = myDialog.findViewById(R.id.name_user_add);
                    info_text = myDialog.findViewById(R.id.info_text);
                    info_text2 = myDialog.findViewById(R.id.info_text2);
                    uPhone.setText(user_phone);
                    uName.setText(user_name);

                    yes = myDialog.findViewById(R.id.add_user_yes);
                    no = myDialog.findViewById(R.id.add_user_no);
                    _continue = myDialog.findViewById(R.id._continue_add_user);
                    send_playstore_link = myDialog.findViewById(R.id.add_user_play_store_link);
                    loading = myDialog.findViewById(R.id.loading_layout_add_user);

                    _continue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.setContentView(R.layout.add_user_step2);
                            view_token = myDialog.findViewById(R.id.token);
                            view_token.setText(rawToken);
                            TextView copy_token;
                            Button send_now, send_later;
                            send_now = myDialog.findViewById(R.id.send_now);
                            send_later = myDialog.findViewById(R.id.send_later);
                            copy_token= myDialog.findViewById(R.id.copy_token);
                            send_now.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String share_subject = "Enlace de descarga";
                                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                    String shareBody = text1 + token + text2;
                                    intent.setType("text/plain");
                                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, share_subject);
                                    intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                    startActivity(Intent.createChooser(intent, share_subject));

                                    //Intent intent2 = new Intent(AddUser.this, Menu.class);
                                    //startActivity(intent2);
                                    //finish();
                                }
                            });
                            send_later.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(AddUser.this, Menu.class);
                                    startActivity(intent);
                                    finish();

                                }
                            });

                            copy_token.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("label", rawToken);
                                    clipboard.setPrimaryClip(clip);
                                    Toast.makeText(getApplicationContext(), "Enlace copiado", Toast.LENGTH_SHORT).show();
                                }
                            });
                            myDialog.show();
                        }
                    });

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            send_playstore_link.setClickable(false);

                            loading.setVisibility(View.VISIBLE);
                            myDialog.setCancelable(false);

                            postData();

                        }
                    });

                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();
                        }
                    });

                    send_playstore_link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String share_subject = "Enlace de descarga";
                            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                            String shareBody = "De Taplock.app, puede descargar la aplicación oficial de Taplock desde el siguiente enlace https://play.google.com/store/apps/details?id=com.supercell.clashroyale&hl=es&gl=US";
                            intent.setType("text/plain");
                            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, share_subject);
                            intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(intent, share_subject));
                        }
                    });
                    myDialog.setCancelable(false);
                    myDialog.show();
                }
            }

            private boolean postData() {


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

                AddUserModel modal = new AddUserModel(email, password, itap_identifier, user_name, user_phone);

                // calling a method to create a post and passing our modal class.
                Call<AddUserModel> call = retrofitAPI.createPost(modal);

                // on below line we are executing our method.
                call.enqueue(new Callback<AddUserModel>() {
                    @Override
                    public void onResponse(Call<AddUserModel> call, Response<AddUserModel> response) {

                        if(response.code() == 200)
                        {
                            AddUserModel responseFromAPI = response.body();

                            message = responseFromAPI.getMessage();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            rawToken = responseFromAPI.getToken();
                            generate_token();
                            yes.setVisibility(View.GONE);
                            no.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            _continue.setVisibility(View.VISIBLE);
                            info_text.setText("Usuario añadido.");
                            info_text.setTextColor(Color.parseColor("#4c9173"));
                            info_text2.setText("El usuario ha sido añadido con la siguiente información:");

                            send_playstore_link.setClickable(true);

                        }
                        else
                        {
                            try {
                                JSONObject obj = new JSONObject(response.errorBody().string());
                                //showError(obj.getString("message"));
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                                startActivity(intent);

                            } catch (IOException | JSONException e) {
                                Toast.makeText(getApplicationContext(), "ERROR: No hay respuesta del servidor, inténtelo mas tarde.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                                startActivity(intent);

                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<AddUserModel> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "ERROR: Ha fallado la conexión con el servidor, inténtelo mas tarde.", Toast.LENGTH_SHORT).show();
                        //showError(t.getMessage());
                    }
                });
                return true;
            }
        });
        go_bacK = findViewById(R.id.go_back);
        go_bacK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go_back_activity();
            }
        });
    }


    public void generate_token()
    {
        token = "https://taplock.app/share-key/"+rawToken+"/";
    }


    private boolean validateData()
    {
        if(view_user_name.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Debe introducir un nombre", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(view_user_phone.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Debe introducir un número de teléfono", Toast.LENGTH_SHORT).show();
            return false;
        }

        user_phone = view_user_phone.getText().toString();
        user_name = view_user_name.getText().toString();
        return true;
    }



    private void maybe_later_func() {
    }

    private void send_now_func() {
    }

    public void go_back_activity()
    {
        Intent intent = new Intent(getApplicationContext(), UsersList.class);
        intent.putExtra("KEY", rawID);
        startActivity(intent);
    }

    private String getEmail(Context mContext)
    {
        String PREF_EMAIL = "email";
        SharedPreferences pref = mContext.getSharedPreferences(PREFS_NAME, mContext.MODE_PRIVATE);
        email = pref.getString(PREF_EMAIL, null);

        return email;
    }

    private String getPassword(Context mContext)
    {
        String PREF_PASSWORD = "password";
        SharedPreferences pref = mContext.getSharedPreferences(PREFS_NAME, mContext.MODE_PRIVATE);
        password = pref.getString(PREF_PASSWORD, null);

        return password;
    }
}