package com.example.Contacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Home extends AppCompatActivity {
    RecyclerView Recycler_View;
    List<ContactModel> Contacts_List;
    public static final int Intent_Code = 1;
    private int Intent_Request = 1;

    RVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_layout);

        Permissions_Needed();

        DatabaseHandler db = new DatabaseHandler(this);

        Recycler_View = findViewById(R.id.recycler_view);

        Contacts_List = db.getAllContacts();

        adapter = new RVAdapter(Contacts_List, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Home.this);

        Recycler_View.setLayoutManager(layoutManager);

        Recycler_View.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    public void Add_Contact(View view) {

        Intent Add_New_Contact = new Intent(this,New_Contact.class);

        startActivity(Add_New_Contact);

    }

    public void Permissions_Needed(){

        while (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }

}