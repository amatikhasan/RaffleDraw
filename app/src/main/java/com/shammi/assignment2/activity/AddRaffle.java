package com.shammi.assignment2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shammi.assignment2.R;
import com.shammi.assignment2.db.DBHelper;
import com.shammi.assignment2.model.RaffleModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddRaffle extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText etTitle,etDesc,etTicketPrice,etTicketLimit,etWinnerLimit,etDate,etTime;
    private TextView btnDate,btnTime;
    private ImageView ivImage;
    private Spinner spType;
    private int raffleID,ticketPrice,ticketSold=0,ticketLimit,winnerLimit;
    private String title,desc,date,time,type="Normal",start,draw;
    private boolean isUpdate=false;
    private ArrayAdapter<String> spinnerAdapter;

    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;

    private static final int IMAGE_REQUEST = 1;
    private Uri filePath;
    private byte[] imageByteArray;

    private Bundle extras;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_raffle);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Raffle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(getApplicationContext());

        etTitle=findViewById(R.id.etTitle);
        etDesc=findViewById(R.id.etDescription);
        etTicketPrice=findViewById(R.id.etTicketPrice);
        etTicketLimit=findViewById(R.id.etTicketLimit);
        etWinnerLimit=findViewById(R.id.etWinnerLimit);
        etDate=findViewById(R.id.etDrawDate);
        etTime=findViewById(R.id.etDrawTime);
        btnDate=findViewById(R.id.btnDate);
        btnTime=findViewById(R.id.btnTime);
        spType=findViewById(R.id.spType);
        ivImage=findViewById(R.id.ivRaffleImage);

        List<String> typeArray=new ArrayList<>();
        typeArray.add("Normal");
        typeArray.add("Marginal");

        spinnerAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,typeArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(spinnerAdapter);

        //get Intent Data
        extras = getIntent().getExtras();
        if (extras != null) {
            raffleID = extras.getInt("raffleID");
            title = extras.getString("title");
            desc = extras.getString("desc");
            type = extras.getString("type");
            draw = extras.getString("draw");
            ticketPrice = extras.getInt("ticketPrice");
            ticketSold = extras.getInt("ticketSold");
            ticketLimit = extras.getInt("ticketLimit");
            winnerLimit = extras.getInt("winnerLimit");
            isUpdate=true;

            //initialize data
            setData();
        }

        int position=spinnerAdapter.getPosition(type);
        spType.setSelection(position);

        //type spinner onclickListener
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = spType.getSelectedItem().toString();

                spType.setSelection(position);

                if (type.equals("Normal")){
                    etWinnerLimit.setEnabled(true);
                    if (winnerLimit>0)
                    etWinnerLimit.setText(String.valueOf(winnerLimit));
                    else
                        etWinnerLimit.getText().clear();
                }
                else {
                    etWinnerLimit.setEnabled(false);
                    etWinnerLimit.setText("1");
                }

                Log.d("Check", "Spinner: " + type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //disable input
        etDate.setInputType(InputType.TYPE_NULL);
        etTime.setInputType(InputType.TYPE_NULL);

        //date button click
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH)+1;
                int year=calendar.get(Calendar.YEAR);

                //date picker dialogue
                datePicker=new DatePickerDialog(AddRaffle.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date=dayOfMonth+"/"+month+"/"+year;
                        etDate.setText(date);
                    }
                },year,month,day);

                datePicker.show();
            }
        });

        //time button click
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int hour=calendar.get(Calendar.HOUR_OF_DAY);
                int minute=calendar.get(Calendar.MINUTE);

                //date picker dialogue
                timePicker=new TimePickerDialog(AddRaffle.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        if (minute < 10) {
                            time = formatTime(String.valueOf(hourOfDay) + ":" + "0" + String.valueOf(minute));

                        } else {
                            time = formatTime(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
                        }

                        etTime.setText(time);
                    }
                },hour,minute,false);

                timePicker.show();
            }
        });

    }

    public void setData(){
        etTitle.setText(title);
        etDesc.setText(desc);
        int position=spinnerAdapter.getPosition(type);
        spType.setSelection(position);
        etTicketPrice.setText(String.valueOf(ticketPrice));
        etTicketLimit.setText(String.valueOf(ticketLimit));
        etWinnerLimit.setText(String.valueOf(winnerLimit));

        String[] drawParts=draw.split(" ");
        String date=drawParts[0];
        String time=drawParts[1]+" "+drawParts[2];

        etDate.setText(date);
        etTime.setText(time);

        Log.d("AddRaffle", "data: " + winnerLimit);

        imageByteArray= dbHelper.getImageByte(raffleID);

        if (imageByteArray != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            ivImage.setImageBitmap(bitmap);
        }

        //input restriction for updating raffle
        if (isUpdate&&ticketSold>0){
            spType.setEnabled(false);
            etTicketPrice.setEnabled(false);
        }

    }

    //format time with AM,PM for button
    public String formatTime(String time) {
        String format, formattedTime, minutes;
        String[] dateParts = time.split(":");
        int hour = Integer.parseInt(dateParts[0]);
        int minute = Integer.parseInt(dateParts[1]);
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        if (minute < 10)
            minutes = "0" + minute;
        else
            minutes = String.valueOf(minute);
        formattedTime = hour + ":" + minutes + " " + format;
//        formattedTime = hour + ":" + minutes;

        return formattedTime;
    }

    private void addRaffle() {
        title=etTitle.getText().toString().trim();
        desc=etDesc.getText().toString().trim();
        date=etDate.getText().toString().trim();
        time=etTime.getText().toString().trim();
        draw=date+" "+time;
        String txtPrice=etTicketPrice.getText().toString().trim();
        String txtLimit=etTicketLimit.getText().toString().trim();
        String txtWinnerLimit=etWinnerLimit.getText().toString().trim();


        Log.d("TAG", "addRaffle: "+ticketPrice+" "+ticketLimit+" "+winnerLimit);
        if (title.isEmpty()||desc.isEmpty()||txtPrice.isEmpty()||txtLimit.isEmpty()||txtWinnerLimit.isEmpty()||date.isEmpty()||time.isEmpty()) {
            Toast.makeText(AddRaffle.this, "Required fields are missing!", Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                ticketPrice=Integer.parseInt(txtPrice);
                ticketLimit=Integer.parseInt(txtLimit);
                winnerLimit=Integer.parseInt(txtWinnerLimit);
            }
            catch (NumberFormatException e){};

            RaffleModel raffleModel = new RaffleModel(title, desc, imageByteArray, type, ticketPrice, ticketLimit, winnerLimit, start, draw);
            DBHelper dbHelper = new DBHelper(this);
            dbHelper.insertRaffle(raffleModel);

            Toast.makeText(this, "Raffle Created!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        }


    }

    public void editRaffle(){
        title=etTitle.getText().toString().trim();
        desc=etDesc.getText().toString().trim();
        ticketPrice=Integer.parseInt(etTicketPrice.getText().toString());
        ticketLimit=Integer.parseInt(etTicketLimit.getText().toString());
        winnerLimit=Integer.parseInt(etWinnerLimit.getText().toString());
        date=etDate.getText().toString().trim();
        time=etTime.getText().toString().trim();
        draw=date+" "+time;
        String txtPrice=etTicketPrice.getText().toString().trim();
        String txtLimit=etTicketLimit.getText().toString().trim();
        String txtWinnerLimit=etWinnerLimit.getText().toString().trim();


        Log.d("TAG", "addRaffle: "+ticketPrice+" "+ticketLimit+" "+winnerLimit);
        if (title.isEmpty()||desc.isEmpty()||txtPrice.isEmpty()||txtLimit.isEmpty()||txtWinnerLimit.isEmpty()||date.isEmpty()||time.isEmpty()) {
            Toast.makeText(AddRaffle.this, "Required fields are missing!", Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                ticketPrice=Integer.parseInt(txtPrice);
                ticketLimit=Integer.parseInt(txtLimit);
                winnerLimit=Integer.parseInt(txtWinnerLimit);
            }
            catch (NumberFormatException e){};
            RaffleModel raffleModel = new RaffleModel(raffleID,title, desc, imageByteArray, type, ticketPrice,0, ticketLimit, winnerLimit, start, draw);
            DBHelper dbHelper = new DBHelper(this);
            dbHelper.updateRaffle(raffleModel);

            Toast.makeText(this, "Raffle Updated!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        }
    }

    public void deleteRaffle(){
        if (ticketSold==0) {
            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete this Raffle?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int ids) {

                            DBHelper dbHelper = new DBHelper(getApplicationContext());

                            dbHelper.deleteRaffle(raffleID);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finishAffinity();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            final android.app.AlertDialog alert = builder.create();
            alert.show();
        }
        else
            Toast.makeText(this, "Sorry! you can't delete this raffle", Toast.LENGTH_SHORT).show();
    }

    //add image button click
    public void addImage(View view){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // special intent for Samsung file manager
        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        // if you want any file type, you can skip next line
        sIntent.putExtra("CONTENT_TYPE", "image/*");
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent chooserIntent;
        if (getPackageManager().resolveActivity(sIntent, 0) != null) {
            // it is device with samsung file manager
            chooserIntent = Intent.createChooser(sIntent, "Select File");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
        } else {
            chooserIntent = Intent.createChooser(intent, "Select File");
        }
        startActivityForResult(chooserIntent, IMAGE_REQUEST);

    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivImage.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageByteArray = stream.toByteArray();
                // bitmap.recycle();
                stream.close();

                Log.d("check", "onActivityResult: " + ivImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //file permission
    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
            int permissionCheck = AddRaffle.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += AddRaffle.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
            }
        } else {
            Log.d("Check", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }


    //For Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (isUpdate) getMenuInflater().inflate(R.menu.menu_update, menu);
        if (!isUpdate) {
            getMenuInflater().inflate(R.menu.menu_add, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //for toolbar arrow
            case android.R.id.home:
                finish();
                break;

            case R.id.menuSave:
                addRaffle();
                break;

            case R.id.menuEdit:
                editRaffle();
              break;

            case R.id.menuDelete:
                deleteRaffle();
                break;

        }

        return true;
    }


}
