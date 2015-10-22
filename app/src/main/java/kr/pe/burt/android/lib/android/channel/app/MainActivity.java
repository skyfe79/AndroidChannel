package kr.pe.burt.android.lib.android.channel.app;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import kr.pe.burt.android.lib.androidchannel.AndroidChannel;
import kr.pe.burt.android.lib.androidchannel.Timer;

public class MainActivity extends AppCompatActivity {

    static final int PING = 0;
    static final int PONG = 1;


    TextView textView;
    AndroidChannel androidChannel;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);


        timer = new Timer(1000, new Timer.OnTimer() {
            int count = 0;
            @Override
            public void onTime(Timer timer) {
                count++;
                textView.setText("count : " + count);
                if(count == 10) {
                    timer.resetInterval(2000);
                }
            }
        });
        timer.start();


        androidChannel = new AndroidChannel(new AndroidChannel.UiCallback() {

            @Override
            public boolean handleUiMessage(Message msg) {
                if(msg.what == PING) {
                    Log.d("TAG", "PING");
                    androidChannel.toWorker().sendEmptyMessageDelayed(PONG, 1000);
                }
                return false;
            }
        }, new AndroidChannel.WorkerCallback() {

            @Override
            public boolean handleWorkerMessage(Message msg) {

                if(msg.what == PONG) {
                    Log.d("TAG", "PONG");
                    androidChannel.toUI().sendEmptyMessageDelayed(PING, 1000);
                }
                return false;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        androidChannel.open();
        androidChannel.toUI().sendEmptyMessage(PING);
    }

    @Override
    protected void onPause() {
        super.onPause();
        androidChannel.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(timer.isAlive()) {
                timer.stop();
            } else {
                timer.start();
            }
        }
        return super.onTouchEvent(event);
    }
}
