package com.libit.wingspayroll.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.libit.wingspayroll.Model.RequestDetailModel;
import com.libit.wingspayroll.R;
import java.util.List;

public class RequestDetailAdapter extends RecyclerView.Adapter<RequestDetailAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<RequestDetailModel> services;
    Context context;

    public RequestDetailAdapter(Context ctx, List<RequestDetailModel> services) {
        this.context = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.services = services;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dailyattendenceview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.Intime.setText(services.get(position).getType());
        holder.Outtime.setText(services.get(position).getDate());
        holder.Viewdetail.setText(services.get(position).getEStatus());



        if(services.get(position).getEStatus().equalsIgnoreCase("Approved")){
            holder.Viewdetail.setTextColor(Color.parseColor("#388E3C"));

        }else if(services.get(position).getEStatus().equalsIgnoreCase("Disapproved")){
            holder.Viewdetail.setTextColor(Color.parseColor("#D32F2F"));
        }else {
            holder.Viewdetail.setTextColor(Color.parseColor("#FFA500"));
        }

        //        holder.Viewdetail.setText("View");
//        holder.Viewdetail.setTextColor(Color.parseColor("#94ccf6"));
//        holder.Viewdetail.setPaintFlags(holder.Viewdetail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);



    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Intime, Outtime, Viewdetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Intime = itemView.findViewById(R.id.Intime);
            Outtime = itemView.findViewById(R.id.Outtime);
            Viewdetail = itemView.findViewById(R.id.Viewdetail);
        }
    }
}
