package usecases;

import entities.Item;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ItemManager is a use case for Item.
 *
 * allItems store all Item in the system.
 * IdToItem is a hashmap storing Id as key and Item as value
 */
public class ItemManager {
    private ArrayList<Item> allItems;
    private HashMap<String, Item> IdToItem = new HashMap<>();

    /**
     * Create an ItemManager with given items
     * @param allItems all current items in the system (if any)
     */
    public ItemManager(ArrayList<Item> allItems) {
        this.allItems = allItems;
        for (Item i: allItems){
            IdToItem.put(i.getId(), i);
        }
    }

    /**
     * Create an empty ItemManager
     */
    public ItemManager(){
        this.allItems = new ArrayList<>();
    }

    /**
     * Add item to the ItemManager
     * @param item Item to be added
     */
    public void addItem(Item item){
        if (IdToItem.containsKey(item.getId())) return;
        allItems.add(item);
        IdToItem.put(item.getId(), item);
    }

    /**
     * Create a new Item with given name and description of the item, add it to the ItemManager
     * @param inputName The assigned Item name
     * @param inputDescription The assigned Item description
     * @return String The ID of the newly created item.
     */
    public String createNewItem(String inputName, String inputDescription){
        Item newItem = new Item(inputName, inputDescription);
        addItem(newItem);
        return newItem.getId();
    }

    /**
     * @param itemId id of an item
     * @return true iff the item is in the system
     */
    public boolean itemInSystem(String itemId) {
        return IdToItem.containsKey(itemId);
    }

    /**
     * Find an item with given id
     * @param itemId The given Item id
     */
    public Item findItem(String itemId){
        if (itemInSystem(itemId)) return IdToItem.get(itemId);
        return null;
    }

    /**
     * Gets all items
     * @return ArrayList<Item> of all items
     */
    public ArrayList<Item> getAllInfo() { return this.allItems; }

    /**
     * Get name of an item with given id
     * @param itemId The given Item id
     * @return Item.ItemStatus of the status of the item
     */
    public Item.ItemStatus getItemStatus(String itemId){return findItem(itemId).getStatus(); }

    /**
     * Represents all Items in the system.
     * @return String of Items.
     */
    public String toString() {
        StringBuilder Items = new StringBuilder();
        int i = 1;
        for (Item item : allItems) {
            if (!(item.getStatus() == Item.ItemStatus.UNAVAILABLE)) {
                Items.append(i).append(". ").append(item.toString());
                Items.append("\n");
                i++;
            }
        }
        return Items.toString();
    }

    /**
     * Returns the string of a single item.
     * @param itemId the item whose string we want to return.
     * @return string representation of the given item
     */
    public String singleItemToString(String itemId){
        Item currentItem = findItem(itemId);
        return currentItem.toString();
    }

    /**
     * Sets the item status to AVAILABLE.
     * @param itemId the item whose status we want to change.
     */
    public void makeItemAvailable(String itemId)  {
        findItem(itemId).setStatus(Item.ItemStatus.AVAILABLE);
    }


    /**
     * Sets the item status to UNAVAILABLE.
     * @param itemId the item whose status we want to change.
     */
    public void makeItemUnavailable(String itemId)  {
        findItem(itemId).setStatus(Item.ItemStatus.UNAVAILABLE);
    }

    /**
     * Removes the given item from the system
     * @param itemId the item to be removed.
     */
    public void removeItemFromSystem(String itemId) {
        Item item = findItem(itemId);
        IdToItem.remove(itemId);
        allItems.remove(item);
        }

    /**
     * @return a string representation of all items with status "Unchecked"
     */
    public String getRequestedItems() {
        String s = "";
        int i = 1;
        for (Item item: allItems) {
            if (item.getStatus() == Item.ItemStatus.UNCHECKED) {
                s = s.concat(i + ". " + item.toString() + "\n");
                i ++;
            }
        }
        return s;
    }

    /**
     * Gives the itemId
     * @param item the Item object
     * @return the itemId in String
     */
    public String getItemIdByItem(Item item) {
        return item.getId();
    }
}







