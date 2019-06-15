package com.example.yournote;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SetInformationActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView uploadImage;
    private static final String TAG = "ImportCSVActivity";
    static final int REQUEST_IMAGE_GET = 1;
    private Uri fullPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_information);

        Button button = findViewById(R.id.enterButton);
        button.setOnClickListener(this);

        uploadImage = findViewById(R.id.uploadImageView);
        uploadImage.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
//            Bitmap thumbnail = data.getParcelableExtra("data");
            fullPhotoUri = data.getData();
            // Do work with photo saved at fullPhotoUri
            uploadImage.setImageURI(fullPhotoUri);
            Log.d(TAG, "onActivityResult: 输出URI"+fullPhotoUri.getPath());
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void selectImage() {
        if(isExternalStorageWritable()){
            Log.d(TAG, "startRecord: 外部存储可以访问");
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_GET);
            }
        }else{
            Log.d(TAG, "startRecord: 外部存储不能访问");
        }
    }

    public void saveHeadImage(){
        uploadImage.buildDrawingCache();
        Bitmap bitmap = uploadImage.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG , 100 , stream);
        byte[] byteArray = stream.toByteArray();
        File dir=new File(Environment.getExternalStorageDirectory ().getAbsolutePath()+"/YourNote/picture" );
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file=new File(dir, "head_image.png" );
        try {
            FileOutputStream fos=new FileOutputStream(file);
            fos.write(byteArray, 0 , byteArray.length);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.uploadImageView:
                selectImage();
                break;
            case R.id.enterButton:
                saveHeadImage();
//                LayoutInflater factory = LayoutInflater.from(SetInformationActivity.this);
//                View layout = factory.inflate(R.layout.nav_header_main, null);
//                ImageView headImageView = layout.findViewById(R.id.headImageView);
//
//                headImageView.setImageURI(fullPhotoUri);
//                TextView userNameText = layout.findViewById(R.id.userNameTextView);
//                TextView userNameEditText = findViewById(R.id.userNameEditText);
//                userNameText.setText(userNameEditText.getText());
            default:
                break;
        }
    }
}
