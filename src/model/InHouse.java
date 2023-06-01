package model;

/**
 * model for InHouse parts
 * @author Aaron Rose
 */
public class InHouse extends Part{

    /**
     * Machine ID for InHouse part
     */
    private int machineId;

    /**
     *Constructor for InHouse parts
     *
     * @param id the ID for the part
     * @param name the name of the part
     * @param price the price of the part
     * @param stock the amount in the inventory
     * @param min the minimum number that must be available
     * @param max the maximum number the company store
     * @param machineId the machine ID for the part
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * The getter for the machineId
     * @return the machineId of the part
     */

    public int getMachineId(){
        return machineId;
    }

    /**
     * The setter for the machineId
     * @param machineId the machineId to set
     */

    public void setMachineId(int machineId){
        this.machineId = machineId;
    }
}
