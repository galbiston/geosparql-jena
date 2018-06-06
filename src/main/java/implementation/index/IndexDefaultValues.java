/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

/**
 *
 *
 */
public interface IndexDefaultValues {

    public static final int UNLIMITED_INDEX = -1;

    public static final int NO_INDEX = 0;
    public static final long INDEX_EXPIRY_INTERVAL = 60000l;
    public static final long FULL_INDEX_WARNING_INTERVAL = 30000l;
    public static final long INDEX_CLEANER_INTERVAL = 1000l;
    public static final long MINIMUM_INDEX_CLEANER_INTERVAL = 100l;
    public static final int UNLIMITED_INITIAL_CAPACITY = 50000;
}
