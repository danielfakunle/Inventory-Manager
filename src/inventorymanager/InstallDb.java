// 10-1-2021---This class installs the database used in my program.
package inventorymanager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InstallDb
{

  public static void main(String[] args)
  {
    // Declaring variables and create table queries.
    String dbName = "StoreName";
    Connection myDbConn;
    String userLoginTable = "CREATE TABLE UserLogin(userID int, username varchar(100), password varchar(100), usertype varchar(100))";
    String userDetailsTable = "CREATE TABLE UserDetails(userID int, employeeName varchar(100), phone varchar(100), email varchar(100))";
    String itemInfoTable = "CREATE TABLE ItemInfo(itemID int, itemName varchar(100), itemCategory varchar(100), expiryDate varchar(100))";
    String itemCostTable = "CREATE TABLE ItemCost(itemID int, unitCost double, itemQuantity int, totalCost double)";
    String itemPriceTable = "CREATE TABLE ItemPrice(itemID int, unitPrice double, itemQuantity int, totalPrice double)";
    String customersTable = "CREATE TABLE Customers(customerID int, name varchar(100), phone varchar(100), email varchar(100))";
    String retailersTable = "CREATE TABLE Retailers(retailerID int, companyName varchar(100), representativeName varchar(100), phone varchar(100), email varchar(100))";
    String inventoryOrdersTable = "CREATE TABLE InventoryOrders(orderID int, retailerName varchar(100), orderedBy varchar(100), status varchar(100), dateCreated varchar(100), dateShipped varchar(100))";
    String orderDetailsTable = "CREATE TABLE OrderDetail(orderID int, productName varchar(100), quantity int, unitCost double)";
    String orderCostTable = "CREATE TABLE OrderCost(orderID int, quantity int, unitCost double, subtotal double)";
    String profitTable = "CREATE TABLE ProfitHistory(entryID int, costofGoods double, priceofGoods double, profit double, date varchar(100))";

    // Declaring insert queries.
    String userLoginQuery = "INSERT INTO UserLogin VALUES (1, 'admin', 'admin', 'Admin')"
      + ", (2, 'employee1', 'emp1', 'Employee')"
      + ", (3, 'owner', 'owner', 'Owner')";
    String userDetailsQuery = "INSERT INTO UserDetails VALUES (1, 'Admin', 'N/A', 'N/A')"
      + ", (2, 'Employee 1', '+1-999-555-3333', 'sisv@gmail.com')"
      + ", (3, 'Owner', 'N/A', 'N/A')";
    String itemInfoQuery = "INSERT INTO ItemInfo VALUES (1, 'Apple Juice', 'Beverages', '01/01/2022')"
      + ", (2, 'Orange Juice', 'Beverages', '01/05/2022')"
      + ", (3, 'Yale Bread', 'Confectionaries', '01/09/2022')"
      + ", (4, 'Indomie Noodles', 'Food', '01/06/2022')"
      + ", (5, 'Sprite', 'Beverages', '01/10/2022')"
      + ", (6, 'Sugar', 'Confectionaries', '01/01/2023')"
      + ", (7, 'Aquafina', 'Water', '01/07/2023')";
    String itemCostQuery = "INSERT INTO ItemCost VALUES (1, 1.75, 24, 42), (2, 2.15, 12, 25.8), (3, 1.15, 8, 9.2)"
      + ", (4, 0.4, 48, 19.2)"
      + ", (5, 1.5, 14, 21)"
      + ", (6, 1.7, 6, 10.2)"
      + ", (7, 0.5, 16, 8)";
    String itemPriceQuery = "INSERT INTO ItemPrice VALUES (1 , 2, 24, 48), (2, 3, 12, 36), (3, 2, 8, 16)"
      + ", (4, 0.6, 48, 28.8)"
      + ", (5, 1.8, 14, 25.2)"
      + ", (6, 2.7, 6, 16.2)"
      + ", (7, 0.8, 16, 12.8)";
    String customersQuery = "INSERT INTO Customers VALUES (1, 'Customer 1', '+1-936-389-3694', 'customer1@gmail.com')"
      + ", (2, 'Customer 2', '+1-347-853-2863', 'customer2@gmail.com')"
      + ", (3, 'Customer 3', '+1-243-363-1478', 'customer3@gmail.com')";
    String retailersQuery = "INSERT INTO Retailers VALUES (1, 'CocaCola', 'Mr. Gonzalez', '+1-832-964-2367', 'cocacola@gmail.com'),"
      + " (2, 'Funtuna Food', 'Mr. Jefe', '+1-763-278-2743', 'funtunafood@gmail.com')";
    String inventoryOrdersQuery = "INSERT INTO InventoryOrders VALUES (1, 'CocaCola', 'Employee 1', 'Not Shipped', '15/08/2021', '10/12/2021')";
    String orderDetailsQuery = "INSERT INTO OrderDetail VALUES (1, 'CocaCola Soft Drinks (x12)', 10, 7.99)";
    String orderCostQuery = "INSERT INTO OrderCost VALUES (1, 10, 7.99, 79.9)";
    String profitQuery = "INSERT INTO ProfitHistory VALUES (1, 0.0, 0.0, 0.0, '01/01/2021')";

    // Creating a new object of JavaDatabase.
    JavaDatabase objDb = new JavaDatabase();
    objDb.setDbName(dbName);
    myDbConn = objDb.getDbConn();
    objDb.createDb(dbName);

    // Creating the tables for the database
    objDb.createTable(userLoginTable, dbName);
    objDb.createTable(userDetailsTable, dbName);
    objDb.createTable(itemInfoTable, dbName);
    objDb.createTable(itemCostTable, dbName);
    objDb.createTable(itemPriceTable, dbName);
    objDb.createTable(customersTable, dbName);
    objDb.createTable(retailersTable, dbName);
    objDb.createTable(inventoryOrdersTable, dbName);
    objDb.createTable(orderDetailsTable, dbName);
    objDb.createTable(orderCostTable, dbName);
    objDb.createTable(profitTable, dbName);

    // Inserting test data into the database.
    myDbConn = objDb.getDbConn();
    try
    {
      System.out.println("Adding test data to tables...");
      Statement s = myDbConn.createStatement();
      s.executeUpdate(userLoginQuery);
      s.executeUpdate(userDetailsQuery);
      s.executeUpdate(itemInfoQuery);
      s.executeUpdate(itemCostQuery);
      s.executeUpdate(itemPriceQuery);
      s.executeUpdate(customersQuery);
      s.executeUpdate(retailersQuery);
      s.executeUpdate(inventoryOrdersQuery);
      s.executeUpdate(orderDetailsQuery);
      s.executeUpdate(orderCostQuery);
      s.executeUpdate(profitQuery);
      System.out.println("Test data added!");
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
}
