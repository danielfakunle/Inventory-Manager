// 10-1-2021---This is the computation class for calculating daily profit.
package inventorymanager;

public class CalculateDailyProfit
{

  // Declaring attributes.
  Double dayPrice;
  Double dayCost;

  // Constructors for objects of this class.
  public CalculateDailyProfit(double dayPrice, double dayCost)
  {
    this.dayPrice = dayPrice;
    this.dayCost = dayCost;
  }

  public CalculateDailyProfit()
  {
    this.dayPrice = 0.0;
    this.dayCost = 0.0;
  }

  // set and get Methods for the dayPrice and dayCost
  public void setDayPrice(Double dayPrice)
  {
    this.dayPrice += dayPrice;
  }

  public Double getDayPrice()
  {
    return dayPrice;
  }

  public void setDayCost(Double dayCost)
  {
    this.dayCost += dayCost;
  }

  public Double getDayCost()
  {
    return dayCost;
  }

  // Method to calculate daily Profit
  public Double calculateDailyProfit(double dayPrice, double dayCost)
  {
    Double dailyProfit;
    dailyProfit = dayPrice - dayCost;
    return dailyProfit;
  }

  public static void main(String[] args)
  {

  }
}

