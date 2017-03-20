package com.pfening.orientdb;

import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import database.DataBean;
import database.DataDAO;
import database.Database;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class UpdateV1 {

    public static void main(String[] args) throws Exception {
        OrientGraphFactory factory = new OrientGraphFactory("plocal:C:/orientdb/databases/gddb", "admin", "admin");
        OrientGraph g = factory.getTx();

        DataDAO d=new DataDAO();
        Database.getInstance().OracleConnect();
        ArrayList<Object> counrties = new ArrayList<>();
        ArrayList<Object> managers = new ArrayList<>();
        ArrayList<Object> uid_db = new ArrayList<>();
        ArrayList<Object> uid_gddb = new ArrayList<>();
        List<DataBean> list = (List<DataBean>) d.readGDDB();
        
        for(Vertex persons : g.getVerticesOfClass("GDDB_Person")) {          
            Object uid = persons.getProperty("uid");
            uid_db.add(uid);
        }
        
        list.stream().forEach((n) -> { uid_gddb.add(n.getId()); });
        
        Collection new_ids = CollectionUtils.removeAll(uid_gddb,uid_db);//new ids from gddb to read to orientdb
        //System.out.println("new ids: "+new_ids);
        
        Collection common = CollectionUtils.retainAll(uid_db,uid_gddb);//common ids 
        
        Collection chg_ids = CollectionUtils.removeAll(uid_db,uid_gddb);//showing the uids which were changed in gddb compared to orientdb
        //System.err.println("changed in orientdb: "+chg_ids);

        System.out.println("orientdb: "+uid_db.size()+" / gddb: "+uid_gddb.size()+" / common: "+common.size()+" / changed: "+chg_ids.size()+" / new: "+new_ids.size());
        
        for(Object mod:chg_ids){
        d.setSelectedItem(mod.toString());
        DataBean one = d.readOne();
          if(one.getStatus().toString().equals("DELETED")){//deleted
            for(Vertex f:g.getVertices("uid",mod.toString())){
                if(f.getProperty("Record_status").equals("Active")){
                    f.setProperty("Record_status", "Deleted");
                    f.setProperty("Record_entered", OffsetDateTime.now().toString());
                }
            }

            }else if(one.getStatus().toString().equals("ACTIVE") && one.getExc().equals("1")){//resolved
                for(Vertex f:g.getVertices("uid",mod.toString())){
                    if(f.getProperty("Record_status").equals("Active")){
                        f.setProperty("Record_status", "Resolved");
                        f.setProperty("Record_entered", OffsetDateTime.now().toString());
                    }
                }
            }
        }
        
        for(Object uj:new_ids){//new ones
            d.setSelectedItem(uj.toString());
            DataBean one = d.readNewOne();
            String now = OffsetDateTime.now().toString();

            Vertex gperson = g.addVertex("class:GDDB_Person");
            gperson.setProperty("fname", one.getFname().replace("'", " "));
            gperson.setProperty("lname", one.getLname().replace("'", " "));
            gperson.setProperty("uid", one.getId());
            gperson.setProperty("manager", one.getSupervisor());
            gperson.setProperty("moddate", one.getExcmod());
            gperson.setProperty("country", one.getCountry());   
            gperson.setProperty("Record_status", "Active");
            gperson.setProperty("Record_entered", now);
            g.commit();         
    
        }
        
        for(Vertex persons : g.getVerticesOfClass("GDDB_Person")) {          
            counrties.add(persons.getProperty("country"));
            managers.add(persons.getProperty("manager"));
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
        
        for (Edge edge : g.getEdges()) {
            //System.out.println(edge.getVertex(Direction.OUT).getId()+" "+edge.getLabel()+" "+edge.getVertex(Direction.IN).getId());
            g.removeEdge(edge);
            g.commit();
        }
        
        for (Vertex persons : g.getVerticesOfClass("GDDB_Person")) {          
            Object manager = persons.getProperty("manager");
            if(persons.getEdges(Direction.IN, "employs").toString().equals("[0]")){
                //System.out.println(persons.getProperty("manager").toString()+" "+persons.getProperty("uid"));

                for (Vertex peoplemanagers : g.getVertices("manager_name", manager)) {
                    g.addEdge(null, peoplemanagers, persons, "employs");
                    g.commit();
                } 
            }
        }
        
        for (Vertex persons : g.getVerticesOfClass("GDDB_Person")) {          
            Object country = persons.getProperty("country");
            if(persons.getEdges(Direction.OUT, "works_in").toString().equals("[0]")){
                //System.out.println(persons.getProperty("manager").toString()+" "+persons.getProperty("uid"));

                for (Vertex workingcountry : g.getVertices("country_name", country)) {
                    g.addEdge(null, persons, workingcountry, "works_in");
                    g.commit();
                } 
            }
        }

        Vertex del = g.addVertex("class:Deleted");
        Vertex res = g.addVertex("class:Resolved");
        
        for (Vertex persons : g.getVerticesOfClass("GDDB_Person")) {          
            Object status = persons.getProperty("Record_status");
            if(status.equals("Deleted")){
                g.addEdge(null, persons, del, "removed");
                g.commit();
            }else if(status.equals("Resolved")){
                g.addEdge(null, persons, res, "registered");
                g.commit();
            }
        }
        
        g.shutdown();
        Database.getInstance().OracleDisconnect();
        
    }
    
}
