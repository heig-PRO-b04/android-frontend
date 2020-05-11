package ch.heigvd.pro.b04.android.Utils;

import androidx.lifecycle.LiveData;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A {@link LiveData} that regularly emits some {@link Unit} instances, at a fixed interval. This
 * might be useful when it comes to polling some services regularly without a push mechanism in
 * place.
 */
public class PollingLiveData extends LiveData<Unit> {

    private Timer timer = new Timer();
    private int rateInMillis;

    public PollingLiveData(int millis) {
        this.rateInMillis = millis;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onActive() {
        super.onActive();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                postValue(Unit.INSTANCE);
            }
        }, 0, rateInMillis);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onInactive() {
        super.onInactive();
        timer.cancel();
        timer.purge();
    }
}
