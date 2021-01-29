package com.sp.mm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {
    //Initialize variable
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        //Assign variable
        drawerLayout= findViewById(R.id.drawer_layout);
    }

    public void ClickMenu(View view){
        //Open drawer
        Main.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        //Close drawer
        Main.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view){
        //Redirect activity to home
        Main.redirectActivity(this,Main.class);
    }

    public void ClickAdd(View view){
        //Redirect activity to add
        Main.redirectActivity(this,Add.class);
    }

    public void ClickSettings(View view){
        //Recreate activity
        recreate();
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
                Intent intent = new Intent(Settings.this, Login.class);
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

    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        Main.closeDrawer(drawerLayout);
    }
}