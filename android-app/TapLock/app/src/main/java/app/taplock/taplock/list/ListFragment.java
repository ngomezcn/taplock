package app.taplock.taplock.list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import app.taplock.taplock.R;
import android.content.SharedPreferences;
import android.widget.Toast;

public class ListFragment extends Fragment implements Adapter.OnNoteListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View listItemsView;

    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> address = new ArrayList<String>();
    ArrayList<String> tokensList;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_list, container, false);

        name.add("Sosmatic");
        address.add("Via Llacuna");

        loadData(mContext);

        Toast.makeText(getContext(), tokensList.size() + "", Toast.LENGTH_SHORT).show();

        for(int i = 0; i < tokensList.size(); i++)
        {
            name.add(tokensList.get(i));
            address.add(tokensList.get(i));

        }

        listItemsView = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = listItemsView.findViewById(R.id.programmingLangList);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Adapter(name,address, this);
        recyclerView.setAdapter(adapter);
        if(name.isEmpty())
        {
             LinearLayout EmptyList = listItemsView.findViewById(R.id.empty_list);
             EmptyList.setVisibility(View.VISIBLE);
             recyclerView.setVisibility(View.GONE);

        }
        return listItemsView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    private boolean loadData(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("shared preferences", mContext.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("test02", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        tokensList = gson.fromJson(json, type);

        if (tokensList == null) {
            tokensList = new ArrayList<>();
        }

        return true;
    }


    @Override
    public void onNoteClick(int position) {

    }
}