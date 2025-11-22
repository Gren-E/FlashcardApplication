package com.fa.util.timer;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class CountdownTimer {

    private LocalDateTime finishTimestamp;
    private LocalDateTime timerStoppedTimestamp;

    private TimerListener timerListener;

    private Timer timer;

    public void countUntil(LocalDateTime finishTimestamp) {
        this.finishTimestamp = finishTimestamp;
    }

    public void setTimerListener(TimerListener listener) {
        timerListener = listener;
    }

    public long getRemainingTimeInSeconds() {
        long seconds = -1;
        if (timerStoppedTimestamp != null) {
            seconds = ChronoUnit.SECONDS.between(timerStoppedTimestamp, finishTimestamp);
        } else if (finishTimestamp != null && LocalDateTime.now().isBefore(finishTimestamp)) {
            seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), finishTimestamp);
        }

        return seconds;
    }

    public LocalDateTime getTimerStoppedTimestamp() {
        return timerStoppedTimestamp;
    }

    public void stop() {
        if (timer != null) {
            timerStoppedTimestamp = LocalDateTime.now();
            timer.cancel();
            timer.purge();
        }
    }

    public void resume() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timerListener != null) {
                    timerListener.updateTime(getRemainingTimeInSeconds());
                }
            }
        }, 0, 1000);

        timerStoppedTimestamp = null;
    }

}
