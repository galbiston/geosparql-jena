/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index.expiring;

import java.util.TimerTask;
import java.util.TreeSet;

/**
 *
 *
 */
public class ExpiringIndexCleaner extends TimerTask {

    private final TreeSet<KeyTimestampPair> tracking = new TreeSet<>();
    private final ExpiringIndex index;
    private long expiryInterval;

    public ExpiringIndexCleaner(ExpiringIndex index, long expiryInterval) {
        this.index = index;
        this.expiryInterval = expiryInterval;
    }

    @Override
    public void run() {

        long thresholdTimestamp = System.currentTimeMillis() - expiryInterval;
        boolean isEarlier = true;
        while (isEarlier) {
            if (tracking.isEmpty()) {
                return;
            }

            KeyTimestampPair current = tracking.first();
            isEarlier = current.isEarlier(thresholdTimestamp);
            if (isEarlier) {
                index.remove(current.getKey());
                tracking.pollFirst();
            }
        }
    }

    public synchronized void update(Object key) {
        Long timestamp = System.currentTimeMillis();
        boolean isNotMatched = true;
        for (KeyTimestampPair pair : tracking) {
            if (pair.hasKey(key)) {
                pair.setTimestamp(timestamp);
                isNotMatched = false;
                break;
            }
        }

        if (isNotMatched) {
            tracking.add(new KeyTimestampPair(key, timestamp));
        }
    }

    public void setExpiryInterval(long expiryInterval) {
        this.expiryInterval = expiryInterval;
    }

    public void clear() {
        tracking.clear();
    }

}
