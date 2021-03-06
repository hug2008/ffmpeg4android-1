package com.onzhou.ffmpeg.decode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.view.View;
import android.widget.TextView;

import com.onzhou.ffmpeg.base.AbsBaseActivity;
import com.onzhou.ffmpeg.task.AssertReleaseTask;

import java.io.File;

import io.reactivex.schedulers.Schedulers;

/**
 * @anchor: andy
 * @date: 2018-10-30
 * @description:
 */
public class FFmpegDecodeActivity extends AbsBaseActivity implements AssertReleaseTask.ReleaseCallback {

    private TextView btnDecode;

    private FFmpegDecode fFmpegDecode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg_decode);
        btnDecode = (TextView) findViewById(R.id.btn_decode);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        AssertReleaseTask videoReleaseTask = new AssertReleaseTask(this, "input.mp4", this);
        AsyncTaskCompat.executeParallel(videoReleaseTask);
    }

    public void onDecodeClick(View view) {
        if (fFmpegDecode == null) {
            fFmpegDecode = new FFmpegDecode();
        }
        btnDecode.setEnabled(false);
        final File fileDir = getExternalFilesDir(null);
        Schedulers.newThread().scheduleDirect(new Runnable() {
            @Override
            public void run() {
                fFmpegDecode.decode(fileDir.getAbsolutePath() + "/input.mp4", fileDir.getAbsolutePath() + "/output.yuv");
            }
        });
    }

    @Override
    public void onReleaseSuccess(String filePath) {
        btnDecode.setEnabled(true);
    }
}
