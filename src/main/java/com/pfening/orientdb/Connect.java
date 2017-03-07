/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfening.orientdb;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import java.util.Set;

public class Connect {

    public static void main(String[] args) {
        OrientGraphFactory factory = new OrientGraphFactory("plocal:C:/orientdb/databases/testdb", "admin", "admin");
        OrientGraph g = factory.getTx();
    
        for (Vertex vertex : g.getVertices()) {
            System.out.println(vertex);
            Set<String> propertyKeys = vertex.getPropertyKeys();
            System.out.println(propertyKeys);
          }
         
        for (Edge edge : g.getEdges()) {
          System.out.println(edge);
            Set<String> outKeys = edge.getVertex(Direction.OUT).getPropertyKeys();
            for (String po:outKeys){
                //System.out.println(po);
            
            Set<String> inKeys = edge.getVertex(Direction.IN).getPropertyKeys();
            for (String pi:inKeys){
                //System.out.println(pi);
                System.out.println(edge.getVertex(Direction.OUT).getProperty(po) + "--" + edge.getLabel() + "-->" + edge.getVertex(Direction.IN).getProperty(pi));
            }
        }
          
         }
        System.err.println("-------------------------------------------------");
        
       for (Vertex v : g.getVertices()) {
           System.out.println(v);           
           for(Edge e : v.getEdges(Direction.OUT, args)){
               System.out.println(e.getLabel());
           }
        }
       
        System.out.println((g.getVertex("#25:0").getProperty("fuel")).toString());
        
        Edge e = g.getEdge("#37:0");
        
        System.out.println(e.getVertex(Direction.OUT).getProperty("name") + "--" + e.getLabel() + "-->" + e.getVertex(Direction.IN).getProperty("city"));
                //OrientVertex vPerson = g.getVertex("#17:0");
                //OrientVertex vAddress = g.getVertex("#21:0");
                //Edge l = g.addEdge(null, vPerson, vAddress, "lives");
                //g.commit();
                //g.shutdown();

    }
    
}
