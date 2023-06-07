package com.libit.wingspayroll.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.libit.wingspayroll.Model.MonthlyAttendanceModel;
import com.libit.wingspayroll.R;

import java.util.List;


public class MonthlyAttendanceAdapter extends RecyclerView.Adapter<MonthlyAttendanceAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<MonthlyAttendanceModel> services;
    Context context;

    public MonthlyAttendanceAdapter(Context ctx, List<MonthlyAttendanceModel> services) {
        this.context = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.services = services;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.monthlyattendancelayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.Date.setText(services.get(position).getDate());
        holder.Day.setText(services.get(position).getDay());
        holder.Intime.setText(services.get(position).getIntime());
        holder.OutTime.setText(services.get(position).getOutTime());
        holder.Duration.setText(services.get(position).getDuration());
        holder.Status.setText(services.get(position).getStatus());

//        holder.viewdetail.setText("View");
//        holder.viewdetail.setTextColor(Color.parseColor("#2195F2"));
//        holder.viewdetail.setPaintFlags(holder.viewdetail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//
//        holder.viewdetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, LeaveApprovedFullDetail.class);
//                intent.putExtra("ID", (services.get(position).getID()));
//                intent.putExtra("UserName", (services.get(position).getUserName()));
//                intent.putExtra("Type", (services.get(position).getType()));
//                intent.putExtra("EStatus", (services.get(position).getEStatus()));
//                intent.putExtra("RequestDate", (services.get(position).getRequestDate()));
//                intent.putExtra("fdate", (services.get(position).getFdate()));
//                intent.putExtra("tdate", (services.get(position).getTdate()));
//                intent.putExtra("reason", (services.get(position).getReason()));
//                intent.putExtra("AName", (services.get(position).getAName()));
//                intent.putExtra("ADate", (services.get(position).getADate()));
//                intent.putExtra("Days", (services.get(position).getDays()));
//                context.startActivity(intent);
//                ((LeaveApprovalActivity)context).finish();
//
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Date,Day,Intime,OutTime,Duration,Status;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Date = itemView.findViewById(R.id.txt_date);
            Day = itemView.findViewById(R.id.txt_day);
            Intime = itemView.findViewById(R.id.txt_intime);
            OutTime = itemView.findViewById(R.id.txt_OutTime);
            Duration = itemView.findViewById(R.id.txt_duration);
            Status = itemView.findViewById(R.id.txt_status);

        }
    }
}

