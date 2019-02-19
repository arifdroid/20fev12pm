package com.example.dell.a19febnoon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class FindUserActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<ContactObject> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

    recyclerView = findViewById(R.id.recyclerId);



    initializeRecycler();
    populateContact();


    }

    private void initializeRecycler() {

        recyclerAdapter = new CustomRecyclerAdapter(userList);
        layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayout.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

    }

    private void populateContact(){

     userList.add(new ContactObject("arif","018"));
        userList.add(new ContactObject("arif","018"));
        userList.add(new ContactObject("arif","018"));
        userList.add(new ContactObject("arif2","018"));
        userList.add(new ContactObject("ari3f","018"));
        userList.add(new ContactObject("ar4if","018"));
        userList.add(new ContactObject("ar2if","018"));
        userList.add(new ContactObject("arif","018"));
        userList.add(new ContactObject("ari1f","018"));
        userList.add(new ContactObject("ar1if","018"));
        userList.add(new ContactObject("ari3f","018"));
        userList.add(new ContactObject("ar7i5f","018"));
        userList.add(new ContactObject("arif","018"));
        userList.add(new ContactObject("ari8f","018"));
        userList.add(new ContactObject("ar9if","018"));


    }
}
