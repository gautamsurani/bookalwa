package models;

import java.io.Serializable;

/**
 * Created by welcome on 23-08-2016.
 */
public class OrderdetailModel implements Serializable
{

    String SR;
    String name;
    String quantity;
    String price;
    String color;
    String type;

    public OrderdetailModel(String sr, String name, String quantity, String price, String color, String type) {
        this. SR=sr;
        this.  name=name;
        this.  quantity=quantity;
        this.  price=price;
        this.  color=color;
        this.  type=type;

     }

    public String getSR() {
        return SR;
    }

    public void setSR(String SR) {
        this.SR = SR;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
