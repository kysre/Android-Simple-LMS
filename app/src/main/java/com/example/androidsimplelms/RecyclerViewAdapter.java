package com.example.androidsimplelms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<ListItem> list;
    private SelectListener listener;

    public RecyclerViewAdapter(Context context, List<ListItem> list, SelectListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.leftTextView.setText(list.get(position).getLeftString());
        holder.rightTextView.setText(list.get(position).getRightString());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView leftTextView, rightTextView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftTextView = itemView.findViewById(R.id.leftTextView);
            rightTextView = itemView.findViewById(R.id.rightTextView);
            cardView = itemView.findViewById(R.id.mainContainerCardView);
        }
    }

    public static class ListItem {
        private String leftString;
        private String rightString;

        public ListItem(String leftString, String rightString) {
            this.leftString = leftString;
            this.rightString = rightString;
        }

        public String getLeftString() {
            return leftString;
        }

        public String getRightString() {
            return rightString;
        }

        public void setLeftString(String leftString) {
            this.leftString = leftString;
        }

        public void setRightString(String rightString) {
            this.rightString = rightString;
        }
    }

    public interface SelectListener {
        void onItemClicked(ListItem listItem);
    }
}
