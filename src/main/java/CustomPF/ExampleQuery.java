/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomPF;

/**
 *
 * @author haozhechen
 */
/*
public class ExampleQuery {

    private static Logger log = LoggerFactory.getLogger(Distance.class);

    public static void main(String[] args) {

        String queryString = Prefix_osgb + Prefix_gml + Prefix_ntu + Prefix_geo + Prefix_geof + Prefix_pf + Prefix_xsd + Prefix_rdf + Prefix_rdfs
                + "SELECT ?aGML ?bGML ?distance WHERE{ "
                + "ntu:A ntu:hasPointGeometry ?aGeom . ?aGeom gml:asGML ?aGML . "
                + "ntu:B ntu:hasPointGeometry ?bGeom . ?bGeom gml:asGML ?bGML . "
                + "?distance ext:distance(?aGML ?bGML) ."
                //                + "FILTER ( ext:distance(?aGML, ?bGML) > 1)"
                + " }"
                + "LIMIT 2";

        Query query = QueryFactory.create(queryString);
        Model model = make();

        try (QueryExecution qExec = QueryExecutionFactory.create(query, model)) {
            ResultSet rs = qExec.execSelect();
            log.info("success in executing result!");
            ResultSetFormatter.out(rs);
        }

    }

}
*/
