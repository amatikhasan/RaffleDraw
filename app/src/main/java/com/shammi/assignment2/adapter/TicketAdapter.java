package com.shammi.assignment2.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shammi.assignment2.R;
import com.shammi.assignment2.activity.TicketActivity;
import com.shammi.assignment2.model.TicketModel;

import java.util.ArrayList;


public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {
    Context context;
    ArrayList<TicketModel> data;
    private ArrayList<TicketModel> arrayList;
    public static String status;

    public TicketAdapter(Context context, ArrayList<TicketModel> data,String status) {
        this.context = context;
        this.data = data;
        this.arrayList = new ArrayList<TicketModel>();
        this.arrayList.addAll(data);

        Log.d("TAG", "TicketAdapter: "+status);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ticket_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TicketModel obj = data.get(position);

        holder.ticketNumber.setText(String.valueOf(obj.getNumber()));
        holder.customerName.setText(obj.getCustomerName());
        holder.raffleTitle.setText(obj.getRaffleTitle());
        String txtTicketPrice="$ "+obj.getPrice();
        holder.ticketPrice.setText(txtTicketPrice);
        holder.purchasedAt.setText(obj.getPurchasedAt());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                Intent intent = new Intent(context, TicketActivity.class);
                intent.putExtra("ticketNumber", obj.getId());
                intent.putExtra("raffleID", obj.getRaffleID());
                intent.putExtra("raffleTitle", obj.getRaffleTitle());
                intent.putExtra("ticketPrice", obj.getPrice());
                intent.putExtra("purchasedAt", obj.getPurchasedAt());
                intent.putExtra("customerID", obj.getCustomerId());
                intent.putExtra("customerName", obj.getCustomerName());
                intent.putExtra("customerContact", obj.getCustomerContact());

                intent.putExtra("status", status);

                context.startActivity(intent);
//                ((Activity) context).finish();
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView ticketNumber, customerName,ticketPrice,raffleTitle,purchasedAt,draw;
        CardView card;


        public ViewHolder(View itemView) {
            super(itemView);
            ticketNumber = itemView.findViewById(R.id.tvTicketNumber);
            customerName = itemView.findViewById(R.id.tvCustomerName);
            raffleTitle = itemView.findViewById(R.id.tvRaffleName);
            ticketPrice = itemView.findViewById(R.id.tvTicketPrice);
            purchasedAt = itemView.findViewById(R.id.tvPurchasedAt);
            draw = itemView.findViewById(R.id.tvDraw);


            card = itemView.findViewById(R.id.cardTicket);
        }

    }

}
