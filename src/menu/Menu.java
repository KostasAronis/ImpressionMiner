package menu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Menu
{
    private static BufferedReader input;
    private static Menu rootMenu;
    private MenuItem exitItem;
    private boolean isRootMenu;;
    private List<MenuItem> itemList = new ArrayList<MenuItem>();
    private String title; 

    public Menu()
    {
        if (rootMenu == null) 
        {
            input = new BufferedReader(new InputStreamReader(System.in));
            Menu.rootMenu = this;
            isRootMenu = true;
            setTitle("Menu");
        } 
        else 
        {
            setTitle("Sub Menu");
            exitItem = new MenuItem("Back");
        }
        exitItem.setExitItem(true);
    }
//#region Public Methods

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void addItem(MenuItem item)
    {
        itemList.add(item);
    }

    public void execute()
    {
        MenuItem item = null;
        do {
            print();
            
        } while (!item.isExitItem());
    }

//#endregion

//#region Private Methods

private void print()
{

}

//#endregion
}