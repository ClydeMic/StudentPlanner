package com.example.studentplanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Adapters.JournalAdapter;
import com.example.Models.User;
import com.example.Models.journal;
import com.example.studentplanner.Constant;
import com.example.studentplanner.HomeActivity;
import com.example.studentplanner.R;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class JournalFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<journal> arrayList;
    private SwipeRefreshLayout refreshLayout;
    private JournalAdapter journalAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;


/**
 * A simple {@link Fragment} subclass.
 */


    public JournalFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_journal,container,false);
        init();
        return view;
    }

    private void init(){

        sharedPreferences=getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView=view.findViewById(R.id.recyclerJournal);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout=view.findViewById(R.id.swipejournal);
        toolbar=view.findViewById(R.id.toolbarJournal);
        ((HomeActivity)getContext()).setSupportActionBar(toolbar);

        getJournals();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }

    private void getJournals() {

        arrayList=new ArrayList<>();
        refreshLayout.setRefreshing(true);

        StringRequest request = new StringRequest(Request.Method.GET, Constant.JOURNAL, response -> {

            try {
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("success")){



                    JSONArray array = new JSONArray(object.getString("journal"));
                    Log.e(TAG, "getJournals: "+array);

                    for(int i=0;i<array.length();i++){
                        JSONObject journalObject=array.getJSONObject(i);
                        JSONObject userObject= (JSONObject) journalObject.get("user");

                        User user=new User();
                        user.setId(userObject.getInt("id"));
                        user.setUserName(userObject.getString("name"));
                        user.setPhoto(userObject.getString("photo"));

                        journal journal = new journal();
                        journal.setId(journalObject.getInt("id"));
                        journal.setUser(user);
                        journal.setLikes(journalObject.getInt("likesCount"));
                       journal.setComments(journalObject.getInt("commentCount"));
                        journal.setDate(journalObject.getString("created_at"));
                        journal.setAbout(journalObject.getString("about"));
                        journal.setTag(journalObject.getString("tag"));
                        journal.setFeelings(journalObject.getString("feelings"));
                        journal.setPhoto(journalObject.getString("photo"));
                        journal.setSelfLike(journalObject.getBoolean("selfLike"));

                        arrayList.add(journal);


                    }
                    journalAdapter= new JournalAdapter(getContext(),arrayList);
                    recyclerView.setAdapter(journalAdapter);


                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "error message: ");
            }
            refreshLayout.setRefreshing(false);

        },error -> {
            Log.e(TAG, "error message3: ");
            error.printStackTrace();
            refreshLayout.setRefreshing(false);

        }){
            //PROVIDE TOKEN HEADER
            public Map<String,String> getHeaders() throws AuthFailureError{
                String token=sharedPreferences.getString("token","");
                HashMap<String,String>map=new HashMap<>();
                map.put("Authorization","Bearer"+token);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);

    }


}
