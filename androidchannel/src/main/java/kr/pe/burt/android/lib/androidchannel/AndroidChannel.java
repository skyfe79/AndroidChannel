package kr.pe.burt.android.lib.androidchannel;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/**
 * Created by burt on 15. 9. 23..
 */
public class AndroidChannel {

    public interface UiCallback {
        boolean handleUiMessage(Message msg);
    }

    public interface WorkerCallback {
        boolean handleWorkerMessage(Message msg);
    }

    HandlerThread   workerThread            = null;

    Handler         mainThreadHandler       = null;
    Handler         workerThreadHandler     = null;

    UiCallback      uiCallback              = null;
    WorkerCallback  workerCallback          = null;

    boolean isChannelOpened                 = false;

    /**
     * Create channel and open channel
     * @param uiCallback        handler callback for ui messages
     * @param workerCallback    handler callback for worker messages
     */
    public AndroidChannel(final UiCallback uiCallback, final WorkerCallback workerCallback) {

        this.uiCallback = uiCallback;
        this.workerCallback = workerCallback;

        open();
    }

    /**
     * To send message to ui thread, You should get mainThreadHandler by using toUI() method
     * @return main thread handler
     */
    public Handler toUI() {
        return mainThreadHandler;
    }

    /**
     * To send message to worker thread, You should get workerThreadHandler by using toWorker() method
     * @return worker thread handler
     */
    public Handler toWorker() {
        return workerThreadHandler;
    }

    /**
     * Open channel
     * @return true if success to open channel, or return false
     */
    public boolean open() {

        if(isChannelOpened == true) {
            return true;
        }

        if(uiCallback == null || workerCallback == null)
            return false;

        mainThreadHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return uiCallback.handleUiMessage(msg);
            }
        });

        workerThread = new HandlerThread("android-channel-worker-thread");
        workerThread.start();
        workerThreadHandler = new Handler(workerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return workerCallback.handleWorkerMessage(msg);
            }
        });

        isChannelOpened = true;
        return true;
    }

    /**
     * Close channel
     */
    public void close() {
        if(isChannelOpened == false)
            return;

        mainThreadHandler.removeCallbacksAndMessages(null);
        workerThreadHandler.removeCallbacksAndMessages(null);

        workerThread.quit();
        workerThread = null;

        workerThreadHandler = null;
        mainThreadHandler = null;

        isChannelOpened = false;
    }

}
