/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index;

import java.util.Arrays;
import java.util.List;

/**
 *
 *
 */
public interface IndexDefaultValues {

    /*
     * Default Indexing and Registry Sizes
     */
    public static final Integer GEOMETRY_LITERAL_INDEX_MAX_SIZE_DEFAULT = 10000;
    public static final Integer GEOMETRY_TRANSFORM_INDEX_MAX_SIZE_DEFAULT = 1000;
    public static final Integer QUERY_REWRITE_INDEX_MAX_SIZE_DEFAULT = 10000;
    public static final Integer INDEX_MINIMUM_SIZE = 1;

    /*
     * Index Storage Filenames
     */
    public static final String CRS_REGISTRY_FILENAME = "geosparql-CRS.registry";
    public static final String MATH_TRANSFORM_REGISTRY_FILENAME = "geosparql-MathTransform.registry";
    public static final String GEOMETRY_LITERAL_INDEX_FILENAME = "geosparql-GeometryLiteral.index";
    public static final String GEOMETRY_TRANSFORM_INDEX_FILENAME = "geosparql-GeometryTransform.index";
    public static final String QUERY_REWRITE_INDEX_FILENAME = "geosparql-QueryRewrite.index";
    public static final List<String> INDEX_REGISTRY_FILENAMES = Arrays.asList(CRS_REGISTRY_FILENAME, MATH_TRANSFORM_REGISTRY_FILENAME, GEOMETRY_LITERAL_INDEX_FILENAME, GEOMETRY_TRANSFORM_INDEX_FILENAME, QUERY_REWRITE_INDEX_FILENAME);

}
