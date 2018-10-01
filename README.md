# README #

Implemenation of GeoSPARQL standard using Apache Jena.

## Access
GeoSPARQL Jena can be accessed as a library using Maven etc. from Maven Central.

```
<dependency>
    <groupId>io.github.galbiston</groupId>
    <artifactId>geosparql-jena</artifactId>
    <version>1.0.0</version>
</dependency>
```

##ESPG Database

Licencing and size of file.
Embedded option or 
See Apache SIS pages for details.

##Equals Relations
The three equals relations (sfEquals, ehEquals and rccEquals) use spatial equality and not lexical equality.
This 

- True if two geometries have at least one point in common and no point of either geometry lies in the exterior of the other geometry.

Therefore, two empty geometries are not spatially equal and will return false. 
Shapes which differ in the number of points but have the same geometry are equal and will return true, e.g. LINESTRING (0 0, 0 10) and LINESTRING (0 0, 0 5, 0 10) are spatially equal.
