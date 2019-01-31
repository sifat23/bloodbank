package com.example.namikaze.blooddonate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class DetailsActivity extends AppCompatActivity {


    String group = "";
    private static final int SELECTED_IMAGE = 100;
    int year_x, month_x, day_x;
    static final int Dilog_ID = 0;
    Uri pickedImage;
    String fname;
    String address;
    String number;
    String dateofbirth;
    String gender;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Doners");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    CircleImageView picture;

    EditText inputFirstNmae, inputAddress, inputPhoneNumber;

    RadioGroup radioSexGroup;
    RadioButton radioButton;

    Spinner spinner;

    TextView dateOfBirth, imageSet;

    Button btnContinue;

    CheckBox checkCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final Calendar calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);

        //all resorece file connetion
        dateOfBirth = (TextView) findViewById(R.id.show_dob);
        imageSet = (TextView) findViewById(R.id.set_image_click);

        picture = (CircleImageView) findViewById(R.id.set_image);

        spinner = (Spinner) findViewById(R.id.blood_spinner);

        inputAddress = (EditText) findViewById(R.id.address);
        inputFirstNmae = (EditText) findViewById(R.id.first_name);
        inputPhoneNumber = (EditText) findViewById(R.id.phone_number);

        radioSexGroup = (RadioGroup) findViewById(R.id.radio_group);

        btnContinue = (Button) findViewById(R.id.btn_continue);

        checkCondition = (CheckBox) findViewById(R.id.check_to_conditon);//ends here

        //for blodd group spinner
        String[] groups = new String[]{
                "A+ve",
                "A-ve",
                "B+ve",
                "B-ve",
                "AB+ve",
                "Ab-ve",
                "O+ve",
                "O-ve"
        };

        SpinnerAdapter adapter = new SpinnerAdapter(DetailsActivity.this, android.R.layout.simple_list_item_1);
        adapter.addAll(groups);
        adapter.add("Blood Group...");
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem() == "Blood Group...") {
                    //Do nothing.
                } else {
                    group = spinner.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        //button image
        imageSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECTED_IMAGE);
            }
        });//ends here

        //button date
        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Dilog_ID);
            }
        });//ends here



        //button continue
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int select = radioSexGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(select);
                fname = inputFirstNmae.getText().toString();
                address = inputAddress.getText().toString();
                number = inputPhoneNumber.getText().toString();
                dateofbirth = dateOfBirth.getText().toString();
                gender = (String) radioButton.getText();


                if (TextUtils.isEmpty(fname)) {
                    Toast.makeText(getApplicationContext(), "Enter Your First Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(getApplicationContext(), "Enter Your Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(getApplicationContext(), "Enter Your Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(dateofbirth)) {
                    Toast.makeText(getApplicationContext(), "Put Your Date of Birth", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(group)) {
                    Toast.makeText(getApplicationContext(), "Select Your Blood Group", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!checkCondition.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Please Check Terms and Conditions", Toast.LENGTH_SHORT).show();
                    return;
                }

                Date c = Calendar.getInstance().getTime();

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(c);

                Date date_1 = null, date_2 = null;
                try {
                    date_1 = df.parse(dateofbirth);
                    date_2 = df.parse(formattedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long difference = Math.abs(date_1.getTime() - date_2.getTime());
                long seconds = difference / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;
                long month = days / 30;
                long year = month / 12;

                int realYear = (int) year;
                if (realYear >= 18) {
                    if (pickedImage != null) {
                        StorageReference ref = storageReference.child("images/" + System.currentTimeMillis() + "." + getImageExtantion(pickedImage));
                        ref.putFile(pickedImage)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        String id = mDatabase.push().getKey();
                                        String image = taskSnapshot.getDownloadUrl().toString();
                                        Donner donner = new Donner(id, image, fname, address, number, gender, dateofbirth, group);
                                        mDatabase.child(id).setValue(donner);
                                        Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "No Image file is selected", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(DetailsActivity.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "You cannot be a blood doner", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == Dilog_ID) {
            return new DatePickerDialog(this, dpickerListner, year_x, month_x, day_x);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month + 1;
            day_x = dayOfMonth;

            String dob = String.valueOf(day_x + "/" + month_x + "/" + year_x);
            dateOfBirth.setText(dob);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECTED_IMAGE) {
            pickedImage = data.getData();
            picture.setImageURI(pickedImage);
        }
    }

    private String getImageExtantion(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


}
