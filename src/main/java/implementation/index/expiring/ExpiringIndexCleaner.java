/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index.expiring;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class ExpiringIndexCleaner extends TimerTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TreeSet<KeyTimestampPair> tracking = new TreeSet<>();
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

            KeyTimestampPair current = tracking.first();
            isEarlier = current.isEarlier(thresholdTimestamp);
            if (isEarlier) {
                tracking.pollFirst();
                Object key = current.getKey();
                if (refresh.containsKey(key)) {
                    Long timestamp = refresh.get(key);
                    tracking.add(new KeyTimestampPair(key, timestamp));
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

    public void setExpiryInterval(long expiryInterval) {
        this.expiryInterval = expiryInterval;
    }

    public void clear() {
        tracking.clear();
    }

}
