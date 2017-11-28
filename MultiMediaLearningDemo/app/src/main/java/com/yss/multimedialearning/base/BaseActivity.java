package com.yss.multimedialearning.base;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.yss.multimedialearning.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yangshao on 2017/11/28.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @OnClick(R.id.tipBt)
    public void showTip() {
        new AlertDialog.Builder(this)
                .setTitle(getTitleRes())
                .setView(getLayoutInflater().inflate(getDialogRes(), null))
                .show();
    }

    protected abstract int getDialogRes();

    protected abstract int getTitleRes();
}
