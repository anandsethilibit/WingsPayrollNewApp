package com.libit.wingspayroll.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.libit.wingspayroll.Admin.MobAttDetailViewActivity;
import com.libit.wingspayroll.Model.MobileAttendanceModel;
import com.libit.wingspayroll.R;

import java.util.List;

public class MobileAttendanceAdapter extends RecyclerView.Adapter<MobileAttendanceAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<MobileAttendanceModel> services;
    Context context;

    public MobileAttendanceAdapter(Context ctx, List<MobileAttendanceModel> services) {
        this.context = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.services = services;

    }

    @NonNull
    @Override
    public MobileAttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mobileattendancelist_layout, parent, false);
        return new MobileAttendanceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MobileAttendanceAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.Name.setText(services.get(position).getName());
        holder.Date.setText(services.get(position).getDate());
//        holder.Day.setText(services.get(position).getDay());
//        holder.Intime.setText(services.get(position).getIntime());
//        holder.OutTime.setText(services.get(position).getOutTime());
//        holder.Duration.setText(services.get(position).getDuration());
//        holder.Status.setText(services.get(position).getStatus());
//        holder.viewdetail.setText("View");
//        holder.viewdetail.setTextColor(Color.parseColor("#2195F2"));
//        holder.viewdetail.setPaintFlags(holder.viewdetail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        holder.Viewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MobAttDetailViewActivity.class);
                intent.putExtra("MID", (services.get(position).getId()));
                intent.putExtra("MEmpName", (services.get(position).getName()));
                intent.putExtra("MDate", (services.get(position).getDate()));
                intent.putExtra("MDay", (services.get(position).getDays()));
                intent.putExtra("MRequestDate", (services.get(position).getEmpCode()));
                intent.putExtra("MAddress", (services.get(position).getInAddress()));
                intent.putExtra("MInTime", (services.get(position).getInTime()));
                intent.putExtra("MOutTime", (services.get(position).getOutTime()));
                intent.putExtra("MImage", (services.get(position).getAtten_image()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Name,Date;
        Button Viewdetails;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.txt_Mname);
            Date = itemView.findViewById(R.id.txt_MDate);
            Viewdetails = itemView.findViewById(R.id.btn_Viewdetails3);
        }
    }
}

