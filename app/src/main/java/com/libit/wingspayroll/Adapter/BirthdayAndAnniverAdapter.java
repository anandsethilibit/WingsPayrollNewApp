package com.libit.wingspayroll.Adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.libit.wingspayroll.Model.BirthdayAndAnniversaryModel;
import com.libit.wingspayroll.R;
import java.util.List;


public class BirthdayAndAnniverAdapter extends RecyclerView.Adapter<BirthdayAndAnniverAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<BirthdayAndAnniversaryModel> services;
    Context context;


    public BirthdayAndAnniverAdapter(Context ctx, List<BirthdayAndAnniversaryModel> services) {
        this.context = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.services = services;
    }

    @NonNull
    @Override
    public BirthdayAndAnniverAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.birthdayandanniver_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BirthdayAndAnniverAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.EmpName.setText(services.get(position).getEmployeeName());
        holder.BirthdayDate.setText(services.get(position).getDateOfBirth());
        holder.Type.setText(services.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView EmpName,BirthdayDate,Type;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            EmpName = itemView.findViewById(R.id.txt_EmpName);
            BirthdayDate = itemView.findViewById(R.id.txt_dDate);
            Type = itemView.findViewById(R.id.txt_type);
        }
    }
}
