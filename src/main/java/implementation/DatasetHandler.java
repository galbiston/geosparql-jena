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
package implementation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.solver.stats.Stats;
import org.apache.jena.tdb.solver.stats.StatsResults;
import org.apache.jena.tdb.store.DatasetGraphTDB;
import org.apache.jena.tdb.sys.TDBInternal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tdb.tdbstats;

/**
 *
 *
 */
public class DatasetHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final Boolean loadDataset(Dataset dataset, String graphName, String sourceRDFFile, Boolean inferenceEnabled, Model geosparqlSchema) {

        Boolean isCompleted = true;

        try {
            dataset.begin(ReadWrite.WRITE);

            LOGGER.info("Loading: {} into {}: Started", sourceRDFFile, graphName);
            Model dataModel = RDFDataMgr.loadModel(sourceRDFFile);
            if (inferenceEnabled) {
                InfModel infModel = ModelFactory.createRDFSModel(geosparqlSchema, dataModel);
                infModel.prepare();
                if (graphName.isEmpty()) {
                    Model defaultModel = dataset.getDefaultModel();
                    defaultModel.add(infModel);
                } else {
                    dataset.addNamedModel(graphName, infModel);
                }
            } else {
                if (graphName.isEmpty()) {
                    Model defaultModel = dataset.getDefaultModel();
                    defaultModel.add(dataModel);
                } else {
                    dataset.addNamedModel(graphName, dataModel);
                }
            }
            dataset.commit();
            LOGGER.info("Loading: {} into {}: Completed", sourceRDFFile, graphName);
        } catch (Exception ex) {
            isCompleted = false;
            LOGGER.error("TDB Load Error: {}", ex.getMessage());
        } finally {
            dataset.end();
        }

        return isCompleted;
    }

    public static void optimiseTDB(File datasetFolder) {
        //TDB Optimisation file generation based on:
        //https://jena.apache.org/documentation/tdb/optimizer.html#generating-statistics-for-union-graphs
        //https://github.com/apache/jena/blob/master/jena-cmds/src/main/java/tdb/tdbstats.java

        Dataset dataset = TDBFactory.createDataset(datasetFolder.getAbsolutePath());

        dataset.begin(ReadWrite.READ);
        File statsFile = new File(datasetFolder, "stats.opt");
        try (FileOutputStream outputStream = new FileOutputStream(statsFile)) {
            DatasetGraphTDB datasetGraphTDB = TDBInternal.getDatasetGraphTDB(dataset);

            Node unionGraph = NodeFactory.createURI("urn:x-arq:UnionGraph");
            tdbstats.init();
            StatsResults statsResults = tdbstats.stats(datasetGraphTDB, unionGraph);
            Stats.write(outputStream, statsResults);
        } catch (IOException ex) {
            LOGGER.error("Optimise TDB Error: {} - {}", statsFile, ex.getMessage());
        } finally {
            dataset.end();
            dataset.close();
            TDBFactory.release(dataset);
        }
    }

    public static Boolean clearDataset(File datasetFolder) {

        Dataset dataset = TDBFactory.createDataset(datasetFolder.getAbsolutePath());
        Boolean isCleared;
        try {
            dataset.begin(ReadWrite.WRITE);
            Iterator<String> iterator = dataset.listNames();
            while (iterator.hasNext()) {
                String graphName = iterator.next();
                dataset.removeNamedModel(graphName);
            }
            Model defaultModel = dataset.getDefaultModel();
            defaultModel.removeAll();
            dataset.commit();
            isCleared = true;
        } catch (Exception ex) {
            LOGGER.error("TDB Folder clearance: {} - {}", datasetFolder.getAbsolutePath(), ex.getMessage());
            isCleared = false;
        } finally {
            dataset.end();
        }

        dataset.close();
        TDBFactory.release(dataset);
        return isCleared;
    }

}
