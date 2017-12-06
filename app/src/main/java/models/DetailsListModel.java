package models;

import java.io.Serializable;

/**
 * Created by welcome on 12-07-2017.
 */

public class DetailsListModel implements Serializable {

    String attribute = "";
    String value = "";


    public DetailsListModel() {
    }

    public DetailsListModel(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;

    }


    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
