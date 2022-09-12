//9-18-2021---This class displays the checkout frame where the user complete the checkout process, and manage customers.
package inventorymanager;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class CheckoutFrame extends JDialog implements ActionListener
{
  // Declaring GUI components.
  JPanel sidePanel;
  JPanel mainPanel;
  JLabel checkoutTitle;
  JLabel customerTableTitle;
  JTextField customerSearchBar;
  JLabel customerSearchLabel;
  String[] customerColumnNames;
  ArrayList<ArrayList<String>> customerList;
  Object[][] customerData;
  JTable customerTable;
  DefaultTableModel customerTableModel;
  JScrollPane customerTableScrollPane;
  JLabel itemsTableTitle;
  JTextField itemsSearchBar;
  JLabel itemsSearchLabel;
  String[] itemsColumnNames;
  Object[][] itemsData;
  ArrayList<ArrayList<String>> itemInfoList;
  ArrayList<ArrayList<String>> itemCostList;
  ArrayList<ArrayList<String>> itemPriceList;
  ArrayList<ArrayList<String>> profitList;
  Object[][] profitData;
  Object[][] itemInfoData;
  Object[][] itemCostData;
  Object[][] itemPriceData;
  JTable itemsTable;
  DefaultTableModel itemsTableModel;
  JScrollPane itemsTableScrollPane;
  String[] checkoutColumnNames;
  Object[][] checkoutData;
  JTable checkoutTable;
  DefaultTableModel checkoutTableModel;
  JScrollPane checkoutTableScrollPane;
  JLabel customerEditTitle;
  JButton addNew;
  JButton update;
  JButton delete;
  JLabel idLabel;
  JTextField idField;
  JLabel nameLabel;
  JTextField nameField;
  JLabel phoneLabel;
  JTextField phoneField;
  JLabel emailLabel;
  JTextField emailField;
  JLabel itemCheckoutTitle;
  JLabel totalLabel;
  JLabel totalValue;
  JButton remove;
  JButton clear;
  JButton checkout;
  JButton addToCheckout;
  int startCustomerId;
  TableRowSorter customerSorter;
  TableRowSorter itemsSorter;

  // Constructor for the jframe.
  public CheckoutFrame(String dbName, String infoTableName, String priceTableName, String[] infoColumnNames, String[] priceColumnNames, String customerTableName, String retailerTableName, String[] customerColumnNames, String[] retailerColumnNames, String costTableName, String[] costColumnNames)
  {
    this.setTitle("Store Name Inventory Manager");
    this.setBounds(0, 0, 1060, 720);
    this.getContentPane().setBackground(Color.white);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setResizable(false);
    this.setLayout(null);
    this.setModal(true);

    // Declaring objects of the GUI components.
    this.mainPanel = new JPanel(null);
    this.sidePanel = new JPanel(null);
    this.checkoutTitle = new JLabel("Checkout");
    this.customerTableTitle = new JLabel("Customers");
    this.customerSearchBar = new JTextField();
    this.customerSearchLabel = new JLabel("Search");
    this.customerColumnNames = new String[]
    {
      "ID", "Name", "Phone", "Email"
    };

    // Connecting to the database
    JavaDatabase objDb = new JavaDatabase(dbName);
    objDb.setDbConn();

    // Using database object to get the data from the respective table and convert them to 2darrays.
    customerList = objDb.getData(customerTableName, customerColumnNames);
    this.customerData = objDb.to2dArray(customerList);

    this.customerTableModel = new DefaultTableModel(customerData, this.customerColumnNames)
    {
      public boolean isCellEditable(int row, int column)
      {
        return false;
      }
    };
    this.customerTable = new JTable(customerTableModel);
    this.customerTableScrollPane = new JScrollPane(customerTable);

    this.itemsTableTitle = new JLabel("Items");
    this.itemsSearchBar = new JTextField();
    this.itemsSearchLabel = new JLabel("Search");
    this.itemsColumnNames = new String[]
    {
      "ID", "Name", "Category", "UnitPrice", "Quantity", "Unit Cost"
    };

    this.itemInfoList = objDb.getData(infoTableName, infoColumnNames);
    this.itemPriceList = objDb.getData(priceTableName, priceColumnNames);
    this.itemCostList = objDb.getData(costTableName, costColumnNames);
    this.itemInfoData = objDb.to2dArray(itemInfoList);
    this.itemPriceData = objDb.to2dArray(itemPriceList);
    this.itemCostData = objDb.to2dArray(itemCostList);
    this.itemsData = new Object[itemInfoList.size()][6];

    // Loops to get inventory data and put it into itemData for this class.
    for (int i = 0; i < itemInfoList.size(); i++)
    {
      for (int j = 0; j < 3; j++)
      {
        itemsData[i][j] = itemInfoData[i][j];
      }
    }
    for (int i = 0; i < itemPriceList.size(); i++)
    {
      for (int j = 1; j < 3; j++)
      {
        itemsData[i][j + 2] = itemPriceData[i][j];
      }
    }
    for (int i = 0; i < itemCostList.size(); i++)
    {
      for (int j = 1; j < 2; j++)
      {
        itemsData[i][j + 4] = itemCostData[i][j];
      }
    }

    this.itemsTableModel = new DefaultTableModel(itemsData, itemsColumnNames)
    {
      public boolean isCellEditable(int row, int column)
      {
        return false;
      }
    };
    this.itemsTable = new JTable(itemsTableModel);
    this.itemsTableScrollPane = new JScrollPane(itemsTable);
    this.checkoutColumnNames = new String[]
    {
      "ID", "Name", "UnitPrice", "Quantity", "Total Price", "Unit Cost"
    };
    this.checkoutData = new Object[][]
    {
    };
    this.checkoutTableModel = new DefaultTableModel()
    {
      public boolean isCellEditable(int row, int column)
      {
        return false;
      }
    };
    this.checkoutTable = new JTable(checkoutTableModel);
    checkoutTableModel.addColumn("ID");
    checkoutTableModel.addColumn("Name");
    checkoutTableModel.addColumn("UnitPrice");
    checkoutTableModel.addColumn("Quantity");
    checkoutTableModel.addColumn("Total Price");
    checkoutTableModel.addColumn("Unit Cost");
    this.checkoutTableScrollPane = new JScrollPane(checkoutTable);
    this.customerEditTitle = new JLabel("Customer Edit");
    this.addNew = new JButton("Add New");
    this.update = new JButton("Update");
    this.delete = new JButton("Delete");
    this.idLabel = new JLabel("ID");
    this.idField = new JTextField();
    this.nameLabel = new JLabel("Name");
    this.nameField = new JTextField();
    this.phoneLabel = new JLabel("Phone");
    this.phoneField = new JTextField();
    this.emailLabel = new JLabel("Email");
    this.emailField = new JTextField();
    this.itemCheckoutTitle = new JLabel("Item Checkout");
    this.totalLabel = new JLabel("Total: $");
    this.totalValue = new JLabel("0.00");
    this.remove = new JButton("Remove");
    this.clear = new JButton("Clear");
    this.checkout = new JButton("Checkout");
    this.addToCheckout = new JButton("--->");
    customerSorter = new TableRowSorter<>(customerTableModel);
    customerTable.setRowSorter(customerSorter);
    itemsSorter = new TableRowSorter<>(itemsTableModel);
    itemsTable.setRowSorter(itemsSorter);

    // Customizing GUI components.
    sidePanel.setBounds(0, 0, 80, 720);
    sidePanel.setBackground(LoginFrame.PRIMARY_COLOR);
    mainPanel.setBounds(80, 0, 980, 720);
    mainPanel.setBackground(LoginFrame.FRAME_COLOR);
    checkoutTitle.setBounds(15, 25, 120, 24);
    checkoutTitle.setFont(Dashboard.WELCOME_FONT);
    customerTableTitle.setBounds(50, 70, 70, 25);
    customerTableTitle.setFont(Dashboard.PANEL_ITEMS_FONT);
    customerSearchBar.setBounds(105, 95, 305, 25);
    customerSearchBar.setFont(LoginFrame.TEXT_FONT);
    customerSearchLabel.setBounds(50, 95, 65, 25);
    customerSearchLabel.setFont(LoginFrame.TEXT_FONT);
    customerTable.setFont(Dashboard.CHART_FONT);
    customerTable.setRowHeight(25);
    customerTable.getTableHeader().setFont(Dashboard.CHART_FONT);
    customerTableScrollPane.setBounds(50, 130, 360, 180);
    itemsTableTitle.setBounds(50, 320, 50, 25);
    itemsTableTitle.setFont(Dashboard.PANEL_ITEMS_FONT);
    itemsSearchBar.setBounds(105, 345, 305, 25);
    itemsSearchBar.setFont(LoginFrame.TEXT_FONT);
    itemsSearchLabel.setBounds(50, 345, 65, 25);
    itemsSearchLabel.setFont(LoginFrame.TEXT_FONT);
    itemsTable.setFont(Dashboard.CHART_FONT);
    itemsTable.setRowHeight(25);
    itemsTable.getTableHeader().setFont(Dashboard.CHART_FONT);
    itemsTableScrollPane.setBounds(50, 380, 360, 260);
    customerEditTitle.setBounds(500, 70, 120, 25);
    customerEditTitle.setFont(Dashboard.PANEL_ITEMS_FONT);
    addNew.setBounds(500, 95, 100, 25);
    addNew.setFont(LoginFrame.TEXT_FONT);
    addNew.setFocusPainted(false);
    addNew.addActionListener(this);
    update.setBounds(615, 95, 100, 25);
    update.setFont(LoginFrame.TEXT_FONT);
    update.setFocusPainted(false);
    update.addActionListener(this);
    delete.setBounds(730, 95, 100, 25);
    delete.setFont(LoginFrame.TEXT_FONT);
    delete.setFocusPainted(false);
    delete.addActionListener(this);
    idLabel.setBounds(502, 135, 25, 25);
    idLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    idField.setBounds(550, 135, 75, 25);
    idField.setBorder(BorderFactory.createEmptyBorder());
    idField.setFont(LoginFrame.TEXT_FONT);
    nameLabel.setBounds(652, 135, 40, 25);
    nameLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    nameField.setBounds(720, 135, 190, 25);
    nameField.setBorder(BorderFactory.createEmptyBorder());
    nameField.setFont(LoginFrame.TEXT_FONT);
    phoneLabel.setBounds(502, 175, 45, 25);
    phoneLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    phoneField.setBounds(560, 175, 125, 25);
    phoneField.setBorder(BorderFactory.createEmptyBorder());
    phoneField.setFont(LoginFrame.TEXT_FONT);
    emailLabel.setBounds(702, 175, 40, 25);
    emailLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    emailField.setBounds(770, 175, 140, 25);
    emailField.setBorder(BorderFactory.createEmptyBorder());
    emailField.setFont(LoginFrame.TEXT_FONT);
    itemCheckoutTitle.setBounds(500, 240, 120, 25);
    itemCheckoutTitle.setFont(Dashboard.PANEL_ITEMS_FONT);
    checkoutTable.setFont(Dashboard.CHART_FONT);
    checkoutTable.setRowHeight(25);
    checkoutTable.getTableHeader().setFont(Dashboard.CHART_FONT);
    checkoutTableScrollPane.setBounds(500, 270, 410, 300);
    totalLabel.setBounds(720, 575, 60, 25);
    totalLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    totalValue.setBounds(770, 575, 60, 25);
    totalValue.setFont(Dashboard.PANEL_ITEMS_FONT);
    remove.setBounds(500, 615, 100, 25);
    remove.setFont(LoginFrame.TEXT_FONT);
    remove.setFocusPainted(false);
    remove.addActionListener(this);
    clear.setBounds(615, 615, 100, 25);
    clear.setFont(LoginFrame.TEXT_FONT);
    clear.setFocusPainted(false);
    clear.addActionListener(this);
    checkout.setBounds(730, 615, 100, 25);
    checkout.setFont(LoginFrame.TEXT_FONT);
    checkout.setFocusPainted(false);
    checkout.addActionListener(this);
    addToCheckout.setBounds(420, 408, 68, 25);
    addToCheckout.setFont(LoginFrame.TEXT_FONT);
    addToCheckout.setFocusPainted(false);
    addToCheckout.addActionListener(this);
    idField.setEditable(false);

    // Using DocumentLister to implement filter function for the customer table
    customerSearchBar.getDocument().addDocumentListener(new DocumentListener()
    {
      @Override
      public void insertUpdate(DocumentEvent e)
      {
        search(customerSearchBar.getText());
      }

      @Override
      public void removeUpdate(DocumentEvent e)
      {
        search(customerSearchBar.getText());
      }

      @Override
      public void changedUpdate(DocumentEvent e)
      {
        search(customerSearchBar.getText());
      }

      public void search(String searchText)
      {
        if (searchText.length() == 0)
        {
          customerSorter.setRowFilter(null);
        }
        else
        {
          customerSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
      }
    });

    // Using DocumentLister to implement filter function for the items table
    itemsSearchBar.getDocument().addDocumentListener(new DocumentListener()
    {
      @Override
      public void insertUpdate(DocumentEvent e)
      {
        search(itemsSearchBar.getText());
      }

      @Override
      public void removeUpdate(DocumentEvent e)
      {
        search(itemsSearchBar.getText());
      }

      @Override
      public void changedUpdate(DocumentEvent e)
      {
        search(itemsSearchBar.getText());
      }

      public void search(String searchText)
      {
        if (searchText.length() == 0)
        {
          itemsSorter.setRowFilter(null);
        }
        else
        {
          itemsSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
      }
    });

    // Adding mouselistener to table to get selected row.
    customerTable.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        int currentRow = customerTable.getSelectedRow();
        idField.setText(String.valueOf(customerTable.getValueAt(currentRow, 0)));
        nameField.setText(String.valueOf(customerTable.getValueAt(currentRow, 1)));
        phoneField.setText(String.valueOf(customerTable.getValueAt(currentRow, 2)));
        emailField.setText(String.valueOf(customerTable.getValueAt(currentRow, 3)));
      }
    });

    // This code snippet inputs an customerID for a new customer at the start of the frame
    // If no items in table.
    if (customerTable.getRowCount() == 0)
    {
      idField.setText("1");
    }
    // Here, setting the new customer id to a value 1 higher than highest id value.
    else
    {
      startCustomerId = Integer.valueOf(String.valueOf(customerData[customerList.size() - 1][0])) + 1;
      idField.setText(String.valueOf(startCustomerId));
    }

    // Adding GUI components to the frame.
    mainPanel.add(checkoutTitle);
    mainPanel.add(customerTableScrollPane);
    mainPanel.add(customerTableTitle);
    mainPanel.add(customerSearchBar);
    mainPanel.add(customerSearchLabel);
    mainPanel.add(itemsTableScrollPane);
    mainPanel.add(itemsTableTitle);
    mainPanel.add(itemsSearchBar);
    mainPanel.add(itemsSearchLabel);
    mainPanel.add(checkoutTableScrollPane);
    mainPanel.add(customerEditTitle);
    mainPanel.add(addNew);
    mainPanel.add(update);
    mainPanel.add(delete);
    mainPanel.add(idLabel);
    mainPanel.add(idField);
    mainPanel.add(nameLabel);
    mainPanel.add(nameField);
    mainPanel.add(phoneLabel);
    mainPanel.add(phoneField);
    mainPanel.add(emailLabel);
    mainPanel.add(emailField);
    mainPanel.add(itemCheckoutTitle);
    mainPanel.add(totalLabel);
    mainPanel.add(totalValue);
    mainPanel.add(remove);
    mainPanel.add(clear);
    mainPanel.add(checkout);
    mainPanel.add(addToCheckout);
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
    String infoTableName = "ItemInfo";
    String priceTableName = "itemPrice";
    String costTableName = "ItemCost";
    String customerTableName = "Customers";
    String retailerTableName = "Retailers";
    String[] customerColumnNames =
    {
      "customerID", "name", "phone", "email"
    };
    String[] retailerColumnNames =
    {
      "retailerID", "companyName", "representativeName", "phone", "email"
    };
    String[] infoColumnNames =
    {
      "itemID", "itemName", "itemCategory", "expiryDate"
    };
    String[] priceColumnNames =
    {
      "itemID", "unitPrice", "itemQuantity", "totalPrice"
    };
    String[] costColumnNames =
    {
      "itemID", "unitCost", "itemQuantity", "totalCost"
    };

    // Creating a new object of jframe.
    new CheckoutFrame(dbName, infoTableName, priceTableName, infoColumnNames, priceColumnNames, customerTableName,
      retailerTableName, customerColumnNames, retailerColumnNames, costTableName, costColumnNames);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    // Getting the identifier for the object that performed the action.
    Object command = e.getSource();

    // Declaring variables.
    int deleteDecision;
    int newCustomerId;
    int customerId = 0;
    String name;
    String email;
    String phone;
    double checkoutTotal = 0;
    int customerRow = -1;
    String customerName;
    PrintWriter recieptWriter = null;
    Date todaysDate = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    File recieptFile;
    CalculateDailyProfit objProfit = new CalculateDailyProfit();
    Double totalCost = 0.0;
    Double currentDayCost = 0.0;
    Double currentDayPrice = 0.0;
    Double currentDailyProfit = 0.0;

    // Info for database access.
    String dbName = "StoreName";
    String infoTableName = "ItemInfo";
    String priceTableName = "itemPrice";
    String costTableName = "ItemCost";
    String customerTableName = "Customers";
    String retailerTableName = "Retailers";
    String profitTableName = "ProfitHistory";
    String[] profitColumnNames =
    {
      "entryID", "costofGoods", "priceofGoods", "profit", "date"
    };
    String[] customerColumnNames =
    {
      "customerID", "name", "phone", "email"
    };
    String[] retailerColumnNames =
    {
      "retailerID", "companyName", "representativeName", "phone", "email"
    };
    String[] infoColumnNames =
    {
      "itemID", "itemName", "itemCategory", "expiryDate"
    };
    String[] priceColumnNames =
    {
      "itemID", "unitPrice", "itemQuantity", "totalPrice"
    };
    String[] costColumnNames =
    {
      "itemID", "unitCost", "itemQuantity", "totalCost"
    };
    String customerQuery = "INSERT INTO Customers VALUES (?,?,?,?)";
    String updateCostQuery;
    String updatePriceQuery;

    // Connecting to the database.
    JavaDatabase objDb = new JavaDatabase(dbName);
    Connection myDbConn = objDb.getDbConn();

    // Conditions for specific actions.
    if (command.equals(addNew))
    {
      // Setting all data entry fields to null.
      customerList = objDb.getData(customerTableName, customerColumnNames);
      customerData = objDb.to2dArray(customerList);
      // If no items in table.
      if (customerTable.getRowCount() == 0)
      {
        idField.setText("1");
      }
      // Here, setting the new customer id to a value 1 higher than highest id value.
      else
      {
        newCustomerId = Integer.valueOf(String.valueOf(customerData[customerList.size() - 1][0])) + 1;
        idField.setText(String.valueOf(newCustomerId));
      }
      nameField.setText("");
      phoneField.setText("");
      emailField.setText("");
    }
    else if (command.equals(update))
    {
      // This code snippet attempts to update the database using text 
      // retrieved from the frame.
      int customerIdExists = 0;
      // When the customer exists.
      for (int i = 0; i < customerTable.getRowCount(); i++)
      {
        if (Integer.valueOf(idField.getText())
          == Integer.valueOf(String.valueOf(customerTable.getValueAt(i, 0))))
        {
          customerIdExists = 1;
          customerId = Integer.valueOf(idField.getText());
          break;
        }
      }

      if (customerIdExists == 1)
      {
        try
        {
          String updateCustomerQuery = "UPDATE Customers SET name = ?, "
            + "phone = ?, email = ? WHERE customerID = " + customerId;
          PreparedStatement ps = myDbConn.prepareStatement(updateCustomerQuery);

          name = nameField.getText();
          phone = phoneField.getText();
          email = emailField.getText();

          ps.setString(1, name);
          ps.setString(2, phone);
          ps.setString(3, email);
          ps.executeUpdate();

          // The following code snippet reloads the frame.
          JOptionPane.showMessageDialog(new JFrame(), "Data updated "
            + "successfully. Reloading...");
          this.dispose();
          new CheckoutFrame(dbName, infoTableName, priceTableName,
            infoColumnNames, priceColumnNames, customerTableName,
            retailerTableName, customerColumnNames, retailerColumnNames,
            costTableName, costColumnNames);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error updating "
            + "data.", "Error", JOptionPane.ERROR_MESSAGE);;
        }
      }
      // When inserting a new customer.
      else
      {
        try
        {
          PreparedStatement ps = myDbConn.prepareStatement(customerQuery);

          customerId = Integer.parseInt(idField.getText());
          name = nameField.getText();
          phone = phoneField.getText();
          email = emailField.getText();

          ps.setInt(1, customerId);
          ps.setString(2, name);
          ps.setString(3, phone);
          ps.setString(4, email);
          ps.executeUpdate();

          // The following code snippet reloads the frame.
          JOptionPane.showMessageDialog(new JFrame(), "Data updated "
            + "successfully. Reloading...");
          this.dispose();
          new CheckoutFrame(dbName, infoTableName, priceTableName,
            infoColumnNames, priceColumnNames, customerTableName,
            retailerTableName, customerColumnNames, retailerColumnNames,
            costTableName, costColumnNames);
        }
        catch (NumberFormatException nfe)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error updating data. "
            + "Make sure the data you've entered is valid.", "Incorrect "
            + "Values", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error updating "
            + "data.", "Error", JOptionPane.ERROR_MESSAGE);;
        }
      }
    }
    else if (command.equals(delete))
    {
      // This code snippet attempts to delete data in the 
      // selected row from the database
      deleteDecision = JOptionPane.showConfirmDialog(new JFrame(), "Are you "
        + "sure you want to delete the selected item?", "Delete "
        + "Customer", JOptionPane.YES_NO_OPTION);
      if (deleteDecision == JOptionPane.YES_OPTION)
      {
        try
        {
          int currentRow = customerTable.getSelectedRow();
          String idForRow = String.valueOf(customerTable.getValueAt(currentRow, 0));
          Statement s = myDbConn.createStatement();

          String deleteCustomerQuery = "DELETE FROM Customers WHERE customerID = " + Integer.parseInt(idForRow);

          s.executeUpdate(deleteCustomerQuery);

          // The following code snippet reloads the frame.
          this.dispose();
          new CheckoutFrame(dbName, infoTableName, priceTableName, infoColumnNames, priceColumnNames, customerTableName,
            retailerTableName, customerColumnNames, retailerColumnNames, costTableName, costColumnNames);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error deleting data. Select a row if not selected.", "Error", JOptionPane.ERROR_MESSAGE);;
        }
      }
    }
    else if (command.equals(remove))
    {
      try
      {
        int currentRow = checkoutTable.getSelectedRow();
        int idForRow = Integer.valueOf(String.valueOf(checkoutTable.getValueAt(currentRow, 0)));
        int quantityForRow = Integer.valueOf(String.valueOf(checkoutTable.getValueAt(currentRow, 3)));
        int rowForId = 0;

        for (int i = 0; i < itemsTable.getRowCount(); i++)
        {
          if (Integer.valueOf(String.valueOf(itemsTable.getValueAt(i, 0))) == idForRow)
          {
            rowForId = i;
            break;
          }
        }

        int itemQuantity = Integer.valueOf(String.valueOf(itemsTable.getValueAt(rowForId, 4)));
        int newItemQuantity = quantityForRow + itemQuantity;

        checkoutTableModel.removeRow(currentRow);
        checkoutTableModel.fireTableDataChanged();
        itemsTableModel.setValueAt(newItemQuantity, rowForId, 4);
        itemsTableModel.fireTableDataChanged();
      }
      catch (Exception ex)
      {
        JOptionPane.showMessageDialog(new JFrame(), "Error removing data. Select a row if not selected.", "Error", JOptionPane.ERROR_MESSAGE);;
      }

    }
    else if (command.equals(checkout))
    {
      // This code snippet attempts to checkout items in the checkout table
      double valueRecieved;
      double total = Double.valueOf(totalValue.getText());
      double amountDue;
      // Here, we are making sure a customer is selected
      for (int i = 0; i < customerTable.getRowCount(); i++)
      {
        if (customerTable.isRowSelected(i) == true)
        {
          customerRow = i;
          break;
        }
      }
      if (customerRow == -1)
      {
        JOptionPane.showMessageDialog(new JFrame(), "Please select a customer.",
          "Error", JOptionPane.ERROR_MESSAGE);
      }
      else if (checkoutTable.getRowCount() == 0)
      {
        JOptionPane.showMessageDialog(new JFrame(), "Please select add items "
          + "for checkout.", "Error", JOptionPane.ERROR_MESSAGE);
      }
      // If a customer is selected.
      else
      {
        try
        {
          valueRecieved = Double.valueOf(JOptionPane.showInputDialog(new JFrame(),
            "Enter amount recieved."));
          // Throwing an exception for when amount recieved is less than total
          if (valueRecieved < total)
          {
            throw new ArithmeticException();
          }
          amountDue = valueRecieved - total;
          // Displaying checkout details.
          JOptionPane.showMessageDialog(new JFrame(), "Amount Recieved: "
            + valueRecieved + "\nCheckout Total: " + total + "\nAmount Due: "
            + amountDue, "Confirm Payment", JOptionPane.PLAIN_MESSAGE);

          // Printing reciept to a text file.
          customerName = String.valueOf(customerTable.getValueAt(customerRow, 1));
          recieptWriter = new PrintWriter(new FileOutputStream(customerName + " reciept.txt", false));
          recieptWriter.println("Date: " + dateFormat.format(todaysDate));
          recieptWriter.println("Customer: " + customerName);
          recieptWriter.println("-------------------------------------------------");
          recieptWriter.println("Name                    Quantity      Total Price");
          for (int i = 0; i < checkoutTable.getRowCount(); i++)
          {
            String itemName = String.valueOf(checkoutTable.getValueAt(i, 1));
            String quantity = String.valueOf(checkoutTable.getValueAt(i, 3));
            String totalPrice = String.valueOf(checkoutTable.getValueAt(i, 4));
            recieptWriter.println(itemName + "          " + quantity + "          " + totalPrice);
          }
          recieptWriter.println("-------------------------------------------------");
          recieptWriter.println("-----------THANK YOU FOR YOUR PURCHASE!----------");
          recieptWriter.close();

          // This code snippet attempts to open the reciept file.
          recieptFile = new File(customerName + " reciept.txt");
          if (!Desktop.isDesktopSupported())
          {
            System.out.println("not supported");
            return;
          }
          Desktop desktop = Desktop.getDesktop();
          if (recieptFile.exists())
          {
            desktop.open(recieptFile);
          }

          // This code snippet adds data for calculating daily profit
          for (int i = 0; i < checkoutTable.getRowCount(); i++)
          {
            totalCost += Double.valueOf(String.valueOf(checkoutTable.getValueAt(i, 5)))
              * Double.valueOf(String.valueOf(checkoutTable.getValueAt(i, 3)));
          }

          profitList = objDb.getData(profitTableName, profitColumnNames);
          profitData = objDb.to2dArray(profitList);
          currentDayCost = Double.valueOf(String.valueOf(profitData[0][1]));
          currentDayPrice = Double.valueOf(String.valueOf(profitData[0][2]));
          currentDailyProfit = Double.valueOf(String.valueOf(profitData[0][3]));

          objProfit.setDayCost(totalCost);
          currentDayCost += objProfit.getDayCost();
          objProfit.setDayPrice(Double.valueOf(String.valueOf(totalValue.getText())));
          currentDayPrice += objProfit.getDayPrice();
          currentDailyProfit = objProfit.calculateDailyProfit(currentDayPrice, currentDayCost);
          String updateProfitQuery = "UPDATE ProfitHistory SET costofGoods = "
            + currentDayCost + ", priceofGoods = " + currentDayPrice
            + ", profit = " + currentDailyProfit + "WHERE entryID = 1";
          Statement s = myDbConn.createStatement();
          s.executeUpdate(updateProfitQuery);
          //This code snippet attempts to update the database with the correct 
          // quantities for the items
          itemInfoList = objDb.getData(infoTableName, infoColumnNames);
          itemCostList = objDb.getData(costTableName, costColumnNames);
          itemPriceList = objDb.getData(priceTableName, priceColumnNames);
          itemInfoData = objDb.to2dArray(itemInfoList);
          itemCostData = objDb.to2dArray(itemCostList);
          itemPriceData = objDb.to2dArray(itemPriceList);
          for (int i = 0; i < itemsTable.getRowCount(); i++)
          {
            double itemCost = Double.valueOf(String.valueOf(itemCostData[i][1]));
            double itemPrice = Double.valueOf(String.valueOf(itemPriceData[i][1]));
            int itemId = Integer.valueOf(String.valueOf(itemsTable.getValueAt(i, 0)));
            int newItemQuantity = Integer.valueOf(String.valueOf(itemsTable.getValueAt(i, 4)));
            double newTotalCost = newItemQuantity * itemCost;
            double newTotalPrice = newItemQuantity * itemPrice;
            updateCostQuery = "UPDATE ItemCost SET itemQuantity = " + newItemQuantity 
              + ", totalCost = " + newTotalCost + "WHERE itemID = " + itemId;
            updatePriceQuery = "UPDATE ItemPrice SET itemQuantity = " + newItemQuantity 
              + ", totalPrice = " + newTotalPrice + "WHERE itemID = " + itemId;

            s.executeUpdate(updateCostQuery);
            s.executeUpdate(updatePriceQuery);
          }

          // The following code snippet reloads the frame.
          JOptionPane.showMessageDialog(new JFrame(), "Successfully checked out. Reloading...");
          this.dispose();
          new CheckoutFrame(dbName, infoTableName, priceTableName, infoColumnNames, priceColumnNames, customerTableName,
            retailerTableName, customerColumnNames, retailerColumnNames, costTableName, costColumnNames);

        }
        catch (NumberFormatException nfe)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Please input a valid number.", "Invalid entry", JOptionPane.ERROR_MESSAGE);
          nfe.printStackTrace();
        }
        catch (ArithmeticException ae)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Amount Recieved can't be less than total!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ioe)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Unable to print reciept. Please complete the checkout process again.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        catch (Exception exc)
        {
          exc.printStackTrace();
        }
      }
    }

    else if (command.equals(clear))
    {
      // This code snippet attempts to clear items from the checkout table.
      deleteDecision = JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to clear all items from checkout", "Clear Checkout", JOptionPane.YES_NO_OPTION);
      if (deleteDecision == JOptionPane.YES_OPTION)
      {
        int numberOfRows = checkoutTableModel.getRowCount();
        for (int i = 0; i < numberOfRows; i++)
        {
          checkoutTableModel.removeRow(0);
        }

        // The following code snippet reloads the frame.
        JOptionPane.showMessageDialog(new JFrame(), "Done. Reloading...");
        this.dispose();
        new CheckoutFrame(dbName, infoTableName, priceTableName, infoColumnNames, priceColumnNames, customerTableName,
          retailerTableName, customerColumnNames, retailerColumnNames, costTableName, costColumnNames);
      }
    }

    // Condition for when items are added to the checkout table from the items table.
    else if (command.equals(addToCheckout))
    {
      // Condition for when there are no items yet in the checkout table.
      if (checkoutTableModel.getRowCount() == 0)
      {
        try
        {
          int selectedRow = itemsTable.getSelectedRow();
          Object[] newData = new Object[6];
          int currentItemQuantity = Integer.valueOf(String.
            valueOf(itemsTableModel.getValueAt(selectedRow, 4)));
          int newItemQuantity = Integer.valueOf(String.valueOf(itemsTableModel.
            getValueAt(selectedRow, 4))) - 1;
          if (currentItemQuantity == 0)
          {
            JOptionPane.showMessageDialog(new JFrame(), "Item is no longer "
              + "available.", "Error", JOptionPane.ERROR_MESSAGE);;
          }
          else
          {
            for (int i = 0; i < 2; i++)
            {
              newData[i] = itemsData[selectedRow][i];
            }
            for (int i = 3; i < 4; i++)
            {
              newData[i - 1] = itemsData[selectedRow][i];
            }
            newData[3] = 1;
            newData[5] = itemsData[selectedRow][5];
            itemsTableModel.setValueAt(newItemQuantity, selectedRow, 4);
          }
          checkoutTableModel.addRow(newData);
          checkoutTableModel.fireTableDataChanged();

        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error adding item. "
            + "Select a row if not selected.", "Error", JOptionPane.ERROR_MESSAGE);;
          ex.printStackTrace();
        }
      }

      // Condition for when there are already items in checkout table.
      else if (checkoutTableModel.getRowCount() > 0)
      {
        try
        {
          int selectedRow = itemsTable.getSelectedRow();
          Object[] newData = new Object[6];
          int currentItemQuantity = Integer.valueOf(String.
            valueOf(itemsTableModel.getValueAt(selectedRow, 4)));
          int newItemQuantity = Integer.valueOf(String.valueOf(itemsTableModel.
            getValueAt(selectedRow, 4))) - 1;
          if (currentItemQuantity == 0)
          {
            JOptionPane.showMessageDialog(new JFrame(), "Item is no longer "
              + "available.", "Error", JOptionPane.ERROR_MESSAGE);;
          }
          else
          {
            // Loop through items in checkout table so only quantity 
            // can be added.
            // If item doesn't exist, add to table like normal.
            ArrayList<ArrayList<String>> inventorySize = objDb.
              getData(infoTableName, infoColumnNames);
            int itemExisted = 0;
            for (int i = 0; i < checkoutTableModel.getRowCount(); i++)
            {
              if (checkoutTableModel.getValueAt(i, 1).equals(itemsData[selectedRow][1]))
              {
                int checkoutItemQuantity = Integer.valueOf(String.
                  valueOf(checkoutTableModel.getValueAt(i, 3)));
                checkoutTableModel.setValueAt(checkoutItemQuantity + 1, i, 3);
                itemsTableModel.setValueAt(newItemQuantity, selectedRow, 4);
                itemExisted = 1;
                break;
              }
            }
            if (itemExisted == 0)
            {
              for (int i = 0; i < 2; i++)
              {
                newData[i] = itemsData[selectedRow][i];
              }
              for (int i = 3; i < 4; i++)
              {
                newData[i - 1] = itemsData[selectedRow][i];
              }
              newData[3] = 1;
              newData[5] = itemsData[selectedRow][5];
              itemsTableModel.setValueAt(newItemQuantity, selectedRow, 4);
              checkoutTableModel.addRow(newData);
            }
          }
          checkoutTableModel.fireTableDataChanged();
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error adding item. "
            + "Select a row if not selected.", "Error", JOptionPane.ERROR_MESSAGE);;
          ex.printStackTrace();
        }
      }

      // Calculating the total Price to be displayed in checkout table.
      for (int i = 0; i < checkoutTable.getRowCount(); i++)
      {
        double tableTotalPrice = Double.valueOf(String.valueOf(checkoutTable.
          getValueAt(i, 2))) * Double.valueOf(String.valueOf(checkoutTable.
            getValueAt(i, 3)));
        checkoutTable.setValueAt(tableTotalPrice, i, 4);
        checkoutTableModel.fireTableDataChanged();
      }

      // Displaying the checkout total below the checkout table.
      for (int i = 0; i < checkoutTable.getRowCount(); i++)
      {
        checkoutTotal += Double.valueOf(String.valueOf(checkoutTable.getValueAt(i, 4)));
      }
      totalValue.setText(String.valueOf(checkoutTotal));
    }

    this.validate();

    this.repaint();
  }
}