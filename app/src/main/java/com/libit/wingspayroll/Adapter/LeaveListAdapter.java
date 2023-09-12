package com.libit.wingspayroll.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.libit.wingspayroll.LeaveApprovalActivity;
import com.libit.wingspayroll.LeaveApprovedFullDetail;
import com.libit.wingspayroll.Model.LeaveListModel;
import com.libit.wingspayroll.R;

import java.util.List;

public class LeaveListAdapter extends RecyclerView.Adapter<LeaveListAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<LeaveListModel> services;
    Context context;

    public LeaveListAdapter(Context ctx, List<LeaveListModel> services) {
        this.context = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.services = services;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.detialleavelistlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.username.setText(services.get(position).getUserName());
        holder.requestdate.setText(services.get(position).getRequestDate());
        holder.status.setText(services.get(position).getEStatus());
        holder.viewdetail.setText("View");
        holder.viewdetail.setTextColor(Color.parseColor("#2195F2"));
        holder.viewdetail.setPaintFlags(holder.viewdetail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        holder.viewdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LeaveApprovedFullDetail.class);
                intent.putExtra("ID", (services.get(position).getID()));
                intent.putExtra("UserName", (services.get(position).getUserName()));
                intent.putExtra("Type", (services.get(position).getType()));
                intent.putExtra("EStatus", (services.get(position).getEStatus()));
                intent.putExtra("RequestDate", (services.get(position).getRequestDate()));
                intent.putExtra("fdate", (services.get(position).getFdate()));
                intent.putExtra("tdate", (services.get(position).getTdate()));
                intent.putExtra("reason", (services.get(position).getReason()));
                intent.putExtra("AName", (services.get(position).getAName()));
                intent.putExtra("ADate", (services.get(position).getADate()));
                intent.putExtra("Days", (services.get(position).getDays()));
                context.startActivity(intent);
                ((LeaveApprovalActivity)context).finish();

            }
        });

        holder.sno.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username,requestdate,status,viewdetail, sno;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            requestdate = itemView.findViewById(R.id.txt_requestdate);
            status = itemView.findViewById(R.id.txt_status);
            viewdetail = itemView.findViewById(R.id.viewdetail);
            sno = itemView.findViewById(R.id.sno);

        }
    }
}
