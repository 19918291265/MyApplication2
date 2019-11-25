package com.example.myapplication2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class MyAdapter5 extends RecyclerView.Adapter<MyAdapter5.ViewHolder>{

    private List<Map<String, Object>> list;
    private Context context;

    public MyAdapter5(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter5.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyAdapter5.ViewHolder holder, final int position) {
        holder.tv_0.setText("订单");
        holder.tv_1.setText("访问商店：" + list.get(position).get("deal_shop").toString());
        holder.tv_2.setText("购买商品：" + list.get(position).get("deal_good").toString());
        holder.tv_3.setText("购买数量：" + list.get(position).get("deal_quantity").toString());
        holder.tv_4.setText("总计价格：" + list.get(position).get("deal_price").toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_0;
        private TextView tv_1;
        private TextView tv_2;
        private TextView tv_3;
        private TextView tv_4;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_0 = itemView.findViewById(R.id.tv_0);
            tv_1 = itemView.findViewById(R.id.tv_1);
            tv_2 = itemView.findViewById(R.id.tv_2);
            tv_3 = itemView.findViewById(R.id.tv_3);
            tv_4 = itemView.findViewById(R.id.tv_4);
        }

    }

}