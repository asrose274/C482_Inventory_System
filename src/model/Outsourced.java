package model;

/**
 * model for Outsourced part
 * @author Aaron Rose
 */

public class Outsourced extends Part{

    /**
     * TThe company name for outsourced part
     * @author Aaron Rose
     */
    private String companyName;

    /**
     * Constructor for outsourced parts
     *
     * @param id the id of the part
     * @param name the name of the part
     * @param price the price of the part
     * @param stock the amount in the inventory
     * @param min the minimum amount required to be carried
     * @param max the maximum amount that can be stored
     * @param companyName the company name for outsourced parts
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * getter for the companyName
     * @return the companyName
     */
    public String getCompanyName(){
        return companyName;
    }

    /**
     * setter for the companyName
     * @param companyName the company that provides the part
     */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
}
