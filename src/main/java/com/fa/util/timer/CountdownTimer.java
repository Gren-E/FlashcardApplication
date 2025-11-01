package com.fa.util.timer;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class CountdownTimer {

    private LocalDateTime finishTimestamp;

    private TimerListener timerListener;

    public CountdownTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timerListener != null) {
                    timerListener.updateTime(getRemainingTimeInSeconds());
                }
            }
        }, 0, 1000);}

    public void countUntil(LocalDateTime finishTimestamp) {
        this.finishTimestamp = finishTimestamp;
    }

    public void setTimerListener(TimerListener listener) {
        timerListener = listener;
    }

    public long getRemainingTimeInSeconds() {
        long seconds = 0;
        if (finishTimestamp != null && LocalDateTime.now().isBefore(finishTimestamp)) {
            seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), finishTimestamp);
        }
        return seconds;
    }
}
