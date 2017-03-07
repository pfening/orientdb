package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataDAO {
    
    private String selectedItem;

    public String getSelectedItem() {        
        return selectedItem;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;       
    }
   
    public List<DataBean> readGDDB() throws SQLException, Exception {        
        List<DataBean> gddb = new ArrayList<>();        
        Connection conn = Database.getInstance().getOracleConnection();
        PreparedStatement p = conn.prepareStatement("select LASTNAME,FIRSTNAME,UNIQUE_ID,EXCEPTION_MOD_DATE,SUPERVISOR_ID,COUNTRY_NAME from GDDB.VW_GDDB_PERSONS where EXCEPTION_ID='2' and LC_STATE='ACTIVE'");		
        //PreparedStatement p = conn.prepareStatement("select LASTNAME,FIRSTNAME,UNIQUE_ID,EXCEPTION_MOD_DATE,SUPERVISOR_ID,COUNTRY_NAME from GDDB.VW_GDDB_PERSONS where UNIQUE_ID='PFENIGA1'");
        
        ResultSet result = p.executeQuery();		
            while(result.next()) {
                DataBean entry = new DataBean();
                entry.setFname(result.getString("LASTNAME"));
                entry.setLname(result.getString("FIRSTNAME"));
                entry.setId(result.getString("UNIQUE_ID"));
                entry.setExcmod(result.getString("EXCEPTION_MOD_DATE"));
                entry.setSupervisor(result.getString("SUPERVISOR_ID"));
                entry.setCountry(result.getString("COUNTRY_NAME"));
                gddb.add(entry);
            }		
            result.close();
            p.close();                
            return gddb;   
    }

}
