// 10-1-2021---This class controls database access for the program.
package inventorymanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class JavaDatabase
{

  // Declaring variables.
  String dbName;
  Connection dbConn;
  ArrayList<ArrayList<String>> dataList;

  //Constructor for JavaDatabase (without parameters).
  public JavaDatabase()
  {
    dbName = "";
    dbConn = null;
    dataList = null;
  }

  //Constructor for JavaDatabase (with parameters).
  public JavaDatabase(String dbName)
  {
    setDbName(dbName);
    setDbConn();
    dataList = null;
  }

  // Set and get methods for the dbname, dbconn and data.
  public void setDbName(String dbName)
  {
    this.dbName = dbName;
  }

  public String getDbName()
  {
    return dbName;
  }

  public void setDbConn()
  {
    // Declaring variables.
    String URL = "jdbc:derby:" + dbName;
    dbConn = null;

    // This code snippet attempts to connect to the given database.
    try
    {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      dbConn = DriverManager.getConnection(URL);
    }
    catch (ClassNotFoundException ex) //driver issue
    {
      System.out.println("Driver Not Found. Check Libray!");
    }
    catch (SQLException se)//connection issue
    {
      System.out.println("SQL Connection error!");
    }
  }

  public Connection getDbConn()
  {
    return dbConn;
  }

  public ArrayList<ArrayList<String>> getData(String tableName, String[] tableHeaders)
  {
    // Declaring variables.
    int columnCount = tableHeaders.length;
    Statement s = null;
    ResultSet rs = null;
    String dbQuery = "SELECT * FROM " + tableName;
    this.dataList = new ArrayList<>();

    // This code snippet attempts to read the data from the given tables in the given database.
    try
    {
      // Declaring variables.
      s = this.dbConn.createStatement();
      rs = s.executeQuery(dbQuery);

      while (rs.next())
      {
        ArrayList<String> row = new ArrayList<>();

        for (int i = 0; i < columnCount; i++)
        {
          String cell = rs.getString(tableHeaders[i]);
          row.add(cell);
        }
        this.dataList.add(row);
      }
    }
    catch (SQLException se)
    {
      System.out.println("SQL Error: Not able to get data");
      se.printStackTrace();
    }
    return dataList;
  }

  public void setData(ArrayList<ArrayList<String>> dataList)
  {
    this.dataList = dataList;
  }

  // Method to close database connection.
  public void closeDbConn()
  {
    try
    {
      this.dbConn.close();
    }
    catch (Exception err)
    {
      System.out.println("DB closing error");
    }
  }

  // Method to create a database.
  public void createDb(String newDbName)
  {
    // Declaring variables.
    setDbName(newDbName);
    String URL = "jdbc:derby:" + dbName + ";create=true";
    dbConn = null;

    // This code snippet attempts to create the database from the given database name.
    try
    {
      System.out.println("Creating Database...");
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      dbConn = DriverManager.getConnection(URL);
      System.out.println("Database, " + this.dbName + ", has been created.");
    }
    catch (ClassNotFoundException ex)
    {
      System.out.println("Driver not found, check library");
    }
    catch (SQLException se)
    {
      System.out.println("SQL Connection error. Database was not created...");
    }
  }

  // Method to create a new table in the given database.
  public void createTable(String newTable, String dbName)
  {
    // Declaring variables.
    setDbName(dbName);
    setDbConn();
    Statement s;

    // This code snippet attempts to create a table with the given table name in the given database.
    try
    {
      s = dbConn.createStatement();
      s.execute(newTable);
      System.out.println("New table created!");
    }
    catch (SQLException se)
    {
      System.out.println("Error creating new table" + newTable);
    }
  }

  // Method to convert the data in the form of an arraylist into a 2d array.
  public Object[][] to2dArray(ArrayList<ArrayList<String>> dataList)
  {
    // Declaring the variables.
    Object[][] data = null;

    if (dataList.size() == 0)
    {
      data = new Object[0][0];
    }
    else
    {
      int columnCount = dataList.get(0).size();
      data = new Object[dataList.size()][columnCount];
      // Loops to get a row and read each element in that row.
      for (int r = 0; r < dataList.size(); r++)
      {
        ArrayList<String> row = dataList.get(r);

        for (int c = 0; c < columnCount; c++)
        {
          data[r][c] = row.get(c);
        }
      }
    }
    return data;
  }

  public static void main(String[] args)
  {

  }
}

