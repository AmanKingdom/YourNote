package com.example.yournote;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.yournote.domain.Part;
import com.example.yournote.tools.DatabaseHelper;
import com.example.yournote.tools.PermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private List<Part> partList = new ArrayList<Part>();

    private DatabaseHelper dbHelper;
    SQLiteDatabase db;

    private RecyclerViewPartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionUtils.isGrantExternalRW(this, 1);

        dbHelper = new DatabaseHelper(this, 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 添加单词词条
        FloatingActionButton addWordFab = findViewById(R.id.addWordFab);
        addWordFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Toast.makeText(MainActivity.this, "添加单词(该功能未开放,敬请期待)", Toast.LENGTH_SHORT).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        File headImageFile=new File(Environment.getExternalStorageDirectory ().getAbsolutePath()+"/YourNote/picture/head_image.png" );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

//        initPartList();

        adapter = new RecyclerViewPartAdapter(partList);
        RecyclerView recyclerView = findViewById(R.id.partRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initPartList(){
        db = dbHelper.getReadableDatabase();
        String sql = "select * from table_part";
        Cursor cursor = db.rawQuery(sql, null);
        this.partList.clear();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int part = cursor.getInt(cursor.getColumnIndex("part"));
                String partName = cursor.getString(cursor.getColumnIndex("part_name"));
                Part part_item = new Part(id, part, partName);
                this.partList.add(part_item);
            } while (cursor.moveToNext());
            cursor.close();
        }else{
            Part part = new Part(1, 1, "还没有数据噢");
            this.partList.add(part);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initPartList();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.set_information) {
            Intent intent = new Intent(MainActivity.this, SetInformationActivity.class);
            startActivity(intent);
        } else if (id == R.id.import_csv) {
            Intent intent = new Intent(MainActivity.this, ImportCSVActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
