package org.simpumind.com.business.org.simpumind.com.business;

/**
 * Created by simpumind on 12/10/15.
 */
public class Item
{
     String name;
     int drawableId;

    public Item(){

    }
    Item(String name, int drawableId)
    {
        this.name = name;
        this.drawableId = drawableId;
    }

    public String getName() {
        return name;
    }

    public int getDrawableId() {
        return drawableId;
    }
}
