/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index.expiring;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.TimerTask;

/**
 *
 *
 */
public class ExpiringIndexCleaner extends TimerTask {

    //private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ArrayDeque<KeyTimestampPair> tracking = new ArrayDeque<>();
    private final HashMap<Object, Long> refresh = new HashMap<>();
    private final ExpiringIndex index;
    private long expiryInterval;

    public ExpiringIndexCleaner(ExpiringIndex index, long expiryInterval) {
        this.index = index;
        this.expiryInterval = expiryInterval;
    }

    @Override
    public void run() {

        //LOGGER.info("Run Start - Tracker: {}, Refresh: {}, Index: {}", tracking.size(), refresh.size(), index.size());
        long thresholdTimestamp = System.currentTimeMillis() - expiryInterval;
        boolean isEarlier = true;
        while (isEarlier) {
            if (tracking.isEmpty()) {
                return;
            }

            KeyTimestampPair current = tracking.getFirst();
            isEarlier = current.isEarlier(thresholdTimestamp);
            if (isEarlier) {
                tracking.pollFirst();
                Object key = current.getKey();
                if (refresh.containsKey(key)) {
                    Long timestamp = refresh.get(key);

                    //Check whether the refresh is still valid. Tolerate that may not be cleared upto (expiryInterval - cleanerInterval) later.
                    if (thresholdTimestamp < timestamp) {
                        tracking.add(new KeyTimestampPair(key, timestamp));
                    }
                    refresh.remove(key);
                } else {
                    index.remove(key);
                }

            }
        }
        //LOGGER.info("Run End - Tracker: {}, Refresh: {}, Index: {}", tracking.size(), refresh.size(), index.size());
    }

    public synchronized void refresh(Object key) {
        refresh.put(key, System.currentTimeMillis());
    }

    public synchronized void put(Object key) {
        tracking.add(new KeyTimestampPair(key, System.currentTimeMillis()));
    }

    public synchronized void setExpiryInterval(long expiryInterval) {
        this.expiryInterval = expiryInterval;
    }

    public synchronized void clear() {
        tracking.clear();
    }

}
