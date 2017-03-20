/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfening.orientdb;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import java.time.OffsetDateTime;

/**
 *
 * @author PFENIGA1
 */
public class AddProperties {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        OrientGraphFactory factory = new OrientGraphFactory("plocal:C:/orientdb/databases/gddb", "admin", "admin");
        OrientGraph g = factory.getTx();
        String now = OffsetDateTime.now().toString();
        for (Vertex persons : g.getVerticesOfClass("GDDB_Person")) {  
            //persons.removeProperty("Record_entered");
            persons.setProperty("Record_status", "Active");
            persons.setProperty("Record_entered", now);
            g.commit();
        }
    }
    
}
