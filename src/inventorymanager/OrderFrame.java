//9-18-2021---This class displays the order frame which allows the user to manage all orders.
package inventorymanager;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class OrderFrame extends JDialog implements ActionListener
{

  // Declaring GUI components.
  JPanel sidePanel;
  JPanel mainPanel;
  JLabel orderTitle;
  String[] columnNames;
  Object[][] inventoryOrdersData;
  Object[][] retailersData;
  Object[][] ordersTableData;
  ArrayList<ArrayList<String>> inventoryOrdersList;
  ArrayList<ArrayList<String>> retailersList;
  Object[][] orderDetailsData;
  Object[][] orderCostData;
  ArrayList<ArrayList<String>> orderDetailsList;
  ArrayList<ArrayList<String>> orderCostList;
  JTable orderTable;
  DefaultTableModel orderTableModel;
  JScrollPane orderTableScrollPane;
  JLabel retailerLabel;
  String[] retailers;
  JComboBox<String> retailerMenu;
  JLabel orderIdLabel;
  JTextField orderIdField;
  JLabel orderedByLabel;
  JTextField orderedByField;
  JLabel statusLabel;
  JTextField statusField;
  JLabel dateCreatedLabel;
  JDateChooser dateCreatedChooser;
  JLabel dateShippedLabel;
  JDateChooser dateShippedChooser;
  public static SimpleDateFormat dateFormat;
  JButton newOrder;
  JButton updateOrder;
  JButton deleteOrder;
  JButton confirmShipment;
  JLabel moreDetailsTitle;
  JLabel productNameLabel;
  JTextField productNameField;
  JLabel quantityLabel;
  JTextField quantityField;
  JLabel unitCostLabel;
  JTextField unitCostField;
  JLabel subTotalLabel;
  JTextField subTotalField;
  double subTotalValue;
  JButton editRetailers;
  int startOrderId;

  // Constructor for the jframe.
  public OrderFrame(String dbName, String inventoryOrdersTableName, String retailersTableName, String orderDetailsTableName, String orderCostTableName, String[] orderDetailsColumnNames, String[] orderCostColumnNames, String[] inventoryOrdersColumnNames, String[] retailersColumnNames)
  {
    this.setTitle("Store Name Inventory Manager");
    this.setBounds(0, 0, 960, 720);
    this.getContentPane().setBackground(Color.white);
    this.setLocationByPlatform(true);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setResizable(false);
    this.setLayout(null);
    this.setModal(true);

    // Declaring objects of the GUI components.
    this.mainPanel = new JPanel(null);
    this.sidePanel = new JPanel(null);
    this.orderTitle = new JLabel("Orders");
    this.columnNames = new String[]
    {
      "Order ID", "Retailer", "Ordered By", "Status", "Date Created", "Date Shipped"
    };

    // Connecting to the database
    JavaDatabase objDb = new JavaDatabase(dbName);
    objDb.setDbConn();

    // Using database object to get the data from the respective table and convert them to 2darrays.
    inventoryOrdersList = objDb.getData(inventoryOrdersTableName, inventoryOrdersColumnNames);
    retailersList = objDb.getData(retailersTableName, retailersColumnNames);
    inventoryOrdersData = objDb.to2dArray(inventoryOrdersList);
    retailersData = objDb.to2dArray(retailersList);
    orderDetailsList = objDb.getData(orderDetailsTableName, orderDetailsColumnNames);
    orderCostList = objDb.getData(orderCostTableName, orderCostColumnNames);
    orderDetailsData = objDb.to2dArray(orderDetailsList);
    orderCostData = objDb.to2dArray(orderCostList);

    this.ordersTableData = new Object[inventoryOrdersList.size()][6];

    // Loops to add the data to an ordersTableData 2darray in a specific order.
    for (int i = 0; i < inventoryOrdersList.size(); i++)
    {
      for (int j = 0; j < 6; j++)
      {
        ordersTableData[i][j] = inventoryOrdersData[i][j];
      }
    }

    this.orderTableModel = new DefaultTableModel(ordersTableData, columnNames)
    {
      public boolean isCellEditable(int row, int column)
      {
        return false;
      }
    };
    this.orderTable = new JTable(orderTableModel);
    this.orderTableScrollPane = new JScrollPane(orderTable);
    this.retailerLabel = new JLabel("Retailer");

    // Getting retailers data and adding it to a combo box
    this.retailers = new String[retailersList.size()];
    for (int i = 0; i < retailersList.size(); i++)
    {
      for (int j = 1; j < 2; j++)
      {
        retailers[i] = String.valueOf(retailersData[i][j]);
      }
    }
    this.retailerMenu = new JComboBox<String>(retailers);

    this.orderIdLabel = new JLabel("Order ID");
    this.orderIdField = new JTextField();
    this.orderedByLabel = new JLabel("Ordered By");
    this.orderedByField = new JTextField();
    this.statusLabel = new JLabel("Status");
    this.statusField = new JTextField();
    this.dateCreatedLabel = new JLabel("Date Created");
    this.dateCreatedChooser = new JDateChooser();
    this.dateShippedLabel = new JLabel("Date Shipped");
    this.dateShippedChooser = new JDateChooser();
    this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    this.newOrder = new JButton("New Order");
    this.updateOrder = new JButton("Update Order");
    this.deleteOrder = new JButton("Delete Order");
    this.confirmShipment = new JButton("Confirm Shipment");
    this.moreDetailsTitle = new JLabel("More Details");
    this.productNameLabel = new JLabel("Product Name");
    this.productNameField = new JTextField();
    this.quantityLabel = new JLabel("Quantity");
    this.quantityField = new JTextField();
    this.unitCostLabel = new JLabel("Unit Cost");
    this.unitCostField = new JTextField();
    this.subTotalLabel = new JLabel("Sub Total");
    this.subTotalField = new JTextField();
    this.editRetailers = new JButton("Edit Retailers");

    // Customizing GUI components.
    sidePanel.setBounds(0, 0, 80, 720);
    sidePanel.setBackground(LoginFrame.PRIMARY_COLOR);
    mainPanel.setBounds(80, 0, 880, 720);
    mainPanel.setBackground(LoginFrame.FRAME_COLOR);
    orderTitle.setBounds(15, 25, 120, 24);
    orderTitle.setFont(Dashboard.WELCOME_FONT);
    orderTable.setFont(Dashboard.CHART_FONT);
    orderTable.setRowHeight(25);
    orderTable.getTableHeader().setFont(Dashboard.CHART_FONT);
    orderTableScrollPane.setBounds(50, 345, 620, 275);
    retailerMenu.setFont(Dashboard.PANEL_ITEMS_FONT);
    orderIdLabel.setBounds(50, 80, 65, 25);
    orderIdLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    orderIdField.setBounds(135, 80, 75, 25);
    orderIdField.setBorder(BorderFactory.createEmptyBorder());
    orderIdField.setFont(LoginFrame.TEXT_FONT);
    orderIdField.setEnabled(false);
    orderedByLabel.setBounds(50, 120, 100, 25);
    orderedByLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    orderedByField.setBounds(135, 120, 200, 25);
    orderedByField.setBorder(BorderFactory.createEmptyBorder());
    orderedByField.setFont(LoginFrame.TEXT_FONT);
    retailerLabel.setBounds(50, 160, 70, 25);
    retailerLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    retailerMenu.setBounds(135, 160, 200, 25);
    statusLabel.setBounds(50, 200, 100, 25);
    statusLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    statusField.setBounds(135, 200, 110, 25);
    statusField.setBorder(BorderFactory.createEmptyBorder());
    statusField.setFont(LoginFrame.TEXT_FONT);
    statusField.setEnabled(false);
    dateCreatedLabel.setBounds(387, 80, 95, 25);
    dateCreatedLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    dateCreatedChooser.setBounds(482, 80, 127, 25);
    dateCreatedChooser.setFont(LoginFrame.TEXT_FONT);
    dateCreatedChooser.setDateFormatString("dd/MM/yyyy");
    dateShippedLabel.setBounds(387, 120, 95, 25);
    dateShippedLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    dateShippedChooser.setBounds(482, 120, 127, 25);
    dateShippedChooser.setFont(LoginFrame.TEXT_FONT);
    dateShippedChooser.setDateFormatString("dd/MM/yyyy");
    newOrder.setBounds(387, 160, 100, 25);
    newOrder.setFont(LoginFrame.TEXT_FONT);
    newOrder.setFocusPainted(false);
    newOrder.addActionListener(this);
    updateOrder.setBounds(500, 160, 130, 25);
    updateOrder.setFont(LoginFrame.TEXT_FONT);
    updateOrder.setFocusPainted(false);
    updateOrder.addActionListener(this);
    deleteOrder.setBounds(500, 200, 130, 25);
    deleteOrder.setFont(LoginFrame.TEXT_FONT);
    deleteOrder.setFocusPainted(false);
    deleteOrder.addActionListener(this);
    confirmShipment.setBounds(643, 200, 167, 25);
    confirmShipment.setFont(LoginFrame.TEXT_FONT);
    confirmShipment.setFocusPainted(false);
    confirmShipment.addActionListener(this);
    moreDetailsTitle.setBounds(50, 230, 100, 25);
    moreDetailsTitle.setFont(Dashboard.PANEL_ITEMS_FONT);
    moreDetailsTitle.setForeground(LoginFrame.PRIMARY_COLOR);
    productNameLabel.setBounds(50, 260, 105, 25);
    productNameLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    productNameField.setBounds(165, 260, 235, 25);
    productNameField.setBorder(BorderFactory.createEmptyBorder());
    productNameField.setFont(LoginFrame.TEXT_FONT);
    quantityLabel.setBounds(50, 300, 105, 25);
    quantityLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    quantityField.setBounds(165, 300, 75, 25);
    quantityField.setBorder(BorderFactory.createEmptyBorder());
    quantityField.setFont(LoginFrame.TEXT_FONT);
    unitCostLabel.setBounds(437, 260, 105, 25);
    unitCostLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    unitCostField.setBounds(512, 260, 125, 25);
    unitCostField.setBorder(BorderFactory.createEmptyBorder());
    unitCostField.setFont(LoginFrame.TEXT_FONT);
    subTotalLabel.setBounds(437, 300, 105, 25);
    subTotalLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    subTotalField.setBounds(512, 300, 125, 25);
    subTotalField.setBorder(BorderFactory.createEmptyBorder());
    subTotalField.setFont(LoginFrame.TEXT_FONT);
    editRetailers.setBounds(685, 345, 130, 25);
    editRetailers.setFont(LoginFrame.TEXT_FONT);
    editRetailers.setFocusPainted(false);
    editRetailers.addActionListener(this);

    // This code snippet inputs an itemId for a new item at the start of the frame
    // If no items in table.
    if (orderTable.getRowCount() == 0)
    {
      orderIdField.setText("1");
    }
    // Here, setting the new order id to a value 1 higher than highest id value.
    else
    {
      startOrderId = Integer.valueOf(String.valueOf(inventoryOrdersData[inventoryOrdersList.size() - 1][0])) + 1;
      orderIdField.setText(String.valueOf(startOrderId));
    }

    statusField.setText("Not Shipped");

    // Adding mouselistener to table to get selected row.
    orderTable.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        int currentRow = orderTable.getSelectedRow();
        DateTimeFormatter localDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        orderIdField.setText(String.valueOf(orderTable.getValueAt(currentRow, 0)));
        orderedByField.setText(String.valueOf(orderTable.getValueAt(currentRow, 2)));
        retailerMenu.setSelectedItem(orderTable.getValueAt(currentRow, 1));
        statusField.setText(String.valueOf(orderTable.getValueAt(currentRow, 3)));
        dateCreatedChooser.setDate(InventoryFrame.convertStringToDate(String.valueOf(orderTable.getValueAt(currentRow, 4)), localDateFormat));
        dateShippedChooser.setDate(InventoryFrame.convertStringToDate(String.valueOf(orderTable.getValueAt(currentRow, 5)), localDateFormat));
        productNameField.setText(String.valueOf(orderDetailsData[currentRow][1]));
        quantityField.setText(String.valueOf(orderDetailsData[currentRow][2]));
        unitCostField.setText(String.valueOf(orderDetailsData[currentRow][3]));
        subTotalField.setText(String.valueOf(orderCostData[currentRow][3]));
      }
    });


    // Adding GUI components to the frame.
    mainPanel.add(orderTitle);
    mainPanel.add(orderTableScrollPane);
    mainPanel.add(orderIdLabel);
    mainPanel.add(orderIdField);
    mainPanel.add(orderedByLabel);
    mainPanel.add(orderedByField);
    mainPanel.add(retailerMenu);
    mainPanel.add(retailerLabel);
    mainPanel.add(statusLabel);
    mainPanel.add(statusField);
    mainPanel.add(dateCreatedLabel);
    mainPanel.add(dateCreatedChooser);
    mainPanel.add(dateShippedLabel);
    mainPanel.add(dateShippedChooser);
    mainPanel.add(newOrder);
    mainPanel.add(updateOrder);
    mainPanel.add(deleteOrder);
    mainPanel.add(confirmShipment);
    mainPanel.add(moreDetailsTitle);
    mainPanel.add(productNameLabel);
    mainPanel.add(productNameField);
    mainPanel.add(quantityLabel);
    mainPanel.add(quantityField);
    mainPanel.add(unitCostLabel);
    mainPanel.add(unitCostField);
    mainPanel.add(subTotalLabel);
    mainPanel.add(subTotalField);
    mainPanel.add(editRetailers);
    this.add(mainPanel);
    this.add(sidePanel);

    // Displaying the frame.
    this.setVisible(true);
  }

  public static void main(String[] args)
  {
    // This code snippet attempts to change the look and feel of the frame.
    try
    {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    }
    catch (Exception e)
    {

    }

    // Info for database access.
    String dbName = "StoreName";
    String inventoryOrdersTableName = "InventoryOrders";
    String retailersTableName = "Retailers";
    String orderDetailsTableName = "OrderDetail";
    String orderCostTableName = "OrderCost";
    String[] inventoryOrdersColumnNames =
    {
      "orderID", "retailerName", "orderedBy", "status", "dateCreated", "dateShipped"
    };
    String[] retailersColumnNames =
    {
      "retailerID", "companyName", "representativeName", "phone", "email"
    };
    String[] orderDetailsColumnNames =
    {
      "orderID", "productName", "quantity", "unitCost"
    };
    String[] orderCostColumnNames =
    {
      "orderID", "quantity", "unitCost", "subtotal"
    };

    // Creating a new object of jframe.
    new OrderFrame(dbName, inventoryOrdersTableName, retailersTableName, orderDetailsTableName, orderCostTableName, orderDetailsColumnNames, orderCostColumnNames, inventoryOrdersColumnNames, retailersColumnNames);

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    // Getting the identifier for the object that performed the action.
    Object command = e.getSource();

    // Declaring variables.
    int deleteDecision;
    int confirmDecision;
    int newOrderId;
    int orderId = 0;
    String orderedBy;
    String retailer;
    String status;
    String dateCreated;
    String dateShipped;
    String productName;
    int quantity;
    double unitCost;
    double subTotal;

    // Info for database access.
    String dbName = "StoreName";
    String inventoryOrdersTableName = "InventoryOrders";
    String retailersTableName = "Retailers";
    String orderDetailsTableName = "OrderDetail";
    String orderCostTableName = "OrderCost";
    String[] inventoryOrdersColumnNames =
    {
      "orderID", "retailerName", "orderedBy", "status", "dateCreated", "dateShipped"
    };
    String[] retailersColumnNames =
    {
      "retailerID", "companyName", "representativeName", "phone", "email"
    };
    String[] orderDetailsColumnNames =
    {
      "orderID", "productName", "quantity", "unitCost"
    };
    String[] orderCostColumnNames =
    {
      "orderID", "quantity", "unitCost", "subtotal"
    };
    String inventoryOrdersQuery = "INSERT INTO InventoryOrders VALUES (?,?,?,?,?,?)";
    String orderDetailsQuery = "INSERT INTO OrderDetail VALUES (?,?,?,?)";
    String orderCostQuery = "INSERT INTO OrderCost VALUES (?,?,?,?)";

    // Connecting to the database.
    JavaDatabase objDb = new JavaDatabase(dbName);
    Connection myDbConn = objDb.getDbConn();

    // Conditions for specific actions.
    if (command.equals(newOrder))
    {
      // Setting all data entry fields to null.
      inventoryOrdersList = objDb.getData(inventoryOrdersTableName, inventoryOrdersColumnNames);
      inventoryOrdersData = objDb.to2dArray(inventoryOrdersList);
      // If no items in table.
      if (orderTable.getRowCount() == 0)
      {
        orderIdField.setText("1");
      }
      else
      {
        // Here, setting the new item id to a value 1 higher than highest id value.
        newOrderId = Integer.valueOf(String.valueOf(inventoryOrdersData[inventoryOrdersList.size() - 1][0])) + 1;
        orderIdField.setText(String.valueOf(newOrderId));
      }
      statusField.setText("Not Shipped");
      orderedByField.setText("");
      dateCreatedChooser.setDate(null);
      dateShippedChooser.setDate(null);
      productNameField.setText("");
      unitCostField.setText("");
      quantityField.setText("");
      subTotalField.setText("");

    }
    else if (command.equals(updateOrder))
    {
      // This code snippet attempts to update the database using text retrieved from the frame.
      int orderIdExists = 0;
      // When the order exists.
      for (int i = 0; i < orderTable.getRowCount(); i++)
      {
        if (Integer.valueOf(orderIdField.getText()) == Integer.valueOf(String.valueOf(orderTable.getValueAt(i, 0))))
        {
          orderIdExists = 1;
          orderId = Integer.valueOf(orderIdField.getText());
          break;
        }
      }

      if (orderIdExists == 1)
      {
        try
        {
          String updateOrdersQuery = "UPDATE InventoryOrders SET retailerName = ?, orderedBy = ?, status = ?, dateCreated = ?, dateShipped = ? WHERE orderID = " + orderId;
          String updateDetailsQuery = "UPDATE OrderDetail SET productName = ?, quantity = ?, unitCost = ? WHERE orderID = " + orderId;
          String updateCostQuery = "UPDATE OrderCost SET quantity = ?, unitCost = ?, subtotal = ? WHERE orderID = " + orderId;

          PreparedStatement ps = myDbConn.prepareStatement(updateOrdersQuery);

          orderedBy = orderedByField.getText();
          retailer = String.valueOf(retailerMenu.getSelectedItem());
          status = statusField.getText();
          dateCreated = InventoryFrame.convertDateToString(dateCreatedChooser.getDate());
          dateShipped = InventoryFrame.convertDateToString(dateShippedChooser.getDate());
          productName = productNameField.getText();
          quantity = Integer.parseInt(quantityField.getText());
          unitCost = Double.parseDouble(unitCostField.getText());
          subTotal = Double.parseDouble(subTotalField.getText());

          ps.setString(1, retailer);
          ps.setString(2, orderedBy);
          ps.setString(3, status);
          ps.setString(4, dateCreated);
          ps.setString(5, dateShipped);
          ps.executeUpdate();

          ps = myDbConn.prepareStatement(updateDetailsQuery);

          ps.setString(1, productName);
          ps.setInt(2, quantity);
          ps.setDouble(3, unitCost);
          ps.executeUpdate();

          ps = myDbConn.prepareStatement(updateCostQuery);

          ps.setInt(1, quantity);
          ps.setDouble(2, unitCost);
          ps.setDouble(3, subTotal);
          ps.executeUpdate();

          // The following code snippet reloads the frame.
          JOptionPane.showMessageDialog(new JFrame(), "Data updated successfully. Reloading...");
          this.dispose();
          new OrderFrame(dbName, inventoryOrdersTableName, retailersTableName, orderDetailsTableName, orderCostTableName, orderDetailsColumnNames, orderCostColumnNames, inventoryOrdersColumnNames, retailersColumnNames);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error updating data.", "Error", JOptionPane.ERROR_MESSAGE);;
        }
      }
      // When inserting a new order.
      else
      {
        try
        {
          PreparedStatement ps = myDbConn.prepareStatement(inventoryOrdersQuery);

          orderId = Integer.parseInt(orderIdField.getText());
          orderedBy = orderedByField.getText();
          retailer = String.valueOf(retailerMenu.getSelectedItem());
          status = statusField.getText();
          dateCreated = InventoryFrame.convertDateToString(dateCreatedChooser.getDate());
          dateShipped = InventoryFrame.convertDateToString(dateShippedChooser.getDate());
          productName = productNameField.getText();
          quantity = Integer.parseInt(quantityField.getText());
          unitCost = Double.parseDouble(unitCostField.getText());
          subTotal = Double.parseDouble(subTotalField.getText());

          ps.setInt(1, orderId);
          ps.setString(2, retailer);
          ps.setString(3, orderedBy);
          ps.setString(4, status);
          ps.setString(5, dateCreated);
          ps.setString(6, dateShipped);
          ps.executeUpdate();

          ps = myDbConn.prepareStatement(orderDetailsQuery);

          ps.setInt(1, orderId);
          ps.setString(2, productName);
          ps.setInt(3, quantity);
          ps.setDouble(4, unitCost);
          ps.executeUpdate();

          ps = myDbConn.prepareStatement(orderCostQuery);

          ps.setInt(1, orderId);
          ps.setInt(2, quantity);
          ps.setDouble(3, unitCost);
          ps.setDouble(4, subTotal);
          ps.executeUpdate();
          JOptionPane.showMessageDialog(new JFrame(), "Data Updated Successfully. Click OK to reset window");

          // The following code snippet reloads the frame.
          this.dispose();
          new OrderFrame(dbName, inventoryOrdersTableName, retailersTableName, orderDetailsTableName, orderCostTableName, orderDetailsColumnNames, orderCostColumnNames, inventoryOrdersColumnNames, retailersColumnNames);
        }
        catch (NumberFormatException nfe)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error updating data. Please make sure the data you've entered is valid", "Incorrect Values", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error updating data.", "Error", JOptionPane.ERROR_MESSAGE);;
        }
      }
    }
    else if (command.equals(deleteOrder))
    {
      // This code snippet attempts to delete data in the selected row from the database
      deleteDecision = JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to delete this order", "Delete Order", JOptionPane.YES_NO_OPTION);
      if (deleteDecision == JOptionPane.YES_OPTION)
      {
        try
        {
          int currentRow = orderTable.getSelectedRow();
          String idForRow = String.valueOf(orderTable.getValueAt(currentRow, 0));
          Statement s = myDbConn.createStatement();

          String deleteOrderQuery = "DELETE FROM InventoryOrders WHERE orderID = " + Integer.parseInt(idForRow);
          String deleteOrderDetailQuery = "DELETE FROM OrderDetail WHERE orderID = " + Integer.parseInt(idForRow);
          String deleteOrderCostQuery = "DELETE FROM OrderCost WHERE orderID = " + Integer.parseInt(idForRow);

          s.executeUpdate(deleteOrderQuery);
          s.executeUpdate(deleteOrderDetailQuery);
          s.executeUpdate(deleteOrderCostQuery);

          // The following code snippet reloads the frame.
          this.dispose();
          new OrderFrame(dbName, inventoryOrdersTableName, retailersTableName, orderDetailsTableName, orderCostTableName, orderDetailsColumnNames, orderCostColumnNames, inventoryOrdersColumnNames, retailersColumnNames);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error deleting data. Select a row if not selected", "Error", JOptionPane.ERROR_MESSAGE);;
        }
      }
    }
    else if (command.equals(confirmShipment))
    {
      // This code snippet attempts to mark the selected order as "Shipped"
      confirmDecision = JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to confirm the shipment", "Confirm Shipment", JOptionPane.YES_NO_OPTION);
      if (confirmDecision == JOptionPane.YES_OPTION)
      {
        try
        {
          orderId = Integer.valueOf(String.valueOf(orderIdField.getText()));
          String setShippedQuery = "UPDATE InventoryOrders SET status = 'Shipped' WHERE orderID = " + orderId;
          Statement s = myDbConn.createStatement();
          s.executeUpdate(setShippedQuery);

          // The following code snippet reloads the frame.
          JOptionPane.showMessageDialog(new JFrame(), "Done. Reloading...");
          this.dispose();
          new OrderFrame(dbName, inventoryOrdersTableName, retailersTableName, orderDetailsTableName, orderCostTableName, orderDetailsColumnNames, orderCostColumnNames, inventoryOrdersColumnNames, retailersColumnNames);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error updating data.", "Error", JOptionPane.ERROR_MESSAGE);;
        }
      }
    }
    else if (command.equals(editRetailers))
    {
      // Info for Database access.
      String retailerTableName = "Retailers";
      String[] retailerColumnNames =
      {
        "retailerID", "companyName", "representativeName", "phone", "email"
      };

      // Creating a new object of jframe.
      new RetailerFrame(dbName, retailerTableName, retailerColumnNames);
    }

    this.validate();
    this.repaint();
  }

}

