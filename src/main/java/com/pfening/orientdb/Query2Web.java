/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pfening.orientdb;

import com.google.gson.Gson;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import java.util.ArrayList;

import static spark.Spark.get;

public class Query2Web {
    public static void main(String[] args) throws Exception {
        OrientGraphFactory factory = new OrientGraphFactory("plocal:C:/orientdb/databases/gddb", "admin", "admin");
        OrientGraph g = factory.getTx();

        Gson gson = new Gson();
        Iterable<Vertex> person = g.getVerticesOfClass("GDDB_Person");
        ArrayList<Object> props = new ArrayList<>();
        for(Vertex v:person){            
            props.add(g.getVertex(v.getId()).getProperties().toString());            
        }

        get("/all", (req, res) -> props, gson::toJson);             

    }
}
