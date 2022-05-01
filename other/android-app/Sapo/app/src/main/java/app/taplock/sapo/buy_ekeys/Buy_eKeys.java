package app.taplock.sapo.buy_ekeys;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import app.taplock.sapo.R;
import app.taplock.sapo.RetrofitAPI;
import app.taplock.sapo.sign_in.SignIn;
import app.taplock.sapo.users_list.UsersList;
import app.taplock.sapo.users_list.UsersListModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Buy_eKeys extends AppCompatActivity {
    private String PREFS_NAME = "app.taplock.auth";
    private String email;
    private String password;

    String rawID;

    // Payment
    String checkout_url;
    String message;

    String itap_name, itap_address, itap_identifier;
    int amount = 1;

    TextView vName,vAddress, vAmount, vIndividualPrice, vSubtotal, vIva, vFees, vFinalPrice;
    EditText EdAmount;

    private TextView extra_info;

    float individual_price = 0;
    float subtotal = 0;
    float iva = 0;
    float fees = 0;
    float final_price = 0;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ekeys);
        email = getEmail(getApplicationContext());
        password = getPassword(getApplicationContext());

        itap_name = getIntent().getStringExtra("name");
        itap_address = getIntent().getStringExtra("address");
        individual_price = Float.parseFloat(getIntent().getStringExtra("price"));
        rawID = getIntent().getStringExtra("key");
        itap_identifier = rawID.substring(1).substring(0, rawID.length() - 2);
        calculate_data();

        extra_info = findViewById(R.id.extra_info);
        extra_info.setText("Se enviará un correo a su email con todos los pasos para realizar el pago. " + email);

        // Static data
        vName = findViewById(R.id.name);
        vName.setText(itap_name);

        vAddress= findViewById(R.id.address);
        vAddress.setText(itap_address);

        vIndividualPrice = findViewById(R.id.individual_price);
        vIndividualPrice.setText(String.format ("%.2f", individual_price));

        // Dinamic data

        vAmount = findViewById(R.id.amount);
        vAmount.setText(String.valueOf(amount));

        vSubtotal = findViewById(R.id.subtotal);
        vIva = findViewById(R.id.iva);
        vFees = findViewById(R.id.fees);
        vFinalPrice = findViewById(R.id.final_price);
        EdAmount = findViewById(R.id.editable_amount);
        EdAmount.setText(String.valueOf(amount));

        update_price();
        myDialog = new Dialog(this);

        findViewById(R.id.next_step).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.setContentView(R.layout.buy_itap_step1);

                Button btnFollow;
                btnFollow = (Button) myDialog.findViewById(R.id.next_step_pop_up);
                btnFollow.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onClick(View v) {
                        myDialog.setContentView(R.layout.buy_itap_step2);

                        TextView vName, vAddress, vAmount, vIndividualPrice, vSubtotal, vIva, vFees, vFinalPrice;

                        vName = myDialog.findViewById(R.id.name);
                        vAddress= myDialog.findViewById(R.id.address);
                        vIndividualPrice = myDialog.findViewById(R.id.individual_price);
                        vAmount = myDialog.findViewById(R.id.amount);
                        vSubtotal = myDialog.findViewById(R.id.subtotal);
                        vIva = myDialog.findViewById(R.id.iva);
                        vFees = myDialog.findViewById(R.id.fees);
                        vFinalPrice = myDialog.findViewById(R.id.final_price);

                        vName.setText(itap_name);
                        vAddress.setText(itap_address);
                        vIndividualPrice.setText(String.format ("%.2f", individual_price));
                        vSubtotal.setText(String.format ("%.2f", subtotal));
                        vIva.setText(String.format ("%.2f", iva));
                        vFees.setText(String.format ("%.2f", fees));
                        vFinalPrice.setText(String.format ("%.2f", final_price));
                        vAmount.setText(String.valueOf(amount));

                        myDialog.show();

                        Button btnFollow;
                        btnFollow = (Button) myDialog.findViewById(R.id.next_step_2);
                        btnFollow.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onClick(View v) {

                                myDialog.setContentView(R.layout.buy_itap_step3);
                                RelativeLayout loading_layout;
                                loading_layout = myDialog.findViewById(R.id.loading_layout);
                                loading_layout.setVisibility(View.GONE);

                                myDialog.show();


                                Button btnFollow;
                                btnFollow = (Button) myDialog.findViewById(R.id.checkout);
                                btnFollow.setOnClickListener(new View.OnClickListener() {
                                    @SuppressLint("DefaultLocale")
                                    @Override
                                    public void onClick(View v) {
                                        loading_layout.setVisibility(View.VISIBLE);

                                        //myDialog.dismiss();
                                        postData();
                                    }
                                });
                            }
                        });
                     }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
            }
        });



        EdAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                EdAmount.setSelection(EdAmount.getText().toString().length());

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                EdAmount.setSelection(EdAmount.getText().toString().length());

                if(s.toString().isEmpty())
                {

                }
                else
                {
                    amount = Integer.parseInt(EdAmount.getText().toString());

                    if(amount < 1)
                    {
                        Toast.makeText(getApplicationContext(), "La cantidad mínima es 1", Toast.LENGTH_SHORT).show();
                        amount = 1;
                        EdAmount.setText(String.valueOf(1));
                        EdAmount.setSelection(EdAmount.getText().toString().length());
                    }
                    else
                    {
                        amount = amount - 1;
                    }

                    if(amount > 9999)
                    {
                        Toast.makeText(getApplicationContext(), "La cantidad máxima es 9999", Toast.LENGTH_SHORT).show();
                        amount = 999;
                        EdAmount.setText(String.valueOf(9999));
                        EdAmount.setSelection(EdAmount.getText().toString().length());
                    }
                    else
                    {
                        amount = amount + 1;
                    }

                    //amount = Integer.parseInt(EdAmount.getText().toString());
                    update_price();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }




    private boolean postData() {

        //blurBackgroundOnSendData();

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
       // Toast.makeText(getApplicationContext(), "ID: " + itap_identifier, Toast.LENGTH_LONG).show();
        Buy_eKeysModel modal = new Buy_eKeysModel(email, password, itap_identifier, String.valueOf(amount));

        // calling a method to create a post and passing our modal class.
        Call<Buy_eKeysModel> call = retrofitAPI.createPost(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<Buy_eKeysModel>() {
            @Override
            public void onResponse(Call<Buy_eKeysModel> call, Response<Buy_eKeysModel> response) {

                if(response.code() == 200)
                {
                    Buy_eKeysModel responseFromAPI = response.body();
                    //responseData = responseFromAPI;

                    checkout_url = responseFromAPI.getUrl();
                    message = responseFromAPI.getMessage();

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    checkout();
                    //Update();
                    //focusBackgroundAfterSendData();
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
                        Toast.makeText(getApplicationContext(), "ERROR: No hay respuesta del servidor, inténtelo mas tarde.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SignIn.class);
                        startActivity(intent);

                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Buy_eKeysModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR: Ha fallado la conexión con el servidor, inténtelo mas tarde.", Toast.LENGTH_SHORT).show();
                //showError(t.getMessage());
            }
        });

        return true;
    }

    private void checkout() {

        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(checkout_url));
        startActivity(browserIntent);
        finish();
    }


    public void go_back(View view)
    {
        Intent intent = new Intent(getApplicationContext(), UsersList.class);
        intent.putExtra("KEY", rawID);
        startActivity(intent);
    }

    public void edit_amount(View view)
    {
        EdAmount.setSelection(EdAmount.getText().toString().length());
    }

    public void add_amount(View view)
    {

        if(amount >= 9999)
        {
            Toast.makeText(getApplicationContext(), "La cantidad máxima es 9999", Toast.LENGTH_SHORT).show();
            amount = 9999;
        }
        else
        {
            amount = amount + 1;
        }
        update_price();
        EdAmount.setText(String.valueOf(amount));
        EdAmount.setSelection(EdAmount.getText().toString().length());
        EdAmount.clearFocus();

    }

    public void substract_amount(View view)
    {

        if(amount <= 1)
        {
            Toast.makeText(getApplicationContext(), "La cantidad mínima es 1", Toast.LENGTH_SHORT).show();
            amount = 1;
        }
        else
        {
            amount = amount - 1;
        }
        update_price();
        EdAmount.setText(String.valueOf(amount));
        EdAmount.setSelection(EdAmount.getText().toString().length());
        EdAmount.clearFocus();

    }


    private void calculate_data()
    {
        subtotal = amount * individual_price;
        iva = (subtotal*(21f/100.0f));
        fees = (subtotal*(1.4f/100.0f));
        final_price = subtotal+iva+fees;

    }

    private void update_price()
    {
        calculate_data();
        vSubtotal.setText(String.format ("%.2f", subtotal));
        vIva.setText(String.format ("%.2f", iva));
        vFees.setText(String.format ("%.2f", fees));
        vFinalPrice.setText(String.format ("%.2f", final_price));
        vAmount.setText(String.valueOf(amount));
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
        SharedPreferences pref = mContext.getSharedPreferences(PREFS_NAME,mContext.MODE_PRIVATE);
        password = pref.getString(PREF_PASSWORD, null);

        return password;
    }


}