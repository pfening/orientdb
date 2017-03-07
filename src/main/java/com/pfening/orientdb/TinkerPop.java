/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfening.orientdb;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Direction;


/**
 *
 * @author PFENIGA1
 */
public class TinkerPop {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        OrientGraphFactory factory = new OrientGraphFactory("plocal:C:/orientdb/databases/tinker", "admin", "admin");
        OrientGraph graph = factory.getTx();
        /*
        Vertex a = graph.addVertex(null);
        Vertex b = graph.addVertex(null);
        a.setProperty("name", "marko");
        b.setProperty("name", "peter");
        Edge e = graph.addEdge(null, a, b, "knows");
        System.out.println(e.getVertex(Direction.OUT).getProperty("name") + "--" + e.getLabel() + "-->" + e.getVertex(Direction.IN).getProperty("name"));
        */
        
        

        System.out.println("Vertices of " + graph);
        for (Vertex vertex : graph.getVertices()) {
          System.out.println(vertex);
        }
        System.out.println("Edges of " + graph);
        for (Edge edge : graph.getEdges()) {
          System.out.println(edge);
         }
        
        Vertex a = graph.getVertex("#9:0");
        System.out.println("vertex " + a.getId() + " has name " + a.getProperty("name"));
        for(Edge e : a.getEdges(Direction.OUT)) {
          System.out.println(e);
        }
    }
    
}
