package com.example.test_php2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.test_php2.model.User;
import com.example.test_php2.sql.DatabaseHelper;
import com.example.test_php2.sql.DatabaseHelper2;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class first_show_pic_arm extends AppCompatActivity {
    private final AppCompatActivity activity = first_show_pic_arm.this;
    static int visitCount = 0;
    static int visitNext = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_show_pic_arm);
        re_pic();

        Button cancle = findViewById(R.id.cn);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(first_show_pic_arm.this, first_arm.class);
                startActivity(intent);
            }
        });

        Button next = findViewById(R.id.button4);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resizeImage();
                renameSend();
                up_pic();
            }
        });


    }

    public void renameSend(){
        visitNext++;
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy", Locale.KOREA);
        Date now = new Date();
        String count_st = Integer.toString(visitCount);
        String name = count_st+".jpg";
        File sr = Environment.getExternalStorageDirectory();
        String count = Integer.toString(visitNext);
        File from = new File(sr,name);
        File to = new File(sr,count+".jpg");
        from.renameTo(to);
    }

    public void resizeImage(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy", Locale.KOREA);
        Date now = new Date();
        String count_st = Integer.toString(visitCount);
        //final File file = new File(Environment.getExternalStorageDirectory()+"/"+count_st+".jpg");
        //String path = (Environment.getExternalStorageDirectory()+"/"+count_st+".jpg");
        String Newpath = (Environment.getExternalStorageDirectory()+"/"+count_st+".jpg");

        Bitmap photo = BitmapFactory.decodeFile(Newpath);
        photo = Bitmap.createScaledBitmap(photo,480,640,false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 80,bytes);

        File f = new File(Newpath);
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
            //File file = new File(path);
            //file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void up_pic(){
        Toast.makeText(getBaseContext(), "อัพโหลดรูป", Toast.LENGTH_LONG).show();
        //String path = Environment.getExternalStorageDirectory() + "/pic_smile.jpg";
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy", Locale.KOREA);
        Date now = new Date();
        String count_st = Integer.toString(visitNext);
        String path = (Environment.getExternalStorageDirectory()+"/"+count_st+".jpg");
        String url = db1.getNg()+"/pro-android/arm.php";
        //visitCount++;
        Ion.with(this)
                .load(url)
                .setMultipartFile("upload_file", new File(path))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
//                        visitCount++;
                        if(visitNext <= 4){
                            Intent intent = new Intent(first_show_pic_arm.this,first_arm.class);
                            startActivity(intent);
                        }
                        else {
                            process();
                        }
                    }
                });
    }

    public void re_pic(){
        OutputStream out = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy", Locale.KOREA);
        Date now = new Date();
//        String count_st = Integer.toString(visitCount);
        visitCount++;
        String count_st = Integer.toString(visitCount);
        final File file = new File(Environment.getExternalStorageDirectory()+"/"+count_st+".jpg");
        //File file = new File(Environment.getExternalStorageDirectory()+"/"+"smile_"+formatter.format(now)+".jpg");
        ImageView image = (ImageView)findViewById(R.id.imageView2);
        Matrix matrix = new Matrix();
        matrix.postRotate(270);
        Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/"+count_st+".jpg");
        Bitmap rotated = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix,true);


        try {
            out = new FileOutputStream(file);
            rotated.compress(Bitmap.CompressFormat.JPEG, 100, out);
            image.setImageBitmap(rotated);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }


    DatabaseHelper2 db2 = new DatabaseHelper2(activity);
    DatabaseHelper db1 = new DatabaseHelper(activity);
    public void process() {
        String url = db1.getNg()+"/pro-android/arm/first_test.php";
        Ion.with(this)
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        double sum = Double.parseDouble(result);
                        //String sum_st = Double.toString(sum);

                        db2.updateDistArm(sum, db1.getName());


                        Intent intent = new Intent(first_show_pic_arm.this, first_detail_sound.class);
                        startActivity(intent);


                    }
                });
    }


}