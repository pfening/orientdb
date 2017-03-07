package com.pfening.orientdb;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import java.util.Iterator;

public class QueryByManagers {

    public static void main(String[] args) {
        //query by mangagers
        OrientGraphFactory factory = new OrientGraphFactory("plocal:C:/orientdb/databases/gddb", "admin", "admin");
        OrientGraph g = factory.getTx();
        Iterable<Vertex> managers = g.getVerticesOfClass("Manager");
        
        for(Vertex m:managers){
        Iterable<Edge> Edges = g.getVertex(m).getEdges(null, Direction.OUT, args);

        for (Iterator<Edge> it = Edges.iterator(); it.hasNext();) {
            Edge e = it.next();
            Object manager = e.getVertex(Direction.OUT).getProperty("manager_name");
            Object worker = e.getVertex(Direction.IN).getProperty("uid");
            Iterable<Edge> workerE = e.getVertex(Direction.IN).getEdges(Direction.OUT, args);
                    
                    for (Iterator<Edge> wit = workerE.iterator(); wit.hasNext();) {
                    Edge we = wit.next();
                    Object country = we.getVertex(Direction.IN).getProperty("country_name");
                    System.out.println(manager+" "+e.getLabel()+" "+worker+" "+we.getLabel()+" "+country);
                    }

        }
        }
        
    }
    
}
