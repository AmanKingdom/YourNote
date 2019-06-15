package com.example.yournote;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yournote.domain.Chapter;

import java.util.List;

public class RecyclerViewChapterAdapter extends RecyclerView.Adapter<RecyclerViewChapterAdapter.ViewHolder> {
    private List<Chapter> chapters;
    private int thePart;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chapter_list_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.chapterText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Chapter chapter_item = chapters.get(position);
                WordListActivity.actionStart(viewGroup.getContext(), chapter_item.getId(), chapter_item.getChapter(), thePart);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Chapter chapter_item = chapters.get(i);
        viewHolder.chapterText.setText("Chapter"+chapter_item.getChapter()+":"+chapter_item.getChapterName());
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView chapterText;
        View chapterView;

        public ViewHolder(View view){
            super(view);
            chapterView = view;
            chapterText = view.findViewById(R.id.chapterTextView);
        }
    }
    public RecyclerViewChapterAdapter(List<Chapter> chapters, int thePart){
        this.chapters = chapters;
        this.thePart = thePart;
    }
}
