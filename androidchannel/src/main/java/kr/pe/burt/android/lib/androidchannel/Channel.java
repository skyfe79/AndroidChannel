package kr.pe.burt.android.lib.androidchannel;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/**
 * Created by burt on 15. 9. 23..
 */
public class Channel {

    HandlerThread workerThread;

    Handler mainThreadHandler;
    Handler workerThreadHandler;

    public Channel(final UiCallback uiCallback, final WorkerCallback workerCallback) {
        mainThreadHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return uiCallback.handleUiMessage(msg);
            }
        });
        workerThread = new HandlerThread("channel-worker-thread");
        workerThread.start();
        workerThreadHandler = new Handler(workerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return workerCallback.handleWorkerMessage(msg);
            }
        });
    }


    public Handler toUI() {
        return mainThreadHandler;
    }

    public Handler toWorker() {
        return workerThreadHandler;
    }


    public interface UiCallback {
        boolean handleUiMessage(Message msg);
    }

    public interface WorkerCallback {
        boolean handleWorkerMessage(Message msg);
    }
}
