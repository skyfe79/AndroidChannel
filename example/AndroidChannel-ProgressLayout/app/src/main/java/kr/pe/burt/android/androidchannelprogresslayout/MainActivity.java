package kr.pe.burt.android.androidchannelprogresslayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    ProgressLayout progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressLayout = (ProgressLayout)findViewById(R.id.progressLayout);
        progressLayout.setProgressLayoutListener(new ProgressLayoutListener() {
            @Override
            public void onProgressCompleted() {
                Log.v("TAG", "onProgressCompleted");
            }

            @Override
            public void onProgressChanged(int progress) {
                Log.v("TAG", "onProgressChanged : " + progress);
            }
        });
    }

    public void startButtonClicked(View v) {
        progressLayout.start();
    }

    public void stopButtonClicked(View v) {
        progressLayout.stop();
    }

    public void cancelButtonClicked(View v) {
        progressLayout.cancel();
    }
}
