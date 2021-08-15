package app.taplock.sapo.users_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import app.taplock.sapo.MainActivity;
import app.taplock.sapo.R;
import app.taplock.sapo.RetrofitAPI;
import app.taplock.sapo.add_user.AddUser;
import app.taplock.sapo.buy_ekeys.Buy_eKeys;
import app.taplock.sapo.menu.Menu;
import app.taplock.sapo.sign_in.SignIn;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersList extends AppCompatActivity implements AppAdapter.OnNoteListener {
    private String PREFS_NAME = "app.taplock.auth";

    boolean loading = false;

    private Boolean is_searching = false;

    private String email;
    private String password;
    private String identifier;

    private TextView nameTXT;
    private TextView address;
    private TextView active_users;
    private TextView down_users;
    private TextView used_eKeys;
    private TextView available_eKeys;
    private JsonObject user_list;

    // Layout
    private RelativeLayout loading_layout;
    private LinearLayout layout_background;
    private TextView error_text;
    private Button btn_add_user, btn_buy_ekeys;
    private EditText search_home;

    private String itap_name, itap_address, itap_identifier, individual_price;

    UsersListModel responseData;

    // Recycler view
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<String> nameFilter = new ArrayList<String>();
    ArrayList<String> phoneFilter = new ArrayList<String>();
    ArrayList<String> statusFilter = new ArrayList<String>();
    ArrayList<String> seed_idFilter = new ArrayList<String>();

    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> phone = new ArrayList<String>();
    ArrayList<String> status = new ArrayList<String>();
    ArrayList<String> seed_id = new ArrayList<String>();
    String rawID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        //Toast.makeText(UsersList.this, getIntent().getStringExtra("KEY"), Toast.LENGTH_SHORT).show();

        /*name.add("Naim");
        phone.add("609513287");
        status.add("Activo");
        seed_id.add("82386");

        name.add("Naim");
        phone.add("609513287");
        status.add("Activo");
        seed_id.add("82386");*/

        // Post data charger
        rawID = getIntent().getStringExtra("KEY");

        identifier = rawID.substring(1, rawID.length() - 1);
        email = getEmail(getApplicationContext());
        password = getPassword(getApplicationContext());

        // Layout
        loading_layout = findViewById(R.id.loading_layout);
        layout_background = findViewById(R.id.layout_background);
        error_text = findViewById(R.id.error_text);
        btn_add_user = findViewById(R.id.add_user);
        btn_buy_ekeys = findViewById(R.id.buy_ekeys_btn);
        search_home = findViewById(R.id.search_users_list);
        recyclerView = findViewById(R.id.user_recyclerview);

        nameTXT = findViewById(R.id.name);
        address = findViewById(R.id.address);
        active_users = findViewById(R.id.active_users);
        down_users = findViewById(R.id.down_users);
        used_eKeys = findViewById(R.id.used_eKeys);
        available_eKeys =  findViewById(R.id.available_eKeys);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        //InputMethodManager imm  = (InputMethodManager)getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //Log.e(TAG, "Result :"+imm.isActive());
        postData();

        // Listener

        search_home.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nameFilter.clear();
                phoneFilter.clear();
                statusFilter.clear();
                seed_idFilter.clear();

                if(s.toString().isEmpty())
                {
                    is_searching = false;
                    Update();
                    show_elements();
                }
                else
                {
                    is_searching = true;
                    Filter(s.toString());
                    on_search_hide_elements();
                }
            }
        });

        btn_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!loading)
               {
                    go_to_add_users();
               }
            }
        });

        btn_buy_ekeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loading)
                {
                    go_to_buy_ekeys();
                }
            }
        });
    }

    private void go_to_add_users() {
        Intent intent = new Intent(getApplicationContext(), AddUser.class);

        intent.putExtra("key", rawID);
        startActivity(intent);
        finish();
    }

    public void on_search_hide_elements()
    {
        View hide_01 = findViewById(R.id.hide_01);
        LinearLayout hide_02 = findViewById(R.id.hide_02);
        View hide_03 = findViewById(R.id.hide_03);
        LinearLayout hide_04 = findViewById(R.id.hide_04);

        hide_01.setVisibility(View.GONE);
        hide_02.setVisibility(View.GONE);
        hide_03.setVisibility(View.GONE);
        hide_04.setVisibility(View.GONE);
    }

    public void show_elements()
    {
        View hide_01 = findViewById(R.id.hide_01);
        LinearLayout hide_02 = findViewById(R.id.hide_02);
        View hide_03 = findViewById(R.id.hide_03);
        LinearLayout hide_04 = findViewById(R.id.hide_04);

        hide_01.setVisibility(View.VISIBLE);
        hide_02.setVisibility(View.VISIBLE);
        hide_03.setVisibility(View.VISIBLE);
        hide_04.setVisibility(View.VISIBLE);
    }

    public void go_to_menu(View view)
    {

        if(is_searching)
        {
            show_elements();
            search_home.clearFocus();
            search_home.setText("");
        }
        else
        {
            Intent intent = new Intent(UsersList.this, Menu.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean postData() {

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
        UsersListModel modal = new UsersListModel(email, password, identifier);

        // calling a method to create a post and passing our modal class.
        Call<UsersListModel> call = retrofitAPI.createPost(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<UsersListModel>() {
            @Override
            public void onResponse(Call<UsersListModel> call, Response<UsersListModel> response) {

                if(response.code() == 200)
                {
                    UsersListModel responseFromAPI = response.body();
                    responseData = responseFromAPI;

                    nameTXT.setText(responseFromAPI.getName());
                    address.setText(responseFromAPI.getAddress());
                    String price = responseFromAPI.getIndividual_price();

                    active_users.setText(String.valueOf(responseFromAPI.getActive_users()));
                    down_users.setText(String.valueOf(responseFromAPI.getDown_users()));
                    int _used_ekeys = responseFromAPI.getUsed_eKeys();
                    used_eKeys.setText(String.valueOf(_used_ekeys));
                    available_eKeys.setText(String.valueOf(responseFromAPI.getAvailable_eKeys()));
                    //Toast.makeText(getApplicationContext(), responseFromAPI.getUser_list().get(String.valueOf(0)).getAsJsonObject().getAsJsonPrimitive("name").toString(), Toast.LENGTH_LONG).show();

                    JsonObject jsonRespone = responseFromAPI.getUser_list();

                    int i = 0;
                    while(true)
                    {
                        try {
                            name.add(jsonRespone.get(String.valueOf(i)).getAsJsonObject().get("name").toString().substring(1).substring(0, jsonRespone.get(String.valueOf(i)).getAsJsonObject().getAsJsonPrimitive("name").toString().length() - 2));
                            phone.add(jsonRespone.get(String.valueOf(i)).getAsJsonObject().get("phone").toString().substring(1).substring(0, jsonRespone.get(String.valueOf(i)).getAsJsonObject().getAsJsonPrimitive("phone").toString().length() - 2));
                            status.add(jsonRespone.get(String.valueOf(i)).getAsJsonObject().get("status").toString().substring(1).substring(0, jsonRespone.get(String.valueOf(i)).getAsJsonObject().getAsJsonPrimitive("status").toString().length() - 2));
                            seed_id.add(jsonRespone.get(String.valueOf(i)).getAsJsonObject().get("seed_id").toString().substring(1).substring(0, jsonRespone.get(String.valueOf(i)).getAsJsonObject().getAsJsonPrimitive("seed_id").toString().length() - 2));

                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                        i = i + 1;
                    }

                    Update();
                    focusBackgroundAfterSendData();
                }
                else
                {
                    try {
                        JSONObject obj = new JSONObject(response.errorBody().string());
                        showError(obj.getString("message"));
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
            public void onFailure(Call<UsersListModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR: Ha fallado la conexión con el servidor, inténtelo mas tarde.", Toast.LENGTH_SHORT).show();
                showError(t.getMessage());
            }
        });

        return true;
    }

    private void Update() {
        //Toast.makeText(getApplicationContext(),name.get(1),Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),phone.size(),Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),status.size(),Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),status.size() + seed_id.size(),Toast.LENGTH_SHORT).show();

        adapter = new app.taplock.sapo.users_list.AppAdapter(name,phone,status,seed_id, this);
        recyclerView.setAdapter(adapter);

    }
    private void Filter(String text) {

        for (int i = 0;i < name.size();i++) {

            if(name.get(i).toLowerCase().contains(text.toLowerCase()) || phone.get(i).toLowerCase().contains(text.toLowerCase()) || status.get(i).toLowerCase().contains(text.toLowerCase()))
            {
                nameFilter.add(name.get(i));
                phoneFilter.add(phone.get(i));
                statusFilter.add(status.get(i));
                seed_idFilter.add(seed_id.get(i));
            }
            adapter = new AppAdapter(nameFilter,phoneFilter,statusFilter, seed_idFilter, this);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onNoteClick(int position) {

        //Toast.makeText(getApplicationContext(),position+"" ,Toast.LENGTH_SHORT).show();

        String _seed_id;
        if(search_home.getText().toString().matches("")) {
            //Toast.makeText(getApplicationContext(), "nonce"+seed_id.get(position),Toast.LENGTH_SHORT).show();
            _seed_id = seed_id.get(position);
            //_seed_id = seed_id.get(position);
        }
        else
        {
            //_seed_id = seed_idFilter.get(position);
            _seed_id = seed_idFilter.get(position);
            //Toast.makeText(getApplicationContext(), "FILTER"+seed_idFilter.get(position),Toast.LENGTH_SHORT).show();
        }

        //Intent intent = new Intent(getApplicationContext(), UsersList.class);
        //intent.putExtra("KEY", _seed_id);
        //startActivity(intent);
    }

    public void go_to_buy_ekeys()
    {
        Intent intent = new Intent(getApplicationContext(), Buy_eKeys.class);

        intent.putExtra("name", responseData.getName());
        intent.putExtra("address", responseData.getAddress());
        intent.putExtra("price", responseData.getIndividual_price());
        intent.putExtra("key", rawID);
        startActivity(intent);
        finish();
    }

    private void blurBackgroundOnSendData()
    {

        loading = true;
        loading_layout.setVisibility(View.VISIBLE);
        layout_background.setAlpha((float) 0.5);
        btn_add_user.setClickable(false);
        btn_buy_ekeys.setClickable(true);


        search_home.clearFocus();
        search_home.setFocusableInTouchMode(false);
    }

    private void focusBackgroundAfterSendData()
    {
        loading = false;
        loading_layout.setVisibility(View.GONE);
        layout_background.setAlpha((float) 1);
        btn_add_user.setClickable(true);
        btn_buy_ekeys.setClickable(true);

        search_home.setFocusableInTouchMode(true);
    }

    private void showError(String s)
    {
        error_text.setText(s);
    }

    private String getEmail(Context mContext)
    {
        String PREF_EMAIL = "email";
        SharedPreferences pref = mContext.getSharedPreferences(PREFS_NAME,mContext.MODE_PRIVATE);
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