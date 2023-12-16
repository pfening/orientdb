package org.example;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import java.util.HashMap;
import java.util.Map;

public class N3 {
    public static void main(String[] args) {

        OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
        ODatabaseSession db = orient.open("dummy", "root", "");

        createSchema(db);
        createPeople(db);
        executeAnotherQuery(db);

        db.close();
        orient.close();
    }

    private static void createSchema(ODatabaseSession db) {

        OClass person = db.getClass("Person");

        if (person == null) {
            person = db.createVertexClass("Person");
        }

        OClass location = db.getClass("Location");

        if (location == null) {
            location = db.createVertexClass("Location");
        }

        if (person.getProperty("name") == null) {
            person.createProperty("name", OType.STRING);
            person.createIndex("Person_index", OClass.INDEX_TYPE.UNIQUE, "name");
        }

        if (location.getProperty("city") == null) {
            location.createProperty("city", OType.STRING);
            location.createIndex("Location_index", OClass.INDEX_TYPE.UNIQUE, "city");
        }

        if (location.getProperty("country") == null) {
            location.createProperty("country", OType.STRING);
            location.createIndex("Country_index", OClass.INDEX_TYPE.DICTIONARY, "country");
        }

        if (db.getClass("FriendOf") == null) {
            db.createEdgeClass("FriendOf");
        }

        if (db.getClass("LivingIn") == null) {
            db.createEdgeClass("LivingIn");
        }

    }

    private static void createPeople(ODatabaseSession db) {
        OVertex alice = createPerson(db, "Alice", "Foo");
        OVertex bob = createPerson(db, "Bob", "Bar");
        OVertex jim = createPerson(db, "Jim", "Baz");

        OVertex gabor = createPerson(db, "Gabor", "Pfening");
        OVertex alex = createPerson(db, "Alex", "Pfening");
        OVertex laci = createPerson(db, "Laszlo", "Pfening");

        OVertex ozd = createLocation(db, "Ozd", "Hungary");
        OVertex slustice = createLocation(db, "Slustice", "Czechia");

        OEdge edge1 = alice.addEdge(bob, "FriendOf");
        edge1.save();
        OEdge edge2 = bob.addEdge(jim, "FriendOf");
        edge2.save();

        OEdge edge3 = alice.addEdge(ozd, "LivingIn");
        edge3.save();
        OEdge edge4 = laci.addEdge(ozd, "LivingIn");
        edge4.save();
        OEdge edge5 = alex.addEdge(slustice, "LivingIn");
        edge5.save();
        OEdge edge6 = gabor.addEdge(slustice, "LivingIn");
        edge6.save();

        OEdge edge7 = gabor.addEdge(alex, "FriendOf");
        edge7.save();
        OEdge edge9 = alex.addEdge(laci, "FriendOf");
        edge9.save();

    }

    private static OVertex createLocation(ODatabaseSession db, String city, String country) {
        OVertex result = db.newVertex("Location");
        result.setProperty("city", city);
        result.setProperty("country", country);
        result.save();
        return result;
    }

    private static OVertex createPerson(ODatabaseSession db, String name, String surname) {
        OVertex result = db.newVertex("Person");
        result.setProperty("name", name);
        result.setProperty("surname", surname);
        result.save();
        return result;
    }


    private static void executeAnotherQuery(ODatabaseSession db) {
        String query =
                " MATCH                                           " +
                        "   {class:Person, as:a, where: (name = :name1)}, " +
                        "   {class:Person, as:b, where: (name = :name2)}, " +
                        "   {as:a} -FriendOf-> {as:x} -FriendOf-> {as:b}  " +
                        " RETURN x.name as friend                         ";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name1", "Alice");
        params.put("name2", "Jim");

        OResultSet rs = db.query(query, params);

        while (rs.hasNext()) {
            OResult item = rs.next();
            System.out.println("friend: " + item.getProperty("friend"));
        }

        rs.close();
    }

}
