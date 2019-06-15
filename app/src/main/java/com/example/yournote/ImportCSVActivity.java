package com.example.yournote;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yournote.tools.DatabaseHelper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class ImportCSVActivity extends AppCompatActivity{
    private static final String TAG = "ImportCSVActivity";
    private DatabaseHelper dbHelper;
    SQLiteDatabase db;

    private static final String TAG1 = "FileChoose";
    private static final int CHOOSE_FILE_CODE = 0;

    private boolean isInitPartAndChapter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_csv);

        dbHelper = new DatabaseHelper(this, 1);

        Button uploadWordsCSVButton = findViewById(R.id.uploadWordsCSVButton);
        uploadWordsCSVButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                isInitPartAndChapter = false;
                chooseFile();
            }
        });

        Button initButton = findViewById(R.id.initButton);
        initButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                isInitPartAndChapter = true;
                chooseFile();
            }
        });

        Button clearPartChapterButton = findViewById(R.id.clearDatabaseButton);
        clearPartChapterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                clearDatabase();
            }
        });
    }

    private void clearDatabase(){
        db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + "table_part");
        db.execSQL("DELETE FROM " + "table_chapter");
        db.execSQL("DELETE FROM " + "table_word");
        db.execSQL("DELETE FROM " + "table_example");
        db.close();
    }

    // 调用系统文件管理器
    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(".csv/*").addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Choose File"), CHOOSE_FILE_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "你的手机居然没有文件管理器？？", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
// 文件选择完之后，自动调用此函数
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_FILE_CODE) {
                Uri uri = data.getData();
                String sPath1 = null;
                sPath1 = getPath(this, uri); // Paul Burke写的函数，根据Uri获得文件路径
                Log.d(TAG, "onActivityResult: 您选择的文件及其路径为："+sPath1);

                if(isInitPartAndChapter){
                    initPathAndChapterOnDatabase(sPath1);
                }else{
                    initWordsOnDatabase(sPath1);
                }
            }
        } else {
            Log.e(TAG1, "onActivityResult() error, resultCode: " + resultCode);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 用于初始化数据库表，同样的需要人工点击按钮选择初始化csv数据表
    private void initPathAndChapterOnDatabase(String filePath) {
        int i = 0;// 用于标记打印的条数
        try {
//            File csv = new File(filePath);
//            BufferedReader br = new BufferedReader(new FileReader(csv));

//            解决读取中文乱码问题
            InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath), "gbk");
            BufferedReader br = new BufferedReader(isr);

            br.readLine();
            String line = "";
            while ((line = br.readLine()) != null) {
                i++;
                System.out.println("第" + i + "行：" + line);// 输出每一行数据

                String[] buffer = line.split(",");// 以逗号分隔
                System.out.println("第" + i + "行：" + buffer[1]);// 取第一列数据
                System.out.println("第" + i + "行：" + buffer[2]);
                System.out.println("第" + i + "行：" + buffer[3]);

//                if(i>1){
                int part = Integer.valueOf(buffer[0]);
                String partName = buffer[1];
                int chapter = Integer.valueOf(buffer[2]);
                String chapterName = buffer[3];

                db = dbHelper.getWritableDatabase();
                String has_this_part_sql = "select * from table_part where part like "+part;

                Cursor cursor = db.rawQuery(has_this_part_sql, null);
                if(cursor.getCount() == 0){
                    Log.d(TAG, "initPathAndChapterOnDatabase: 没有该part，可以添加");

                    ContentValues values = new ContentValues();
                    values.put("part", part);
                    values.put("part_name", partName);
                    db.insert("table_part", "part", values);
                }else{
                    Log.d(TAG, "initPathAndChapterOnDatabase: 数据库已经存在该part:"+partName+",不用添加");
                }
                cursor.close();

//                    String insert_chapter_sql = "insert into table_chapter(chapter,chapter_name, part) values("+chapter+", "+chapterName+", "+part+")";
//                    db.execSQL(insert_chapter_sql);

                ContentValues values = new ContentValues();
                values.put("chapter", chapter);
                values.put("chapter_name", chapterName);
                values.put("part", part);
                db.insert("table_chapter", "chapter", values);
                db.close();

//                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initWordsOnDatabase(String filePath) {
        int i = 0;// 用于标记打印的条数
        try {
            // 解决读取中文乱码问题
            InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath), "gbk");
            BufferedReader br = new BufferedReader(isr);

            br.readLine();
            String line = "";

            while ((line = br.readLine()) != null) {
                i++;
                System.out.println("第" + i + "行：" + line);// 输出每一行数据

                String[] buffer = line.split(",");// 以逗号分隔
                System.out.println("第" + i + "行：" + buffer[0]);// 取第一列数据
                System.out.println("第" + i + "行：" + buffer[1]);
                System.out.println("第" + i + "行：" + buffer[2]);
                System.out.println("第" + i + "行：" + buffer[3]);
                System.out.println("第" + i + "行：" + buffer[4]);
                System.out.println("第" + i + "行：" + buffer[5]);

                int part = Integer.valueOf(buffer[0]);
                int chapter = Integer.valueOf(buffer[1]);
                String word = buffer[2];
                String wordTranslation = buffer[3];
                String example = buffer[4];
                String exampleTranslation = buffer[5];

                db = dbHelper.getWritableDatabase();
                String find_chapter_sql = "select * from table_chapter c where c.part="+part+" and c.chapter="+chapter;

                Cursor find_chapter_cursor = db.rawQuery(find_chapter_sql, null);

                if (find_chapter_cursor.moveToFirst()) {
                    int chapter_id = find_chapter_cursor.getInt(find_chapter_cursor.getColumnIndex("id"));
                    Log.d(TAG, "initWordsOnDatabase: 找到part"+part+"的chapter："+chapter+"，id为："+chapter_id);

                    String has_this_word_sql = "select * from table_word where word like '"+word+"'";

                    Cursor has_this_word_cursor = db.rawQuery(has_this_word_sql, null);
                    if(has_this_word_cursor.getCount() == 0){
                        Log.d(TAG, "initWordsOnDatabase: 没有该word，可以添加");

                        ContentValues values = new ContentValues();
                        values.put("word", word);
                        values.put("word_translation", wordTranslation);
                        values.put("chapter", chapter_id);
                        // 把单词存进单词表，下一步就是要把单词的翻译表搞定
                        db.insert("table_word", "word", values);
                    }else{
                        Log.d(TAG, "initWordsOnDatabase: 数据库已经存在该word:"+word+",不用添加");
                    }
                    has_this_word_cursor.close();

                    // 添加翻译到翻译表
                    Cursor this_word_cursor = db.rawQuery(has_this_word_sql, null);
                    if(this_word_cursor.moveToFirst() && this_word_cursor.getCount() == 1){
                        int word_id = this_word_cursor.getInt(this_word_cursor.getColumnIndex("id"));

                        ContentValues example_values = new ContentValues();
                        example_values.put("example", example);
                        example_values.put("example_translation", exampleTranslation);
                        example_values.put("word", word_id);
                        db.insert("table_example", "example", example_values);
                    }else{
                        Log.d(TAG, "initWordsOnDatabase: csv文件数据有问题");
                        Toast.makeText(this, "出了点毛病，你的csv文件数据有问题", Toast.LENGTH_SHORT).show();
                    }
                    this_word_cursor.close();
                }else{
                    Log.d(TAG, "initWordsOnDatabase: 没有对应的找到chapter："+chapter);
                }
                find_chapter_cursor.close();
                db.close();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
