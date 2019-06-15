package com.example.yournote;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yournote.domain.Part;

import java.util.List;


public class RecyclerViewPartAdapter extends RecyclerView.Adapter<RecyclerViewPartAdapter.ViewHolder> {
    private List<Part> parts;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.part_list_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.partText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Part part_item = parts.get(position);
                ChaptersListActivity.actionStart(viewGroup.getContext(), part_item.getPart());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Part part_item = parts.get(i);
        viewHolder.partText.setText("Part"+part_item.getPart()+":"+part_item.getPartName());
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView partText;
        View partView;

        public ViewHolder(View view){
            super(view);
            partView = view;
            partText = view.findViewById(R.id.partTextView);
        }
    }
    public RecyclerViewPartAdapter(List<Part> parts){
        this.parts = parts;
    }
}
