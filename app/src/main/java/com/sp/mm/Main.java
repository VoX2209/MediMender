package com.sp.mm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

public class Main extends AppCompatActivity {

    //Initialize variable

    DrawerLayout drawerLayout;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    String userID, profileID;

    TextView medicineName, medicineHour, medicineMin, medicineShape, medicineDay, medicineQuantity, monday, tuesday, wednesday, thursday, friday, saturday, sunday;


    List<String> tags;
    private static final String TAG = "TAG" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = fAuth.getCurrentUser().getUid();

        medicineName = findViewById(R.id.medName);
        medicineQuantity = findViewById(R.id.medQuantity);
        medicineHour = findViewById(R.id.medHour);
        medicineMin = findViewById(R.id.medMinute);
        medicineShape = findViewById(R.id.medShape);

        monday = findViewById(R.id.medMon);
        tuesday = findViewById(R.id.medTue);
        wednesday = findViewById(R.id.medWed);
        thursday = findViewById(R.id.medThu);
        friday = findViewById(R.id.medFri);
        saturday = findViewById(R.id.medSat);
        sunday = findViewById(R.id.medSun);

        //Assign Variable
        drawerLayout = findViewById(R.id.drawer_layout);

        DocumentReference documentReference = fstore.collection("users").document(userID).collection("medications").document("med1");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(fAuth.getCurrentUser() != null) {
                    medicineName.setText(documentSnapshot.getString("Medication Name"));
                    medicineQuantity.setText(documentSnapshot.getString("Medication Quantity"));
                    //medicineHour.setText(documentSnapshot.get("Medication Hour"));
                    //medicineMin.setText(documentSnapshot.getString("Medication Minute"));
                    medicineShape.setText(documentSnapshot.getString("Medication Shape"));

                    monday.setText(documentSnapshot.getString("1Monday"));
                    tuesday.setText(documentSnapshot.getString("2Tuesday"));
                    wednesday.setText(documentSnapshot.getString("3Wednesday"));
                    thursday.setText(documentSnapshot.getString("4Thursday"));
                    friday.setText(documentSnapshot.getString("5Friday"));
                    saturday.setText(documentSnapshot.getString("6Saturday"));
                    sunday.setText(documentSnapshot.getString("7Sunday"));

                }
            }
        });
    }
    public void Note(List<String> tags){
        this.tags = tags;
    }

    public void ClickMenu(View view){
        //Open Drawer;
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        //Close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout){
        //Close drawer layout
        //Check condition
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            //When drawer is open
            //Close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        //Recreate activity
        recreate();
    }

    public void ClickAdd(View view){
        //Redirect activity to Add
        redirectActivity(this,Add.class);
    }

    public void ClickSettings(View view) {
        //Redirect activity to Settings
        redirectActivity(this, Settings.class);
    }

    public void ClickLogout(View view) {
        //Close app
        logout(this);
        
    }

    public void logout(Activity activity) {
        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Set title
        builder.setTitle("Logout");
        //Set message
        builder.setMessage("Are you sure you want to logout?");
        //Positive yes button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Exit app
                //System.exit(0);
                //Finish activity
                //activity.finishAffinity();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Main.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        });
        //Negative no button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss dialog
                dialog.dismiss();
            }
        });
        //Show dialog
        builder.show();
        }

    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialize Intent
        Intent intent = new Intent(activity,aClass);
        //Set Flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start Activity
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        new MenuInflater(this).inflate(R.menu.option,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.logout):
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(),Login.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}