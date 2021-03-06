package com.onzhou.ffmpeg.streamer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.view.View;
import android.widget.TextView;

import com.onzhou.ffmpeg.base.AbsBaseActivity;
import com.onzhou.ffmpeg.task.AssertReleaseTask;

import java.io.File;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @anchor: andy
 * @date: 2018-10-29
 * @description:
 */
public class StreamActivity extends AbsBaseActivity implements AssertReleaseTask.ReleaseCallback {

    /**
     * 推流地址
     */
    private static final String PUBLISH_ADDRESS = "rtmp://192.168.1.102:1935/onzhou/live";

    /**
     * 开始推流按钮
     */
    private TextView mBtnStartPublish;

    private NowStreamer nowStreamer;

    private Disposable publishDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupViews();
        setupVideo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (publishDisposable != null && !publishDisposable.isDisposed()) {
            publishDisposable.dispose();
            publishDisposable = null;
        }
    }

    private void setupViews() {
        mBtnStartPublish = (TextView) findViewById(R.id.btn_start_publish);
    }

    public void setupVideo() {
        AssertReleaseTask videoReleaseTask = new AssertReleaseTask(this, "input.mp4", this);
        AsyncTaskCompat.executeParallel(videoReleaseTask);
    }

    @Override
    public void onReleaseSuccess(String videoPath) {
        mBtnStartPublish.setEnabled(true);
    }

    public void onStartClick(View view) {
        if (nowStreamer == null) {
            nowStreamer = new NowStreamer();
        }
        if (publishDisposable == null) {
            publishDisposable = Schedulers.newThread().scheduleDirect(new Runnable() {
                @Override
                public void run() {
                    final File intputVideo = new File(getExternalFilesDir(null), "input.mp4");
                    nowStreamer.startPublish(intputVideo.getAbsolutePath(), PUBLISH_ADDRESS);
                }
            });
        }
    }


}
