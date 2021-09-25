/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasetojson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Graham
 */
public class DatabaseTest {
    
     //make connection
    //populate JSONArray
    public static JSONArray getJSONData(){
        
        Connection conn;
        ResultSetMetaData metadata;
        ResultSet resultset = null;        
        PreparedStatement pstSelect = null, pstUpdate = null;
        
        String query, key, value;
        
        JSONArray result = new JSONArray();
        
        
        boolean hasresults;
        int resultCount, columnCount, updateCount;
        
        try {
            
            /* Identify the Server */
            
            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password = "Sonicwent2fast.mov";
            System.out.println("Connecting to " + server + "...");
            
            /* Open Connection (MySQL JDBC driver must be on the classpath!) */

            conn = DriverManager.getConnection(server, username, password);

            /* Test Connection */
            
            if (conn.isValid(0)) {
                
                /* Connection Open! */
                
                System.out.println("Connected Successfully!");
                
                /* Prepare Select Query */
                
                query = "SELECT * FROM people";
                pstSelect = conn.prepareStatement(query);
                
                /* Execute Select Query */
                
                System.out.println("Submitting Query ...");
                
                hasresults = pstSelect.execute();                
                
                /* Get Results */
                
                System.out.println("Getting Results ...");
                
                while ( hasresults || pstSelect.getUpdateCount() != -1 ) {

                    if ( hasresults ) {
                        
                        /* Get ResultSet Metadata */
                        
                        resultset = pstSelect.getResultSet();
                        metadata = resultset.getMetaData();
                        columnCount = metadata.getColumnCount();
                        
                        /* Get Column Names; Print as Table Header */
                        JSONArray columnHeaders = new JSONArray(); //Column Headers
                        for (int i = 1; i <= columnCount; i++) {
                            
                            key = metadata.getColumnLabel(i);
                            columnHeaders.add(key);

                        }
                        
                        /* Get Data; Print as Table Rows */
                        
                        while(resultset.next()) {
                            /* Loop Through ResultSet Columns; Print Values */
                            JSONObject jsonObj = new JSONObject();
                            for (int i = 2; i <= columnCount; i++) {  
                                
                                jsonObj.put(metadata.getColumnLabel(i),resultset.getString(i));
                                
                                

                            }
                            result.add(jsonObj);
                        }
                        
                    }

                    else {

                        resultCount = pstSelect.getUpdateCount();  

                        if ( resultCount == -1 ) {
                            break;
                        }

                    }
                    
                    /* Check for More Data */

                    hasresults = pstSelect.getMoreResults();

                }
                
            }
            
            System.out.println();
            
            /* Close Database Connection */
            
            conn.close();
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        /* Close Other Database Objects */
        
        finally {
            
            if (resultset != null) { try { resultset.close(); } catch (Exception e) { e.printStackTrace(); } }
            
            if (pstSelect != null) { try { pstSelect.close(); } catch (Exception e) { e.printStackTrace(); } }
            
            if (pstUpdate != null) { try { pstUpdate.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
     return result;   
    }
}

    

