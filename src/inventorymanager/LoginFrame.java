// 10-1-2021---This class prompts the user to login to use the product.
package inventorymanager;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

// Constructor for the jframe.
public class LoginFrame extends JFrame implements ActionListener
{

  // Declaring GUI components.
  JPanel mainPanel;
  JLabel nameLabel;
  JLabel passLabel;
  JTextField nameField;
  JPasswordField passField;
  JButton loginButton;
  JLabel header;

  // Declaring fonts (as constants).
  public static final Font LABEL_FONT
    = new Font("Roboto Light", Font.PLAIN, 15);
  public static final Font TEXT_FONT
    = new Font("Roboto Medium", Font.PLAIN, 13);
  public static final Font HEADER_FONT
    = new Font("Roboto Medium", Font.PLAIN, 20);

  // Declaring colors (as constants).
  public static final Color FRAME_COLOR = new Color(229, 229, 229);
  public static final Color PRIMARY_COLOR = new Color(129, 23, 27);
  public static final Color SECONDARY_COLOR = new Color(84, 8, 4);

  // Constructor for the jframe.
  public LoginFrame(String dbName,
                    String loginTableName, String[] loginColumnNames)
  {
    super("Login");
    this.setBounds(0, 0, 400, 300);
    this.getContentPane().setBackground(Color.white);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    this.setLayout(null);

    // This code snippet prompts the user with a confirmation dialog 
    // when closing the frame.
    this.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent we)
      {
        int result = JOptionPane.showConfirmDialog(new JFrame(), "Are you "
          + "sure you want to quit?", "Close Application",
          JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION)
        {
          setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        else
        {
          setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
      }
    });

    // Declaring objects of the GUI components.
    this.mainPanel = new JPanel(null);
    this.nameLabel = new JLabel("Username");
    this.passLabel = new JLabel("Password");
    this.nameField = new JTextField();
    this.passField = new JPasswordField();
    this.loginButton = new JButton("Login");
    this.header = new JLabel("STORE NAME");

    // Customizing GUI components.
    mainPanel.setBounds(0, 0, 400, 300);
    mainPanel.setBackground(FRAME_COLOR);
    nameLabel.setBounds(70, 90, 70, 15);
    nameLabel.setFont(LABEL_FONT);
    nameField.setBounds(150, 85, 155, 25);
    nameField.setFont(TEXT_FONT);
    nameField.setBorder(BorderFactory.createEmptyBorder());
    passLabel.setBounds(70, 135, 70, 15);
    passLabel.setFont(LABEL_FONT);
    passField.setBounds(150, 130, 155, 25);
    passField.setBorder(BorderFactory.createEmptyBorder());
    loginButton.setBounds(160, 185, 70, 25);
    loginButton.setFont(TEXT_FONT);
    loginButton.setFocusPainted(false);
    loginButton.addActionListener(this);
    header.setBounds(130, 25, 130, 40);
    header.setFont(HEADER_FONT);
    header.setForeground(PRIMARY_COLOR);

    // Adding GUI components to the frame.
    mainPanel.add(nameLabel);
    mainPanel.add(nameField);
    mainPanel.add(passLabel);
    mainPanel.add(passField);
    mainPanel.add(loginButton);
    mainPanel.add(header);
    this.add(mainPanel);

    // Displaying the frame.
    this.setVisible(true);
  }

  public static void main(String[] args)
  {
    // This code snippet attempts to change the look and feel of the frame.
    try
    {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf."
        + "windows.WindowsLookAndFeel");
    }
    catch (Exception e)
    {
    }

    // Info for database access.
    String dbName = "StoreName";
    String loginTableName = "UserLogin";
    String[] loginColumnNames =
    {
      "userID", "username", "password", "usertype"
    };

    // Creating a new object of jframe.
    new LoginFrame(dbName, loginTableName, loginColumnNames);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    // Getting the identifier for the object that performed the action.
    Object command = e.getSource();

    // Info for database access.
    String dbName = "StoreName";
    String loginTableName = "UserLogin";
    String profitTableName = "ProfitHistory";
    String infoTableName = "ItemInfo";
    String costTableName = "ItemCost";
    String priceTableName = "ItemPrice";
    String inventoryOrdersTableName = "InventoryOrders";
    String orderDetailsTableName = "OrderDetail";
    String orderCostTableName = "OrderCost";
    String userDetailsTableName = "UserDetails";
    String userLoginTableName = "UserLogin";
    String[] userDetailsColumnNames =
    {
      "userID", "employeeName", "phone", "email"
    };
    String[] userLoginColumnNames =
    {
      "userID", "username", "password", "usertype"
    };
    String[] loginColumnNames =
    {
      "userID", "username", "password", "usertype"
    };
    String[] profitColumnNames =
    {
      "entryID", "costofGoods", "priceofGoods", "profit", "date"
    };
    String[] inventoryOrdersColumnNames =
    {
      "orderID", "retailerName", "orderedBy", "status",
      "dateCreated", "dateShipped"
    };
    String[] orderDetailsColumnNames =
    {
      "orderID", "productName", "quantity", "unitCost"
    };
    String[] orderCostColumnNames =
    {
      "orderID", "quantity", "unitCost", "subtotal"
    };
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

    // Connecting to the database.
    JavaDatabase objDb = new JavaDatabase(dbName);
    Connection myDbConn = objDb.getDbConn();

    // This code snippet checks input with login data in database. 
    // If incorrect, user does not gain acess.
    if (command.equals(loginButton))
    {
      String username = nameField.getText();
      String password = String.valueOf(passField.getPassword());
      ArrayList<ArrayList<String>> userLoginList
        = objDb.getData(loginTableName, loginColumnNames);
      Object[][] userLoginData = objDb.to2dArray(userLoginList);
      int loggedIn = 0;

      for (int i = 0; i < userLoginList.size(); i++)
      {
        if (userLoginData[i][1].equals(username))
        {
          if (userLoginData[i][2].equals(password))
          {
            int userId = Integer.valueOf(String.valueOf(userLoginData[i][0]));
            this.dispose();
            // Info for database access.

            // Creating a new object of jframe.
            new Dashboard(dbName, userLoginTableName, userLoginColumnNames,
              userDetailsTableName, userDetailsColumnNames, userId,
              inventoryOrdersTableName, orderDetailsTableName,
              orderCostTableName, orderDetailsColumnNames,
              orderCostColumnNames, inventoryOrdersColumnNames, infoTableName,
              costTableName, priceTableName, infoColumnNames, costColumnNames,
              priceColumnNames, profitTableName, profitColumnNames);

            loggedIn = 1;
            break;
          }
          else
          {
            JOptionPane.showMessageDialog(new JFrame(),
              "Username or password is incorrect.", "Error",
              JOptionPane.ERROR_MESSAGE);
            break;
          }
        }
      }
      if (loggedIn == 0)
      {
        JOptionPane.showMessageDialog(new JFrame(),
          "Username or password is incorrect.", "Error",
          JOptionPane.ERROR_MESSAGE);
      }
    }

    this.validate();
    this.repaint();
  }

}
