package com.example.yournote.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DBName = "your_note.db";
    private static final String TablePart = "table_part";
    private static final String TableChapter = "table_chapter";
    private static final String TableWord = "table_word";
    private static final String TableExample = "table_example";

    private static final String CREATE_TABLE_PART = "create table IF NOT EXISTS " + TablePart + "(id integer primary KEY autoincrement, part integer, part_name VARCHAR(255))";
    private static final String CREATE_TABLE_CHAPTER = "create table IF NOT EXISTS " + TableChapter + "(id integer primary KEY autoincrement, chapter integer, chapter_name VARCHAR(255), part integer not null REFERENCES 'table_part' ('id') DEFERRABLE INITIALLY DEFERRED);";
    private static final String CREATE_TABLE_WORD = "create table IF NOT EXISTS " + TableWord + "(id integer primary KEY autoincrement, word VARCHAR(255), word_translation VARCHAR(255), chapter integer not null REFERENCES 'chapter' ('id') DEFERRABLE INITIALLY DEFERRED)";
    private static final String CREATE_TABLE_EXAMPLE = "create table IF NOT EXISTS " + TableExample + "(id integer primary KEY autoincrement, example VARCHAR(255), example_translation VARCHAR(255), word integer not null REFERENCES 'word' ('id') DEFERRABLE INITIALLY DEFERRED)";

    public DatabaseHelper(Context context, int version) {
        super(context, DBName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PART);
        db.execSQL(CREATE_TABLE_CHAPTER);
        db.execSQL(CREATE_TABLE_WORD);
        db.execSQL(CREATE_TABLE_EXAMPLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
