package com.example.yournote;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yournote.domain.Word;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class RecyclerViewWordAdapter extends RecyclerView.Adapter<RecyclerViewWordAdapter.ViewHolder> {
    private List<Word> words;
    private Context context;
    private int part;

    @NonNull
    @Override
    public RecyclerViewWordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.word_list_item, viewGroup, false);
        return new RecyclerViewWordAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewWordAdapter.ViewHolder viewHolder, int i) {
        final Word word_item = words.get(i);
        viewHolder.wordText.setText(word_item.getWord()+" "+word_item.getWordTranslation());
        viewHolder.examplesText.setText(word_item.showExamples());

        final MediaPlayer mediaPlayer = new MediaPlayer();
        String audio_file_path = Environment.getExternalStorageDirectory ().getAbsolutePath()+"/YourNote/audio";
        final String audio_path = audio_file_path+"/p"+part+"ch"+word_item.getChapter()+"_"+word_item.getWord()+".mp3";
        File audio_folder = new File(audio_file_path);
        if (!audio_folder.exists()) {
            audio_folder.mkdirs();
        }

        boolean audioExists = false;
        try {
            File file = new File(audio_path);
            android.util.Log.i("音频文件是否存在？", "file.exists()="+ file.exists());
            if(file.exists()){
                FileInputStream fis = new FileInputStream(new File(file.getAbsolutePath()));
                mediaPlayer.setDataSource(fis.getFD());

                mediaPlayer.prepare();  // Mediaplayer进入到准备状态
                audioExists = true;
            }

            final boolean finalAudioExists = audioExists;
            viewHolder.playButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(finalAudioExists){
                        if(!mediaPlayer.isPlaying()){
                            mediaPlayer.start();
                            Log.d(TAG, "onClick: 播放音频："+audio_path);
                            Toast.makeText(context, "播放"+word_item.getWord(), Toast.LENGTH_SHORT).show();
                        }else if(mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                            Log.d(TAG, "onClick: 暂停播放音频："+audio_path);
                            Toast.makeText(context, "暂停播放"+word_item.getWord(), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(context, "音频文件:"+audio_path+" 不存在", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView wordText;
        TextView examplesText;
        Button playButton;

        public ViewHolder(View view){
            super(view);
            wordText = view.findViewById(R.id.wordTextView);
            examplesText = view.findViewById(R.id.examplesTextView);
            playButton = view.findViewById(R.id.playButton);
        }
    }
    public RecyclerViewWordAdapter(List<Word> words, Context context, int part){
        this.context = context;
        this.words = words;
        this.part = part;
    }
}
