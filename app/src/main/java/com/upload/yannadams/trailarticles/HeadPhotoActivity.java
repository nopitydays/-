package com.upload.yannadams.trailarticles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Field;

public class HeadPhotoActivity extends AppCompatActivity {
    private ImageView h[]=new ImageView[17];
    private int imgI;
    private int c;
    public HeadPhotoActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("更换头像");
        toolbar.setNavigationIcon(R.drawable.icon__home_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    private void init() {
        imgI = 1;
        for(imgI=1;imgI<=16;++imgI){
            try{
                Field field = R.id.class.getField("h"+imgI);
                int id = (Integer)field.getInt(new R.id());
                c=id-imgI;
                h[imgI]=(ImageView)findViewById(id);
                h[imgI].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GlobalVariable.getInstance().setHeadid(v.getId()-c);
                        Toast.makeText(getApplicationContext(), "成功"+(v.getId()-c), Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

