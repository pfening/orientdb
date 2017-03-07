package com.pfening.orientdb;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import database.DataBean;
import database.DataDAO;
import database.Database;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Test {


    public static void main(String[] args) throws Exception {

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

            System.out.println(fname+" "+lname+" "+id+" "+supervisor+" "+excmod+" "+country);
            
            counrties.add(country);
            managers.add(supervisor);
   
        }

        List<Object> distCountries = counrties.stream().distinct().collect(Collectors.toList());
        System.out.println(distCountries);
        List<Object> distManagers = managers.stream().distinct().collect(Collectors.toList());
        System.out.println(distManagers);
        
        Database.getInstance().OracleDisconnect(); 
        
    }
    
}