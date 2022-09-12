// 10-1-2021---This class displays a dashboard that shows a summary 
// of information within the program.
package inventorymanager;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.Instant;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class Dashboard extends JFrame implements ActionListener
{

  // Declaring GUI components.
  JPanel mainPanel;
  JPanel sidePanel;
  JLabel welcomeText;
  JLabel storeText;
  JLabel user;
  JPanel inventoryPanel;
  JPanel ordersPanel;
  JPanel profitPanel;
  JLabel inventoryTitle;
  JLabel storageLabel;
  JLabel storageValue;
  JLabel inventoryStatus;
  JLabel inventoryCostTitle;
  JLabel inventoryCostValue;
  JButton manageInventory;
  JButton manageOrders;
  JLabel ordersTitle;
  JLabel currentOrdersLabel;
  JLabel currentOrdersValue;
  JLabel nextArrivalLabel;
  JLabel nextArrivalValue;
  JLabel orderAlert;
  JLabel profitsTitle;
  JButton endTheDay;
  JButton checkout;
  JButton editAccounts;
  JButton help;
  JButton about;
  JLabel soFarText;
  JLabel soFarValue;
  Double inventoryCost = 0.0;
  ArrayList<ArrayList<String>> profitList;
  Object[][] profitData;
  ArrayList<ArrayList<String>> userDetailsList;
  Object[][] userDetailsData;
  ArrayList<ArrayList<String>> userLoginList;
  Object[][] userLoginData;
  ArrayList<ArrayList<String>> itemCostList;
  Object[][] itemCostData;
  Object[][] inventoryOrdersData;
  ArrayList<ArrayList<String>> inventoryOrdersList;
  Date todaysDate = new Date();
  Date nextArrivalDate;
  DateTimeFormatter localDateFormat
    = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  int daysBetween;
  int markOrderDecision;
  JButton refreshDashboard;
  int totalQuantity = 0;
  int orderSoonCounter;
  Object[][] orderDetailsData;
  ArrayList<ArrayList<String>> orderDetailsList;
  static int idOfUser;
  static String userPrivilege;
  JPanel profitHider;
  JLabel profitHiderText;

  // Declaring fonts (as constants).
  public static final Font SUB_HEADER_FONT
    = new Font("Roboto Medium", Font.PLAIN, 14);
  public static final Font WELCOME_FONT
    = new Font("Roboto Thin", Font.PLAIN, 24);
  public static final Font PANEL_TITLE_FONT
    = new Font("Roboto Medium", Font.PLAIN, 16);
  public static final Font PANEL_ITEMS_FONT
    = new Font("Roboto Medium", Font.PLAIN, 14);
  public static final Font DISPLAY_FONT
    = new Font("Roboto Thin", Font.PLAIN, 28);
  public static final Font CHART_FONT
    = new Font("Roboto Medium", Font.PLAIN, 11);
  public static final Font PROFIT_FONT
    = new Font("Roboto Thin", Font.PLAIN, 72);
  public static final Font PROFIT_TEXT_FONT
    = new Font("Roboto Medium", Font.PLAIN, 18);

  // Constructor for the jframe.
  public Dashboard(String dbName, String userLoginTableName, String[] userLoginColumnNames, String userDetailsTableName,
                   String[] userDetailsColumnNames, int userId, String inventoryOrdersTableName, String orderDetailsTableName,
                   String orderCostTableName, String[] orderDetailsColumnNames, String[] orderCostColumnNames,
                   String[] inventoryOrdersColumnNames, String infoTableName, String costTableName, String priceTableName,
                   String[] infoColumnNames, String[] costColumnNames, String[] priceColumnNames, String profitTableName,
                   String[] profitColumnNames)
  {
    super("Store Name Inventory Manager");
    this.setBounds(0, 0, 960, 720);
    this.getContentPane().setBackground(Color.white);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    this.setLayout(null);

    // This code snippet prompts the user with a confirmation dialog when closing the frame.
    this.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent we)
      {
        int result = JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to quit?", "Close Application", JOptionPane.YES_NO_OPTION);
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

    // Connecting to the database
    JavaDatabase objDb = new JavaDatabase(dbName);
    objDb.setDbConn();

    // Using database object to get the data from the respective table and convert them to 2darrays.
    profitList = objDb.getData(profitTableName, profitColumnNames);
    this.profitData = objDb.to2dArray(profitList);
    itemCostList = objDb.getData(costTableName, costColumnNames);
    itemCostData = objDb.to2dArray(itemCostList);
    inventoryOrdersList = objDb.getData(inventoryOrdersTableName, inventoryOrdersColumnNames);
    inventoryOrdersData = objDb.to2dArray(inventoryOrdersList);
    orderDetailsList = objDb.getData(orderDetailsTableName, orderDetailsColumnNames);
    orderDetailsData = objDb.to2dArray(orderDetailsList);
    userDetailsList = objDb.getData(userDetailsTableName, userDetailsColumnNames);
    userDetailsData = objDb.to2dArray(userDetailsList);
    userLoginList = objDb.getData(userLoginTableName, userLoginColumnNames);
    userLoginData = objDb.to2dArray(userLoginList);

    // Getting the total cost of the inventory
    for (int i = 0; i < itemCostList.size(); i++)
    {
      inventoryCost += Double.valueOf(String.valueOf(itemCostData[i][3]));
    }
    
    
    // Declaring objects of the GUI components.
    this.mainPanel = new JPanel(null);
    this.sidePanel = new JPanel();
    this.welcomeText = new JLabel("Welcome, ");
    this.user = new JLabel("");

    // Setting the text of the welcome user to the user that logged in
    for (int i = 0; i < userDetailsList.size(); i++)
    {
      idOfUser = userId;
      if (userId == Integer.valueOf(String.valueOf(userDetailsData[i][0])))
      {
        welcomeText.setText("Welcome, " + String.valueOf(userDetailsData[i][1]));
      }
    }

    this.storeText = new JLabel("STORE NAME");
    this.inventoryPanel = new JPanel(null);
    this.ordersPanel = new JPanel(null);
    this.profitPanel = new JPanel(null);
    this.manageInventory = new JButton("Manage Inventory");
    this.manageOrders = new JButton("Manage Orders");
    this.inventoryTitle = new JLabel("INVENTORY  +" + Integer.valueOf(String.valueOf(orderDetailsData[0][2])));
    this.storageLabel = new JLabel("Storage:");
    this.storageValue = new JLabel("100 / 500");

    // Getting the total quantity of items
    for (int i = 0; i < itemCostList.size(); i++)
    {
      totalQuantity += Integer.valueOf(String.valueOf(itemCostData[i][2]));
    }

    storageValue.setText(totalQuantity + " / 500");

    this.inventoryStatus = new JLabel("--> Okay!");

    // Checking if the quantity for items is between 1 and 5. If it is for more than 3 items, the inventory is not okay
    for (int i = 0; i < itemCostList.size(); i++)
    {
      if (orderSoonCounter == 3)
      {
        break;
      }
      if (Integer.valueOf(String.valueOf(itemCostData[i][2])) >= 0 && Integer.valueOf(String.valueOf(itemCostData[i][2])) <= 5)
      {
        orderSoonCounter++;
      }
    }

    if (totalQuantity >= 500)
    {
      orderSoonCounter = 0;
      inventoryStatus.setText("--> Full!");
    }

    if (orderSoonCounter == 3)
    {
      inventoryStatus.setText("--> Order Soon!");
    }

    this.inventoryCostTitle = new JLabel("Inventory Cost:");
    this.inventoryCostValue = new JLabel("$" + String.format("%.2f", inventoryCost), SwingConstants.CENTER);
    this.ordersTitle = new JLabel("ORDERS");
    this.currentOrdersLabel = new JLabel("Current Orders:");
    this.currentOrdersValue = new JLabel("" + inventoryOrdersList.size());
    this.nextArrivalLabel = new JLabel("Next Arrival:");
    this.nextArrivalValue = new JLabel("");

    // Setting Info About next order
    nextArrivalDate = InventoryFrame.convertStringToDate(String.valueOf(inventoryOrdersData[0][5]), localDateFormat);
  
    for (int i=0; i<inventoryOrdersList.size(); i++)
    {
      if (nextArrivalDate.after(InventoryFrame.convertStringToDate(String.valueOf(inventoryOrdersData[i][5]), localDateFormat)))
      {
        nextArrivalDate = InventoryFrame.convertStringToDate(String.valueOf(inventoryOrdersData[i][5]), localDateFormat);
      }
    }
    
    if (todaysDate.after(nextArrivalDate))
    {
      nextArrivalValue.setText("Should have arrived by " + InventoryFrame.convertDateToString(nextArrivalDate));
    }
    if (todaysDate.before(nextArrivalDate))
    {
      daysBetween = Period.between(Instant.ofEpochMilli(todaysDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate(),
        Instant.ofEpochMilli(nextArrivalDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate()).getDays();
      nextArrivalValue.setText("in " + daysBetween + " days, by " + InventoryFrame.convertDateToString(nextArrivalDate));
    }
    if (InventoryFrame.convertDateToString(todaysDate).equals(InventoryFrame.convertDateToString(nextArrivalDate)))
    {
      nextArrivalValue.setText("Today!");
      markOrderDecision = JOptionPane.showConfirmDialog(new JFrame(), "An order should be arriving today. Do you want to mark the order as shipped?", "New Items!", JOptionPane.YES_NO_OPTION);

      if (markOrderDecision == JOptionPane.YES_OPTION)
      {
        // Some Database access info.
        String retailersTableName = "Retailers";
        String[] retailersColumnNames =
        {
          "retailerID", "companyName", "representativeName", "phone", "email"
        };

        new OrderFrame(dbName, inventoryOrdersTableName, retailersTableName, orderDetailsTableName, orderCostTableName, orderDetailsColumnNames, orderCostColumnNames, inventoryOrdersColumnNames, retailersColumnNames);
      }
    }

    this.orderAlert = new JLabel("");

    // If items are out of stock, display text;
    for (int i = 0; i < itemCostList.size(); i++)
    {
      if (Integer.valueOf(String.valueOf(itemCostData[i][2])) == 0)
      {
        orderAlert.setText("Some items are out of stock!");
      }
    }

    this.profitsTitle = new JLabel("PROFITS");
    this.endTheDay = new JButton("End the Day");
    this.checkout = new JButton("Checkout");
    this.editAccounts = new JButton("Edit Accounts");
    this.help = new JButton("Help");
    this.about = new JButton("About");
    this.soFarText = new JLabel("Made so Far!", SwingConstants.CENTER);
    this.soFarValue = new JLabel("$" + String.format("%.2f", Double.valueOf(String.valueOf(profitData[0][3]))), SwingConstants.CENTER);
    refreshDashboard = new JButton("Refresh Dashboard");
    this.profitHider = new JPanel();
    this.profitHiderText = new JLabel("This feature is unavailable to employees");

    // Customizing GUI components.
    sidePanel.setBounds(0, 0, 80, 720);
    sidePanel.setBackground(LoginFrame.PRIMARY_COLOR);
    mainPanel.setBounds(80, 0, 880, 720);
    mainPanel.setBackground(LoginFrame.FRAME_COLOR);
    welcomeText.setBounds(15, 25, 420, 24);
    welcomeText.setFont(WELCOME_FONT);
    user.setBounds(125, 25, 120, 24);
    user.setFont(WELCOME_FONT);
    storeText.setBounds(15, 54, 120, 24);
    storeText.setFont(SUB_HEADER_FONT);
    storeText.setForeground(LoginFrame.PRIMARY_COLOR);
    inventoryPanel.setBounds(50, 120, 370, 160);
    inventoryPanel.setBackground(Color.white);
    ordersPanel.setBounds(440, 120, 370, 160);
    ordersPanel.setBackground(Color.white);
    profitPanel.setBounds(50, 300, 507, 230);
    profitPanel.setBackground(Color.white);
    inventoryTitle.setBounds(15, 12, 190, 16);
    inventoryTitle.setFont(PANEL_TITLE_FONT);
    inventoryTitle.setForeground(LoginFrame.PRIMARY_COLOR);
    storageLabel.setBounds(15, 18, 60, 50);
    storageLabel.setFont(PANEL_ITEMS_FONT);
    storageValue.setBounds(75, 18, 105, 50);
    storageValue.setFont(PANEL_ITEMS_FONT);
    inventoryStatus.setBounds(190, 18, 140, 50);
    inventoryStatus.setFont(PANEL_ITEMS_FONT);
    inventoryCostTitle.setBounds(15, 45, 180, 40);
    inventoryCostTitle.setFont(PANEL_ITEMS_FONT);
    inventoryCostValue.setBounds(15, 75, 340, 40);
    inventoryCostValue.setFont(DISPLAY_FONT);
    inventoryCostValue.setForeground(LoginFrame.PRIMARY_COLOR);
    manageInventory.setBounds(112, 118, 150, 25);
    manageInventory.setFont(LoginFrame.TEXT_FONT);
    manageInventory.setFocusPainted(false);
    manageInventory.addActionListener(this);
    ordersTitle.setBounds(15, 12, 90, 16);
    ordersTitle.setFont(PANEL_TITLE_FONT);
    ordersTitle.setForeground(LoginFrame.PRIMARY_COLOR);
    currentOrdersLabel.setBounds(15, 18, 100, 50);
    currentOrdersLabel.setFont(PANEL_ITEMS_FONT);
    currentOrdersValue.setBounds(120, 18, 25, 50);
    currentOrdersValue.setFont(PANEL_ITEMS_FONT);
    nextArrivalLabel.setBounds(15, 40, 80, 50);
    nextArrivalLabel.setFont(PANEL_ITEMS_FONT);
    nextArrivalValue.setBounds(100, 45, 310, 40);
    nextArrivalValue.setFont(PANEL_ITEMS_FONT);
    nextArrivalValue.setForeground(LoginFrame.PRIMARY_COLOR);
    orderAlert.setBounds(15, 70, 190, 40);
    orderAlert.setFont(PANEL_ITEMS_FONT);
    orderAlert.setForeground(LoginFrame.PRIMARY_COLOR);
    manageOrders.setBounds(112, 118, 150, 25);
    manageOrders.setFont(LoginFrame.TEXT_FONT);
    manageOrders.setFocusPainted(false);
    manageOrders.addActionListener(this);
    profitsTitle.setBounds(15, 12, 90, 16);
    profitsTitle.setFont(PANEL_TITLE_FONT);
    profitsTitle.setForeground(LoginFrame.PRIMARY_COLOR);
    endTheDay.setBounds(350, 180, 132, 25);
    endTheDay.setFont(LoginFrame.TEXT_FONT);
    endTheDay.setFocusPainted(false);
    endTheDay.addActionListener(this);
    checkout.setBounds(577, 300, 110, 75);
    checkout.setFont(LoginFrame.TEXT_FONT);
    checkout.setFocusPainted(false);
    checkout.addActionListener(this);
    editAccounts.setBounds(577, 386, 110, 75);
    editAccounts.setFont(LoginFrame.TEXT_FONT);
    editAccounts.setFocusPainted(false);
    editAccounts.addActionListener(this);
    help.setBounds(703, 300, 110, 75);
    help.setFont(LoginFrame.TEXT_FONT);
    help.setFocusPainted(false);
    help.addActionListener(this);
    about.setBounds(703, 386, 110, 75);
    about.setFont(LoginFrame.TEXT_FONT);
    about.setFocusPainted(false);
    about.addActionListener(this);
    soFarValue.setBounds(15, 45, 480, 70);
    soFarValue.setFont(PROFIT_FONT);
    soFarValue.setForeground(LoginFrame.PRIMARY_COLOR);
    soFarText.setBounds(15, 120, 470, 40);
    soFarText.setFont(PROFIT_TEXT_FONT);
    refreshDashboard.setBounds(577, 473, 237, 58);
    refreshDashboard.setFont(LoginFrame.TEXT_FONT);
    refreshDashboard.setFocusPainted(false);
    refreshDashboard.addActionListener(this);
    profitHider.setBounds(50, 340, 507, 190);
    profitHider.setBackground(Color.LIGHT_GRAY);
    profitHiderText.setBounds(190, 18, 140, 50);
    profitHiderText.setFont(PANEL_ITEMS_FONT);
    profitHider.setVisible(false);

    // Code to disable certain features for users with lower privileges
    for (int i = 0; i < userDetailsList.size(); i++)
    {
      if (userId == Integer.valueOf(String.valueOf(userDetailsData[i][0])))
      {
        userPrivilege = String.valueOf(userLoginData[i][3]);
      }
    }
    if (userPrivilege.equals("Employee"))
    {
      profitHider.setVisible(true);
      editAccounts.setEnabled(false);
      endTheDay.setEnabled(false);
    }

    // Adding GUI components to the frame.
    mainPanel.add(welcomeText);
    mainPanel.add(user);
    mainPanel.add(storeText);
    mainPanel.add(inventoryPanel);
    mainPanel.add(ordersPanel);
    inventoryPanel.add(inventoryTitle);
    inventoryPanel.add(storageLabel);
    inventoryPanel.add(storageValue);
    inventoryPanel.add(inventoryStatus);
    inventoryPanel.add(inventoryCostTitle);
    inventoryPanel.add(inventoryCostValue);
    inventoryPanel.add(manageInventory);
    ordersPanel.add(ordersTitle);
    ordersPanel.add(currentOrdersLabel);
    ordersPanel.add(currentOrdersValue);
    ordersPanel.add(nextArrivalLabel);
    ordersPanel.add(nextArrivalValue);
    ordersPanel.add(orderAlert);
    ordersPanel.add(manageOrders);
    profitPanel.add(profitsTitle);
    profitPanel.add(endTheDay);
    profitPanel.add(soFarValue);
    profitPanel.add(soFarText);
    mainPanel.add(checkout);
    mainPanel.add(editAccounts);
    mainPanel.add(help);
    mainPanel.add(about);
    mainPanel.add(refreshDashboard);
    mainPanel.add(profitHider);
    profitHider.add(profitHiderText);
    mainPanel.add(profitPanel);
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
    int userId = 1;
    String profitTableName = "ProfitHistory";
    String infoTableName = "ItemInfo";
    String costTableName = "ItemCost";
    String priceTableName = "ItemPrice";
    String inventoryOrdersTableName = "InventoryOrders";
    String orderDetailsTableName = "OrderDetail";
    String orderCostTableName = "OrderCost";
    String userDetailsTableName = "UserDetails";
    String userLoginTableName = "UserLogin";
    String[] inventoryOrdersColumnNames =
    {
      "orderID", "retailerName", "orderedBy", "status", "dateCreated", "dateShipped"
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
    String[] profitColumnNames =
    {
      "entryID", "costofGoods", "priceofGoods", "profit", "date"
    };
    String[] userDetailsColumnNames =
    {
      "userID", "employeeName", "phone", "email"
    };
    String[] userLoginColumnNames =
    {
      "userID", "username", "password", "usertype"
    };

    // Disposing the frame and Create new object of Dashboard.
    new Dashboard(dbName, userLoginTableName, userLoginColumnNames, userDetailsTableName, userDetailsColumnNames, userId, inventoryOrdersTableName, orderDetailsTableName, orderCostTableName, orderDetailsColumnNames, orderCostColumnNames, inventoryOrdersColumnNames, infoTableName, costTableName, priceTableName, infoColumnNames, costColumnNames, priceColumnNames, profitTableName, profitColumnNames);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    // Getting the object that performed the action.
    Object command = e.getSource();

    // Info for database access.
    String dbName = "StoreName";
    int userId = idOfUser;
    String profitTableName = "ProfitHistory";
    String infoTableName = "ItemInfo";
    String costTableName = "ItemCost";
    String priceTableName = "ItemPrice";
    String inventoryOrdersTableName = "InventoryOrders";
    String orderDetailsTableName = "OrderDetail";
    String orderCostTableName = "OrderCost";
    String userDetailsTableName = "UserDetails";
    String userLoginTableName = "UserLogin";
    String customerTableName = "Customers";
    String retailerTableName = "Retailers";
    String loginTableName = "UserLogin";
    String[] inventoryOrdersColumnNames =
    {
      "orderID", "retailerName", "orderedBy", "status", "dateCreated", "dateShipped"
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
    String[] profitColumnNames =
    {
      "entryID", "costofGoods", "priceofGoods", "profit", "date"
    };
    String[] userDetailsColumnNames =
    {
      "userID", "employeeName", "phone", "email"
    };
    String[] userLoginColumnNames =
    {
      "userID", "username", "password", "usertype"
    };
    String[] customerColumnNames =
    {
      "customerID", "name", "phone", "email"
    };
    String[] retailerColumnNames =
    {
      "retailerID", "companyName", "representativeName", "phone", "email"
    };
    String[] loginColumnNames =
    {
      "userID", "username", "password", "usertype"
    };

    // Conditions for specific actions.
    if (command.equals(refreshDashboard))
    {
      // Disposing the frame and Create new object of Dashboard.
      this.dispose();
      new Dashboard(dbName, userLoginTableName, userLoginColumnNames, 
        userDetailsTableName, userDetailsColumnNames, userId, 
        inventoryOrdersTableName, orderDetailsTableName, orderCostTableName, 
        orderDetailsColumnNames, orderCostColumnNames, 
        inventoryOrdersColumnNames, infoTableName, costTableName, 
        priceTableName, infoColumnNames, costColumnNames, priceColumnNames, 
        profitTableName, profitColumnNames);
    }
    else if (command.equals(manageInventory))
    {
      // Create new object of InventoryFrame.
      new InventoryFrame(dbName, infoTableName, costTableName, priceTableName, 
        infoColumnNames, costColumnNames, priceColumnNames);
    }
    else if (command.equals(checkout))
    {
      // Creating a new object of jframe.
      new CheckoutFrame(dbName, infoTableName, priceTableName, infoColumnNames, 
        priceColumnNames, customerTableName, retailerTableName, 
        customerColumnNames, retailerColumnNames, costTableName, 
        costColumnNames);
    }
    else if (command.equals(endTheDay))
    {
      // Creating a new object of jframe.
      new EndOfDayFrame(dbName, profitTableName, profitColumnNames);
    }
    else if (command.equals(manageOrders))
    {
      // Create new object of OrderFrame
      new OrderFrame(dbName, inventoryOrdersTableName, retailerTableName, 
        orderDetailsTableName, orderCostTableName, orderDetailsColumnNames, 
        orderCostColumnNames, inventoryOrdersColumnNames, retailerColumnNames);
    }
    else if (command.equals(editAccounts))
    {
      // Create new object of EditAccounts
      new EditAccounts(dbName, loginTableName, infoTableName, 
        loginColumnNames, infoColumnNames);
    }
    else if (command.equals(help))
    {
      // Create new object of HelpFrame
      new HelpFrame();
    }
    else if (command.equals(about))
    {
      // Displaying message that contains info on program version.
      JOptionPane.showMessageDialog(new JFrame(), "Store Name Inventory Management System \nversion 1.0 \nCreated by Daniel Fakunle", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    this.validate();
    this.repaint();
  }
}

