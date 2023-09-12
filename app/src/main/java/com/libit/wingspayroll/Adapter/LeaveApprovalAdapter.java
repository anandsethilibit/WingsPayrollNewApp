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
import com.libit.wingspayroll.LeaveApprovalDetail;
import com.libit.wingspayroll.Model.LeaveApprovalModel;
import com.libit.wingspayroll.R;
import java.util.List;


public class LeaveApprovalAdapter extends RecyclerView.Adapter<LeaveApprovalAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<LeaveApprovalModel> services;
    Context context;

    public LeaveApprovalAdapter(Context ctx, List<LeaveApprovalModel> services) {
        this.context = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.services = services;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.approvallayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.username.setText(services.get(position).getUserName());
        holder.FDateTdate.setText(services.get(position).getFdate()+" To "+services.get(position).getTdate());
        holder.ReqDate.setText(services.get(position).getRequestDate());
        holder.viewdetail.setText("View");
        holder.viewdetail.setTextColor(Color.parseColor("#2195F2"));
        holder.viewdetail.setPaintFlags(holder.viewdetail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        holder.viewdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LeaveApprovalDetail.class);
                intent.putExtra("ID", (services.get(position).getID()));
                intent.putExtra("UserName", (services.get(position).getUserName()));
                intent.putExtra("Type", (services.get(position).getType()));
                intent.putExtra("RequestDate", (services.get(position).getRequestDate()));
                intent.putExtra("EStatus", (services.get(position).getEStatus()));
                intent.putExtra("fdate", (services.get(position).getFdate()));
                intent.putExtra("tdate", (services.get(position).getTdate()));
                intent.putExtra("reson", (services.get(position).getReason()));
                intent.putExtra("Days", (services.get(position).getDays()));
                context.startActivity(intent);
                ((LeaveApprovalActivity)context).finish();
            }
        });

        holder.sno.setText(String.valueOf(position + 1));


//        holder.aprrovalbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                String UserId = StaticDataHelper.getStringFromPreferences(context, "userid");
////                ((LeaveApprovalActivity)context).approveleave(services.get(position).getID());
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username,FDateTdate, ReqDate,viewdetail, sno;
        //ImageButton aprrovalbtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            FDateTdate = itemView.findViewById(R.id.FDateTdate);
            ReqDate = itemView.findViewById(R.id.ReqDate);
            viewdetail = itemView.findViewById(R.id.viewdetail);
            sno = itemView.findViewById(R.id.sno);
            //aprrovalbtn = itemView.findViewById(R.id.aprrovalbtn);
        }
    }
}
