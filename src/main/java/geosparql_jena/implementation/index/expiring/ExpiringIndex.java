/*
 * Copyright 2018 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geosparql_jena.implementation.index.expiring;

import static geosparql_jena.implementation.index.IndexDefaultValues.FULL_INDEX_WARNING_INTERVAL;
import static geosparql_jena.implementation.index.IndexDefaultValues.INDEX_CLEANER_INTERVAL;
import static geosparql_jena.implementation.index.IndexDefaultValues.INDEX_EXPIRY_INTERVAL;
import static geosparql_jena.implementation.index.IndexDefaultValues.MINIMUM_INDEX_CLEANER_INTERVAL;
import static geosparql_jena.implementation.index.IndexDefaultValues.UNLIMITED_INDEX;
import static geosparql_jena.implementation.index.IndexDefaultValues.UNLIMITED_INITIAL_CAPACITY;
import java.lang.invoke.MethodHandles;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @param <K>
 * @param <V>
 */
public class ExpiringIndex<K, V> extends ConcurrentHashMap<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private String label;
    private long maxSize;
    private long expiryInterval;
    private long cleanerInterval;
    private long fullIndexWarningInterval;
    private long fullIndexWarning;
    private ExpiringIndexCleaner indexCleaner;
    private Timer cleanerTimer;

    /**
     * Instance of Expiring Index that will remove items after a period of time
     * which have not been accessed.
     *
     * @param label Name of the index.
     * @param maxSize Maximum size of the index when items will no longer be
     * added. Unlimited size (-1) will still remove expired items.
     */
    public ExpiringIndex(String label, int maxSize) {
        this(label, maxSize, INDEX_EXPIRY_INTERVAL, INDEX_CLEANER_INTERVAL, FULL_INDEX_WARNING_INTERVAL);
    }

    /**
     * Instance of Expiring Index that will remove items after a period of time
     * which have not been accessed.
     *
     * @param label Name of the index.
     * @param maxSize Maximum size of the index when items will no longer be
     * added. Unlimited size (-1) will still remove expired items.
     * @param expiryInterval Duration that items remain in index.
     */
    public ExpiringIndex(String label, int maxSize, long expiryInterval) {
        this(label, maxSize, expiryInterval, INDEX_CLEANER_INTERVAL, FULL_INDEX_WARNING_INTERVAL);
    }

    /**
     * Instance of Expiring Index that will remove items after a period of time
     * which have not been accessed.
     *
     * @param label Name of the index.
     * @param maxSize Maximum size of the index when items will no longer be
     * added. Unlimited size (-1) will still remove expired items.
     * @param expiryInterval Duration that items remain in index.
     * @param cleanerInterval Frequency that items are checked for removal from
     * index.
     */
    public ExpiringIndex(String label, int maxSize, long expiryInterval, long cleanerInterval) {
        this(label, maxSize, expiryInterval, cleanerInterval, FULL_INDEX_WARNING_INTERVAL);
    }

    /**
     * Instance of Expiring Index that will remove items after a period of time
     * which have not been accessed.
     *
     * @param label Name of the index.
     * @param maxSize Maximum size of the index when items will no longer be
     * added. Unlimited size (-1) will still remove expired items.
     * @param expiryInterval Duration that items remain in index.
     * @param cleanerInterval Frequency that items are checked for removal from
     * index.
     * @param fullIndexWarningInterval Full index warning frequency.
     */
    public ExpiringIndex(String label, int maxSize, long expiryInterval, long cleanerInterval, long fullIndexWarningInterval) {
        super(maxSize > UNLIMITED_INDEX ? maxSize : UNLIMITED_INITIAL_CAPACITY);
        this.label = label;
        this.maxSize = maxSize > UNLIMITED_INDEX ? maxSize : Long.MAX_VALUE;
        setCleanerInterval(cleanerInterval);
        setExpiryInterval(expiryInterval);
        this.fullIndexWarningInterval = fullIndexWarningInterval;
        this.fullIndexWarning = System.currentTimeMillis();
        this.indexCleaner = new ExpiringIndexCleaner(this, expiryInterval);

        this.cleanerTimer = null;
    }

    @Override
    public V put(K key, V value) {
        if (super.mappingCount() < maxSize) {
            indexCleaner.put(key);
            return super.put(key, value);
        } else {
            long currentSystemTime = System.currentTimeMillis();
            long difference = currentSystemTime - fullIndexWarning;
            if (difference > fullIndexWarningInterval) {
                fullIndexWarning = currentSystemTime;
                LOGGER.warn("{} Index Full: {} - Warning suppressed for {}ms", label, maxSize, fullIndexWarningInterval);
            }
        }

        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean isContained = super.containsKey(key);
        if (isContained) {
            indexCleaner.refresh(key);
        }
        return isContained;
    }

    @Override
    public V get(Object key) {
        V value = super.get(key);
        if (value != null) {
            indexCleaner.refresh(key);
        }
        return value;
    }

    @Override
    public void clear() {
        super.clear();
        indexCleaner.clear();
    }

    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize > UNLIMITED_INDEX ? maxSize : Long.MAX_VALUE;
    }

    public long getCleanerInterval() {
        return cleanerInterval;
    }

    public final void setCleanerInterval(long cleanerInterval) {
        if (MINIMUM_INDEX_CLEANER_INTERVAL < cleanerInterval) {
            this.cleanerInterval = cleanerInterval;
        } else {
            LOGGER.warn("Cleaner Interval: {} less than minimum: {}. Setting to minimum.", cleanerInterval, MINIMUM_INDEX_CLEANER_INTERVAL);
            this.cleanerInterval = MINIMUM_INDEX_CLEANER_INTERVAL;
        }
    }

    public long getExpiryInterval() {
        return expiryInterval;
    }

    public final void setExpiryInterval(long expiryInterval) {

        long minimum_interval = cleanerInterval + 1;
        if (expiryInterval < minimum_interval) {
            LOGGER.warn("Expiry Interval: {} cannot be less than Cleaner Interval: {}. Setting to Minimum Interval: {}", expiryInterval, cleanerInterval, minimum_interval);
            this.expiryInterval = minimum_interval;
        } else {
            this.expiryInterval = expiryInterval;
        }

        if (this.indexCleaner != null) {
            this.indexCleaner.setExpiryInterval(this.expiryInterval);
        }
    }

    public long getFullIndexWarningInterval() {
        return fullIndexWarningInterval;
    }

    public void setFullIndexWarningInterval(long fullIndexWarningInterval) {
        this.fullIndexWarningInterval = fullIndexWarningInterval;
    }

    public long getFullIndexWarning() {
        return fullIndexWarning;
    }

    public void setFullIndexWarning(long fullIndexWarning) {
        this.fullIndexWarning = fullIndexWarning;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void startExpiry() {
        startExpiry(cleanerInterval);
    }

    public void startExpiry(long cleanerInterval) {
        if (cleanerTimer == null) {
            setCleanerInterval(cleanerInterval);
            cleanerTimer = new Timer(label, true);
            indexCleaner = new ExpiringIndexCleaner(indexCleaner);
            cleanerTimer.scheduleAtFixedRate(indexCleaner, this.cleanerInterval, this.cleanerInterval);
        }
    }

    public void stopExpiry() {
        if (cleanerTimer != null) {
            cleanerTimer.cancel();
            cleanerTimer = null;
        }
    }

    @Override
    public String toString() {
        return "ExpiringIndex{" + "label=" + label + ", maxSize=" + maxSize + ", expiryInterval=" + expiryInterval + ", cleanerInterval=" + cleanerInterval + ", fullIndexWarningInterval=" + fullIndexWarningInterval + ", fullIndexWarning=" + fullIndexWarning + ", indexCleaner=" + indexCleaner + ", cleanerTimer=" + cleanerTimer + '}';
    }

}
