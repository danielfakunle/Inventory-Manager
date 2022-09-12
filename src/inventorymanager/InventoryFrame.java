// 9-18-2021---This class displays the inventory frame which allows the user to manage the inventory.
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class InventoryFrame extends JDialog implements ActionListener
{

  // Declaring GUI components.
  JPanel sidePanel;
  JPanel mainPanel;
  JLabel inventoryTitle;
  String[] allColumnNames;
  ArrayList<ArrayList<String>> itemInfoList;
  ArrayList<ArrayList<String>> itemCostList;
  ArrayList<ArrayList<String>> itemPriceList;
  Object[][] itemInfoData;
  Object[][] itemCostData;
  Object[][] itemPriceData;
  Object[][] allData;
  JTable inventoryTable;
  DefaultTableModel inventoryTableModel;
  JScrollPane inventoryTableScrollPane;
  JTextField searchBar;
  JLabel categoryLabel;
  String[] categories;
  JComboBox<String> categoryMenu;
  JLabel idLabel;
  JTextField idField;
  JLabel nameLabel;
  JTextField nameField;
  JLabel unitCostLabel;
  JTextField unitCostField;
  JLabel unitPriceLabel;
  JTextField unitPriceField;
  JLabel quantityLabel;
  JTextField quantityField;
  JLabel totalCostLabel;
  JTextField totalCostField;
  JLabel totalPriceLabel;
  JTextField totalPriceField;
  JLabel expiryDateLabel;
  JDateChooser expiryDateChooser;
  SimpleDateFormat dateFormat;
  JLabel searchLabel;
  JButton addNew;
  JButton update;
  JButton delete;
  int startItemId;
  TableRowSorter inventorySorter;

  // Constructor for the jframe.
  public InventoryFrame(String dbName, String infoTableName,
                        String costTableName, String priceTableName,
                        String[] infoColumnNames, String[] costColumnNames,
                        String[] priceColumnNames)
  {
    this.setTitle("Store Name Inventory Manager");
    this.setBounds(0, 0, 960, 720);
    this.getContentPane().setBackground(Color.white);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setResizable(false);
    this.setLayout(null);
    this.setModal(true);

    this.allColumnNames = new String[]
    {
      "ID", "Name", "Category", "Unit Cost", "Unit Price", "Quantity", "Total Cost", "Total Price", "Expiry Date"
    };
    // Connecting to the database
    JavaDatabase objDb = new JavaDatabase(dbName);
    objDb.setDbConn();

    // Using database object to get the data from the respective table 
    // and convert them to 2darrays.
    itemInfoList = objDb.getData(infoTableName, infoColumnNames);
    itemCostList = objDb.getData(costTableName, costColumnNames);
    itemPriceList = objDb.getData(priceTableName, priceColumnNames);
    itemInfoData = objDb.to2dArray(itemInfoList);
    itemCostData = objDb.to2dArray(itemCostList);
    itemPriceData = objDb.to2dArray(itemPriceList);

    this.allData = new Object[itemInfoList.size()][9];

    // Loops to add the data to an allData 2darray in a specific order.
    for (int i = 0; i < itemInfoList.size(); i++)
    {
      for (int j = 0; j < 3; j++)
      {
        allData[i][j] = itemInfoData[i][j];
      }
    }
    for (int i = 0; i < itemCostList.size(); i++)
    {
      for (int j = 1; j < 2; j++)
      {
        allData[i][j + 2] = itemCostData[i][j];
      }
    }
    for (int i = 0; i < itemPriceList.size(); i++)
    {
      for (int j = 1; j < 2; j++)
      {
        allData[i][j + 3] = itemPriceData[i][j];
      }
    }
    for (int i = 0; i < itemCostList.size(); i++)
    {
      for (int j = 2; j < 4; j++)
      {
        allData[i][j + 3] = itemCostData[i][j];
      }
    }
    for (int i = 0; i < itemPriceList.size(); i++)
    {
      for (int j = 3; j < 4; j++)
      {
        allData[i][j + 4] = itemPriceData[i][j];
      }
    }
    for (int i = 0; i < itemInfoList.size(); i++)
    {
      for (int j = 3; j < 4; j++)
      {
        allData[i][j + 5] = itemInfoData[i][j];
      }
    }

    // Declaring objects of the GUI components.
    this.mainPanel = new JPanel(null);
    this.sidePanel = new JPanel(null);
    this.inventoryTitle = new JLabel("Inventory");
    this.inventoryTableModel = new DefaultTableModel(allData, allColumnNames)
    {
      public boolean isCellEditable(int row, int column)
      {
        return false;
      }
    };
    this.inventoryTable = new JTable(inventoryTableModel);
    this.inventoryTableScrollPane = new JScrollPane(inventoryTable);
    this.categoryLabel = new JLabel("Category");
    this.categories = new String[]
    {
      "Beverages", "Food", "Water", "Confectionaries"
    };
    this.categoryMenu = new JComboBox<String>(categories);
    this.searchBar = new JTextField();
    this.idLabel = new JLabel("ID");
    this.idField = new JTextField();
    this.nameLabel = new JLabel("Name");
    this.nameField = new JTextField();
    this.unitCostLabel = new JLabel("Unit Cost");
    this.unitCostField = new JTextField();
    this.unitPriceLabel = new JLabel("Unit Price");
    this.unitPriceField = new JTextField();
    this.quantityLabel = new JLabel("Quantity");
    this.quantityField = new JTextField();
    this.totalCostLabel = new JLabel("Total Cost");
    this.totalCostField = new JTextField();
    this.totalPriceLabel = new JLabel("Total Price");
    this.totalPriceField = new JTextField();
    this.expiryDateLabel = new JLabel("Expiry Date");
    this.expiryDateChooser = new JDateChooser();
    this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    this.searchLabel = new JLabel("Search");
    this.addNew = new JButton("Add New");
    this.update = new JButton("Update");
    this.delete = new JButton("Delete");
    inventorySorter = new TableRowSorter<>(inventoryTableModel);
    inventoryTable.setRowSorter(inventorySorter);

    // Customizing GUI components.
    sidePanel.setBounds(0, 0, 80, 720);
    sidePanel.setBackground(LoginFrame.PRIMARY_COLOR);
    mainPanel.setBounds(80, 0, 880, 720);
    mainPanel.setBackground(LoginFrame.FRAME_COLOR);
    inventoryTitle.setBounds(15, 25, 120, 24);
    inventoryTitle.setFont(Dashboard.WELCOME_FONT);
    inventoryTable.setFont(Dashboard.CHART_FONT);
    inventoryTable.setRowHeight(25);
    inventoryTable.getTableHeader().setFont(Dashboard.CHART_FONT);
    inventoryTableScrollPane.setBounds(50, 285, 760, 335);
    categoryMenu.setFont(Dashboard.PANEL_ITEMS_FONT);
    searchBar.setBounds(105, 80, 320, 25);
    searchBar.setBorder(BorderFactory.createEmptyBorder());
    searchBar.setFont(LoginFrame.TEXT_FONT);
    searchLabel.setBounds(50, 80, 65, 25);
    searchLabel.setFont(LoginFrame.TEXT_FONT);
    idLabel.setBounds(50, 120, 25, 25);
    idLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    idField.setBounds(105, 120, 75, 25);
    idField.setBorder(BorderFactory.createEmptyBorder());
    idField.setFont(LoginFrame.TEXT_FONT);
    idField.setEnabled(false);
    nameLabel.setBounds(50, 160, 40, 25);
    nameLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    nameField.setBounds(105, 160, 200, 25);
    nameField.setBorder(BorderFactory.createEmptyBorder());
    nameField.setFont(LoginFrame.TEXT_FONT);
    categoryLabel.setBounds(50, 200, 70, 25);
    categoryLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    categoryMenu.setBounds(130, 200, 150, 25);
    unitCostLabel.setBounds(50, 240, 100, 25);
    unitCostLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    unitCostField.setBounds(130, 240, 110, 25);
    unitCostField.setBorder(BorderFactory.createEmptyBorder());
    unitCostField.setFont(LoginFrame.TEXT_FONT);
    unitPriceLabel.setBounds(350, 120, 65, 25);
    unitPriceLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    unitPriceField.setBounds(435, 120, 110, 25);
    unitPriceField.setBorder(BorderFactory.createEmptyBorder());
    unitPriceField.setFont(LoginFrame.TEXT_FONT);
    quantityLabel.setBounds(350, 160, 65, 25);
    quantityLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    quantityField.setBounds(435, 160, 75, 25);
    quantityField.setBorder(BorderFactory.createEmptyBorder());
    quantityField.setFont(LoginFrame.TEXT_FONT);
    totalCostLabel.setBounds(350, 200, 75, 25);
    totalCostLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    totalCostField.setBounds(435, 200, 110, 25);
    totalCostField.setBorder(BorderFactory.createEmptyBorder());
    totalCostField.setFont(LoginFrame.TEXT_FONT);
    totalPriceLabel.setBounds(350, 240, 75, 25);
    totalPriceLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    totalPriceField.setBounds(435, 240, 110, 25);
    totalPriceField.setBorder(BorderFactory.createEmptyBorder());
    totalPriceField.setFont(LoginFrame.TEXT_FONT);
    expiryDateLabel.setBounds(587, 120, 95, 25);
    expiryDateLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    expiryDateChooser.setBounds(682, 120, 127, 25);
    expiryDateChooser.setFont(LoginFrame.TEXT_FONT);
    expiryDateChooser.setDateFormatString("dd/MM/yyyy");
    addNew.setBounds(637, 160, 100, 25);
    addNew.setFont(LoginFrame.TEXT_FONT);
    addNew.setFocusPainted(false);
    addNew.addActionListener(this);
    update.setBounds(637, 200, 100, 25);
    update.setFont(LoginFrame.TEXT_FONT);
    update.setFocusPainted(false);
    update.addActionListener(this);
    delete.setBounds(637, 240, 100, 25);
    delete.setFont(LoginFrame.TEXT_FONT);
    delete.setFocusPainted(false);
    delete.addActionListener(this);

    // Using DocumentLister to implement filter function for the customer table
    searchBar.getDocument().addDocumentListener(new DocumentListener()
    {
      @Override
      public void insertUpdate(DocumentEvent e)
      {
        search(searchBar.getText());
      }

      @Override
      public void removeUpdate(DocumentEvent e)
      {
        search(searchBar.getText());
      }

      @Override
      public void changedUpdate(DocumentEvent e)
      {
        search(searchBar.getText());
      }

      public void search(String searchText)
      {
        if (searchText.length() == 0)
        {
          inventorySorter.setRowFilter(null);
        }
        else
        {
          inventorySorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
      }
    });

    // This code snippet inputs an itemId for a new item at the start of the frame
    // If no items in table.
    if (inventoryTable.getRowCount() == 0)
    {
      idField.setText("1");
    }
    // Here, setting the new customer id to a value 1 higher than highest id value.
    else
    {
      startItemId = Integer.valueOf(String.valueOf(itemInfoData[itemInfoList.size() - 1][0])) + 1;
      idField.setText(String.valueOf(startItemId));
    }
    // Adding mouselistener to table to get selected row.
    inventoryTable.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        int currentRow = inventoryTable.getSelectedRow();
        DateTimeFormatter localDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        idField.setText(String.valueOf(inventoryTable.getValueAt(currentRow, 0)));
        nameField.setText(String.valueOf(inventoryTable.getValueAt(currentRow, 1)));
        categoryMenu.setSelectedItem(inventoryTable.getValueAt(currentRow, 2));
        unitCostField.setText(String.valueOf(inventoryTable.getValueAt(currentRow, 3)));
        unitPriceField.setText(String.valueOf(inventoryTable.getValueAt(currentRow, 4)));
        quantityField.setText(String.valueOf(inventoryTable.getValueAt(currentRow, 5)));
        totalCostField.setText(String.valueOf(inventoryTable.getValueAt(currentRow, 6)));
        totalPriceField.setText(String.valueOf(inventoryTable.getValueAt(currentRow, 7)));
        expiryDateChooser.setDate(convertStringToDate(String.valueOf(inventoryTable.getValueAt(currentRow, 8)), localDateFormat));
      }
    });

    // Adding GUI components to the frame.
    mainPanel.add(inventoryTitle);
    mainPanel.add(inventoryTableScrollPane);
    mainPanel.add(searchBar);
    mainPanel.add(idLabel);
    mainPanel.add(idField);
    mainPanel.add(nameLabel);
    mainPanel.add(nameField);
    mainPanel.add(categoryMenu);
    mainPanel.add(categoryLabel);
    mainPanel.add(unitCostLabel);
    mainPanel.add(unitCostField);
    mainPanel.add(unitPriceLabel);
    mainPanel.add(unitPriceField);
    mainPanel.add(quantityLabel);
    mainPanel.add(quantityField);
    mainPanel.add(totalCostLabel);
    mainPanel.add(totalCostField);
    mainPanel.add(totalPriceLabel);
    mainPanel.add(totalPriceField);
    mainPanel.add(expiryDateLabel);
    mainPanel.add(expiryDateChooser);
    mainPanel.add(searchLabel);
    mainPanel.add(addNew);
    mainPanel.add(update);
    mainPanel.add(delete);
    this.add(mainPanel);
    this.add(sidePanel);

    // Displaying the frame.
    this.setVisible(true);
  }

  // Methods to convert a date into a string and vice versa
  public static String convertDateToString(Date date)
  {
    SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String dateToString = newDateFormat.format(date);
    return (dateToString);
  }

  public static Date convertStringToDate(String string, DateTimeFormatter format)
  {
    LocalDate localDate = LocalDate.parse(string, format);
    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    return date;
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
    String costTableName = "ItemCost";
    String priceTableName = "ItemPrice";
    String[] infoColumnNames =
    {
      "itemID", "itemName", "itemCategory", "expiryDate"
    };
    String[] costColumnNames =
    {
      "itemID", "unitCost", "itemQuantity", "totalCost"
    };
    String[] priceColumnNames =
    {
      "itemID", "unitPrice", "itemQuantity", "totalPrice"
    };

    // Creating a new object of jframe.
    new InventoryFrame(dbName, infoTableName, costTableName, priceTableName, infoColumnNames, costColumnNames, priceColumnNames);

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    // Getting the identifier for the object that performed the action.
    Object command = e.getSource();

    // Declaring variables.
    int deleteDecision;
    int newItemId;
    int itemId = 0;
    String itemName;
    String category;
    double unitCost;
    double unitPrice;
    int quantity;
    double totalCost;
    double totalPrice;
    String expiryDate;

    // Info for database access.
    String dbName = "StoreName";
    String infoTableName = "ItemInfo";
    String costTableName = "ItemCost";
    String priceTableName = "ItemPrice";
    String[] infoColumnNames =
    {
      "itemID", "itemName", "itemCategory", "expiryDate"
    };
    String[] costColumnNames =
    {
      "itemID", "unitCost", "itemQuantity", "totalCost"
    };
    String[] priceColumnNames =
    {
      "itemID", "unitPrice", "itemQuantity", "totalPrice"
    };
    String infoQuery = "INSERT INTO ItemInfo VALUES (?,?,?,?)";
    String costQuery = "INSERT INTO ItemCost VALUES (?,?,?,?)";
    String priceQuery = "INSERT INTO ItemPrice VALUES (?,?,?,?)";

    // Connecting to the database.
    JavaDatabase objDb = new JavaDatabase(dbName);
    Connection myDbConn = objDb.getDbConn();

    // Conditions for specific actions.
    if (command.equals(addNew))
    {
      // Setting all data entry fields to null.
      itemInfoList = objDb.getData(infoTableName, infoColumnNames);
      itemInfoData = objDb.to2dArray(itemInfoList);
      // If no items in table.
      if (inventoryTable.getRowCount() == 0)
      {
        idField.setText("1");
      }
      // Here, setting the new customer id to a value 1 higher than highest id value.
      else
      {
        newItemId = Integer.valueOf(String.valueOf(itemInfoData[itemInfoList.size() - 1][0])) + 1;
        idField.setText(String.valueOf(newItemId));
      }
      nameField.setText("");
      unitCostField.setText("");
      unitPriceField.setText("");
      quantityField.setText("");
      totalCostField.setText("");
      totalPriceField.setText("");
      expiryDateChooser.setDate(null);
    }
    else if (command.equals(update))
    {
      // This code snippet attempts to update the database using text retrieved from the frame.
      int itemIdExists = 0;
      // When the item exists.
      for (int i = 0; i < inventoryTable.getRowCount(); i++)
      {
        if (Integer.valueOf(idField.getText()) == Integer.valueOf(String.
          valueOf(inventoryTable.getValueAt(i, 0))))
        {
          itemIdExists = 1;
          itemId = Integer.valueOf(idField.getText());
          break;
        }
      }

      if (itemIdExists == 1)
      {
        try
        {
          String updateInfoQuery = "UPDATE ItemInfo SET itemName = ?, "
            + "itemCategory = ?, expiryDate = ? WHERE itemID = " + itemId;
          String updateCostQuery = "UPDATE ItemCost SET unitCost = ?, "
            + "itemQuantity = ?, totalCost = ? WHERE itemID = " + itemId;
          String updatePriceQuery = "UPDATE ItemPrice SET unitPrice = ?, "
            + "itemQuantity = ?, totalPrice = ? WHERE itemID = " + itemId;

          PreparedStatement ps = myDbConn.prepareStatement(updateInfoQuery);

          itemName = nameField.getText();
          category = String.valueOf(categoryMenu.getSelectedItem());
          unitCost = Double.parseDouble(unitCostField.getText());
          unitPrice = Double.parseDouble(unitPriceField.getText());
          quantity = Integer.parseInt(quantityField.getText());
          totalCost = Double.parseDouble(totalCostField.getText());
          totalPrice = Double.parseDouble(totalPriceField.getText());
          expiryDate = dateFormat.format(expiryDateChooser.getDate());

          ps.setString(1, itemName);
          ps.setString(2, category);
          ps.setString(3, expiryDate);
          ps.executeUpdate();

          ps = myDbConn.prepareStatement(updateCostQuery);

          ps.setDouble(1, unitCost);
          ps.setInt(2, quantity);
          ps.setDouble(3, totalCost);
          ps.executeUpdate();

          ps = myDbConn.prepareStatement(updatePriceQuery);

          ps.setDouble(1, unitPrice);
          ps.setInt(2, quantity);
          ps.setDouble(3, totalPrice);
          ps.executeUpdate();

          // The following code snippet reloads the frame.
          JOptionPane.showMessageDialog(new JFrame(), "Data updated "
            + "successfully. Reloading...");
          this.dispose();
          new InventoryFrame(dbName, infoTableName, costTableName,
            priceTableName, infoColumnNames, costColumnNames,
            priceColumnNames);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error "
            + "updating data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
      // When inserting a new item.
      else
      {
        try
        {
          PreparedStatement ps = myDbConn.prepareStatement(infoQuery);

          itemId = Integer.parseInt(idField.getText());
          itemName = nameField.getText();
          category = String.valueOf(categoryMenu.getSelectedItem());
          unitCost = Double.parseDouble(unitCostField.getText());
          unitPrice = Double.parseDouble(unitPriceField.getText());
          quantity = Integer.parseInt(quantityField.getText());
          totalCost = Double.parseDouble(totalCostField.getText());
          totalPrice = Double.parseDouble(totalPriceField.getText());
          expiryDate = dateFormat.format(expiryDateChooser.getDate());

          ps.setInt(1, itemId);
          ps.setString(2, itemName);
          ps.setString(3, category);
          ps.setString(4, expiryDate);
          ps.executeUpdate();

          ps = myDbConn.prepareStatement(costQuery);

          ps.setInt(1, itemId);
          ps.setDouble(2, unitCost);
          ps.setInt(3, quantity);
          ps.setDouble(4, totalCost);
          ps.executeUpdate();

          ps = myDbConn.prepareStatement(priceQuery);

          ps.setInt(1, itemId);
          ps.setDouble(2, unitPrice);
          ps.setInt(3, quantity);
          ps.setDouble(4, totalPrice);
          ps.executeUpdate();

          // The following code snippet reloads the frame.
          JOptionPane.showMessageDialog(new JFrame(), "Data updated "
            + "successfully. Reloading...");
          this.dispose();
          new InventoryFrame(dbName, infoTableName, costTableName,
            priceTableName, infoColumnNames, costColumnNames, priceColumnNames);
        }
        catch (NumberFormatException nfe)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error updating data. "
            + "Make sure the data you've entered is valid.",
            "Incorrect Values", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error updating data.",
            "Error", JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace();
        }
      }
    }
    else if (command.equals(delete))
    {
      // This code snippet attempts to delete data in the selected row from the database
      deleteDecision = JOptionPane.showConfirmDialog(new JFrame(), "Are you "
        + "sure you want to delete the selected item?",
        "Delete Item", JOptionPane.YES_NO_OPTION);
      if (deleteDecision == JOptionPane.YES_OPTION)
      {
        try
        {
          int currentRow = inventoryTable.getSelectedRow();
          String idForRow = String.valueOf(inventoryTable.getValueAt(currentRow, 0));
          Statement s = myDbConn.createStatement();

          String deleteInfoQuery = "DELETE FROM ItemInfo WHERE itemID = "
            + Integer.parseInt(idForRow);
          String deleteCostQuery = "DELETE FROM ItemCost WHERE itemID = "
            + Integer.parseInt(idForRow);
          String deletePriceQuery = "DELETE FROM ItemPrice WHERE itemID = "
            + Integer.parseInt(idForRow);

          s.executeUpdate(deleteInfoQuery);
          s.executeUpdate(deleteCostQuery);
          s.executeUpdate(deletePriceQuery);

          // The following code snippet reloads the frame.
          this.dispose();
          new InventoryFrame(dbName, infoTableName, costTableName,
            priceTableName, infoColumnNames, costColumnNames,
            priceColumnNames);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error deleting data. "
            + "Select a row if not selected.",
            "Error", JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace();
        }
      }
    }

    this.validate();
    this.repaint();
  }

}
