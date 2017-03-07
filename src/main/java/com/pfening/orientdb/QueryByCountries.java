package com.pfening.orientdb;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import java.util.Iterator;

public class QueryByCountries {
//query by country
    public static void main(String[] args) {
        OrientGraphFactory factory = new OrientGraphFactory("plocal:C:/orientdb/databases/gddb", "admin", "admin");
        OrientGraph g = factory.getTx();
        Iterable<Vertex> countries = g.getVerticesOfClass("Country");
        
        for(Vertex c:countries){
        Iterable<Edge> countryE = g.getVertex(c).getEdges(null, Direction.IN, args);
        
        for (Iterator<Edge> it = countryE.iterator(); it.hasNext();) {
            Edge e = it.next();
            Object country = e.getVertex(Direction.IN).getProperty("country_name");
            Object worker = e.getVertex(Direction.OUT).getProperty("uid");
            
            Iterable<Edge> managerE = e.getVertex(Direction.OUT).getEdges(Direction.IN, args);

            for (Iterator<Edge> mit = managerE.iterator(); mit.hasNext();) {
            Edge me = mit.next();
            Object manager = me.getVertex(Direction.OUT).getProperty("manager_name");

            System.out.println(manager+" "+me.getLabel()+" "+worker+" "+e.getLabel()+" "+country);
            }

        }
        }
        
    }
    
}