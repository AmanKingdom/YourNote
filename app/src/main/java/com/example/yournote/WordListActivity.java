package com.example.yournote;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.yournote.domain.Example;
import com.example.yournote.domain.Word;
import com.example.yournote.tools.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class WordListActivity extends BaseActivity {
    private List<Word> wordList = new ArrayList<Word>();
    int theChapterID;   //这会儿可真的要用id了
    int theChapter;   //也要用到chapter
    private static final String TAG = "WordListActivity";

    private DatabaseHelper dbHelper;
    SQLiteDatabase db;

    private RecyclerViewWordAdapter adapter;

    private int thePart;

    public static void actionStart(Context context, int chapterID, int chapter, int partID){
        Intent intent = new Intent(context, WordListActivity.class);
        intent.putExtra("chapterID", chapterID+"");
        intent.putExtra("chapter", chapter+"");
        intent.putExtra("partID", partID+"");
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        Intent intent = getIntent();
        theChapterID = Integer.parseInt(intent.getStringExtra("chapterID"));
        theChapter = Integer.parseInt(intent.getStringExtra("chapter"));
        thePart = Integer.parseInt(intent.getStringExtra("partID"));
        Log.d(TAG, "onCreate: 获取到chapterID:"+ theChapterID);

        dbHelper = new DatabaseHelper(this, 1);

//        initWordList();

        adapter = new RecyclerViewWordAdapter(wordList, this, thePart);
        RecyclerView recyclerView = findViewById(R.id.wordRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initWordList();
        adapter.notifyDataSetChanged();
    }

    public void initWordList(){
        boolean isNull = true;
        db = dbHelper.getReadableDatabase();

        String find_chapter_sql = "select * from table_chapter where id="+theChapterID;
        Cursor chapter_cursor = db.rawQuery(find_chapter_sql, null);

        if(chapter_cursor.moveToFirst()){
//            int chapter = chapter_cursor.getInt(chapter_cursor.getColumnIndex("chapter"));
//            chapter_cursor.close();
//            Log.d(TAG, "initWordList: 通过chapter的id:"+theChapterID+",获取到chapter:"+chapter);

            String find_word_sql = "select * from table_word where chapter="+ theChapterID;
            Cursor word_cursor = db.rawQuery(find_word_sql, null);

            if (word_cursor.moveToFirst()) {
                do {
                    int word_id = word_cursor.getInt(word_cursor.getColumnIndex("id"));
                    String word = word_cursor.getString(word_cursor.getColumnIndex("word"));
                    String wordTranslation = word_cursor.getString(word_cursor.getColumnIndex("word_translation"));

                    Log.d(TAG, "initWordList: 从数据库获取到单词："+word_id+","+word+","+wordTranslation);

                    String find_example_sql = "select * from table_example where word="+word_id;
                    Cursor example_cursor = db.rawQuery(find_example_sql, null);

                    if(example_cursor.moveToFirst()){
                        List<Example> exampleList = new ArrayList<>();
                        do{
                            int example_id = example_cursor.getInt(example_cursor.getColumnIndex("id"));
                            String example = example_cursor.getString(example_cursor.getColumnIndex("example"));
                            String exampleTranslation = example_cursor.getString(example_cursor.getColumnIndex("example_translation"));

                            Log.d(TAG, "initWordList: 得到例子及其翻译："+example_id+","+example+","+exampleTranslation);

                            Example example_item = new Example(example_id, example, exampleTranslation, word_id);
                            exampleList.add(example_item);
                        }while(example_cursor.moveToNext());
                        Word word_item = new Word(word_id, word, wordTranslation, theChapter, exampleList);
                        Log.d(TAG, "initWordList: 展示一下单词信息："+word_item.toString());
                        this.wordList.add(word_item);
                        isNull = false;
                    }
                    example_cursor.close();
                } while (word_cursor.moveToNext());
                word_cursor.close();
            }
        }
        db.close();
        if(isNull){
            Log.d(TAG, "initWordList: 数据库没有单词数据");
            List<Example> exampleList = new ArrayList<>();
            Word word_item = new Word(1, "empty", "数据为空", theChapter, exampleList);
            this.wordList.add(word_item);
        }
    }
}
