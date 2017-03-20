package com.pfening.orientdb;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import database.DataBean;
import database.DataDAO;
import database.Database;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InitLoad {

    public static void main(String[] args) throws Exception {
        OrientGraphFactory factory = new OrientGraphFactory("plocal:C:/orientdb/databases/gddb", "root", "root");
        OrientGraph g = factory.getTx();

        DataDAO d=new DataDAO();
        Database.getInstance().OracleConnect();
        
        ArrayList<Object> counrties = new ArrayList<>(); 
        ArrayList<Object> managers = new ArrayList<>(); 

        List<DataBean> list = (List<DataBean>) d.readGDDB();
        for (DataBean n:list){         
            String fname = n.getFname();
            String lname = n.getLname();
            String id = n.getId();
            String supervisor = n.getSupervisor();
            String excmod = n.getExcmod();
            String country = n.getCountry();
            String now = OffsetDateTime.now().toString();
            
                System.out.println(fname+" "+lname+" "+id+" "+supervisor+" "+excmod+" "+country);
            
            Vertex gperson = g.addVertex("class:GDDB_Person");
            gperson.setProperty("fname", fname);
            gperson.setProperty("lname", lname);
            gperson.setProperty("uid", id);
            gperson.setProperty("manager", supervisor);
            gperson.setProperty("moddate", excmod);
            gperson.setProperty("country", country);   
            gperson.setProperty("Record_status", "Active");
            gperson.setProperty("Record_entered", now);
            counrties.add(country);
            managers.add(supervisor);
        }
        
        List<Object> distCountries = counrties.stream().distinct().collect(Collectors.toList());
        System.out.println(distCountries);
        for(Object dc:distCountries){
            Vertex dcountry = g.addVertex("class:Country");
            dcountry.setProperty("country_name", dc);
        }
        List<Object> distManagers = managers.stream().distinct().collect(Collectors.toList());
        System.out.println(distManagers);
        for(Object dm:distManagers){
            Vertex dmanager = g.addVertex("class:Manager");
            dmanager.setProperty("manager_name", dm);
        }
        
        for (Vertex persons : g.getVerticesOfClass("GDDB_Person")) {          
            Object manager = persons.getProperty("manager");
                for (Vertex peoplemanagers : g.getVertices("manager_name", manager)) {
                    Edge m = g.addEdge(null, peoplemanagers, persons, "employs");
                }           
        }

        for (Vertex persons : g.getVerticesOfClass("GDDB_Person")) {          
            Object country = persons.getProperty("country");
                for (Vertex workingcountry : g.getVertices("country_name", country)) {
                    Edge c = g.addEdge(null, persons, workingcountry, "works_in");
                }           
        }
        g.commit();
        g.shutdown();

        Database.getInstance().OracleDisconnect(); 
        
    }
    
}
