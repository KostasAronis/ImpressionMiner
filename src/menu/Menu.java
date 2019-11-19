package menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        this.itemList = new ArrayList<MenuItem>();
        
        if (Menu.rootMenu == null) {
            Menu.input = new BufferedReader(new InputStreamReader(System.in)); // create the input stream
            Menu.rootMenu = this; // assign this instance to static rootMenu
            this.isRootMenu = true; // let this instance know it's a root menu
            this.setTitle("Menu");
            
            this.exitItem = new MenuItem("Exit"); // A root menu will exit from the program
        }
        else {
            this.setTitle("Sub Menu");
            this.exitItem = new MenuItem("Back"); // A sub menu will go back one level
        }
        this.exitItem.setExitItem(true); // Let the MenuItem know that it is the exit item for this menu
    }
//#region Public Methods

    //Use this method to set the title of the menu
    public void setTitle(String title)
    {
        this.title = title;
    }

    //Use this method to add menuitem
    public void addItem(MenuItem item)
    {
        this.itemList.add(item);
    }

    public void execute()
    {
        MenuItem item = null;
        do 
        {
            this.print();
            item = this.getUserInput();
            if(!item.isExitItem())
                item.invoke();
        } 
        while (!item.isExitItem());
    }

    public String toString() 
    {
        return "menu=[" + this.title + "]  items=" + this.itemList.toString();
    }
//#endregion

//#region Private Methods
    private int getExitIndex()
    {
        return this.itemList.size() + 1;
    }

    private MenuItem getUserInput()
    {
        MenuItem item = null;
        String input = null;
        
        try { 
            input = Menu.input.readLine();

            if(input.equals("p"))
                return new MenuItem(null);;

            int option = Integer.parseInt(input); // Throws NumberFormatException for non-numberic input
            
            if (option < 1 || option > this.getExitIndex())
                throw new NumberFormatException(); // Taking advantage of above to catch out-of-range numbers
            
            if (option == this.getExitIndex()) {
                item = exitItem;
                
                // If 'this' menu is the root menu, close the input stream
                if (this.isRootMenu)
                    Menu.input.close();
            }
            else item = itemList.get(option - 1); // -1 as itemList(0) -> item 1 in printed menu
        }
        catch (NumberFormatException ex) 
        {
            System.out.println("\nError: '" + input + "' is not a valid menu option!");
            item = new MenuItem(null); // Return a dummy menu item which ensures it cannot be invoked
            //ConsoleUtils.pauseExecution();
        }
        catch (IOException ex) 
        {
            if(item == exitItem && this.isRootMenu)
                System.out.println("Application Exit");
            else
                ex.printStackTrace(); 
        }
        finally 
        { 
            return item; 
        }
    }
    //Print Menu with all available options
    private void print()
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n");
        
        if (this.title.equals("") == false)
            sb.append(this.title + "\n");
        
        for (int i = 0; i < this.itemList.size(); i++)
            sb.append("\n" + (i + 1) + "... " + this.itemList.get(i).getLabel());
        
        sb.append("\n" + getExitIndex() + "... " + exitItem.getLabel());
        sb.append("\n> ");
        
        System.out.print(sb.toString());
    }

//#endregion

}