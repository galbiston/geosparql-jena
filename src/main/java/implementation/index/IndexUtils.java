/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 */
public class IndexUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexUtils.class);

    public static final void write(File indexFile, Map index) {

        LOGGER.info("Writing Index - {}: Started", indexFile);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(indexFile))) {
            objectOutputStream.writeObject(index);
        } catch (IOException ex) {
            LOGGER.error("Store Index exception: {}", ex.getMessage());
        }
        LOGGER.info("Writing Index - {}: Completed", indexFile);
    }

    @SuppressWarnings("unchecked")
    public static final void read(File indexFile, Map index) {
        LOGGER.info("Reading Index - {}: Started", indexFile);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(indexFile))) {
            @SuppressWarnings("unchecked")
            Map readIndex = (Map) objectInputStream.readObject();
            //TODO ensure index is large enough to take readIndex. Use interface to access setMaxSize method. Require making all indexes objects rather than static variables.
            index.putAll(readIndex);
        } catch (IOException | ClassNotFoundException ex) {
            LOGGER.error("Read Index exception: {}", ex.getMessage());
        }
        LOGGER.info("Reading Index - {}: Completed", indexFile);
    }

}
