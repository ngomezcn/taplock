package app.taplock.sapo.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import app.taplock.sapo.GetCredentials;
import app.taplock.sapo.MainActivity;
import app.taplock.sapo.R;
import app.taplock.sapo.RetrofitAPI;
import app.taplock.sapo.add_itap.AddItap;
import app.taplock.sapo.add_itap.AddItapModel;
import app.taplock.sapo.menu.Menu;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment implements AppAdapter.OnNoteListener {
    private String PREFS_NAME = "app.taplock.auth";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout layout_background;

    private GetCredentials credentials;

    private RelativeLayout loading_layout;
    private String email;
    private String password;

    private EditText search_home;
    private TextView error_text;

    ArrayList<String> nameFilter = new ArrayList<String>();
    ArrayList<String> addressFilter = new ArrayList<String>();
    ArrayList<String> statusFilter = new ArrayList<String>();

    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> address = new ArrayList<String>();
    ArrayList<String> status = new ArrayList<String>();

    private View listItemsView;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //name.add("Tienda Farinetas");
        //address.add("Plaza Antonio, 511, 46º D");
        //status.add("Activo");

        listItemsView = inflater.inflate(R.layout.home_fragment, container, false);
        recyclerView = listItemsView.findViewById(R.id.programmingLangList);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AppAdapter(name,address,status, this);
        recyclerView.setAdapter(adapter);
        return listItemsView;
    }

    Button add_ip_btn;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        email = getEmail(mContext);
        password = getPassword(mContext);

        search_home = getView().findViewById(R.id.search_home);
        add_ip_btn = getView().findViewById(R.id.add_ip_btn);

        loading_layout = getView().findViewById(R.id.loading_layout);
        layout_background = getView().findViewById(R.id.layout_background);
        error_text = getView().findViewById(R.id.error_text);

        add_ip_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), AddItap.class);
                startActivity(intent);

            }
        });

        postData();
        search_home.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //adapter = new AppAdapter(carss);
                //recyclerView.setAdapter(adapter);
                nameFilter.clear();
                addressFilter.clear();
                statusFilter.clear();

                if(s.toString().isEmpty())
                {
                    Update();
                }
                else
                {
                    Filter(s.toString());
                }
            }
        });
    }


    private void Update() {
        adapter = new AppAdapter(name,address,status, this);
        recyclerView.setAdapter(adapter);

    }
        private void Filter(String text) {

        for (int i = 0;i < name.size();i++) {

            if(name.get(i).toLowerCase().contains(text.toLowerCase()) || address.get(i).toLowerCase().contains(text.toLowerCase()))
            {
                nameFilter.add(name.get(i));
                addressFilter.add(address.get(i));
                statusFilter.add(status.get(i));
            }
            adapter = new AppAdapter(nameFilter,addressFilter,statusFilter, this);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onNoteClick(int position) {
        Toast.makeText(getContext(),"HOLA "+position,Toast.LENGTH_SHORT).show();
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
        //AddItapModel modal = new AddItapModel("naimgomezcn@gmail.com", "19401940n", "name", "address", "PC0Z58C");

        GetItapsModel modal = new GetItapsModel(email, password);

        // calling a method to create a post and passing our modal class.
        Call<GetItapsModel> call = retrofitAPI.createPost(modal);

        // on below line we are executing our method.
        call.enqueue(new Callback<GetItapsModel>() {
            @Override
            public void onResponse(Call<GetItapsModel> call, Response<GetItapsModel> response) {

                if(response.code() == 200)
                {
                    GetItapsModel responseFromAPI = response.body();

                    int index = 0;
                    for (int i = 0; i < 999; i++)
                    {
                        try {
                            name.add(responseFromAPI.getResponse().get(String.valueOf(i)).getAsJsonObject().getAsJsonPrimitive("name").toString().substring(1).substring(0, responseFromAPI.getResponse().get(String.valueOf(i)).getAsJsonObject().getAsJsonPrimitive("name").toString().length() - 2));
                            address.add(responseFromAPI.getResponse().get(String.valueOf(i)).getAsJsonObject().getAsJsonPrimitive("address").toString().substring(1).substring(0, responseFromAPI.getResponse().get(String.valueOf(i)).getAsJsonObject().getAsJsonPrimitive("address").toString().length() - 2));
                            status.add(responseFromAPI.getResponse().get(String.valueOf(i)).getAsJsonObject().getAsJsonPrimitive("status").toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                    Update();
                    focusBackgroundAfterSendData();
                }
                else
                {
                    try {
                        JSONObject obj = new JSONObject(response.errorBody().string());
                        showError(obj.getString("message"));

                    } catch (IOException | JSONException e) {
                        Toast.makeText(getContext(), "ERROR: No hay respuesta del servidor, inténtelo mas tarde.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetItapsModel> call, Throwable t) {
                Toast.makeText(getContext(), "ERROR: Ha fallado la conexión con el servidor, inténtelo mas tarde.", Toast.LENGTH_SHORT).show();
                showError(t.getMessage());
            }
        });

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
        loading_layout.setVisibility(View.VISIBLE);
        layout_background.setAlpha((float) 0.5);
        add_ip_btn.setClickable(false);

        search_home.clearFocus();
        search_home.setFocusableInTouchMode(false);

    }

    private void focusBackgroundAfterSendData()
    {
        loading_layout.setVisibility(View.GONE);
        layout_background.setAlpha((float) 1);
        add_ip_btn.setClickable(true);
        search_home.setFocusableInTouchMode(true);
    }
}

