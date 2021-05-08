package com.example.Contacts;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "A1";
    private static final String TABLE_CONTACTS = "Contacts";
    private static final String KEY_ID = "ID";
    private static final String KEY_NAME = "Name";
    private static final String KEY_PH_NO = "Phone_No";
    private static final String KEY_ADDRESS = "Address" ;
    private static final String KEY_IMAGE = "Photo" ;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_IMAGE + " TEXT "
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    void addContact(ContactModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhone_No());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_IMAGE, contact.getImage());

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    ContactModel getContact(int id) {
        //SQLiteDatabase db = this.getReadableDatabase();
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE ID = " + (""+id);

        Cursor cursor = db.rawQuery(selectQuery, null);

        //Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID}, KEY_ID + "=?",
                //new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ContactModel contact = new ContactModel();
        contact.setContact_ID(cursor.getInt(0));
        contact.setName(cursor.getString(1));
        contact.setPhone_No(cursor.getString(2));
        contact.setAddress(cursor.getString(3));
        contact.setImage(cursor.getString(4));
        return contact;
    }

    // code to get all contacts in a list view
    public List<ContactModel> getAllContacts() {
        List<ContactModel> contactList = new ArrayList<ContactModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                ContactModel contact = new ContactModel();
                contact.setContact_ID(cursor.getInt(0));
                contact.setName(cursor.getString(1));
                contact.setPhone_No(cursor.getString(2));
                contact.setAddress(cursor.getString(3));
                contact.setImage(cursor.getString(4));

                // Adding contact to list

                contactList.add(contact);

            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updateContact(ContactModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhone_No());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_IMAGE, contact.getImage());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getContact_ID())});
    }

    // Deleting single contact
    public void deleteContact(ContactModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getContact_ID())});
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}