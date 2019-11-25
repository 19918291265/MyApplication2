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

public class MyAdapter4 extends RecyclerView.Adapter<MyAdapter4.ViewHolder>{

    private List<Map<String, Object>> list;
    private Context context;

    public MyAdapter4(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter4.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.privategood_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter4.ViewHolder holder, final int position) {
        holder.textView.setText(list.get(position).get("good_name").toString());
        final String good_id = list.get(position).get("good_id").toString();
        final String shop_id = list.get(position).get("shop_id").toString();
        final String account_id = list.get(position).get("his").toString();

        holder.button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_1 = new Intent(context,ChangeGoodDateActivity.class);
                intent_1.putExtra("id",good_id);
                context.startActivity(intent_1);
            }
        });
        holder.button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BuyGoodActivity.class);
                intent.putExtra("good_id",good_id);
                intent.putExtra("shop_id",shop_id);
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
        private Button button_1;
        private Button button_2;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_item);
            button_1 = itemView.findViewById(R.id.bt_item_date);
            button_2 = itemView.findViewById(R.id.bt_item_in);
        }

    }

}
