/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfening.orientdb;

import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import database.DataBean;
import database.DataDAO;
import database.Database;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author PFENIGA1
 */
public class UpdateE {

    public static void main(String[] args) {
        OrientGraphFactory factory = new OrientGraphFactory("plocal:C:/orientdb/databases/gddb", "admin", "admin");
        OrientGraph g = factory.getTx();
        
        
        for (Edge edge : g.getEdges()) {
            System.out.println(edge.getVertex(Direction.OUT).getId()+" "+edge.getLabel()+" "+edge.getVertex(Direction.IN).getId());
            g.removeEdge(edge);
            g.commit();
        }
        
        for (Vertex persons : g.getVerticesOfClass("GDDB_Person")) {          
            Object manager = persons.getProperty("manager");
            if(persons.getEdges(Direction.IN, "employs").toString().equals("[0]")){
                System.out.println(persons.getProperty("manager").toString()+" "+persons.getProperty("uid"));

                for (Vertex peoplemanagers : g.getVertices("manager_name", manager)) {
                    g.addEdge(null, peoplemanagers, persons, "employs");
                    g.commit();
                } 
            }
        }
        
        for (Vertex persons : g.getVerticesOfClass("GDDB_Person")) {          
            Object country = persons.getProperty("country");
            if(persons.getEdges(Direction.OUT, "works_in").toString().equals("[0]")){
                System.out.println(persons.getProperty("manager").toString()+" "+persons.getProperty("uid"));

                for (Vertex workingcountry : g.getVertices("country_name", country)) {
                    g.addEdge(null, persons, workingcountry, "works_in");
                    g.commit();
                } 
            }
        }

        
        System.err.println("-------------------------------------------------------------");
        for (Vertex mgr : g.getVerticesOfClass("Manager")) {   
            if(mgr.getEdges(Direction.OUT, "employs").toString().equals("[0]")){
                System.out.println(mgr.getProperty("manager_name").toString()+" "+mgr.getId());
            }   
        }
        
        System.err.println("-------------------------------------------------------------");
        for (Vertex ppl : g.getVerticesOfClass("GDDB_Person")) {   
            if(ppl.getEdges(Direction.IN, "employs").toString().equals("[0]")){
                System.out.println(ppl.getProperty("manager").toString()+" "+ppl.getId());
            }   
        }
        
        g.shutdown();       
    }
    
}
