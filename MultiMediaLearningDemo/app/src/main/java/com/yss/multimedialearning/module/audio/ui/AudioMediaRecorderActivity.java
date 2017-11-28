package com.yss.multimedialearning.module.audio.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yss.multimedialearning.R;
import com.yss.multimedialearning.base.BaseActivity;
import com.yss.multimedialearning.module.audio.utils.AudioRecorderUtils;
import com.yss.multimedialearning.module.audio.utils.FileUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangshao on 2017/11/28.
 * <p>
 * 使用MediaRecorder采集音频
 * capture audio source with MediaRecorder
 * <p>
 * MediaRecorder doc: http://androiddoc.qiniudn.com/reference/android/media/MediaRecorder.html
 * <p>
 * thks: http://www.jianshu.com/p/06eca50ddda4
 */

public class AudioMediaRecorderActivity extends BaseActivity implements View.OnTouchListener,
        AudioRecorderUtils.OnAudioStatusUpdateListener {

    private final int REQUEST_PERMISSION = 10;
    private String path;

    private AudioRecorderDialog recorderDialog;
    private AudioRecorderUtils recorderUtils;
    private RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private TextView button;
    private long downT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_media_recorder);
        ButterKnife.bind(this);
        button = (TextView) findViewById(R.id.button);
        button.setOnTouchListener(this);

        recorderDialog = new AudioRecorderDialog(this);
        recorderDialog.setShowAlpha(0.98f);
        path = Environment.getExternalStorageDirectory() + "/multi_media/audio/";
        recorderUtils = new AudioRecorderUtils(path);
        recorderUtils.setOnAudioStatusUpdateListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        initRecycler();
        checkPermission();
        setTitle(R.string.audio_media_recorder_title);
    }

    private void initRecycler() {
        adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.audio_list_item, null);
                AudioListViewHolder holder = new AudioListViewHolder(view);
                return holder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
                AudioListViewHolder holder = (AudioListViewHolder) holder1;
                File file = new File(path);
                if (file.exists()) {
                    holder.pathText.setText(file.listFiles()[position].getName());
                }

            }

            @Override
            public int getItemCount() {
                File file = new File(path);
                if (file.exists() && file.listFiles() != null) {
                    return file.listFiles().length;
                }
                return 0;
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                recorderUtils.startRecord();
                downT = System.currentTimeMillis();
                recorderDialog.showAtLocation(view, Gravity.CENTER, 0, 0);
                button.setBackgroundResource(R.drawable.shape_recoder_btn_recoding);
                return true;
            case MotionEvent.ACTION_UP:
                recorderUtils.stopRecord();
                recorderDialog.dismiss();
                button.setBackgroundResource(R.drawable.shape_recoder_btn_normal);
                return true;
        }
        return false;
    }

    @Override
    public void onUpdate(double db, long time) {
        if (null != recorderDialog) {
            int level = (int) db;
            recorderDialog.setLevel(level);
            recorderDialog.setTime(System.currentTimeMillis() - downT);
        }
    }

    @Override
    public void onStop(String filePath) {
        Toast.makeText(AudioMediaRecorderActivity.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(AudioMediaRecorderActivity.this, Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AudioMediaRecorderActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }
    }

    public class AudioListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.audio_path)
        TextView pathText;

        public AudioListViewHolder(View item) {
            super(item);
            ButterKnife.bind(this, item);
        }
    }

    @OnClick(R.id.clean)
    public void deleteFiles() {
        new AlertDialog.Builder(this).setMessage("确定要清空App内所有音频文件吗？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        File file = new File(path);
                        if (file.exists() && file.listFiles() != null) {
                            FileUtils.deleteDirWithFile(file);
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }).show();
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_audio_media_recorder;
    }

    @Override
    protected int getTitleRes() {
        return R.string.audio_media_recorder_title;
    }
}
