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

public class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.ViewHolder> {


    private List<Map<String, Object>> list;
    private Context context;

    public MyAdapter1(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.publicshop_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter1.ViewHolder holder, final int position) {
        holder.textView.setText(list.get(position).get("name").toString());
       final String shop_id = list.get(position).get("shop_id").toString();
       final String account_id = list.get(position).get("his").toString();

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BeforePublicGoodActivity.class);
                intent.putExtra("id",shop_id);
                intent.putExtra("his",account_id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private Button button;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_item);
            button = itemView.findViewById(R.id.bt_item);
        }
    }
}