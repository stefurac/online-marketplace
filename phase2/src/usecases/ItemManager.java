package usecases;

import entities.Item;
import entities.ItemStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ItemManager is a use case for Item.
 *
 * IdToItem is a hashmap storing Id as key and Item as value
 */
public class ItemManager {
    private final HashMap<String, Item> itemIdToItem = new HashMap<>();

    /**
     * Create an ItemManager with given Items
     * @param allItems all current Items in the system
     */
    public ItemManager(List<Item> allItems) {
        for (Item item: allItems) {
            this.itemIdToItem.put(item.getId(), item); }
    }

    public HashMap<String, Item> getItemIdToItem() {
        return itemIdToItem;
    }

    /**
     * @return an ArrayList of Items
     */
    public ArrayList<Item> itemList() {
        ArrayList<Item> items = new ArrayList<>();
        for (Map.Entry<String, Item> idsToItems: getItemIdToItem().entrySet()) {
            items.add(idsToItems.getValue()); }
        return items; }

    /**
     * Create a new Item with given name and description of the item, add it to the ItemManager
     * @param inputName The assigned Item name
     * @param inputDescription The assigned Item description
     * @return a String that represents an item id of a newly created item.
     */
    public String createNewItem(String inputName, String inputDescription) {
        Item newItem = new Item(inputName, inputDescription);
        getItemIdToItem().put(newItem.getId(), newItem);
        return newItem.getId();
    }

    /**
     * Find an item with given id
     * @param itemId The given Item id
     * @return the Item associated with an itemId if there is any that exists
     */
    public Item itemByItemId(String itemId){
            return getItemIdToItem().get(itemId); }

    /**
     * Removes an item from the system
     * @param itemId the item ID
     * @return true iff the item is successfully removed
     */
    public boolean removeByItemId(String itemId) {
        ItemStatus status = statusByItemId(itemId);
        if (status.equals(ItemStatus.AVAILABLE) || status.equals(ItemStatus.UNCHECKED)) {
            changeStatus(itemId, ItemStatus.REMOVED);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Deletes the given item from the system
     * @param itemId the item to be deleted.
     */
    public void deleteItem(String itemId) { //previously removeByItemId
        getItemIdToItem().remove(itemId);
    }

    /**
     * Re-adds a removed item
     * @param itemId the item ID
     * @return true iff the item is successfully re-added
     */
    public boolean reAddByItemId(String itemId) {
        if (statusByItemId(itemId).equals(ItemStatus.REMOVED)) {
            changeStatus(itemId, ItemStatus.AVAILABLE);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Gets the toString of the item
     * @param itemId the item ID
     * @return the toString of the item
     */
    public String getToString(String itemId) {
        return itemIdToItem.get(itemId).toString();
    }

    /**
     * Gets the itemId
     * @param item the Item object
     * @return the itemId in String
     */
    public String getItemIdByItem(Item item) {
        return item.getId(); }

    /**
     * Gets the name of an item
     * @param itemId the item ID
     * @return the name
     */
    public String nameByItemId(String itemId) {
        return itemIdToItem.get(itemId).getName();
    }

    /**
     * Gets the description of an item
     * @param itemId the item ID
     * @return the description
     */
    public String descriptionByItemId(String itemId) {
        return itemIdToItem.get(itemId).getDescription();
    }

    /**
     * Gets a list of item IDs that have the given status
     * @param status the status
     * @return a list of item IDs that have the given status
     */
    public ArrayList<String> itemIdsByStatus(ItemStatus status) {
        ArrayList<String> itemIds = new ArrayList<>();
        for (Map.Entry <String, Item> entry : getItemIdToItem().entrySet()) {
            if (entry.getValue().getStatus().equals(status)){
                itemIds.add(entry.getKey());
            }
        }
        return itemIds;
    }

    /**
     * Gets the status of an item
     * @param itemId the item ID
     * @return the ItemStatus
     */
    public ItemStatus statusByItemId(String itemId){
        return itemByItemId(itemId).getStatus(); }

    /**
     * Sets the status of an item to the give status
     * @param itemId the item ID
     * @param status the new ItemStatus
     */
    public void changeStatus(String itemId, ItemStatus status) {
        itemByItemId(itemId).setStatus(status); }

    /**
     * Represents all Items which are not "UNAVAILABLE" in the system.
     * @return String of Items that do not have status "UNAVAILABLE".
     */
    public String toString() {
        StringBuilder Items = new StringBuilder();
        int i = 1;
        for (Item item : itemList()) {
            if (!(item.getStatus() == ItemStatus.UNAVAILABLE)) {
                Items.append(i).append(". ").append(item.toString());
                Items.append("\n");
                i++;
            }
        }
        return Items.toString();
    }
}







