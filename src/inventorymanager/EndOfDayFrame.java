//9-18-2021---This class displays the End of ay Frame which displays the daily profit so far.
package inventorymanager;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class EndOfDayFrame extends JDialog implements ActionListener
{

  // Declaring GUI components.
  JPanel mainPanel;
  JLabel costLabel;
  JLabel priceLabel;
  JLabel profitLabel;
  JLabel costValue;
  JLabel priceValue;
  JLabel profitValue;
  JButton endTheDay;
  JLabel header;
  ArrayList<ArrayList<String>> profitList;
  Object[][] profitData;

  // Constructor for the jframe.
  public EndOfDayFrame(String dbName, String profitTableName, String[] profitColumnNames)
  {
    this.setTitle("Store Name Inventory Manager");
    this.setBounds(0, 0, 400, 300);
    this.getContentPane().setBackground(Color.white);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setResizable(false);
    this.setLayout(null);
    this.setModal(true);

    // Declaring objects of the GUI components.
    this.mainPanel = new JPanel(null);
    this.costLabel = new JLabel("Cost Of Items Sold");
    this.priceLabel = new JLabel("Price Of Items Sold");
    this.profitLabel = new JLabel("Profit");

    // Connecting to the database
    JavaDatabase objDb = new JavaDatabase(dbName);
    objDb.setDbConn();

    // Using database object to get the data from the respective table and convert them to 2darrays.
    profitList = objDb.getData(profitTableName, profitColumnNames);
    this.profitData = objDb.to2dArray(profitList);

    this.costValue = new JLabel(String.valueOf(profitData[0][1]));
    this.priceValue = new JLabel(String.valueOf(profitData[0][2]));
    this.profitValue = new JLabel(String.format("%.2f", Double.valueOf(String.valueOf(profitData[0][3]))));
    this.endTheDay = new JButton("End The Day");
    this.header = new JLabel("DAILY PROFIT");

    // Customizing GUI components.
    mainPanel.setBounds(0, 0, 400, 300);
    mainPanel.setBackground(LoginFrame.FRAME_COLOR);
    header.setBounds(130, 25, 130, 40);
    header.setFont(LoginFrame.HEADER_FONT);
    header.setForeground(LoginFrame.PRIMARY_COLOR);
    costLabel.setBounds(90, 70, 170, 15);
    costLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    costValue.setBounds(250, 65, 155, 25);
    costValue.setFont(Dashboard.PANEL_ITEMS_FONT);
    priceLabel.setBounds(90, 105, 170, 15);
    priceLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    priceValue.setBounds(250, 105, 155, 25);
    priceValue.setFont(Dashboard.PANEL_ITEMS_FONT);
    profitLabel.setBounds(90, 145, 70, 15);
    profitLabel.setFont(Dashboard.PANEL_ITEMS_FONT);
    profitValue.setBounds(250, 145, 155, 25);
    profitValue.setFont(Dashboard.PANEL_ITEMS_FONT);
    endTheDay.setBounds(135, 185, 120, 25);
    endTheDay.setFont(LoginFrame.TEXT_FONT);
    endTheDay.setFocusPainted(false);
    endTheDay.addActionListener(this);

    // Adding GUI components to the frame.
    mainPanel.add(header);
    mainPanel.add(costLabel);
    mainPanel.add(costValue);
    mainPanel.add(priceLabel);
    mainPanel.add(priceValue);
    mainPanel.add(profitLabel);
    mainPanel.add(profitValue);
    mainPanel.add(endTheDay);
    this.add(mainPanel);

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
    String profitTableName = "ProfitHistory";
    String[] profitColumnNames =
    {
      "entryID", "costofGoods", "priceofGoods", "profit", "date"
    };

    // Creating a new object of jframe.
    new EndOfDayFrame(dbName, profitTableName, profitColumnNames);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    // Getting the identifier for the object that performed the action.
    Object command = e.getSource();

    // Info for database access.
    String dbName = "StoreName";
    String profitTableName = "ProfitHistory";
    String[] profitColumnNames =
    {
      "entryID", "costofGoods", "priceofGoods", "profit", "date"
    };

    // Connecting to the database.
    JavaDatabase objDb = new JavaDatabase(dbName);
    Connection myDbConn = objDb.getDbConn();

    // Conditions for specific actions.
    if (command.equals(endTheDay))
    {
      // Clearing the record in profit history and adding a new one
      this.dispose();
      String profitHistoryQuery = "INSERT INTO ProfitHistory VALUES (1, 0.0, 0.0, 0.0, '01/01/2021')";
      String deleteProfitHistoryQuery = "DELETE FROM ProfitHistory WHERE entryID = 1";

      try
      {
        Statement s = myDbConn.createStatement();

        s.executeUpdate(deleteProfitHistoryQuery);
        s.executeUpdate(profitHistoryQuery);

        JOptionPane.showMessageDialog(new JFrame(), "Profit values reset.");

      }
      catch (Exception ex)
      {
        JOptionPane.showMessageDialog(new JFrame(), "Unable to End the Day.", "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
      }

    }

    this.validate();
    this.repaint();
  }

}

