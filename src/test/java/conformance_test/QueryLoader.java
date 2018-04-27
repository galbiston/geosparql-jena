/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conformance_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Greg Albiston
 */
public class QueryLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryLoader.class);

    public static String read(String queryFilename) {

        InputStream input = QueryLoader.class.getClassLoader().getResourceAsStream(queryFilename);

        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (NullPointerException | IOException ex) {
            LOGGER.error("Could not open query file: {}", queryFilename);
            return null;
        }
    }

    public static String read(InputStream input) {

        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (NullPointerException | IOException ex) {
            LOGGER.error("Could not open query file: {}", input.toString());
            return null;
        }
    }

    public static String read(File inputFile) {

        try (BufferedReader buffer = new BufferedReader(new FileReader(inputFile))) {
            return buffer.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (NullPointerException | IOException ex) {
            LOGGER.error("Could not open query file: {}", inputFile.getPath());
            return null;
        }
    }

}
