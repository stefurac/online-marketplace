package entities;

import java.util.UUID;

public class Item {
    /**
     * Item is an entity
     *
     * id: The unique identifier of the item.
     * name: The name of the item.
     * description: The description of the item.
     * status: The status of the item in terms of availability.
     */
    private String id;
    private final String name;
    private final String description;
    private ItemStatus status;

    /**
     * Constructor for "Item." Newly constructed Items automatically have status set to "available."
     * @param inputName The assigned Item name.
     * @param inputDescription  The assigned Item description.
     */
    public Item(String inputName, String inputDescription) {
        UUID inputId = UUID.randomUUID();
        this.id = String.valueOf(inputId);
        this.name = inputName;
        this.description = inputDescription;
        this.status = ItemStatus.UNCHECKED;
    }

    /**
     * Gets the item name.
     * @return name
     */
    public String getName() { return name; }

    /**
     * Gets the item id.
     * @return id
     */
    public String getId() { return id; }

    /**
     * Gets the item description.
     * @return description
     */
    public String getDescription() { return description; }

    /**
     * Gets the item status.
     * @return status
     */
    public ItemStatus getStatus() { return status; }


    /**
     * Sets the item id.
     * @param inputId the input id.
     */
    public void setId(String inputId) { id = inputId; }


    /**
     * Sets the item status.
     * @param inputStatus the item status.
     */
    public void setStatus(ItemStatus inputStatus) { status = inputStatus; }

    /**
     * Gets the toString of this Item
     * @return the toString of this Item
     */
    public String toString() {
        return  "Name: " + getName() + "\nDescription: " + getDescription() + "\nStatus: " + getStatus() + "\n";
    }
}
