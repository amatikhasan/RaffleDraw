package com.shammi.assignment2.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shammi.assignment2.R;
import com.shammi.assignment2.adapter.RaffleAdapter;
import com.shammi.assignment2.adapter.TicketAdapter;
import com.shammi.assignment2.db.DBHelper;
import com.shammi.assignment2.model.RaffleModel;
import com.shammi.assignment2.model.TicketModel;
import com.shammi.assignment2.model.WinnerModel;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class RaffleActivity extends AppCompatActivity{

    private Toolbar toolbar;

    private TextView tvTitle, tvDesc, tvType,tvStart,tvDraw,btnSell,btnDraw,btnWinner,btnEdit,tvTicketSold,tvTicketLimit;
    private ImageView ivImage;
    private RecyclerView rvTicket;
    private TextView tvEmptyList;

    private byte[] imageByteArray;
    private  DBHelper dbHelper;
    private Bundle extras;
    private ArrayList<TicketModel> ticketList;
    private ArrayList<Integer> listWinner;
    private TicketAdapter adapter;

    private int raffleID,ticketPrice,ticketSold,ticketLimit,winnerLimit;
    private String raffleTitle, desc, type,start,draw,status="running";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raffle);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Raffle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(getApplicationContext());
        listWinner=new ArrayList<>();

        tvTitle=findViewById(R.id.tvTitle);
        tvDesc=findViewById(R.id.tvDescription);
        tvType=findViewById(R.id.tvType);
        tvTicketSold=findViewById(R.id.tvTicketSold);
        tvTicketLimit=findViewById(R.id.tvTicketLimit);
        tvStart=findViewById(R.id.tvStart);
        tvDraw=findViewById(R.id.tvDraw);
        btnSell=findViewById(R.id.btnSell);
        btnWinner=findViewById(R.id.btnWinner);
        btnDraw=findViewById(R.id.btnDraw);
        btnEdit=findViewById(R.id.btnEdit);
        ivImage=findViewById(R.id.ivImage);
        rvTicket=findViewById(R.id.rvTicket);
        ticketList=new ArrayList<>();
        tvEmptyList=findViewById(R.id.tvEmptyList);

        rvTicket.setHasFixedSize(true);
        rvTicket.setLayoutManager(new LinearLayoutManager(this));

        //get Intent Data
        extras = getIntent().getExtras();
        if (extras != null) {
            raffleID = extras.getInt("raffleID");
            status = extras.getString("status");

            setData();
        }

        ticketList=dbHelper.getTicketList(raffleID);

        if(ticketList.size()>0) {
            adapter = new TicketAdapter(this, ticketList,status);
            rvTicket.setAdapter(adapter);

            tvTicketSold.setText(String.valueOf(ticketList.size()));
        }
        else{
            rvTicket.setVisibility(View.GONE);
            tvEmptyList.setVisibility(View.VISIBLE);
        }

    }

    public void setData(){


        List<RaffleModel> raffleObj=new ArrayList<>();
        raffleObj=dbHelper.getRaffleDetails(raffleID);

        raffleTitle=raffleObj.get(0).getTitle();
        desc=raffleObj.get(0).getDesc();
        type=raffleObj.get(0).getType();
        draw=raffleObj.get(0).getDraw();
        ticketPrice=raffleObj.get(0).getTicketPrice();
        ticketLimit=raffleObj.get(0).getTicketLimit();
        winnerLimit=raffleObj.get(0).getWinnerLimit();
        ticketSold=dbHelper.getTicketSold(raffleID);

        tvTicketSold.setText(String.valueOf(ticketSold));
        tvTitle.setText(raffleTitle);
        tvDesc.setText(desc);
        tvType.setText(type);
        tvTicketLimit.setText(String.valueOf(ticketLimit));
        tvDraw.setText(draw);

        if (raffleObj.get(0).getStart()==null)
            tvStart.setText("To Be Started");
        else
            tvStart.setText(raffleObj.get(0).getStart());

//         imageByteArray= dbHelper.getImageByte(raffleID);
        imageByteArray= raffleObj.get(0).getImage();


        imageByteArray= dbHelper.getImageByte(raffleID);
        if (imageByteArray != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            ivImage.setImageBitmap(bitmap);
        }

        if (status!=null&&status.equals("ended")){
            btnSell.setVisibility(View.INVISIBLE);
            btnEdit.setVisibility(View.INVISIBLE);
            btnDraw.setVisibility(View.INVISIBLE);
            btnWinner.setVisibility(View.VISIBLE);
        }

        Log.d("raffleactivity", "setData: "+raffleID+" "+ticketSold+" "+ticketLimit+" "+draw);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    public void sellTicket(View view){

        if (ticketSold<ticketLimit) {

            Intent intent = new Intent(this, AddTicket.class);
            intent.putExtra("ticketPrice", ticketPrice);
            intent.putExtra("ticketLimit", ticketLimit);
            intent.putExtra("ticketSold", ticketSold);
            intent.putExtra("raffleID", raffleID);
            intent.putExtra("raffleTitle", raffleTitle);
            intent.putExtra("type", type);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "All Ticket Sold!", Toast.LENGTH_SHORT).show();
    }

    public void editRaffle(View view){
        Intent intent = new Intent(this, AddRaffle.class);
        intent.putExtra("raffleID", raffleID);
        intent.putExtra("title", raffleTitle);
        intent.putExtra("desc", desc);
        intent.putExtra("ticketPrice", ticketPrice);
        intent.putExtra("ticketLimit", ticketLimit);
        intent.putExtra("ticketSold", ticketSold);
        intent.putExtra("winnerLimit", winnerLimit);
        intent.putExtra("type", type);
        intent.putExtra("draw", draw);
        startActivity(intent);
    }

    public void draw(final View view){

        if (ticketSold>=winnerLimit) {

            //if the raffle type is normal
            if (type.equals("Normal")) {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to draw this Raffle?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int ids) {

                                ArrayList<Integer> listTicketNumber = new ArrayList<>();
                                Random random = new Random();

                                //get all sold ticket number
                                listTicketNumber = dbHelper.getAllTicketNumber(raffleID);

                                //generate random winner from sold ticket list
                                for (int i = 0; i < winnerLimit; i++) {
                                    int randomIndex = random.nextInt(listTicketNumber.size());
                                    listWinner.add(listTicketNumber.get(randomIndex));

                                    WinnerModel winnerModel = new WinnerModel(raffleID, listTicketNumber.get(randomIndex));
                                    dbHelper.insertWinner(winnerModel);

                                    listTicketNumber.remove(randomIndex);
                                }

                                dbHelper.updateRaffleDraw(raffleID, getDate());

                                showWinner(view);
                                Log.d("Raffle winner", "draw: " + winnerLimit + " " + listWinner.toString());

                                btnDraw.setVisibility(View.INVISIBLE);
                                btnEdit.setVisibility(View.INVISIBLE);
                                btnSell.setVisibility(View.INVISIBLE);
                                btnWinner.setVisibility(View.VISIBLE);

                                status="ended";
                                TicketAdapter.status="ended";

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
            else {
                //if the raffle type is normal
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to draw this Raffle?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int ids) {

                                //custom dialog view layout for selecting winner
                                LayoutInflater layoutInflater = LayoutInflater.from(RaffleActivity.this);
                                final View layout = layoutInflater.inflate(R.layout.marginl_winner_layout, null);

                                AlertDialog.Builder builder = new AlertDialog.Builder(RaffleActivity.this);
                                builder.setView(layout);

                                final EditText etTicketNumber;
                                etTicketNumber=layout.findViewById(R.id.etTicketNumber);

                                builder.setCancelable(false)
                                        .setPositiveButton(android.R.string.ok, null)
                                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });

                                final AlertDialog alertDialog = builder.create();

                                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(final DialogInterface dialog) {
                                        Button okButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);

                                        okButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                String txtTicketNumber=etTicketNumber.getText().toString();

                                                if (txtTicketNumber.isEmpty())
                                                    Toast.makeText(RaffleActivity.this, "Please enter winner ticket Number!", Toast.LENGTH_SHORT).show();
                                                else {

                                                    //manually enter winner ticket number for marginal draw

                                                    int ticketNumber=Integer.parseInt(txtTicketNumber);
                                                    listWinner.add(ticketNumber);

                                                    WinnerModel winnerModel = new WinnerModel(raffleID, ticketNumber);
                                                    dbHelper.insertWinner(winnerModel);

                                                    dbHelper.updateRaffleDraw(raffleID, getDate());

                                                    showWinner(view);
                                                    Log.d("Raffle winner", "draw: " + winnerLimit + " " + listWinner.toString());

                                                    btnDraw.setVisibility(View.INVISIBLE);
                                                    btnEdit.setVisibility(View.INVISIBLE);
                                                    btnSell.setVisibility(View.INVISIBLE);
                                                    btnWinner.setVisibility(View.VISIBLE);

                                                    status="ended";
                                                    TicketAdapter.status="ended";

                                                    dialog.cancel();
                                                }
                                            }
                                        });

                                    };
                                });

                                alertDialog.show();

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
        }
        else
            Toast.makeText(this, "Not enough ticket sold!", Toast.LENGTH_SHORT).show();

    }

    public void showWinner(View view){

//        //checking if the raffle is running or ended, if the raffle is ended get winner from database
//       if (status!=null&&status.equals("ended"))
           listWinner=dbHelper.getWinner(raffleID);


        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View layout = layoutInflater.inflate(R.layout.winner_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);

        TextView winner;

        winner=layout.findViewById(R.id.tvWinner);

        for (int i=0;i<listWinner.size();i++){
            String txtWinner="Winner "+(i+1)+": Ticket "+listWinner.get(i).toString()+" \n";
            winner.append(txtWinner);
        }


        builder.setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    //get today date
    public String getDate(){
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String date = day + "/" + month + "/" + year;
        String time,dateTime;
        if (minute < 10) {
            time = formatTime(String.valueOf(hour) + ":" + "0" + String.valueOf(minute));
        } else {
            time = formatTime(String.valueOf(hour) + ":" + String.valueOf(minute));
        }

        dateTime = date + " " + time;

        return dateTime;
    }

    //formate time with AM,PM for button
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

        return formattedTime;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //for toolbar arrow
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("type", "running");
            startActivity(intent);
            finish();
        }

        return true;
    }
}
