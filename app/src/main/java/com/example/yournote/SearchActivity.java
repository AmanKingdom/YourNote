package com.example.yournote;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yournote.domain.Example;
import com.example.yournote.domain.Word;
import com.example.yournote.tools.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {
    private static final String TAG = "SearchActivity";
    private String theSearchText;

    // 注意！这里的word_list我传入的单词所属chapter是其id而不是chapter原来的意思
    private List<Word> wordList = new ArrayList<Word>();

    private TextView resultText;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        resultText = findViewById(R.id.resultText);

        Button searchButton = findViewById(R.id.searchButton);
        final EditText searchText = findViewById(R.id.searchText);
        searchButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                wordList.clear();
                theSearchText = String.valueOf(searchText.getText());
                search(theSearchText);
            }
        });
    }

    public void search(String searchText) {
        DatabaseHelper dbHelper;
        SQLiteDatabase db;
        dbHelper = new DatabaseHelper(this, 1);

        db = dbHelper.getReadableDatabase();

        String find_word_sql = "select * from table_word where word like '" + searchText +"'";
        Cursor words_cursor = db.rawQuery(find_word_sql, null);

        if (words_cursor.moveToFirst()) {
            do {
                int word_id = words_cursor.getInt(words_cursor.getColumnIndex("id"));
                String word = words_cursor.getString(words_cursor.getColumnIndex("word"));
                String wordTranslation = words_cursor.getString(words_cursor.getColumnIndex("word_translation"));
                int chapter_id = words_cursor.getInt(words_cursor.getColumnIndex("chapter"));

                Log.d(TAG, "搜索单词: 从数据库获取到单词：" + word_id + "," + word + "," + wordTranslation);

                String find_example_sql = "select * from table_example where word=" + word_id;
                Cursor example_cursor = db.rawQuery(find_example_sql, null);

                if (example_cursor.moveToFirst()) {
                    List<Example> exampleList = new ArrayList<>();
                    do {
                        int example_id = example_cursor.getInt(example_cursor.getColumnIndex("id"));
                        String example = example_cursor.getString(example_cursor.getColumnIndex("example"));
                        String exampleTranslation = example_cursor.getString(example_cursor.getColumnIndex("example_translation"));

                        Log.d(TAG, "搜索单词: 得到例子及其翻译：" + example_id + "," + example + "," + exampleTranslation);

                        Example example_item = new Example(example_id, example, exampleTranslation, word_id);
                        exampleList.add(example_item);
                    } while (example_cursor.moveToNext());
                    // 注意！这里的word_list我传入的单词所属chapter是其id而不是chapter原来的意思
                    Word word_item = new Word(word_id, word, wordTranslation, chapter_id, exampleList);
                    this.wordList.add(word_item);
                }
                example_cursor.close();
            } while (words_cursor.moveToNext());
            words_cursor.close();
            showResult(this.wordList);
        }else{
            Toast.makeText(this, "没有找到该单词", Toast.LENGTH_SHORT).show();
        }
    }

    public void showResult(List<Word> wordList){
        DatabaseHelper dbHelper;
        SQLiteDatabase db;
        dbHelper = new DatabaseHelper(this, 1);

        db = dbHelper.getReadableDatabase();

        StringBuilder resultString = new StringBuilder();
        for(Word word: wordList){
//            这里的getChapter()是得到id
            String find_chapter_sql = "select * from table_chapter where id=" + word.getChapter();
            Cursor chapter_cursor = db.rawQuery(find_chapter_sql, null);

            if (chapter_cursor.moveToFirst()) {
                int chapter = chapter_cursor.getInt(chapter_cursor.getColumnIndex("chapter"));
                String chapter_name = chapter_cursor.getString(chapter_cursor.getColumnIndex("chapter_name"));
                Log.d(TAG, "showResult: 找到chapter："+chapter+" "+chapter_name);

                int part = chapter_cursor.getInt(chapter_cursor.getColumnIndex("part"));
                Log.d(TAG, "showResult: 寻找part："+part);

                String find_part_sql = "select * from table_part where part="+part;
                Cursor part_cursor = db.rawQuery(find_part_sql, null);
                if(part_cursor.moveToFirst()){
                    String part_name = part_cursor.getString(part_cursor.getColumnIndex("part_name"));
                    Log.d(TAG, "showResult: 找到part："+part+" "+part_name);

                    resultString.append("*------------------------------------------------------------*");
                    resultString.append("\n 来自:\n 场景part"+part+" "+part_name);
                    resultString.append("\n 章节chapter"+chapter+" "+chapter_name);
                    resultString.append("\n ------------------------------------------------------------ \n");
                    resultString.append(word.getWord()).append(" ").append(word.getWordTranslation()).append("\n").append(word.showExamples()+"\n");
                }
            }
        }
        resultText.setMovementMethod(ScrollingMovementMethod.getInstance());
        resultText.setText(resultString.toString());
    }
}
