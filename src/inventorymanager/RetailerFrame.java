//9-18-2021---This class displays the retailer frame which allows the user to manage retailers.
package inventorymanager;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
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

public class RetailerFrame extends JDialog implements ActionListener
{

  // Declaring GUI components.
  JPanel sidePanel;
  JPanel mainPanel;
  JLabel retailerTitle;
  String[] columnNames;
  ArrayList<ArrayList<String>> retailerList;
  Object[][] retailerData;
  JTable retailerTable;
  DefaultTableModel retailerTableModel;
  JScrollPane retailerTableScrollPane;
  JTextField searchBar;
  JLabel searchLabel;
  JLabel retailerIDLabel;
  JTextField retailerIDField;
  JLabel retailerNameLabel;
  JTextField retailerNameField;
  JLabel repNameLabel;
  JTextField repNameField;
  JLabel phoneLabel;
  JTextField phoneField;
  JLabel emailLabel;
  JTextField emailField;
  JButton addNew;
  JButton update;
  JButton delete;
  int startRetailerId;
  TableRowSorter retailerSorter;

  // Constructor for the jframe.
  public RetailerFrame(String dbName, String retailerTableName, String[] retailerColumnNames)
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
    this.retailerTitle = new JLabel("Retailers");
    this.columnNames = new String[]
    {
      "Retailer ID", "Retailer Name", "Representative Name", "Phone", "Email"
    };
    // Connecting to the database
    JavaDatabase objDb = new JavaDatabase(dbName);
    objDb.setDbConn();

    // Using database object to get the data from the respective table and convert them to 2darrays.
    retailerList = objDb.getData(retailerTableName, retailerColumnNames);
    retailerData = objDb.to2dArray(retailerList);
    this.retailerTableModel = new DefaultTableModel(retailerData, columnNames)
    {
      public boolean isCellEditable(int row, int column)
      {
        return false;
      }
    };
    this.retailerTable = new JTable(retailerTableModel);
    this.retailerTableScrollPane = new JScrollPane(retailerTable);
    this.searchBar = new JTextField();
    this.searchLabel = new JLabel("Search");
    this.retailerIDLabel = new JLabel("Retailer ID");
    this.retailerIDField = new JTextField();
    this.retailerNameLabel = new JLabel("Retailer Name");
    this.retailerNameField = new JTextField();
    this.repNameLabel = new JLabel("Representative Name");
    this.repNameField = new JTextField();
    this.phoneLabel = new JLabel("Phone");
    this.phoneField = new JTextField();
    this.emailLabel = new JLabel("Email");
    this.emailField = new JTextField();
    this.addNew = new JButton("Add New");
    this.update = new JButton("Update");
    this.delete = new JButton("Delete");
    retailerSorter = new TableRowSorter<>(retailerTableModel);
    retailerTable.setRowSorter(retailerSorter);

    // Customizing GUI components.
    sidePanel.setBounds(0, 0, 80, 720);
    sidePanel.setBackground(LoginFrame.PRIMARY_COLOR);
    mainPanel.setBounds(80, 0, 880, 720);
    mainPanel.setBackground(LoginFrame.FRAME_COLOR);
    retailerTitle.setBounds(15, 25, 120, 24);
    retailerTitle.setFont(Dashboard.WELCOME_FONT);
    retailerTable.setFont(Dashboard.CHART_FONT);
    retailerTable.setRowHeight(25);
    retailerTable.getTableHeader().setFont(Dashboard.CHART_FONT);
    retailerTableScrollPane.setBounds(50, 285, 760, 335);
    searchBar.setBounds(105, 80, 320, 25);
    searchBar.setBorder(BorderFactory.createEmptyBorder());
    searchBar.setFont(LoginFrame.TEXT_FONT);
    searchLabel.setBounds(50, 80, 65, 25);
    searchLabel.setFont(LoginFrame.TEXT_FONT);
    retailerIDLabel.setBounds(50, 120, 115, 25);
    retailerIDLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    retailerIDField.setBounds(145, 120, 75, 25);
    retailerIDField.setBorder(BorderFactory.createEmptyBorder());
    retailerIDField.setFont(LoginFrame.TEXT_FONT);
    retailerIDField.setEnabled(false);
    retailerNameLabel.setBounds(50, 160, 130, 25);
    retailerNameLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    retailerNameField.setBounds(165, 160, 200, 25);
    retailerNameField.setBorder(BorderFactory.createEmptyBorder());
    retailerNameField.setFont(LoginFrame.TEXT_FONT);
    repNameLabel.setBounds(50, 200, 165, 25);
    repNameLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    repNameField.setBounds(215, 200, 175, 25);
    repNameField.setBorder(BorderFactory.createEmptyBorder());
    repNameField.setFont(LoginFrame.TEXT_FONT);
    phoneLabel.setBounds(50, 240, 75, 25);
    phoneLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    phoneField.setBounds(115, 240, 175, 25);
    phoneField.setBorder(BorderFactory.createEmptyBorder());
    phoneField.setFont(LoginFrame.TEXT_FONT);
    emailLabel.setBounds(450, 120, 65, 25);
    emailLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    emailField.setBounds(515, 120, 210, 25);
    emailField.setBorder(BorderFactory.createEmptyBorder());
    emailField.setFont(LoginFrame.TEXT_FONT);
    addNew.setBounds(479, 240, 100, 25);
    addNew.setFont(LoginFrame.TEXT_FONT);
    addNew.setFocusPainted(false);
    addNew.addActionListener(this);
    update.setBounds(593, 240, 100, 25);
    update.setFont(LoginFrame.TEXT_FONT);
    update.setFocusPainted(false);
    update.addActionListener(this);
    delete.setBounds(707, 240, 100, 25);
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
          retailerSorter.setRowFilter(null);
        }
        else
        {
          retailerSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
      }
    });

    // This code snippet inputs an itemId for a new item at the start of the frame
    // If no items in table.
    if (retailerTable.getRowCount() == 0)
    {
      retailerIDField.setText("1");
    }
    // Here, setting the new retailer id to a value 1 higher than highest id value.
    else
    {
      startRetailerId = Integer.valueOf(String.valueOf(retailerData[retailerList.size() - 1][0])) + 1;
      retailerIDField.setText(String.valueOf(startRetailerId));
    }

    // Adding mouselistener to table to get selected row.
    retailerTable.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        int currentRow = retailerTable.getSelectedRow();
        retailerIDField.setText(String.valueOf(retailerTable.getValueAt(currentRow, 0)));
        retailerNameField.setText(String.valueOf(retailerTable.getValueAt(currentRow, 1)));
        repNameField.setText(String.valueOf(retailerTable.getValueAt(currentRow, 2)));
        phoneField.setText(String.valueOf(retailerTable.getValueAt(currentRow, 3)));
        emailField.setText(String.valueOf(retailerTable.getValueAt(currentRow, 4)));
      }
    });

    // Adding GUI components to the frame.
    mainPanel.add(retailerTitle);
    mainPanel.add(retailerTableScrollPane);
    mainPanel.add(searchBar);
    mainPanel.add(searchLabel);
    mainPanel.add(retailerIDLabel);
    mainPanel.add(retailerIDField);
    mainPanel.add(retailerNameLabel);
    mainPanel.add(retailerNameField);
    mainPanel.add(repNameLabel);
    mainPanel.add(repNameField);
    mainPanel.add(phoneLabel);
    mainPanel.add(phoneField);
    mainPanel.add(emailLabel);
    mainPanel.add(emailField);
    mainPanel.add(addNew);
    mainPanel.add(update);
    mainPanel.add(delete);
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
    String retailerTableName = "Retailers";
    String[] retailerColumnNames =
    {
      "retailerID", "companyName", "representativeName", "phone", "email"
    };

    // Creating a new object of jframe.
    new RetailerFrame(dbName, retailerTableName, retailerColumnNames);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    // Getting the identifier for the object that performed the action.
    Object command = e.getSource();

    // Declaring variables.
    int deleteDecision;
    int newRetailerId;
    int retailerId = 0;
    String retailerName;
    String repName;
    String phone;
    String email;

    // Info for database access.
    String dbName = "StoreName";
    String retailerTableName = "Retailers";
    String[] retailerColumnNames =
    {
      "retailerID", "companyName", "representativeName", "phone", "email"
    };
    String retailersQuery = "INSERT INTO Retailers VALUES (?,?,?,?,?)";

    // Connecting to the database.
    JavaDatabase objDb = new JavaDatabase(dbName);
    Connection myDbConn = objDb.getDbConn();

    // Conditions for specific actions.
    if (command.equals(addNew))
    {
      // Setting all data entry fields to null.
      retailerList = objDb.getData(retailerTableName, retailerColumnNames);
      retailerData = objDb.to2dArray(retailerList);
      // If no items in table.
      if (retailerTable.getRowCount() == 0)
      {
        retailerIDField.setText("1");
      }
      // Here, setting the new retailer id to a value 1 higher than highest id value.
      else
      {
        newRetailerId = Integer.valueOf(String.valueOf(retailerData[retailerList.size() - 1][0])) + 1;
        retailerIDField.setText(String.valueOf(newRetailerId));
      }
      retailerNameField.setText("");
      repNameField.setText("");
      phoneField.setText("");
      emailField.setText("");
    }
    else if (command.equals(update))
    {
      // This code snippet attempts to update the database using text retrieved from the frame.
      int retailerIdExists = 0;
      // When the retailer exists.
      for (int i = 0; i < retailerTable.getRowCount(); i++)
      {
        if (Integer.valueOf(retailerIDField.getText()) == Integer.valueOf(String.valueOf(retailerTable.getValueAt(i, 0))))
        {
          retailerIdExists = 1;
          retailerId = Integer.valueOf(retailerIDField.getText());
          break;
        }
      }

      if (retailerIdExists == 1)
      {
        try
        {
          String updateRetailerQuery = "UPDATE Retailers SET companyName = ?, representativeName = ?, phone = ?, email = ? WHERE retailerID = " + retailerId;
          PreparedStatement ps = myDbConn.prepareStatement(updateRetailerQuery);

          retailerName = retailerNameField.getText();
          repName = repNameField.getText();
          phone = phoneField.getText();
          email = emailField.getText();

          ps.setString(1, retailerName);
          ps.setString(2, repName);
          ps.setString(3, phone);
          ps.setString(4, email);
          ps.executeUpdate();

          // The following code snippet reloads the frame.
          JOptionPane.showMessageDialog(new JFrame(), "Data updated successfully. Reloading...");
          this.dispose();
          new RetailerFrame(dbName, retailerTableName, retailerColumnNames);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error updating data.", "Error", JOptionPane.ERROR_MESSAGE);;
        }
      }
      // When inserting a new retailer.
      else
      {
        try
        {
          PreparedStatement ps = myDbConn.prepareStatement(retailersQuery);

          retailerId = Integer.parseInt(retailerIDField.getText());
          retailerName = retailerNameField.getText();
          repName = repNameField.getText();
          phone = phoneField.getText();
          email = emailField.getText();

          ps.setInt(1, retailerId);
          ps.setString(2, retailerName);
          ps.setString(3, repName);
          ps.setString(4, phone);
          ps.setString(5, email);
          ps.executeUpdate();

          // The following code snippet reloads the frame.
          JOptionPane.showMessageDialog(new JFrame(), "Data Updated Successfully. Click OK to reset window");
          this.dispose();
          new RetailerFrame(dbName, retailerTableName, retailerColumnNames);
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
    else if (command.equals(delete))
    {
      // This code snippet attempts to delete data in the selected row from the database
      deleteDecision = JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to delete the selected item?", "Delete Retailer", JOptionPane.YES_NO_OPTION);
      if (deleteDecision == JOptionPane.YES_OPTION)
      {
        try
        {
          int currentRow = retailerTable.getSelectedRow();
          String idForRow = String.valueOf(retailerTable.getValueAt(currentRow, 0));
          Statement s = myDbConn.createStatement();

          String deleteRetailerQuery = "DELETE FROM Retailers WHERE retailerID = " + Integer.parseInt(idForRow);

          s.executeUpdate(deleteRetailerQuery);

          // The following code snippet reloads the frame.
          this.dispose();
          new RetailerFrame(dbName, retailerTableName, retailerColumnNames);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error deleting data. Select a row if not selected", "Error", JOptionPane.ERROR_MESSAGE);;
        }
      }
    }

    this.validate();
    this.repaint();
  }

}

