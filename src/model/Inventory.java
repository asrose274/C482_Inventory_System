package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Contains the methods for process required for the Inventory Program
 * @author Aaron Rose
 */

public class Inventory {
    /**
     * @param partId is the starting point for unique part IDs
     * @param productId is the starting point for unique product IDs
     */
    private static int partId = 1;
    private static int productId = 1000;
    /**
     * creates the Parts ObservableList
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    /**
     * gets all existing Parts
     * @return allParts ObservableList
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * creates the Product ObservableList
     */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * gets all existing products
     * @return allProducts ObservableList
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

    /**
     *adds a new part
     * @param newPart part to add to allParts ObservableList
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     *updates an existing part
     * @param index the place of the part in the ObservableList
     * @param selectedPart the part that the user selected
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /**
     *deletes selected part
     * @param selectedPart part the user has selected
     * @return true when removed from allParts
     */
    public static boolean deletePart(Part selectedPart){
        allParts.remove(selectedPart);
       return true;
    };

    /**
     *
     * @param newProduct new product to add to the allProducts Observable list
     */
    public static void addProduct(Product newProduct){

        allProducts.add(newProduct);
    }

    /**
     *
     * @param index the products place in the ObservableList
     * @param selectedProduct
     */
    public static void updateProduct(int index, Product selectedProduct){
        allProducts.set(index, selectedProduct);
    }

    public static boolean deleteProduct(Product selectedProduct){
        allProducts.remove(selectedProduct);
        return true;
    }

    /**
     *
     * @param partId the input from the user in the search box
     * @return the exact part that matches the partId
     */
    public static Part lookupPart(int partId){
        Part searchPart = null;

        for(Part part : allParts){
            if(part.getId() == partId){
                searchPart = part;
            }
        }
        return searchPart;
    }

    /**
     *
     * @param partName the input from the user in the search box
     * @return any parts that contain the partName
     */
    public static ObservableList<Part> lookupPart(String partName){

        ObservableList<Part> searchParts = FXCollections.observableArrayList();

        for(Part part : allParts){
            if (part.getName().contains(partName)){
                searchParts.add(part);
            }
        }

        return searchParts;
    }

    /**
     *
     * @param productId the ID input from the user
     * @return product with the exact matching productId
     */
    public static Product lookupProduct(int productId){
        Product searchProduct = null;

        for(Product product : allProducts){
            if(product.getId() == productId){
                searchProduct = product;
            }
        }
        return searchProduct;
    }

    /**
     *
     * @param productName input from the user
     * @return products to the Product Table who contain the productName
     */
    public static ObservableList<Product> lookupProduct(String productName){

        ObservableList<Product> searchProducts = FXCollections.observableArrayList();

        for(Product product : allProducts){
            if (product.getName().contains(productName)){
                searchProducts.add(product);
            }
        }

        return searchProducts;
    }


    /**
     *
     * @return partId incremented by 1 so ensure unique partIDs
     */
    public static int getNextPartId(){
        return partId++;
    }

    /**
     *
     * @return productId incremented by 1 so ensure unique productId
     */
    public static int getNextProductId(){
        return productId++;
    }

    /**
     * isInteger checks if the input is an integer or not
     * @param input in the user's input
     * @return true or false
     */
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * isDouble checks if the input is a double or not
     * @param input the user's input
     * @return true or false
     */
    public static boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }



}
