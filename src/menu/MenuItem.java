package menu;

import java.lang.reflect.Method;

public class MenuItem
{
    private Object obj;
    private String label;
    private String target;
    private boolean isExitItem;

    public MenuItem(String label) { this(label, null, null); }

    public MenuItem(String label,Object obj,String target) 
    {
        this.label = label;
        this.obj = obj;
        this.target = target;
    }
    public String getLabel()
    {
        return this.label;
    }
    
    //Declares if an item is exit item
    public void setExitItem(boolean isExitItem) { this.isExitItem = isExitItem; }
    
    //This method tell us if an item is Exit item
    public boolean isExitItem() { return this.isExitItem; }
    
    void invoke()
    {
        if (this.target == null) 
            return;
        
        try 
        {
            Method method = this.obj.getClass().getMethod(this.target);
            method.invoke(this.obj);
        }
        catch (Exception ex) 
        { 
            ex.printStackTrace(); 
        }
    }

}