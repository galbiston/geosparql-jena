package benchmarks;

import implementation.GeoSPARQLSupport;
import implementation.GeometryWrapper;
import implementation.datatype.WKTDatatype;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ResourceFactory;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@State(Scope.Thread)
public class PreparedGeometry {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    GeometryWrapper sourceGeometry;
    GeometryWrapper targetGeometry;

    @Setup
    public void prepare() {
        GeoSPARQLSupport.loadFunctionsNoIndex();
        Literal sourceLiteral = ResourceFactory.createTypedLiteral("LINESTRING (0 5, 10 5)", WKTDatatype.INSTANCE);
        sourceGeometry = GeometryWrapper.extract(sourceLiteral);
        Literal targetLiteral = ResourceFactory.createTypedLiteral("LINESTRING (5 10, 5 0)", WKTDatatype.INSTANCE);
        targetGeometry = GeometryWrapper.extract(targetLiteral);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Warmup(iterations = 1, time = 1)
    @Measurement(iterations = 10, time = 1)
    public void measureIntersection() {
        try {
            GeometryWrapper resultGeometry = sourceGeometry.intersection(targetGeometry);
        } catch (FactoryException | MismatchedDimensionException | TransformException ex) {
            LOGGER.error("Exception: {}", ex.getMessage());
        }
    }

}
