//9-18-2021---This class displays the edit accounts frame where the user can manage accounts.
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

public class EditAccounts extends JDialog implements ActionListener
{

  // Declaring GUI components.
  JPanel sidePanel;
  JPanel mainPanel;
  JLabel editAccountsTitle;
  String[] columnNames;
  Object[][] userInfoData;
  Object[][] userLoginData;
  Object[][] allUserData;
  ArrayList<ArrayList<String>> userInfoList;
  ArrayList<ArrayList<String>> userLoginList;
  JTable usersTable;
  DefaultTableModel usersTableModel;
  JScrollPane usersTableScrollPane;
  JLabel userIdLabel;
  JTextField userIdField;
  JLabel usernameLabel;
  JTextField usernameField;
  JLabel passwordLabel;
  JTextField passwordField;
  JLabel userTypeLabel;
  String[] userTypes;
  JComboBox<String> userTypeMenu;
  JLabel employeeLabel;
  JTextField employeeField;
  JLabel phoneLabel;
  JTextField phoneField;
  JLabel emailLabel;
  JTextField emailField;
  JButton addNewUser;
  JButton update;
  JButton delete;
  int startUserId;

  // Constructor for the jframe.
  public EditAccounts(String dbName, String loginTableName, String infoTableName, String[] loginColumnNames, String[] infoColumnNames)
  {
    this.setTitle("Store Name Inventory Manager");
    this.setBounds(0, 0, 960, 720);
    this.getContentPane().setBackground(Color.white);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setResizable(false);
    this.setLayout(null);
    this.setModal(true);

    // Declaring objects of the GUI components.
    this.mainPanel = new JPanel(null);
    this.sidePanel = new JPanel(null);
    this.editAccountsTitle = new JLabel("Edit Accounts");
    this.columnNames = new String[]
    {
      "User ID", "Username", "Password", "User Type", "Employee Name", "Phone", "Email"
    };

    // Connecting to the database
    JavaDatabase objDb = new JavaDatabase(dbName);
    objDb.setDbConn();

    // Using database object to get the data from the respective table and convert them to 2darrays.
    userLoginList = objDb.getData(loginTableName, loginColumnNames);
    userInfoList = objDb.getData(infoTableName, infoColumnNames);
    userLoginData = objDb.to2dArray(userLoginList);
    userInfoData = objDb.to2dArray(userInfoList);

    this.allUserData = new Object[userLoginList.size()][7];

    // Loops to get user data and put it into allUserData for this class.
    for (int i = 0; i < userLoginList.size(); i++)
    {
      for (int j = 0; j < 4; j++)
      {
        allUserData[i][j] = userLoginData[i][j];
      }
    }
    for (int i = 0; i < userInfoList.size(); i++)
    {
      for (int j = 1; j < 4; j++)
      {
        allUserData[i][j + 3] = userInfoData[i][j];
      }
    }
    this.usersTableModel = new DefaultTableModel(allUserData, columnNames)
    {
      public boolean isCellEditable(int row, int column)
      {
        return false;
      }
    };

    this.usersTable = new JTable(usersTableModel);
    this.usersTableScrollPane = new JScrollPane(usersTable);
    this.userIdLabel = new JLabel("User ID");
    this.userIdField = new JTextField();
    this.usernameLabel = new JLabel("Username");
    this.usernameField = new JTextField();
    this.passwordLabel = new JLabel("Password");
    this.passwordField = new JTextField();
    this.userTypeLabel = new JLabel("User Type");
    this.userTypes = new String[]
    {
      "Admin", "Employee"
    };
    this.userTypeMenu = new JComboBox<String>(userTypes);
    this.employeeLabel = new JLabel("Employee Name");
    this.employeeField = new JTextField();
    this.phoneLabel = new JLabel("Phone");
    this.phoneField = new JTextField();
    this.emailLabel = new JLabel("Email");
    this.emailField = new JTextField();
    this.addNewUser = new JButton("Add New User");
    this.update = new JButton("Update");
    this.delete = new JButton("Delete");

    // Customizing GUI components.
    sidePanel.setBounds(0, 0, 80, 720);
    sidePanel.setBackground(LoginFrame.PRIMARY_COLOR);
    mainPanel.setBounds(80, 0, 880, 720);
    mainPanel.setBackground(LoginFrame.FRAME_COLOR);
    editAccountsTitle.setBounds(15, 25, 220, 24);
    editAccountsTitle.setFont(Dashboard.WELCOME_FONT);
    usersTable.setFont(Dashboard.CHART_FONT);
    usersTable.setRowHeight(25);
    usersTable.getTableHeader().setFont(Dashboard.CHART_FONT);
    usersTableScrollPane.setBounds(50, 285, 760, 335);
    userIdLabel.setBounds(50, 80, 105, 25);
    userIdLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    userIdField.setBounds(145, 80, 75, 25);
    userIdField.setBorder(BorderFactory.createEmptyBorder());
    userIdField.setFont(LoginFrame.TEXT_FONT);
    userIdField.setEnabled(false);
    usernameLabel.setBounds(50, 120, 90, 25);
    usernameLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    usernameField.setBounds(145, 120, 200, 25);
    usernameField.setBorder(BorderFactory.createEmptyBorder());
    usernameField.setFont(LoginFrame.TEXT_FONT);
    passwordLabel.setBounds(50, 160, 90, 25);
    passwordLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    passwordField.setBounds(145, 160, 200, 25);
    passwordField.setBorder(BorderFactory.createEmptyBorder());
    passwordField.setFont(LoginFrame.TEXT_FONT);
    userTypeLabel.setBounds(50, 200, 70, 25);
    userTypeLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    userTypeMenu.setBounds(145, 200, 110, 25);
    userTypeMenu.setFont(Dashboard.PANEL_ITEMS_FONT);
    employeeLabel.setBounds(50, 240, 130, 25);
    employeeLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    employeeField.setBounds(175, 240, 200, 25);
    employeeField.setBorder(BorderFactory.createEmptyBorder());
    employeeField.setFont(LoginFrame.TEXT_FONT);
    phoneLabel.setBounds(400, 80, 75, 25);
    phoneLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    phoneField.setBounds(465, 80, 175, 25);
    phoneField.setBorder(BorderFactory.createEmptyBorder());
    phoneField.setFont(LoginFrame.TEXT_FONT);
    emailLabel.setBounds(440, 120, 65, 25);
    emailLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    emailField.setBounds(505, 120, 210, 25);
    emailField.setBorder(BorderFactory.createEmptyBorder());
    emailField.setFont(LoginFrame.TEXT_FONT);
    addNewUser.setBounds(439, 240, 140, 25);
    addNewUser.setFont(LoginFrame.TEXT_FONT);
    addNewUser.setFocusPainted(false);
    addNewUser.addActionListener(this);
    update.setBounds(593, 240, 100, 25);
    update.setFont(LoginFrame.TEXT_FONT);
    update.setFocusPainted(false);
    update.addActionListener(this);
    delete.setBounds(707, 240, 100, 25);
    delete.setFont(LoginFrame.TEXT_FONT);
    delete.setFocusPainted(false);
    delete.addActionListener(this);

    // Adding mouselistener to table to get selected row.
    usersTable.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        int currentRow = usersTable.getSelectedRow();
        userIdField.setText(String.valueOf(usersTable.getValueAt(currentRow, 0)));
        usernameField.setText(String.valueOf(usersTable.getValueAt(currentRow, 1)));
        passwordField.setText(String.valueOf(usersTable.getValueAt(currentRow, 2)));
        employeeField.setText(String.valueOf(usersTable.getValueAt(currentRow, 4)));
        phoneField.setText(String.valueOf(usersTable.getValueAt(currentRow, 4)));
        emailField.setText(String.valueOf(usersTable.getValueAt(currentRow, 4)));
      }
    });

    // This code snippet inputs a userId for a new user at the start of the frame
    // If no items in table.
    if (usersTable.getRowCount() == 0)
    {
      userIdField.setText("1");
    }
    // Here, setting the new user id to a value 1 higher than highest id value.
    else
    {
      startUserId = Integer.valueOf(String.valueOf(allUserData[userInfoList.size() - 1][0])) + 1;
      userIdField.setText(String.valueOf(startUserId));
    }
    // Adding GUI components to the frame.
    mainPanel.add(editAccountsTitle);
    mainPanel.add(usersTableScrollPane);
    mainPanel.add(userIdLabel);
    mainPanel.add(userIdField);
    mainPanel.add(usernameLabel);
    mainPanel.add(usernameField);
    mainPanel.add(passwordLabel);
    mainPanel.add(passwordField);
    mainPanel.add(userTypeLabel);
    mainPanel.add(userTypeMenu);
    mainPanel.add(employeeLabel);
    mainPanel.add(employeeField);
    mainPanel.add(phoneLabel);
    mainPanel.add(phoneField);
    mainPanel.add(emailLabel);
    mainPanel.add(emailField);
    mainPanel.add(addNewUser);
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
    String infoTableName = "UserDetails";
    String loginTableName = "UserLogin";
    String[] infoColumnNames =
    {
      "userID", "employeeName", "phone", "email"
    };
    String[] loginColumnNames =
    {
      "userID", "username", "password", "usertype"
    };

    // Creating a new object of jframe.
    new EditAccounts(dbName, loginTableName, infoTableName, loginColumnNames, infoColumnNames);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    // Getting the identifier for the object that performed the action.
    Object command = e.getSource();

    // Declaring variables.
    int deleteDecision;
    int newUserId;
    int userId = 0;
    String username;
    String password;
    String userType;
    String employeeName;
    String phone;
    String email;

    // Info for database access.
    String dbName = "StoreName";
    String infoTableName = "UserDetails";
    String loginTableName = "UserLogin";
    String[] infoColumnNames =
    {
      "userID", "employeeName", "phone", "email"
    };
    String[] loginColumnNames =
    {
      "userID", "username", "password", "usertype"
    };
    String userLoginQuery = "INSERT INTO UserLogin VALUES (?,?,?,?)";
    String userDetailsQuery = "INSERT INTO UserDetails VALUES (?,?,?,?)";

    // Connecting to the database.
    JavaDatabase objDb = new JavaDatabase(dbName);
    Connection myDbConn = objDb.getDbConn();

    // Conditions for specific actions.
    if (command.equals(addNewUser))
    {
      // Setting all data entry fields to null.
      userInfoList = objDb.getData(infoTableName, infoColumnNames);
      userInfoData = objDb.to2dArray(userInfoList);
      // If no items in table.
      if (usersTable.getRowCount() == 0)
      {
        userIdField.setText("1");
      }
      // Here, setting the new user id to a value 1 higher than highest id value.
      else
      {
        startUserId = Integer.valueOf(String.valueOf(allUserData[userInfoList.size() - 1][0])) + 1;
        userIdField.setText(String.valueOf(startUserId));
      }
      usernameField.setText("");
      passwordField.setText("");
      employeeField.setText("");
      phoneField.setText("");
      emailField.setText("");
    }
    else if (command.equals(update))
    {
      // This code snippet attempts to update the database using text retrieved from the frame.
      int userIdExists = 0;
      // When the user exists.
      for (int i = 0; i < usersTable.getRowCount(); i++)
      {
        if (Integer.valueOf(userIdField.getText()) == Integer.valueOf(String.valueOf(usersTable.getValueAt(i, 0))))
        {
          userIdExists = 1;
          userId = Integer.valueOf(userIdField.getText());
          break;
        }
      }

      if (userIdExists == 1)
      {
        try
        {
          String updateLoginQuery = "UPDATE UserLogin SET username = ?, password = ?, usertype = ? WHERE userID = " + userId;
          String updateDetailsQuery = "UPDATE UserDetails SET employeeName = ?, phone = ?, email = ? WHERE userID = " + userId;

          PreparedStatement ps = myDbConn.prepareStatement(updateLoginQuery);

          username = usernameField.getText();
          password = passwordField.getText();
          userType = String.valueOf(userTypeMenu.getSelectedItem());
          employeeName = employeeField.getText();
          phone = phoneField.getText();
          email = emailField.getText();

          ps.setString(1, username);
          ps.setString(2, password);
          ps.setString(3, userType);
          ps.executeUpdate();

          ps = myDbConn.prepareStatement(updateDetailsQuery);

          ps.setString(1, employeeName);
          ps.setString(2, phone);
          ps.setString(3, email);
          ps.executeUpdate();

          // The following code snippet reloads the frame.
          JOptionPane.showMessageDialog(new JFrame(), "Data updated successfully. Reloading...");
          this.dispose();
          new EditAccounts(dbName, loginTableName, infoTableName, loginColumnNames, infoColumnNames);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error updating data.", "Error", JOptionPane.ERROR_MESSAGE);;
        }
      }
      // When inserting a new user.
      else
      {
        try
        {
          PreparedStatement ps = myDbConn.prepareStatement(userLoginQuery);

          userId = Integer.parseInt(userIdField.getText());
          username = usernameField.getText();
          password = passwordField.getText();
          userType = String.valueOf(userTypeMenu.getSelectedItem());
          employeeName = employeeField.getText();
          phone = phoneField.getText();
          email = emailField.getText();

          ps.setInt(1, userId);
          ps.setString(2, username);
          ps.setString(3, password);
          ps.setString(4, userType);
          ps.executeUpdate();

          ps = myDbConn.prepareStatement(userDetailsQuery);

          ps.setInt(1, userId);
          ps.setString(2, employeeName);
          ps.setString(3, phone);
          ps.setString(4, email);
          ps.executeUpdate();

          // The following code snippet reloads the frame.
          JOptionPane.showMessageDialog(new JFrame(), "Data updated successfully. Reloading...");
          this.dispose();
          new EditAccounts(dbName, loginTableName, infoTableName, loginColumnNames, infoColumnNames);
        }
        catch (NumberFormatException nfe)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error updating data. Make sure the data you've entered is valid.", "Incorrect Values", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error updating data.", "Error", JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace();
        }
      }
    }
    else if (command.equals(delete))
    {
      // This code snippet attempts to delete data in the selected row from the database
      deleteDecision = JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to delete the selected item?", "Delete User", JOptionPane.YES_NO_OPTION);
      if (deleteDecision == JOptionPane.YES_OPTION)
      {
        try
        {
          int currentRow = usersTable.getSelectedRow();
          String idForRow = String.valueOf(usersTable.getValueAt(currentRow, 0));
          Statement s = myDbConn.createStatement();

          String deleteLoginQuery = "DELETE FROM UserLogin WHERE userID = " + Integer.parseInt(idForRow);
          String deleteDetailsQuery = "DELETE FROM UserDetails WHERE userID = " + Integer.parseInt(idForRow);

          s.executeUpdate(deleteLoginQuery);
          s.executeUpdate(deleteDetailsQuery);

          // The following code snippet reloads the frame.
          JOptionPane.showMessageDialog(new JFrame(), "Data updated successfully. Reloading...");
          this.dispose();
          new EditAccounts(dbName, loginTableName, infoTableName, loginColumnNames, infoColumnNames);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(new JFrame(), "Error deleting data. Select a row if not selected.", "Error", JOptionPane.ERROR_MESSAGE);
          ex.printStackTrace();
        }
      }
    }

    this.validate();
    this.repaint();
  }
}

