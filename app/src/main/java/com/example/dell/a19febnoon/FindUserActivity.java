package com.example.dell.a19febnoon;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindUserActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    //userlist is for what we register,
    //contact list is from phone to check back with our register

    ArrayList<ContactObject> userList,contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

    recyclerView = findViewById(R.id.recyclerId);

    contactList.add(new ContactObject("here","1"));

    initializeRecycler();
   // populateContact();

      //  populateContact();

    getContactPhone();

    }

    private void getContactPhone() {
       // String numberPhone2="";


        Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while (phone.moveToNext()){

            String namePhone = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String numberPhone = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


            //normalized numberPhone

            numberPhone = numberPhone.replace(" ","");
            numberPhone =numberPhone.replace("-","");
            numberPhone =numberPhone.replace("(","");
            numberPhone =numberPhone.replace(")","");

//            if(!String.valueOf(numberPhone.charAt(0)).equals("0")){
//               continue; //cant continue here, will bounce out all data with +
//            }

            if(!String.valueOf(numberPhone.charAt(0)).equals("+")) {


                String s = getCountryIso();

                String s2 = Iso2Phone.getPhone(s);

                numberPhone = s2 + numberPhone;

            }



            ContactObject mContact = new ContactObject(namePhone,numberPhone);

            //contactList.add(mContact);


          // recyclerAdapter.notifyDataSetChanged();// dont need, did not display

            //here we check the detail from database if any
//            contactList.add(new ContactObject("here","2"));
//            recyclerAdapter.notifyDataSetChanged();

            getUserDetailFromDatabase(mContact);


        }



    }


    //problem now , method below is not executed. why?


    private void getUserDetailFromDatabase(ContactObject mContact) {

        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user");

        //here we check from number we add, is there record of it in database
        Query query = mUserDB.orderByChild("phone").equalTo(mContact.getPhoneNumber());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    String phoneHere ="";
                    String nameHere ="";

                    for(DataSnapshot childSnapShot:dataSnapshot.getChildren()){

                        if(childSnapShot.child("phone").getValue()!=null){
                            phoneHere=childSnapShot.child("phone").getValue().toString();
                        }

                        if(childSnapShot.child("name").getValue()!=null){
                            nameHere=childSnapShot.child("name").getValue().toString();
                        }

                        ContactObject contactHere = new ContactObject(nameHere,phoneHere);

                        contactList.add(contactHere);
                        recyclerAdapter.notifyDataSetChanged();
                        return;

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private String getCountryIso(){
        String iso ="";

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);

        //this will return the country code


        if(telephonyManager.getNetworkCountryIso()!=null){
            iso = telephonyManager.getNetworkCountryIso();
        }




        return iso;
    }

    private void initializeRecycler() {

        recyclerAdapter = new CustomRecyclerAdapter(contactList);
        layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayout.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

    }

    private void populateContact(){

     contactList.add(new ContactObject("arif","018"));
        contactList.add(new ContactObject("arif","018"));
        contactList.add(new ContactObject("arif","018"));
        contactList.add(new ContactObject("arif2","018"));



    }
}
