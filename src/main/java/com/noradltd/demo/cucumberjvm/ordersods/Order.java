package com.noradltd.demo.cucumberjvm.ordersods;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {

  private static final String COMMA = ",";

  private Long orderNumber;
  private Date orderDate;

  public Order(String orderNum, Date date) {
    orderNumber = new Long(orderNum);
    orderDate = date;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((orderDate == null) ? 0 : orderDate.hashCode());
    result = prime * result + ((orderNumber == null) ? 0 : orderNumber.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Order other = (Order) obj;
    if (orderDate == null) {
      if (other.orderDate != null)
        return false;
    } else if (! orderDate.equals(other.orderDate))
      return false;
    if (orderNumber == null) {
      if (other.orderNumber != null)
        return false;
    } else if (! orderNumber.equals(other.orderNumber))
      return false;
    return true;
  }

  @Override
  public String toString() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return new StringBuilder().append(orderNumber).append(COMMA).append(dateFormat.format(orderDate)).toString();
  }
}
