package com.sp.mm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import android.app.Activity;

import android.content.Intent;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sp.mm.folder.DayViewCheckBox;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;

import android.widget.Spinner;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.Unbinder;


public class Add extends AppCompatActivity {

    private static final String TAG = "TAG" ;
    //Initialize variables
    DrawerLayout drawerLayout;
    TextView tvMedicineTime;
    int hour, minute;


    private boolean[] dayOfWeekList = new boolean[7];
    private View rootView;
    private List<String> doseUnitList;
    private String doseUnit;
    Unbinder unbinder;
    Button btnSave;

    FirebaseFirestore fstore;
    FirebaseAuth fAuth;
    String userID;
    String medicationID;

    List<String> tags;

    Spinner spinnerDropDownView;
    String[] spinnerValueHoldValue = {"adhesive(s)", "capsule(s)", "cachet(s)", "cream(s)", "dragee(s)", "emulsion(s)", "pack(s)", "gel(s)"};

    private CheckBox Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;


    @BindView(R.id.edit_med_name)
    EditText medName;

    /*@BindView(R.id.every_day)
    AppCompatCheckBox everyDay;*/

    @BindView(R.id.dv_sunday)
    DayViewCheckBox dvSunday;

    @BindView(R.id.dv_monday)
    DayViewCheckBox dvMonday;

    @BindView(R.id.dv_tuesday)
    DayViewCheckBox dvTuesday;

    @BindView(R.id.dv_wednesday)
    DayViewCheckBox dvWednesday;

    @BindView(R.id.dv_thursday)
    DayViewCheckBox dvThursday;

    @BindView(R.id.dv_friday)
    DayViewCheckBox dvFriday;

    @BindView(R.id.dv_saturday)
    DayViewCheckBox dvSaturday;

    @BindView(R.id.checkbox_layout)
    LinearLayout checkboxLayout;

    @BindView(R.id.tv_dose_quantity)
    EditText doseQuantity;

    @BindView(R.id.spinner_dose_units)
    AppCompatSpinner spinnerDoseUnits;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        btnSave = findViewById(R.id.btnSave);
        medName = findViewById(R.id.edit_med_name);
        tvMedicineTime = findViewById(R.id.tv_medicine_time);
        doseQuantity = findViewById(R.id.tv_dose_quantity);

        Monday = (CheckBox) findViewById(R.id.dv_monday);
        Tuesday = (CheckBox) findViewById(R.id.dv_tuesday);
        Wednesday = (CheckBox) findViewById(R.id.dv_wednesday);
        Thursday = (CheckBox) findViewById(R.id.dv_thursday);
        Friday = (CheckBox) findViewById(R.id.dv_friday);
        Saturday = (CheckBox) findViewById(R.id.dv_saturday);
        Sunday = (CheckBox) findViewById(R.id.dv_sunday);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();



        spinnerDropDownView =(Spinner)findViewById(R.id.spinner_dose_units);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Add.this, android.R.layout.simple_list_item_1, spinnerValueHoldValue);
        spinnerDropDownView.setAdapter(adapter);

        spinnerDropDownView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //Toast.makeText(Add.this, spinnerDropDownView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String medNameString = medName.getText().toString();
                String medTimeString = tvMedicineTime.getText().toString();
                String quantityString = doseQuantity.getText().toString();
                String hourString = String.valueOf(hour);
                String minString = String.valueOf(minute);

                userID = fAuth.getCurrentUser().getUid();
                medicationID = fAuth.getCurrentUser().getUid();

                StringBuffer day1 = new StringBuffer();
                StringBuffer day2 = new StringBuffer();
                StringBuffer day3 = new StringBuffer();
                StringBuffer day4 = new StringBuffer();
                StringBuffer day5 = new StringBuffer();
                StringBuffer day6 = new StringBuffer();
                StringBuffer day7 = new StringBuffer();

                day1.append(Monday.isChecked());
                day2.append(Tuesday.isChecked());
                day3.append(Wednesday.isChecked());
                day4.append(Thursday.isChecked());
                day5.append(Friday.isChecked());
                day6.append(Saturday.isChecked());
                day7.append(Sunday.isChecked());

                Map<String, Object> data = new HashMap<>();
                data.put("1Monday", day1.toString());    //Add into firestore
                data.put("2Tuesday", day2.toString());    //Add into firestore
                data.put("3Wednesday", day3.toString());    //Add into firestore
                data.put("4Thursday", day4.toString());    //Add into firestore
                data.put("5Friday", day5.toString());    //Add into firestore
                data.put("6Saturday", day6.toString());    //Add into firestore
                data.put("7Sunday", day7.toString());    //Add into firestore

                data.put("Medication Name", medNameString);    //Add into firestore
                data.put("Medication Hour", hourString);    //Add into firestore
                data.put("Medication Minute", minString);    //Add into firestore
                data.put("Medication Time", medTimeString);
                data.put("Medication Quantity", quantityString);    //Add into firestore
                data.put("Medication Shape", spinnerDropDownView.getSelectedItem().toString());

                fstore.collection("users").document(userID).collection("medications").document("med1")
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });

                Intent add;
                add = new Intent(Add.this, Main.class);
                startActivity(add);
                finish();
            }
        });

        tvMedicineTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        Add.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                                //Initialize hour and minute
                                hour = hourOfDay;
                                minute = minutes;
                                //Store hour and minute in string
                                String time = hour + ":" + minute;
                                //Initialize the 24hr time format
                                SimpleDateFormat f24hours = new SimpleDateFormat("HH:mm");
                                try {
                                    Date date = f24hours.parse(time);
                                    //Initialize 12 hours
                                    SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                                    //Set select time on text view
                                    tvMedicineTime.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }}, 12, 0, true
                );
                //Set transparent background
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //Displayed previous selected time
                timePickerDialog.updateTime(hour, minute);
                //Show dialog
                timePickerDialog.show();
            }
        });
    }

    /*@OnClick({R.id.every_day, R.id.dv_monday, R.id.dv_tuesday, R.id.dv_wednesday,
            R.id.dv_thursday, R.id.dv_friday, R.id.dv_saturday, R.id.dv_sunday})
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();


        Checking which checkbox was clicked
        switch (view.getId()) {
            case R.id.dv_monday:
                if (checked) {
                    dayOfWeekList[1] = true;
                    Log.d(TAG, String.valueOf(dayOfWeekList[1]));


                } else {
                    dayOfWeekList[1] = false;
                    //everyDay.setChecked(false);
                }
                break;
            case R.id.dv_tuesday:
                if (checked) {
                    dayOfWeekList[2] = true;
                    days.add("Tuesday");

                } else {
                    dayOfWeekList[2] = false;

                }
                break;
            case R.id.dv_wednesday:
                if (checked) {
                    dayOfWeekList[3] = true;


                } else {
                    dayOfWeekList[3] = false;

                }
                break;
            case R.id.dv_thursday:
                if (checked) {
                    dayOfWeekList[4] = true;


                } else {
                    dayOfWeekList[4] = false;

                }
                break;
            case R.id.dv_friday:
                if (checked) {
                    dayOfWeekList[5] = true;

                } else {
                    dayOfWeekList[5] = false;

                }
                break;
            case R.id.dv_saturday:
                if (checked) {
                    dayOfWeekList[6] = true;

                } else {
                    dayOfWeekList[6] = false;

                }
                break;
            case R.id.dv_sunday:
                if (checked) {
                    dayOfWeekList[0] = true;

                } else {
                    dayOfWeekList[0] = false;

                }
                break;
            /*case R.id.every_day:
                if (checked) {
                    dayOfWeekList[0] = true;
                    dayOfWeekList[1] = true;
                    dayOfWeekList[2] = true;
                    dayOfWeekList[3] = true;
                    dayOfWeekList[4] = true;
                    dayOfWeekList[5] = true;
                    dayOfWeekList[6] = true;
                } else {
                    everyDay.setChecked(false);
                }
                break;

        }




    private View.OnClickListener onEvery = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            everyDay =  findViewById(R.id.every_day);

            CheckBox everyday = (CheckBox) v;

            //Check the current text of Select Button
            if (everDay.getText().toString().equals(getResources().getString(R.string.every_day))) {

                //If Text is Select All then loop to all array List items and check all of them
                for (int i = 0; i < arrayList.size(); i++)
                    adapter.checkCheckBox(i, true);

                //After checking all items change button text
                selectButton.setText(getResources().getString(R.string.every_day));
            } else {
                //If button text is Deselect All remove check from all items
                adapter.removeSelection();

                //After checking all items change button text
                selectButton.setText(getResources().getString(R.string.every_day));
            }
        }
    };*/


    public void Note(List<String> tags){
        this.tags = tags;
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
        recreate();
    }

    public void ClickSettings(View view){
        //Redirect activity to about us
        Main.redirectActivity(this, Settings.class);
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
                Intent intent = new Intent(Add.this, Login.class);
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
