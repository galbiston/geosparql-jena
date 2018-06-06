/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index.expiring;

/**
 *
 *
 */
public class KeyTimestampPair {

    private final Object key;
    private long timestamp;

    public KeyTimestampPair(Object key, long timestamp) {
        this.key = key;
        this.timestamp = timestamp;
    }

    public boolean hasKey(Object key) {
        return this.key.equals(key);
    }

    public Object getKey() {
        return key;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isEarlier(long timestamp) {
        return this.timestamp < timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "KeyTimestampPair{" + "key=" + key + ", timestamp=" + timestamp + '}';
    }
}
