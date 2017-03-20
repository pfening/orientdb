package com.pfening.orientdb;

import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import database.DataBean;
import database.DataDAO;
import database.Database;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateV {

    public static void main(String[] args) throws Exception {
        OrientGraphFactory factory = new OrientGraphFactory("plocal:C:/orientdb/databases/gddb", "admin", "admin");
        OrientGraph g = factory.getTx();

        DataDAO d=new DataDAO();
        Database.getInstance().OracleConnect();
        ArrayList<Object> counrties = new ArrayList<>();
        ArrayList<Object> managers = new ArrayList<>();
        List<DataBean> list = (List<DataBean>) d.readGDDB();
        
        for (DataBean n:list){
            String fname = n.getFname().replace("'", " ");
            String lname = n.getLname().replace("'", " ");
            String id = n.getId();
            String supervisor = n.getSupervisor();
            String excmod = n.getExcmod();
            String country = n.getCountry();
            System.out.println(fname+" "+lname+" "+id+" "+supervisor+" "+excmod+" "+country);

            g.command(new OCommandSQL("update GDDB_Person set fname='"+fname+"',lname='"+lname+"',uid='"+id+"',manager='"+supervisor+"',moddate='"+excmod+"',country='"+country+"' upsert where uid='"+id+"'")).execute();
            g.commit();         
            
            counrties.add(country);
            managers.add(supervisor);        
        }
        
        List<Object> distCountries = counrties.stream().distinct().collect(Collectors.toList());
        for(Object dc:distCountries){
            g.command(new OCommandSQL("update Country set country_name = '"+dc+"' upsert where country_name = '"+dc+"'")).execute();
            g.commit();
        }
        List<Object> distManagers = managers.stream().distinct().collect(Collectors.toList());
        for(Object dm:distManagers){
            g.command(new OCommandSQL("update Manager set manager_name = '"+dm+"' upsert where manager_name = '"+dm+"'")).execute();
            g.commit();
        }

        
        
   /*
        for (Vertex persons : g.getVerticesOfClass("GDDB_Person")) {
            Object manager = persons.getProperty("manager");
            for (Vertex peoplemanagers : g.getVertices("manager_name", manager)) {
                Iterable<Edge> edges = peoplemanagers.getEdges(Direction.OUT, "employs");
                int i=0;
                for (Iterator<Edge> it = edges.iterator(); it.hasNext();) {
                    i++;
                }
                System.err.println(i);
                /*
                if(i==0){
                g.addEdge(null, peoplemanagers, persons, "employs");
                g.commit();
                }else if(i>0){
                    System.out.println("ez mar megvan");
                }

                //g.command(new OCommandSQL("update EDGE set employs out='"+peoplemanagers.getId()+"' in = '"+persons.getId()+"'")).execute();
            }
        }
        */
        
        g.shutdown();
        Database.getInstance().OracleDisconnect();
        
    }
    
}
