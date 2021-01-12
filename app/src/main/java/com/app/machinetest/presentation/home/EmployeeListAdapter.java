package com.app.machinetest.presentation.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.machinetest.R;
import com.app.machinetest.Utilities.ConstantMethods;
import com.app.machinetest.models.EmployeeListModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.MyViewHolder> {

    Context context;
    List<EmployeeListModel> modelList;
    EmployeeItemClickListener itemClickListener;

    public EmployeeListAdapter(Context context, List<EmployeeListModel> response, EmployeeItemClickListener itemClickListener) {

        this.context = context;
        this.modelList = response;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public EmployeeListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeListAdapter.MyViewHolder holder, int position) {

        ConstantMethods.loadShimmerImage(context, modelList.get(position).getProfileImage(),
                holder.ivProfile, holder.shimmerFrameLayout);
        holder.tvPhone.setText(modelList.get(position).getPhone() != null ? modelList.get(position).getPhone() : "--- ------ ------");
        holder.tvEmail.setText(modelList.get(position).getEmail() != null ? modelList.get(position).getEmail() : "--- ------ ------");
        holder.tvName.setText(modelList.get(position).getName() != null ? modelList.get(position).getName() : "--- ------ ------");


    }

    @Override
    public int getItemCount() {
        if (!modelList.isEmpty())
            return modelList.size();
        else
            return 0;
    }

    public int getItemViewType(int position) {
        return position;
    }

    public void updateList(List<EmployeeListModel> list) {
        modelList = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ShimmerFrameLayout shimmerFrameLayout;
        TextView tvName, tvPhone, tvEmail;
        CircleImageView ivProfile;
        LinearLayout llMain;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvName = itemView.findViewById(R.id.tvEmpName);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_image);
            llMain = itemView.findViewById(R.id.llMain);

            llMain.setOnClickListener(v -> {
                itemClickListener.itemClick(getAdapterPosition(), modelList.get(getAdapterPosition()));

            });
        }
    }
}
