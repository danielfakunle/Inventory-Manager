//9-18-2021---This class displays the help frame which provides user documentation.
package inventorymanager;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class HelpFrame extends JDialog
{

  // Declaring GUI components.
  JPanel sidePanel;
  JPanel mainPanel;
  JLabel helpTitle;
  JPanel infoPanel;
  JScrollPane infoScroll;
  JTextArea helpText;

  // Constructor for the jframe.
  public HelpFrame()
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
    this.helpTitle = new JLabel("Help");
    this.infoPanel = new JPanel(new BorderLayout());
    this.helpText = new JTextArea("Welcome to the help screen. This page details the functionality of the features available in this application.\n\n"
      + "1.0  Dashboard\n"
      + "The Dashboard is the main control center for you store. It provides the summary for the important aspects of your store such as information on the inventory, recent orders and the daily profit.\n\n"
      + "1.1  Inventory\n"
      + "Here, you can see the state of your inventory i.e., how much of the items stored do you have. If any of your items have a quantity between 1 and 5 for 3 or more items, you’ll receive and alert in this panel. This panel also shows how much you inventory is worth. Next to the inventory title, if you’ve ordered any items, the quantity of you earliest order would be displayed with a little plus sign before it, just to show how much your total inventory will increase due to the order.\n\n"
      + "1.2  Orders\n"
      + "Here, you can see a summary of all your orders. It shows the total amount of orders as well as the date of the next order.\n\n"
      + "1.3  Profits\n"
      + "Here, details on how much profit you made in a day is provided. Make sure you click the ‘End the Day’ button to see more information and to end the day, which sets the counter back to 0.\n\n"
      + "2.0 Manage Inventory\n"
      + "Gives you full control over the items in your inventory. The system for adding new items should apply to other parts of the application that use the same item adding system. ‘Add New’ creates a new empty record where you can add information for a new item. After adding details for the new item, you MUST click ‘Update’ for the item to be added to the database. You can also select items from the table and make changes to the item, clicking ‘Update’ when done.\n\n"
      + "3.0 Manage Orders\n"
      + "Gives you full control over your orders.\n\n"
      + "3.1 Confirm Shipment\n"
      + "When and order has shipped, click this button for a selected order to mark it as shipped. It will then be displayed in order history.\n\n"
      + "3.2 Order History\n"
      + "Contains the history of shipped orders.\n\n"
      + "3.3 Edit Retailers\n"
      + "Gives you full control over the retailers\n\n"
      + "4.0 Checkout\n"
      + "Here you can checkout items for customers. If a customer doesn’t exist in the database, make sure you add a new customer. After the checkout process is completed the receipt will be displayed.\n\n"
      + "5.0 Edit Accounts\n"
      + "Here, users with elevated privileges can edit the account details of other user as well as themselves.\n\n"
      + "6.0 Refresh Dashboard\n"
      + "As you use the application, you will make changes to certain data so you would have to click this button to make the dashboard reflect the data you’ve altered.\n");
    infoScroll = new JScrollPane(infoPanel);

    // Customizing GUI components.
    sidePanel.setBounds(0, 0, 80, 720);
    sidePanel.setBackground(LoginFrame.PRIMARY_COLOR);
    mainPanel.setBounds(80, 0, 880, 720);
    mainPanel.setBackground(LoginFrame.FRAME_COLOR);
    helpTitle.setBounds(15, 25, 120, 24);
    helpTitle.setFont(Dashboard.WELCOME_FONT);
    infoScroll.setBounds(50, 80, 760, 540);
    infoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    infoPanel.setBackground(Color.white);
    helpText.setFont(Dashboard.PANEL_ITEMS_FONT);
    helpText.setWrapStyleWord(true);
    helpText.setLineWrap(true);
    helpText.setOpaque(false);
    helpText.setEditable(false);
    helpText.setFocusable(false);

    // Adding GUI components to the frame.
    mainPanel.add(helpTitle);
    mainPanel.add(infoScroll);
    infoPanel.add(helpText, BorderLayout.PAGE_START);
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

    // Creating a new object of jframe.
    new HelpFrame();
  }

}

