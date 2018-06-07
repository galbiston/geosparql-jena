/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index.expiring;

import java.util.Objects;

/**
 *
 *
 */
public class KeyTimestampPair implements Comparable<KeyTimestampPair> {

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
    public int compareTo(KeyTimestampPair o) {

        if (o == null) {
            throw new NullPointerException();
        }

        if (timestamp < o.timestamp) {
            return -1;
        } else if (timestamp > o.timestamp) {
            return 1;
        } else {
            if (key.equals(o.key)) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.key);
        hash = 67 * hash + (int) (this.timestamp ^ (this.timestamp >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KeyTimestampPair other = (KeyTimestampPair) obj;
        if (this.timestamp != other.timestamp) {
            return false;
        }
        return Objects.equals(this.key, other.key);
    }

    @Override
    public String toString() {
        return "KeyTimestampPair{" + "key=" + key + ", timestamp=" + timestamp + '}';
    }
}
