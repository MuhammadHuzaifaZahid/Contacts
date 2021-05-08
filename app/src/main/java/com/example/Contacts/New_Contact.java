package com.example.Contacts;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

public class New_Contact extends AppCompatActivity {

    Button Save_Contact;

    public static final int REQUEST_CODE_PickImage=1;

    private static final int REQUEST_CODE_AUTOCOMPLETE = 2;
    String MapBox_Access_Token;

    ImageView Photo;
    EditText Name;
    EditText Phone_No;
    TextView Address;
    String address_json;
    String Image_Uri;
    boolean No_Intents_Called = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_contact_layout);



        MapBox_Access_Token = "pk.eyJ1Ijoic21kLXByb2plY3RzIiwiYSI6ImNrMTF1cnJzMDBrbzYzY3AybDJ2cDJhMXcifQ.QX4XGvQOqNRk9uogsCuneg";

        Save_Contact = findViewById(R.id.Save);

        Name=findViewById(R.id.Edit_Name);

        Photo =findViewById(R.id.image);

        Phone_No =findViewById(R.id.Edit_Phone_No);

        Address =findViewById(R.id.Edit_Address);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (No_Intents_Called)
        {
            finish();
        }
    }

    public void Pick_Image(View view)
    {
        No_Intents_Called = false;
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent,REQUEST_CODE_PickImage);

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            address_json = selectedCarmenFeature.toJson();
            TextView editText = findViewById(R.id.Edit_Address);
            editText.setText(selectedCarmenFeature.placeName());
            EditText d = findViewById(R.id.ad1);
            d.setVisibility(View.INVISIBLE);
        }
        else if (resultCode == RESULT_OK&& requestCode == REQUEST_CODE_PickImage) {

                final Uri imageUri = data.getData();
                Photo.setImageURI(imageUri);
                Image_Uri = imageUri.toString();
        }
        No_Intents_Called = true;
    }

    public void returnReply(View view)
    {
        String name = Name.getText().toString();
        String phno = Phone_No.getText().toString();

        DatabaseHandler db = new DatabaseHandler(this);
        db.addContact(new ContactModel(name,phno,address_json,Image_Uri));

        Back_To_Home(view);
    }

    private void Back_To_Home(View view) {
        Intent Go_Back_Home= new Intent(this,Home.class);
        startActivity(Go_Back_Home);
    }


    public void PlaceAutoComplete(View view) {
        No_Intents_Called = false;
        Intent intent = new PlaceAutocomplete.IntentBuilder()

                .accessToken(MapBox_Access_Token)
                .placeOptions(PlaceOptions.builder().country("PK")
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .build(PlaceOptions.MODE_CARDS))
                .build(New_Contact.this);
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }
}


