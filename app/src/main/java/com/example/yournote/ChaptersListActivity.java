package com.example.yournote;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.yournote.domain.Chapter;
import com.example.yournote.tools.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ChaptersListActivity extends BaseActivity {
    private List<Chapter> chapterList = new ArrayList<Chapter>();
    int thePart;
    private static final String TAG = "ChaptersListActivity";

    private DatabaseHelper dbHelper;
    SQLiteDatabase db;

    private RecyclerViewChapterAdapter adapter;

    public static void actionStart(Context context, int part){
        Intent intent = new Intent(context, ChaptersListActivity.class);
        intent.putExtra("part", part+"");
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters_list);

        Intent intent = getIntent();
        try{
            thePart = Integer.parseInt(intent.getStringExtra("part"));
            // 注意，获取到的是part，不一定是id，因为part和id不是唯一对应关系
            Log.d(TAG, "onCreate: 获取到part:"+ thePart);
        }catch (Exception e){
            Log.d(TAG, "onCreate: 没有获取到part");
            finish();
        }

        dbHelper = new DatabaseHelper(this, 1);

//        initChapterList();

        adapter = new RecyclerViewChapterAdapter(chapterList, thePart);
        RecyclerView recyclerView = findViewById(R.id.chapterRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initChapterList();
        adapter.notifyDataSetChanged();
    }

    public void initChapterList(){
        db = dbHelper.getReadableDatabase();
        String sql = "select * from table_chapter where part="+ thePart;
        Cursor cursor = db.rawQuery(sql, null);
        this.chapterList.clear();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int chapter = cursor.getInt(cursor.getColumnIndex("chapter"));
                String chapterName = cursor.getString(cursor.getColumnIndex("chapter_name"));
                Chapter chapter_item = new Chapter(id, chapter, chapterName, thePart);
                this.chapterList.add(chapter_item);
            } while (cursor.moveToNext());
            cursor.close();
        }else{
            Chapter chapter_item = new Chapter(1, 1, "还没有数据噢", thePart);
            this.chapterList.add(chapter_item);
        }
        db.close();
    }
}
