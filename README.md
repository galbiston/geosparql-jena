# GeoSPARQL Jena #

Implementation of GeoSPARQL 1.0 standard using Apache Jena for SPARQL query or API.

## Features
This implementation follows the 11-052r4 OGC GeoSPARQL standard (http://www.opengeospatial.org/standards/geosparql).
The implementation is pure Java and does not require any set-up or configuration of any third party relational databases and geospatial extensions.

It implements the six Conformance Classes described in the GeoSPARQL document:

* Core
* Topology Vocabulary
* Geometry Extension
* Geometry Topology
* RDFS Entailment Extension
* Query Rewrite Extension

The WKT (as described in 11-052r4) and GML 2.0 Simple Features Profile (11-100r3) serialisations are supported.
Additional serialisations can be implemented by extending the `io.github.galbiston.geosparql_jena.implementation.datatype.GeometryDatatype` and registering with Apache Jena's `org.apache.jena.datatypes.TypeMapper`.

All three spatial relation families are supported: _Simple Feature_, _Egenhofer_ and _RCC8_.

Indexing and caching of spatial objects and relations is performed _on-demand_ during query execution.
Therefore, set-up delays should be minimal.

Benchmarking of the implementation against Strabon and Parliament has found it to be comparable or quicker.
The benchmarking used was the Geographica query and dataset (http://geographica.di.uoa.gr/).
Publication of the benchmarking results are forthcoming.

## Additional Features
The following additional features are also provided:

* Geometry properties are automatically calculated and do not need to be asserted in the dataset.
* Conversion between EPSG spatial/coordinate reference systems is applied automatically. Therefore, mixed datasets or querying can be applied. This is reliance upon local installation of Apache SIS EPSG dataset, see __Key Dependencies__.
* Units of measure are automatically converted to the appropriate units for the coordinate reference system.
* Geometry, transformation and spatial relation results are stored in persistent and configurable time-limited caches to improve response times and reduce recalculations.
* Dataset conversion between serialisations and spatial/coordinate reference systems. Tabular data can also be loaded, see RDF Tables project (https://github.com/galbiston/rdf-tables).
* Functions to test Geometry properties directly on Geometry Literals have been included for convenience.

## Getting Started
GeoSPARQL Jena can be accessed as a library using Maven etc. from Maven Central.

```
<dependency>
    <groupId>io.github.galbiston</groupId>
    <artifactId>geosparql-jena</artifactId>
    <version>1.0.7</version>
</dependency>
```

A HTTP server (SPARQL endpoint) using Apache Jena's Fuseki is available from the GeoSPARQL Fuseki project (https://github.com/galbiston/geosparql-fuseki).

### SPARQL Query Configuration
Using the library for SPARQL querying requires one line of code.
All indexing and caching is performed during query execution and so there should be minimal delay during initialisation.
This will register the Property Functions with ARQ query engine and configures the _indexes_ used for time-limited caching.

There are three _indexes_ which can be configured independently or switched off.
These _indexes_ retain data that may be required again when a query is being executed but may not be required between different queries.
Therefore, the memory usage will grow during query execution and then recede as data is not re-used.
All the _indexes_ support concurrency and can be set to a maximum size or allowed to increase capacity as required.

* _Geometry Literal_: Geometry objects following de-serialisation from `Geometry Literal`.
* _Geometry Transform_: Geometry objects resulting from coordinate transformations between spatial reference systems.
* _Query Rewrite_: results of spatial relations between `Feature` and `Geometry` spatial objects.

Testing has found up to 20% improvement in query completion durations using the indexes.
The _indexes_ can be configured by size, retention duration and frequency of clean up.

* Basic setup with default values: `GeoSPARQLConfig.setupMemoryIndex()`

* Indexes set to maximum sizes: `GeoSPARQLConfig.setupMemoryIndexSize(50000, 50000, 50000)`

* Indexes set to remove objects not used after 5 seconds: `GeoSPARQLConfig.setupMemoryIndexExpiry(5000, 5000, 5000)`

* No indexes setup (Query rewrite still performed but results not stored) : `GeoSPARQLConfig.setupNoIndex()`

* No indexes and no query rewriting: `GeoSPARQLConfig.setupNoIndex(false)`

* Reset indexes and other stored data: `GeoSPARQLConfig.reset()`

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

### Querying Datasets & Models with SPARQL

The setup of GeoSPARQL Jena only needs to be performed once in an application.
After it is setup querying is performed using Apache Jena's standard query methods.

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
If your dataset needs to be separate from your application and accessed over HTTP then you probably need the GeoSPARQL Fuseki project (https://github.com/galbiston/geosparql-fuseki).
The GeoSPARQL functionality needs to be setup in the application or Fuseki server where the dataset is located.

It is **recommended** that `hasDefaultGeometry` properties are included in the dataset to access all functionality.
It is **necessary** that `SpatialObject` classes are asserted or inferred (i.e. a reasoner with the GeoSPARQL schema is applied) in the dataset.
Methods to prepare a dataset can be found in `io.github.galbiston.geosparql_jena.configuration.GeoSPARQLOperations`.

### API
The library can be used as an API in Java.
The main class to handle geometries and their spatial relations is the `GeometryWrapper`.
This can be obtained by parsing the string representation of a geometry using the appropriate datatype (WKT or GML).
There is overlap between spatial relation families so repeated methods are not specified.

* Parse a `Geometry Literal`: `GeometryWrapper geometryWrapper = WKTDatatype.INSTANCE.parse("POINT(1 1)");`

* Extract from a Jena Literal: `GeometryWrapper geometryWrapper = GeometryWrapper.extract(geometryLiteral);`

* Create from a JTS Geometry: `GeometryWrapper geometryWrapper = GeometryWrapper.createGeometry(geometry, srsURI, geometryDatatypeURI);`

* Create from a JTS Point Geometry: `GeometryWrapper geometryWrapper = GeometryWrapper.createPoint(coordinate, srsURI, geometryDatatypeURI);`

* Convert CRS/SRS: `GeometryWrapper otherGeometryWrapper = geometryWrapper.convertCRS("http://www.opengis.net/def/crs/EPSG/0/27700")`

* Spatial Relation: `boolean isCrossing = geometryWrapper.crosses(otherGeometryWrapper);`

* DE-9IM Intersection Pattern: `boolean isRelated = geometryWrapper.relate(otherGeometryWrapper, "TFFFTFFFT");`

* Geometry Property: `boolean isEmpty = geometryWrapper.isEmpty();`

The GeoSPARQL standard specifies that WKT Geometry Literals without an SRS URI are defaulted to CRS84 `http://www.opengis.net/def/crs/OGC/1.3/CRS84`.

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
SIS provides data structures for geographic features and associated meta-data along with methods to manipulate those data structures.
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
The following are implementation points that may be useful during usage.

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

JTS, and other libraries, use the alternative intersection pattern of `T*F**FFF*`.
This is a combination of the _within_ and _contains_ relations and yields the expected results for all geometry types.

The spatial relations utilised by JTS have been applied in the library but feedback on this choice is welcome.
A user can supply their own DE-9IM intersection patterns by using the `geof:relate` filter function.

### Spatial Relations and Geometry Shapes/Types
The spatial relations for the three spatial families do not apply to all combinations of the geometry shapes (`Point`, `LineString`, `Polygon`) and their collections  (`MultiPoint`, `MultiLineString`, `MultiPolygon`).
Therefore, some queries may not produce all the results that may initially be expected.

Some examples are:
* In some relations there may only be results when a collection of shapes is being used, e.g. two multi-points can overlap but two points cannot.
* A relation may only apply for one combination but not its reciprocal, e.g. a line may cross a polygon but a polygon may not cross a line.
* The _RCC8_ family only applies to `Polygon` and `MultiPolygon` types.

Refer to pages 8-10 of 11-052r4 GeoSPARQL standard for more details.

### Equals Relations
The three equals relations (_sfEquals_, _ehEquals_ and _rccEquals_) use spatial equality and not lexical equality.
Therefore, some comparisons using these relations may not be as expected.

The JTS description of _sfEquals_ is:
* True if two geometries have at least one point in common and no point of either geometry lies in the exterior of the other geometry.

Therefore, two empty geometries will return false as they are not spatially equal.
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
The query rewrite functionality can be switched off in the library configuration, see `io.github.galbiston.geosparql_jena.configuration.GeoSPARQLConfig`.

Each dataset is assigned a Query Rewrite Index to store the results of previous tests.
There is the potential that relations are tested multiple times in a query (i.e. *Feature-Feature*, *Feature-Geometry*, *Geometry-Geometry*, *Geometry-Feature*).
Therefore, it is useful to retain the results for at least a short period of time.

Iterating through all combinations of spatial relations for a dataset containing _n_ Geometry Literals will produce 27*n*^2 results.
Control is given on a dataset basis to allow choice in when and how storage of rewrite results is applied, e.g. store all found results on a small dataset but on demand for a large dataset.

This index can be configured on a global and individual dataset basis for the maximum size and duration until unused items are removed.
Query rewriting can be switched on independently of the indexes, i.e. query rewriting can be performed but an index is configured to not store the result.

### Dataset Conversion
Methods to convert datasets between serialisations and spatial/coordinate reference systems are available in:
`io.github.galbiston.geosparql_jena.configuration.GeoSPARQLOperations`

The following list shows some of the operations that can be performed.
Once these operations have been performed they can be serialised to file or stored in a Jena TDB to remove the need to reprocess.

* Load a Jena Model from file: `Model dataModel = RDFDataMgr.loadModel("data.ttl");`

* Convert `Feature-GeometryLiteral` to the GeoSPARQL `Feature-Geometry-GeometryLiteral` structure: `Model geosparqlModel = GeoSPARQLOperations.convertGeometryStructure(dataModel);`

* Convert `Feature-Lat, Feature-Lon` Geo predicates to the GeoSPARQL  `Feature-Geometry-GeometryLiteral` structure, with option to remove Geo predicates:  `Model geosparqlModel = GeoSPARQLOperations.convertGeoPredicates(dataModel, true);`

* Assert additional `hasDefaultGeometry` statements for single `hasGeometry` triples, used in Query Rewriting: `GeoSPARQLOperations.applyDefaultGeometry(geosparqlModel);`

* Convert Geometry Literals to the WGS84 spatial reference system and WKT datatype: `Model model = GeoSPARQLOperations.convert(geosparqlModel, "http://www.opengis.net/def/crs/EPSG/0/4326", "http://www.opengis.net/ont/geosparql#wktLiteral");`

* Apply GeoSPARQL schema with RDFS inferencing and assert additional statements in the Model: `GeoSPARQLOperations.applyInferencing(model);`

* Apply commonly used GeoSPARQL prefixes for URIs to the model: `GeoSPARQLOperations.applyPrefixes(model);`

* Create Spatial Index for a Model within a Dataset for spatial querying: `Dataset dataset = SpatialIndex.wrapModel(model);`

Other operations are available and can be applied to a Dataset containing multiple Models and in some cases files and folders.
These operations do __not__ configure and setup the GeoSPARQL functions or indexes that are required for querying.

### Spatial Index
A Spatial Index can be created to improve searching of a dataset.
The Spatial Index is expected to be unique to the dataset and should not be shared between datasets.
Once built the Spatial Index cannot have additional items added to it.

A Spatial Index is required for the `jena-spatial` property functions and is optional for the GeoSPARQL spatial relations.
Only a single SRS can be used for a Spatial Index and it is recommended that datasets are converted to a single SRS, see `GeoSPARQLOperations`.

Setting up a Spatial Index can be done through `io.github.galbiston.geosparql_jena.configuration.GeoSPARQLConfig`.
Additional methods for building, loading and saving Spatial Indexes are provided in `io.github.galbiston.geosparql_jena.spatial.SpatialIndex`.

### Units URI
Spatial/coordinate reference systems use a variety of measuring systems for defining distances.
These can be specified using a URI identifier, as either URL or URN, with conversion undertaken automatically as required.
It should be noted that there is error inherent in spatial reference systems and some variation in values may occur between different systems.

The following table gives some examples of units that are supported (additonal units can be added to the `UnitsRegistry` using the `javax.measure.Unit` API.
These URI are all in the namespace `http://www.opengis.net/def/uom/OGC/1.0/` and here use the prefix `units`.

URI | Description
---- | ----
units:kilometre or units:kilometer | Kilometres
units:metre or units:meter | Metres
units:mile or units:statuteMile | Miles
units:degree | Degrees
units:radian | Radians

Full listing of default Units can be found in `io.github.galbiston.geosparql_jena.implementation.vocabulary.Unit_URI`.


## Apache Jena Spatial Functions/WGS84 Geo Predicates
The `jena-spatial` module contains several SPARQL functions for querying datasets using the WGS84 Geo predicates for latitude (`http://www.w3.org/2003/01/geo/wgs84_pos#lat`) and longitude (`http://www.w3.org/2003/01/geo/wgs84_pos#long`).
These `jena-spatial` functions are supported for both Geo predicates and Geometry Literals, i.e. a GeoSPARQL dataset.
Additional SPARQL filter functions have been provided to convert Geo predicate properties into WKT strings and calculate Great Circle and Euclidean distances.

### Supported Features
The Geo predicate form of spatial representation is restricted to only 'Point' shapes in the WGS84 spatial/coordinate reference system.
The Geo predicates are properties of the `Feature` and do not use the properties and structure of the GeoSPARQL standard, including Geometry Literals.
Methods are available to convert datasets from Geo predicates to GeoSPARQL structure, see: `io.github.galbiston.geosparql_jena.configuration.GeoSPARQLOperations`

The spatial relations and query re-writing of GeoSPARQL outlined previously has been implemented for Geo predicates.
However, only certain spatial relations are valid for `Point` to `Point` relationships.
Refer to pages 8-10 of 11-052r4 GeoSPARQL standard for more details.

Geo predicates can be converted to Geometry Literals in query and then used with the GeoSPARQL filter functions.

```
    ?subj wgs:lat ?lat .
    ?subj wgs:long ?lon .
    BIND(spatialF:convertLatLon(?lat, ?lon) as ?point) .
    BIND("POLYGON((...))"^^<http://www.opengis.net/ont/geosparql#wktLiteral> AS ?box) . #Coordinate order is Lon/Lat without stated SRS URI.
    FILTER(geof:sfContains(?box, ?point))
```

Alternatively, utilising more shapes, relations and spatial reference systems can be achieved by converting the dataset to the GeoSPARQL structure.

```
    ?subj geo:hasGeometry ?geom .
    ?geom geo:hasSerialization ?geomLit .
    BIND("POLYGON((...))"^^<http://www.opengis.net/ont/geosparql#wktLiteral> AS ?box) . #Coordinate order is Lon/Lat without stated SRS URI.
    FILTER(geof:sfContains(?box, ?geomLit))
```

Datasets can contain both Geo predicates and Geometry Literals without interference.
However, a dataset containing both types will only examine those `Features` which have Geometry Literals for spatial relations, i.e. the check for Geo predicates is a fallback when Geometry Literals aren't found.
Therefore, it is **not** recommended to insert new Geo predicate properties after a dataset has been converted to GeoSPARQL structure (unless corresponding Geometry and Geometry Literals are included).

### Filter Functions
These filter functions are available in the `http://jena.apache.org/function/spatial#` namespace and here use the prefix `spatialF`.

Function Name | Description
------------- | -------------
*?wktString* **spatialF:convertLatLon**(*?lat*, *?lon*) | Converts Lat and Lon double values into WKT string of a Point with WGS84 SRS.
*?wktString* **spatialF:convertLatLonBox**(*?latMin*, *?lonMin*, *?latMax*, *?lonMax*) | Converts Lat and Lon double values into WKT string of a Polygon forming a box with WGS84 SRS.
*?boolean* **spatialF:nearby**(*?geomLit1*, *?geomLit2*, *?distance*, *?unitsURI*) | True, if *geomLit1* is within *distance* of *geomLit2* using the distance *units*.
*?boolean* **spatialF:withinCircle**(*?geomLit1*, *?geomLit2*, *?distance*, *?unitsURI*) | True, if *geomLit1* is within *distance* of *geomLit2* using the distance *units*.
*?distance* **spatialF:greatCircle**(*?lat1*, *?lon1*, *?lat2*, *?lon2*, *?unitsURI*) | Great Circle distance (Vincenty formula) between two Lat/Lon Points in distance *units*.
*?distance* **spatialF:greatCircleGeom**(*?geomLit1*, *?geomLit2*, *?unitsURI*) | Great Circle distance (Vincenty formula) between two Geometry Literals in distance *units*. Use `http://www.opengis.net/def/function/geosparql/distance` from GeoSPARQL standard for Euclidean distance.
*?distance* **spatialF:distance**(*?geomLit1*, *?geomLit2*, *?unitsURI*) | Distance between two Geometry Literals in distance *units*. Chooses distance measure based on SRS type. Great Circle distance for Geographic SRS and Euclidean otherwise.
*?geomLit2* **spatialF:transform**(*?geomLit1*, *?datatypeURI*, *?srsURI*) | Transform Geometry Literal by Datatype and SRS.
*?geomLit2* **spatialF:transformDatatype**(*?geomLit1*, *?datatypeURI*) | Transform Geometry Literal by Datatype.
*?geomLit2* **spatialF:transformSRS**(*?geomLit1*, *?srsURI*) | Transform Geometry Literal by SRS.

### Property Functions
These property functions are available in the `http://jena.apache.org/spatial#` namespace and here use the prefix `spatial`.
This is the same namespace as the `jena-spatial` functions utilise and these form direct replacements.
The subject `Feature` may be bound, to test the pattern is true, or unbound, to find all cases the pattern is true.
These property functions require a `Spatial Index` to be setup for the dataset.

The optional *?limit* parameter restricts the number of results returned. The default value is -1 which returns all results. No guarantee is given for ordering of results.
The optional *?unitsURI* parameter specifies the units of a distance. The default value is kilometres through the string or resource `http://www.opengis.net/def/uom/OGC/1.0/kilometre`.

Function Name | Description
------------- | -------------
*?feature* **spatial:intersectBox**(*?latMin* *?lonMin* *?latMax* *?lonMax* [ *?limit*]) | Find *features* that intersect the provided box, up to the *limit*.
*?feature* **spatial:intersectBoxGeom**(*?geomLit1* *?geomLit2* [ *?limit*]) | Find *features* that intersect the provided box, up to the *limit*.
*?feature* **spatial:withinBox**(*?latMin* *?lonMin* *?latMax* *?lonMax* [ *?limit*]) | Find *features* that intersect the provided box, up to the *limit*.
*?feature* **spatial:withinBoxGeom**(*?geomLit1* *?geomLit2* [ *?limit*]) | Find *features* that are within the provided box, up to the *limit*.
*?feature* **spatial:nearby**(*?lat* *?lon* *?radius* [ *?unitsURI* [ *?limit*]]) | Find *features* that are within *radius* of the *distance* units, up to the *limit*.
*?feature* **spatial:nearbyGeom**(*?geomLit* *?radius* [ *?unitsURI* [ *?limit*]]) | Find *features* that are within *radius* of the *distance* units, up to the *limit*.
*?feature* **spatial:withinCircle**(*?lat* *?lon* *?radius* [ *?unitsURI* [ *?limit*]]) | Find *features* that are within *radius* of the *distance* units, up to the *limit*.
*?feature* **spatial:withinCircleGeom**(*?geomLit* *?radius* [ *?unitsURI* [ *?limit*]]) | Find *features* that are within *radius* of the *distance* units, up to the *limit*.

The Cardinal Functions find all `Features` that are present in the specified direction.
In Geographic spatial reference systems (SRS), e.g. WGS84 and CRS84, the East/West directions wrap around.
Therefore, a search is made from the shape's edge for up to half the range of the SRS (i.e. 180 degrees in WGS84) and will continue across the East/West boundary if necessary.
In other SRS, e.g. Projected onto a flat plane, the East/West check is made from the shape's edge to the farthest limit of the SRS range, i.e. there is no wrap around.

Cardinal Function Name | Description
------------- | -------------
*?feature* **spatial:north**(*?lat* *?lon* [ *?limit*]) | Find *features* that are North of the Lat/Lon point (point to +90 degrees), up to the *limit*.
*?feature* **spatial:northGeom**(*?geomLit* [ *?limit*]) | Find *features* that are North of the Geometry Literal, up to the *limit*.
*?feature* **spatial:south**(*?lat* *?lon* [ *?limit*]) | Find *features* that are South of the Lat/Lon point (point to -90 degrees), up to the *limit*.
*?feature* **spatial:southGeom**(*?geomLit* [ *?limit*]) | Find *features* that are South of the Geometry Literal, up to the *limit*.
*?feature* **spatial:east**(*?lat* *?lon* [ *?limit*]) | Find *features* that are East of the Lat/Lon point (point plus 180 degrees longitude, wrapping round), up to the *limit*.
*?feature* **spatial:eastGeom**(*?geomLit* [ *?limit*]) | Find *features* that are East of the Geometry Literal, up to the *limit*.
*?feature* **spatial:west**(*?lat* *?lon* [ *?limit*]) | Find *features* that are West of the Lat/Lon point (point minus 180 degrees longitude, wrapping round), up to the *limit*.
*?feature* **spatial:westGeom**(*?geomLit* [ *?limit*]) | Find *features* that are West of the Geometry Literal, up to the *limit*.

## Geometry Property Filter Functions
The GeoSPARQL standard provides a set of properties related to geometries, see Section 8.4.
These are applied on the Geometry resource and are automatically determined if not asserted in the data.
However, it may be necessary to retrieve the properties of a Geometry Literal directly without an associated Geometry resource.
Filter functions to do this have been included as part of the `http://www.opengis.net/def/function/geosparql/` namespace as a minor variation.
The relevant functions using the `geof` prefix are:

Geometry Property Filter Function Name | Description
------------- | -------------
*?integer* **geof:dimension**(*?geometryLiteral*) | Topological dimension, e.g. 0 for Point, 1 for LineString and 2 for Polygon.
*?integer* **geof:coordinateDimension**(*?geometryLiteral*) | Coordinate dimension, e.g. 2 for XY coordinates and 4 for XYZM coordinates.
*?integer* **geof:spatialDimension**(*?geometryLiteral*) | Spatial dimension, e.g. 2 for XY coordinates and 3 for XYZM coordinates.
*?boolean* **geof:isEmpty**(*?geometryLiteral*) | True, if geometry is empty.
*?boolean* **geof:isSimple**(*?geometryLiteral*) | True, if geometry is simple.
*?boolean* **geof:isValid**(*?geometryLiteral*) | True, if geometry is topologically valid.

A dataset that follows the GeoSPARQL Feature-Geometry-GeometryLiteral can have simpler SPARQL queries without needing to use these functions by taking advantage of the Query Rewriting functionality.
The `geof:isValid` filter function and `geo:isValid` property for a Geometry resource are not part of the GeoSPARQL standard but have been included as a minor variation.

## Future Work

* Implementing GeoJSON as a `GeometryLiteral` serialisation (https://tools.ietf.org/html/rfc7946).

## Contributors
The following individuals have made contributions to this project:

* Greg Albiston
* Haozhe Chen
* Taha Osman

## Why Use This Implementation?
There are several implementations of the GeoSPARQL standard.
The conformance and completeness of these implementations is difficult to ascertain and varies between features.

However, the following may be of interest when considering whether to use this implementation based on reviewing several alternatives.

This Implementation|Other Implementations
---------- | ----------
Implements all six components of the GeoSPARQL standard.|Generally partially implement the Geometry Topology and Geometry Extensions. Do not implement the Query Rewrite Extension.
Pure Java and does not require a supporting relational database. Configuration requires a single line of code (although Apache SIS may need some setting up, see above).|Require setting up a database, configuring a geospatial extension and setting environment variables.
Uses Apache Jena, which conforms to the W3C standards for RDF and SPARQL. New versions of the standards will quickly feed through.|Not fully RDF and SPARQL compliant, e.g. RDFS/OWL inferencing or SPARQL syntax. Adding your own schema may not produce inferences.
Automatically determines geometry properties and handles mixed cases of units or coordinate reference systems. The GeoSPARQL standard suggests this approach but does not require it.|Tend to produce errors or no results in these situations.
Performs indexing and caching on-demand which reduces set-up time and only performs calculations that are required.|Perform indexing in the data loading phase and initialisation phase, which can lead to lengthy delays (even on relatively small datasets).
Uses JTS which does not truncate coordinate precision and applies spatial equality.|May truncate coordinate precision and apply lexical equality, which is quicker but does not comply with the GeoSPARQL standard.

![Powered by Apache Jena](https://www.apache.org/logos/comdev-test/poweredby/jena.png "Powered by Apache Jena")
