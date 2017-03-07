package com.pfening.orientdb;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;

public class OrientDocument {


    public static void main(String[] args) {
        ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:C:/orientdb/databases/testdb");
        db.open("admin","admin");
        // CREATE A NEW DOCUMENT AND FILL IT
        ODocument doc = new ODocument("Person");
        doc.field( "name", "Laszlo" );
        doc.field( "surname", "Pfening" );
        doc.field( "city", new ODocument("City").field("name","Ozd").field("country", "Hungary") );

        // SAVE THE DOCUMENT
        doc.save();

        db.close();
            }
    
}
