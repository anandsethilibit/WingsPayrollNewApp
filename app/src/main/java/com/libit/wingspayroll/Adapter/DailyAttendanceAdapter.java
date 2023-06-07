package com.libit.wingspayroll.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.libit.wingspayroll.Model.DailyAttendanceModel;
import com.libit.wingspayroll.R;

import java.util.List;

public class DailyAttendanceAdapter extends RecyclerView.Adapter<DailyAttendanceAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<DailyAttendanceModel> services;
    Context context;

    private int lastposition = -1;

    public DailyAttendanceAdapter(Context ctx, List<DailyAttendanceModel> services) {
        this.context = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.services = services;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dailyattendanceview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.logName.setText(services.get(position).getName());
        holder.logDate.setText(services.get(position).getDate());
        holder.logInTime.setText(services.get(position).getIntime());
        holder.logOutTime.setText(services.get(position).getOuttime());

        setAnimation(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return services.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView logName, logDate, logInTime, logOutTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            logName = itemView.findViewById(R.id.dailyname);
            logDate = itemView.findViewById(R.id.dailyDate);
            logInTime = itemView.findViewById(R.id.dailyTime);
            logOutTime = itemView.findViewById(R.id.dailyOutTime);
        }
    }

    private void setAnimation(View viewToAnimation, int position) {

        if (position > lastposition) {
            Animation slideIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimation.startAnimation(slideIn);
            lastposition = position;
        }
    }

}
