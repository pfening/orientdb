/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfening.orientdb;


import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
/**
 *
 * @author PFENIGA1
 */
public class Create {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        OrientGraphFactory factory = new OrientGraphFactory("plocal:C:/orientdb/databases/testdb", "root", "root");
        OrientGraphNoTx g = factory.getNoTx();

        g.createVertexType("Person");
        g.createVertexType("Address");
        g.createVertexType("Cars");
        
        Vertex vPerson = g.addVertex("class:Person");
        vPerson.setProperty("firstName", "John");
        vPerson.setProperty("lastName", "Smith");

        Vertex vAddress = g.addVertex("class:Address");
        vAddress.setProperty("street", "Van Ness Ave.");
        vAddress.setProperty("city", "San Francisco");
        vAddress.setProperty("state", "California");

        Vertex vCars =g.addVertex("class:Cars");
        vCars.setProperty("brand", "Hunday");
        vCars.setProperty("fuel", "diesel");
        
        Edge e = g.addEdge(null, vPerson, vCars, "owns");

        g.commit();
        g.shutdown();
    }
    
}
