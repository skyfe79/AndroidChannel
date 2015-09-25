package kr.pe.burt.android.lib.androidchannel;

import android.os.Message;

/**
 * Created by burt on 15. 9. 23..
 */
public class Timer {
    private final int START_TIMER = 0;
    private final int STOP_TIMER  = 1;

    private Channel channel;
    private int interval=0;
    private OnTimer onTimer;

    volatile  boolean loop = true;

    public Timer(int interval, OnTimer onTimer) {
        this.interval = (interval < 0) ? (interval*-1) : (interval);
        this.onTimer = onTimer;

        channel = new Channel(new Channel.UiCallback() {
            @Override
            public boolean handleUiMessage(Message msg) {
                Timer.this.onTimer.onTime(Timer.this);
                return false;
            }
        }, new Channel.WorkerCallback() {


            Thread jobThread = null;

            @Override
            public boolean handleWorkerMessage(Message msg) {

                if(jobThread == null) {
                    jobThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (loop) {
                                try {
                                    Message msg = channel.toUI().obtainMessage();
                                    channel.toUI().sendMessage(msg);
                                    Thread.sleep(Timer.this.interval);
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                    });
                }

                switch (msg.what) {
                    case START_TIMER:
                        loop = true;
                        jobThread.start();
                        break;
                    case STOP_TIMER:
                        loop = false;
                        jobThread = null;
                        break;
                }
                return false;
            }
        });

    }


    public void start() {
        channel.toWorker().sendEmptyMessage(START_TIMER);
    }

    public void stop() {
        channel.toWorker().sendEmptyMessage(STOP_TIMER);
    }


    public void resetInterval(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }

    public boolean isAlive() {
        return loop;
    }

    public interface OnTimer {
        void onTime(Timer timer);
    }
}
