package com.sp.mm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class Settings extends AppCompatActivity {
    //Initialize variable
    DrawerLayout drawerLayout;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    String userID, profileID;
    Button btnUpdate;

    TextView Name, Email, Password;

    private static final String TAG = "TAG" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = fAuth.getCurrentUser().getUid();


        //Assign variable
        drawerLayout= findViewById(R.id.drawer_layout);
        Name = findViewById(R.id.userName1);
        Name = findViewById(R.id.userName);
        Email = findViewById(R.id.userEmail);
        Password = findViewById(R.id.userPassword);
        btnUpdate = findViewById(R.id.btnUpdate);


        DocumentReference documentReference = fstore.collection("users").document(userID).collection("profile").document("profileID");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    if(fAuth.getCurrentUser() != null) {
                        Name.setText(documentSnapshot.getString("Name"));
                        Email.setText(documentSnapshot.getString("Email"));
                        Password.setText(documentSnapshot.getString("Password"));
                    }
            }
        });

        /*btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               final EditText resetPassword = new EditText(v.getContext());
               final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());

               passwordResetDialog.setTitle("Reset password?");
               passwordResetDialog.setMessage("Enter a new password > 6 Characters long");
               passwordResetDialog.setView(resetPassword);

               passwordResetDialog.setPositiveButton("Yes",onClick(dialog, which){
                   String newPassword = resetPassword.getText().toString();
                   user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>(){
                       @Override
                       public void onSuccess(Void avoid) {
                           Toast.makeText(Settings.this,"Password resetted",Toast.LENGTH_LONG).show();
                       }
                   }).addOnFailureListener(new OnFailureListener(){
                       @Override
                       public void  onFailure(@NonNull Exception e){
                           Toast.makeText(Settings.this,"Password not resetted", Toast.LENGTH_LONG).show();
                       }
                   });

                }
            }
        });

        /*btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DocumentReference rootRef = fstore.collection("users").document(userID)
                        .collection("profile").document("profileID");

                rootRef
                        .update("Name", )

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
            }
        });*/
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