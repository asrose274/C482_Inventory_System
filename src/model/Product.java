package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *model for Product
 * @author Aaron Rose
 */


public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * getter for product ID
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * setter for product ID
     * @param id the id to set
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * getter for product name
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * setter for product name
     * @param name the name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * getter for product Price
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * setter for product price
     * @param price the price to set
     */
    public void setPrice(double price){
        this.price = price;
    }

    /**
     * getter for product stock
     * @return the stock
     */
    public int getStock(){
        return stock;
    }

    /**
     * setter for the product stock
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * getter for the product max
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * setter for the product max
     * @param max the max to set
     */
    public void setMax(int max){
        this.max = max;
    }

    /**
     *getter for the product min
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     *setter for the product min
     * @param min the min to set
     */
    public void setMin(int min){
        this.min = min;
    }

    /**
     *method to associate a part with a product
     * @param part the part to associate
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     *method to disassociate a part from a product
     * @param selectedAssociatedPart the part to remove
     * @return true
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
            associatedParts.remove(selectedAssociatedPart);
            return true;
    }

    /**
     *getter for a product's associated parts
     * @return the list of associated parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
