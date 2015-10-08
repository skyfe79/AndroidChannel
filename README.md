[![Android Gems](http://www.android-gems.com/badge/skyfe79/AndroidChannel.svg?branch=master)](http://www.android-gems.com/lib/skyfe79/AndroidChannel)

# AndroidChannel

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AndroidChannel-green.svg?style=flat)](https://android-arsenal.com/details/1/2547)

AndroidChannel is helper library for inter thread communication between main thread and worker thread. AndroidChannel uses HandlerThread for inter thread communication.



## Setup Gradle

```groovy
dependencies {
	...
    compile 'kr.pe.burt.android.lib:androidchannel:0.0.2'
}
```

## Example

### AndroidChannel-ProgressLayout

I have reimplemented [ProgressLayout](https://github.com/iammert/ProgressLayout) by using AndroidChannel for example. You can check out souce code at [AndroidChannel-ProgressLayout](example/AndroidChannel-ProgressLayout). It shows how to use AndroidChannel when you want to implement a custom animatable view.

### Ping-Pong 

You can make a channel between main thread and worker thread easily.

```java
channel = new Channel(new Channel.UiCallback() {
    @Override
    public boolean handleUiMessage(Message msg) {

        if(msg.what == PING) {
            Log.d("TAG", "PING");
            channel.toWorker().sendEmptyMessageDelayed(PONG, 1000);
        }
        return false;
    }
}, new Channel.WorkerCallback() {
    @Override
    public boolean handleWorkerMessage(Message msg) {

        if(msg.what == PONG) {
            Log.d("TAG", "PONG");
            channel.toUI().sendEmptyMessageDelayed(PING, 1000);
        }
        return false;
    }
});
channel.toUI().sendEmptyMessage(PING);
```

### Timer

If you use AndroidChannel, You can make Timer more easily. I already make Timer class for you. You can just use it. If you want to know how to implement Timer class, you just read [souce code](https://github.com/skyfe79/AndroidChannel/blob/master/androidchannel/src/main/java/kr/pe/burt/android/lib/androidchannel/Timer.java) in the package.

```java
timer = new Timer(1000, new Timer.OnTimer() {
    int count = 0;
    @Override
    public void onTime(Timer timer) {
        count++;
        textView.setText("count : " + count);
    }
});
timer.start();	
```

You can also stop the timer like this.

```java
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
```

## APIs

* channel.open()
 * Use open() method to open channel. If you created a channel by Channel constructor, it is automatically open the channel by default. 
* channel.close() 
 * Use close() method to close channel. close() method removes callbacks and messages in the message queue.
* channel.toUI() 
 * toUI() method returns main thread handler. If you want to send messages to ui thread you should use toUI() method.
* channel.toWorker()
 * toWorker() method returns worker thread handler. If you want to send messages to worker thread you should use toWorker() method.    	

## MIT License

The MIT License

Copyright © 2015 Sungcheol Kim, http://github.com/skyfe79/AndroidChannel

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
