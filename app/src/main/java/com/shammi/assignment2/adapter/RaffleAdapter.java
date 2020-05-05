package com.shammi.assignment2.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shammi.assignment2.R;
import com.shammi.assignment2.activity.RaffleActivity;
import com.shammi.assignment2.model.RaffleModel;

import java.util.ArrayList;


public class RaffleAdapter extends RecyclerView.Adapter<RaffleAdapter.ViewHolder> {
    Context context;
    ArrayList<RaffleModel> data;
    private ArrayList<RaffleModel> arrayList;
    private String status;

    public RaffleAdapter(Context context, ArrayList<RaffleModel> data, String status) {
        this.context = context;
        this.data = data;
        this.status = status;
        this.arrayList = new ArrayList<RaffleModel>();
        this.arrayList.addAll(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.raffle_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RaffleModel obj = data.get(position);

        holder.title.setText(obj.getTitle());
        holder.desc.setText(obj.getDesc());
        holder.type.setText(obj.getType());

        if (obj.getStart() == null)
            holder.start.setText("To Be Started");
        else
            holder.start.setText(obj.getStart());

        holder.draw.setText(obj.getDraw());

//        if (status!=null&&status.equals("running")) {
//            holder.ticketSold.setText(String.valueOf(obj.getTicketSold()));
//            holder.ticketLimit.setText(String.valueOf(obj.getTicketLimit()));
//        }
//        else{
//            holder.llTicket.setVisibility(View.INVISIBLE);
//            holder.ticketLimit.setText(String.valueOf(obj.getTicketLimit()));
//        }
        holder.llTicket.setVisibility(View.INVISIBLE);
        holder.ticketLimit.setText(String.valueOf(obj.getTicketLimit()));


        if (obj.getImage() != null) {
            byte[] decodedByte = obj.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
            holder.image.setImageBitmap(bitmap);
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                Intent intent = new Intent(context, RaffleActivity.class);
                intent.putExtra("raffleID", obj.getId());
                intent.putExtra("status", status);

                context.startActivity(intent);
//                ((Activity) context).finish();
//
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, desc, type, start, draw, ticketSold, ticketLimit;
        ImageView image;
        LinearLayout llTicket;
        CardView card;


        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            desc = itemView.findViewById(R.id.tvDescription);
            type = itemView.findViewById(R.id.tvType);
            start = itemView.findViewById(R.id.tvStart);
            draw = itemView.findViewById(R.id.tvDraw);
            image = itemView.findViewById(R.id.ivImage);
            ticketSold = itemView.findViewById(R.id.tvTicketSold);
            ticketLimit = itemView.findViewById(R.id.tvTicketLimit);
            llTicket = itemView.findViewById(R.id.llTicket);

            card = itemView.findViewById(R.id.cardRaffle);
        }

    }


}
