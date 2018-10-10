# GeoSPARQL Jena #

Implemenation of GeoSPARQL 1.0 standard using Apache Jena for SPARQL query or API.

## Features
This implementation follows the 11-052r4 OGC GeoSPARQL standard (http://www.opengeospatial.org/standards/geosparql).
The implementation is pure Java and does not require any set-up or configuration of any third party relational databases and geospatial extensions.

It implements the six Conformance classes described in the GeoSPARQL document:

* Core
* Topology Vocabulary
* Geometry Extension
* Geometry Topology
* RDFS Entailment Extension
* Query Rewrite Extension

The WKT (as described in 11-052r4) and GML 2.0 Simple Features Profile (11-100r3) serialisations are supported.
Additional serialisations can be implemented by extending the `io.github.galbiston.geosparql_jena.implementation.datatype.GeometryDatatype`.

All three spatial relation families are supported: _Simple Feature_, _Egenhofer_ and _RCC8_.

Indexing and caching of spatial objects and relations is performed _on-demand_ during query execution.
Therefore, set-up delays should be minimal.

Benchmarking of the implementation against Strabon and Parliament has found it to be comparable or quicker.
The benchmarking used was the Geographica query and dataset (http://geographica.di.uoa.gr/).
Publication of the benchmarking results are forthcoming.

## Additional Features
The following additional features are also provided:

* Geometry properties are automatically calculated and do not need to be asserted in the dataset.
* Conversion between EPSG spatial/coordinate reference systems is applied automatically. Therefore, mixed datasets or querying can be applied. This is reliant upon local installation of Apache SIS EPSG dataset, see below.
* Units of measure are automatically converted to the appropriate units for the coordinate reference system.
* Geometry, transformation and spatial relation results are stored in persistent and configurable time-limited caches to improve response times and reduce recalculations.
* Dataset conversion between serialisations and spatial/coordinate reference systems. Tabular data can also be loaded, see RDF Tables project (https://github.com/galbiston/rdf-tables).

## Getting Started
GeoSPARQL Jena can be accessed as a library using Maven etc. from Maven Central.

```
<dependency>
    <groupId>io.github.galbiston</groupId>
    <artifactId>geosparql-jena</artifactId>
    <version>1.0.2</version>
</dependency>
```

A HTTP server (SPARQL endpoint) using Apache Jena's Fuseki is available from the GeoSPARQL Fuseki project (https://github.com/galbiston/geosparql-fuseki).

### SPARQL Query Configuration
Using the library for SPARQL querying requires one line of code.
All indexing and caching is performed during query execution and so there should be minimal delay during initialisation.
This will registers the Property Functions with ARQ query engine and configures the _indexes_ used for time-limited caching.

There are three _indexes_ which can be configured independently or switched off.
These _indexes_ retain data that may be required again when a query is being executed but may not be required between different queries.
Therefore, the memory usage will grow during query execution and then recede as data is not re-used.
All the _indexes_ support concurrency and can be set to a maximum size or allowed to increase capacity as required.

* _Geometry Literal_: Geometry objects following deserialisation from `Geometry Literal`.
* _Geometry Transform_: Geometry objects resulting from coordinate transformations.
* _Query Rewrite_: results of spatial relations between `Feature` and `Geometry` spatial objects.

Testing has found up to 20% improvement in query completion durations using the indexes.
The _indexes_ can be configured by size, retention duration and frequency of clean up.

* Basic setup with default values: `GeoSPARQLConfig.setupMemoryIndex()`

* Indexes set to maximum sizes: `GeoSPARQLConfig.setupMemoryIndexSize(50000, 50000, 50000)`

* Indexes set to remove objects not used after 5 seconds: `GeoSPARQLConfig.setupMemoryIndexExpiry(5000, 5000, 5000)`

* No indexes setup: `GeoSPARQLConfig.setupNoIndex()`

* No indexes and no query rewrite setup: `GeoSPARQLConfig.setupNoIndex(false)`

* Reset indexes: `GeoSPARQLConfig.reset()`

A variety of configuration methods are provided in `io.github.galbiston.geosparql_jena.configuration.GeoSPARQLConfig`.
Caching of frequently used but small quantity data is also applied in several _registries_, e.g. coordinate reference systems and mathematical transformations.

Example GeoSPARQL query:

```
PREFIX geo: <http://www.opengis.net/ont/geosparql#>

SELECT ?obj
WHERE{
    ?subj geo:sfContains ?obj
}ORDER by ?obj
```

### Example SPARQL Query

The setup of GeoSPARQL Jena only needs to be performed once in an application.
After it is setup then perform querying using Apache Jena's standard query methods.

To query a Model with GeoSPARQL or standard SPARQL:

```
GeoSPARQLConfig.setupMemoryIndex();
Model model = .....;
String query = ....;

try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
    ResultSet rs = qe.execSelect();
    ResultSetFormatter.outputAsTSV(rs);
}
```

More information on SPARQL querying using Apache Jena can be found on their website (https://jena.apache.org/tutorials/sparql.html).
If your dataset needs to be separate from your application and accessed over HTTP then you probably need the GeoSPARQL Fuseki project(https://github.com/galbiston/geosparql-fuseki).
The GeoSPARQL functionality needs to be setup where the dataset is located.

### API
The library can be used as an API in Java.
The main class to handle geometries and their spatial relations is the `GeometryWrapper`.
This can be obtained by parsing the string representation of a geometry using the appropriate datatype (WKT or GML).
There is overlap between spatial relation families so repeated methods are not specified.

* Parse a `Geometry Literal`: `GeometryWrapper geometryWrapper = WKTDatatype.INSTANCE.parse("POINT(1 1)");`

* Extract from a Jena Literal: `GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometryLiteral);`

* Convert CRS/SRS: `GeometryWrapper otherGeometryWrapper = geometryWrapper.convertCRS("http://www.opengis.net/def/crs/EPSG/0/27700")`

* Spatial Relation: `boolean isCrossing = geometryWrapper.crosses(otherGeometryWrapper);`

* DE-9IM Intersection Pattern: `boolean isRelated = geometryWrapper.relate(otherGeometryWrapper, "TFFFTFFFT");`

* Geometry Property: `boolean isEmpty = geometryWrapper.isEmpty();`

## Key Dependencies

### GeoSPARQL
The OGC GeoSPARQL standard supports representing and querying geospatial data on the Semantic Web.
GeoSPARQL defines a vocabulary for representing geospatial data in RDF, and it defines an extension to the SPARQL query language for processing geospatial data.
In addition, GeoSPARQL is designed to accommodate systems based on qualitative spatial reasoning and systems based on quantitative spatial computations.

The GeoSPARQL standard is based upon the OGC Simple Features standard (http://www.opengeospatial.org/standards/sfa) used in relational databases.
Modifications and enhancements have been made for usage with RDF and SPARQL.
The Simple Features standard, and by extension GeoSPARQL, simplify calculations to Euclidean planer geometry.
Therefore, datasets using a geographic spatial/coordinate reference system, which are based on latitude and longitude on an ellipsoid, e.g. WGS84, will have minor error introduced.
This error has been deemed acceptable due to the simplification in calculation it offers.

### Apache Jena
A Java framework for building Semantic Web and Linked Data applications.
The framework provides standard compliance for RDF and SPARQL and include extensions for persistent storage (TDB) and HTTP server (Fuseki).

### Apache SIS/SIS_DATA Environment Variable
Apache Spatial Information System (SIS) is a free software, Java language library for developing geospatial applications.
SIS provides data structures for geographic features and associated metadata along with methods to manipulate those data structures.
The library is an implementation of GeoAPI 3.0 interfaces and can be used for desktop or server applications.

A subset of the EPSG spatial/coordinate reference systems are included by default.
The full EPSG dataset is not distributed due to the EPSG terms of use being incompatible with the Apache Licence.
Several options are available to include the EPSG dataset by setting the `SIS_DATA` environment variable (http://sis.apache.org/epsg.html).

An embedded EPSG dataset can be included in a Gradle application by adding the following dependency to `build.gradle`:
```
ext.sisVersion = "0.8"
implementation "org.apache.sis.non-free:sis-embedded-data:$sisVersion"
```

### Java Topology Suite
The JTS Topology Suite is a Java library for creating and manipulating vector geometry.

## Note
Below are implementation points that may be useful during usage.

### GeoSPARQL Schema
An RDF/XML schema has been published for the GeoSPARQL v1.0 standard (v1.0.1 - http://schemas.opengis.net/geosparql/1.0/geosparql_vocab_all.rdf).
This can be applied to Jena Models (https://jena.apache.org/documentation/inference/) to provide RDFS and OWL inferencing on a GeoSPARQL conforming dataset.
However, the published schema does not conform with the standard.

The property `hasDefaultGeometry` is missing from the schema and instead the `defaultGeometry` property is stated.

This prevents RDFS inferencing being performed correctly and has been reported to the OGC Standards Tracker.
A corrected version of the schema is available in the `Resources` folder.

### Spatial Relations
The GeoSPARQL and Simple Features standard both define the DE-9IM intersection patterns for the three spatial relation families.
However, these patterns are not consistent with the patterns stated by the JTS library for certain relations.

For example, GeoSPARQL/Simple Features use `TFFFTFFFT` _equals_ relations in _Simple Feature_, _Egenhofer_ and _RCC8_.
However, this does not yield a true result when comparing a pair of point geometries.
The Simple Features standard states that the boundary of a point is empty.
Therefore, the boundary intersection of two points would also be empty.

JTS, and others, use the alternative intersection pattern of `T*F**FFF*`.
This is a combination of the _within_ and _contains_ relations and yields the expected results for all geometry types.

The spatial relations utilised by JTS have been applied in the library but feedback on this choice is welcome.
A user can supply their own DE-9IM intersection patterns by using the `geof:relate` filter function.

### Spatial Relations and Geometry Shapes/Types
The spatial relations for the three spatial families do not apply to all combinations of the geometry shapes (`Point`, `LineString`, `Polygon`) and their collections  (`MultiPoint`, `MultiLineString`, `MultiPolygon`).
Therefore, some queries may not produce all the results that may initially be expected.

Some examples are:
* In some relations there may only be results when the collection is being used, e.g. two multi points can overlap but two points cannot.
* A relation may only apply for one combination but not its reciprocal, e.g. a line may cross a polygon but a polygon may not cross a line.
* The _RCC8_ family only applies to `Polygon` and `MultiPolygon` types.

Refer to pages 8-10 of 11-052r4 GeoSPARQL standard for more details.

### Equals Relations
The three equals relations (_sfEquals_, _ehEquals_ and _rccEquals_) use spatial equality and not lexical equality.
Therefore, some comparisons using these relations may not be as expected.

The JTS description of _sfEquals_ is:
* True if two geometries have at least one point in common and no point of either geometry lies in the exterior of the other geometry.

Therefore, two empty geometries are not spatially equal and will return false.
Shapes which differ in the number of points but have the same geometry are equal and will return true.

e.g. `LINESTRING (0 0, 0 10)` and `LINESTRING (0 0, 0 5, 0 10)` are spatially equal.

### Query Rewrite Extension
The Query Rewrite Extension provides for simpler querying syntax.
`Feature` and `Geometry` can be used in spatial relations without needing the relations to be asserted in the dataset.
This also means the `Geometry Literal` does not need to be specified in the query.
In the case of `Features` this requires the `hasDefaultGeometry` property to be used in the dataset.

This means the query:

```
    ?subj geo:hasDefaultGeometry ?subjGeom .
    ?subjGeom geo:hasSerialization ?subjLit .

    ?obj geo:hasDefaultGeometry ?objGeom .
    ?objGeom geo:hasSerialization ?objLit .

    FILTER(geof:sfContains(?subjLit, ?objLit))
```

becomes:

```
    ?subj geo:sfContains ?obj .
```

Methods are available to apply the `hasDefaultGeometry` property to every `Geometry` with a single `hasGeometry` property, see `io.github.galbiston.geosparql_jena.configuration.GeoSPARQLOperations`.

Depending upon the spatial relation, queries may include the specified `Feature` and `Geometry` in the results.
e.g. FeatureA is bound in a query on a dataset only containing FeatureA and GeometryA. The results FeatureA and GeometryA are returned rather than no results.
Therefore, filtering using `FILTER(!sameTerm(?subj, ?obj))` etc. may be needed in some cases.
The query rewrite functionality can be switched off in the library configuration.

### Dataset Conversion
Methods to convert datasets between serialisations and spatial/coordinate reference systems are available in:
`io.github.galbiston.geosparql_jena.implementation.data_conversion.ConvertData`

These conversions can be applied to files, folders and Jena Models.

## Future Work

* Implementing GeoJSON as a `GeometryLiteral` serialisation (https://tools.ietf.org/html/rfc7946).

## Contributors
The following individuals have made contributions to this project:

* Greg Albiston
* Haozhe Chen
* Taha Osman

## Why Use This Implementation
There are several implementations of the GeoSPARQL standard.
The conformance and completeness of these implementations is difficult to ascertain and varies between points.

However, the following may be of interest when considering whether to use this implementation based on reviewing several alternatives.

This Implementation|Other Implementations
---------- | ----------
Implements all six components of the GeoSPARQL standard.|Generally partially implement the Geometry Topology and Geometry Extensions. Do not implement the Query Rewrite Extension.
Pure Java and does not require a supporting relational database. Configuration requires a single line of code (although Apache SIS may some need setting up, see above).|Require setting up a database, configuring a geospatial extension and setting environment variables.
Uses Apache Jena, which conforms to the W3C standards for RDF and SPARQL. New versions of the standards will quickly feed through.|Not fully RDF and SPARQL compliant, e.g. RDFS/OWL inferencing or SPARQL syntax. Adding your own schema probably won't produce inferences.
Automatically determines geometry properties and handles mixed cases of units or coordinate reference systems. The GeoSPARQL standard suggests this approach but does not require it.|Tend to produce errors or no results in these situations.
Performs indexing and caching on-demand which reduces set-up time and only performs calculations that are required.|Perform indexing in the data loading phase and initialisation phase, which can lead to lengthy delays (even on relatively small datasets).
Uses JTS which does not truncate coordinate precision and applies spatial equality.|May truncate coordinate precision and apply lexical equality, which is quicker but does not comply with the standards.

![Powered by Apache Jena](https://www.apache.org/logos/comdev-test/poweredby/jena.png "Powered by Apache Jena")