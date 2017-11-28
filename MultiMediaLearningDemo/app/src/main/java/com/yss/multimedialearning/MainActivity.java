package com.yss.multimedialearning;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.yss.multimedialearning.module.audio.ui.AudioMediaRecorderActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.goto_tasks)
    public void goToTasks() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, TasksActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.audio_media_recorder)
    public void audioMediaRecorder() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, AudioMediaRecorderActivity.class);
        startActivity(intent);
    }
}
