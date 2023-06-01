package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.*;


/**
 * Controls the logic and GUI for the MainWindow
 * @author Aaron Rose
 */

public class MainWindowController implements Initializable {
    /**
     * implements the columns for the tables and the search bars
     */
    public TableView allPartTable;
    public TableColumn partIdCol;
    public TableColumn partNameCol;
    public TableColumn partInvCol;
    public TableColumn partPriceCol;
    public TableView allProductTable;
    public TableColumn prodIdCol;
    public TableColumn prodNameCol;
    public TableColumn prodInvCol;
    public TableColumn prodPriceCol;
    public TextField partSearchText;
    public TextField productSearchText;


    /**
     *variables used to help pass data between controllers
     */
    private static boolean firstTime = true;

    private static Part modifyPart = null;

    private static Product modifyProduct = null;

    private static int modifyPartIndex;

    private static int modifyProductIndex;

    private static Part deletedPart;

    private static Product deletedProduct;





    /**
     * Add test data method
     * creates test data to initialize when the program starts
     */
    private void addTestData(){

        if(!firstTime){
            return;
        }
        firstTime = false;

        InHouse frame = new InHouse(Inventory.getNextPartId(), "bike frame", 10.00, 3, 1, 20, 1);
        Inventory.addPart(frame);
        InHouse tire = new InHouse(Inventory.getNextPartId(), "tire", 5.00, 6, 2, 20, 2);
        Inventory.addPart(tire);
        Outsourced bell = new Outsourced(Inventory.getNextPartId(), "bell", 2.00, 4, 1, 10, "Bell Bros");
        Inventory.addPart(bell);
        Product bike = new Product(Inventory.getNextProductId(), "Bike", 20.00, 2, 1, 10);
        bike.addAssociatedPart(frame);
        bike.addAssociatedPart(tire);
        bike.addAssociatedPart(tire);
        bike.addAssociatedPart(bell);
        Inventory.addProduct(bike);

        Product unicycle = new Product(Inventory.getNextProductId(), "Unicycle", 50.00, 1, 1, 5);
        unicycle.addAssociatedPart(frame);
        unicycle.addAssociatedPart(tire);
        Inventory.addProduct(unicycle);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**
         * Calls method to add test data
         */
        addTestData();

        /**
         * Initializes the Part and Product tables
         */
        allPartTable.setItems(getAllParts());
        allProductTable.setItems(getAllProducts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        prodIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * code block for the part search box
     * @param actionEvent
     * searches existing parts by part ID or part name
     */
    public void onSearchParts(ActionEvent actionEvent) {
        String searchText = partSearchText.getText();
        ObservableList<Part> partSearchList = FXCollections.observableArrayList();

        /**
         * determines whether to search for partId or partName
         */
        if(isInteger(searchText)){
            int searchID = Integer.parseInt(searchText);

            if(lookupPart(searchID) == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Search Failed");
                alert.setHeaderText("Part not found");
                alert.setContentText("This part does not exist");
                alert.showAndWait();}
            else{
                partSearchList.add(lookupPart(searchID));
                allPartTable.setItems(partSearchList);
            }
        }
        else if(isInteger(searchText) == false){
            allPartTable.setItems(lookupPart(searchText));
        }
        /**
         * if the search box is blank it will return all parts
         */
        else{
            allPartTable.setItems(getAllParts());
        }

    }

    /**
     * code block for the add part button
     * @param actionEvent event that triggers the code block
     * @throws IOException
     * takes the user to the AddPart screen
     */
    public void onAddPartAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * the code block for the modify part button
     * It includes a check and alert if the user has not selected a Part from the Parts table
     * Takes them to the ModifyPart screen
     */
    public void onModifyPart(ActionEvent actionEvent) throws IOException {
        modifyPart = (Part) allPartTable.getSelectionModel().getSelectedItem();

        if(modifyPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Modify Part Error");
            alert.setContentText("You must select a part to modify");
            alert.showAndWait();
        }
        else {
                modifyPartIndex = getAllParts().indexOf(modifyPart);
                ModifyPartController.setModifyPartIndex(modifyPartIndex);
                ModifyPartController.setModifyPart(modifyPart);
                Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
                Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Modify Part");
                stage.setScene(scene);
                stage.show();
        }
    }

    /**
     * This is the code block for the delete part button
     * It includes a check and alert if the user has not selected a Part from the table
     */
    public void deletePartAction(ActionEvent actionEvent) {
        deletedPart = (Part) allPartTable.getSelectionModel().getSelectedItem();
        if(deletedPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Delete Part Error");
            alert.setContentText("You must select a part to delete");
            alert.showAndWait();
        }

        else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Action");
            confirm.setHeaderText("Please Confirm Action");
            confirm.setContentText("Are you sure you want to delete this part?");
            Optional<ButtonType> selection = confirm.showAndWait();

            if (selection.get() == ButtonType.OK) {
                Inventory.deletePart(deletedPart);
            }
        }
    }

    /**
     * code block for the products search box
     * @param actionEvent hitting enter while the search box is selected
     */
    public void onSearchProducts(ActionEvent actionEvent) {
        String searchText = productSearchText.getText();
        ObservableList<Product> productsSearchList = FXCollections.observableArrayList();

        if(isInteger(searchText)){
            int searchID = Integer.parseInt(searchText);

            if(lookupProduct(searchID) == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Search Failed");
                alert.setHeaderText("Product not found");
                alert.setContentText("This product does not exist");
                alert.showAndWait();}
            else{
                productsSearchList.add(lookupProduct(searchID));
                allProductTable.setItems(productsSearchList);
            }
        }
        else if(isInteger(searchText) == false){
            allProductTable.setItems(lookupProduct(searchText));
        }
        else{
            allProductTable.setItems(getAllProducts());
        }
    }

    /**
     * code block for the add product button
     * @param actionEvent
     * @throws IOException
     * takes the user to the AddProduct screen
     */
    public void onAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * code block for the modify product button
     * @param actionEvent
     * @throws IOException
     * checks the user has selected a product and if they have will take them to the ModifyProduct page
     * if they have not an error will pop up
     */
    public void onModifyProduct(ActionEvent actionEvent) throws IOException {
        modifyProduct = (Product) allProductTable.getSelectionModel().getSelectedItem();
        if(modifyProduct == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Modify Part Error");
            alert.setContentText("You must select a part to modify");
            alert.showAndWait();
        }
        else {
            modifyProductIndex = getAllProducts().indexOf(modifyProduct);
            ModifyProductController.setModifyProduct(modifyProduct);
            ModifyProductController.setModifyProductIndex(modifyProductIndex);
            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));
            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Modify Product");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * code block for delete product button
     * @param actionEvent the event that triggers the code block
     */
    public void deleteProduct(ActionEvent actionEvent) {
        deletedProduct = (Product) allProductTable.getSelectionModel().getSelectedItem();
        ObservableList<Part> associatedParts = FXCollections.observableArrayList();
        associatedParts = deletedProduct.getAllAssociatedParts();
        /**
         * checks the user has selected a product
         */
        if(deletedProduct == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Delete Product Error");
            alert.setContentText("You must select a product to delete");
            alert.showAndWait();
        }

        /**
         *checks product to see if there are associated parts
         */
        else if(associatedParts.size() != 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot delete this product");
            alert.setContentText("There are parts still associated with this product");
            alert.showAndWait();
        }

        /**
         * has user confirm the action
         */
        else{
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Action");
            confirm.setHeaderText("Please Confirm Action");
            confirm.setContentText("Are you sure you want to delete this product?");
            Optional<ButtonType> selection = confirm.showAndWait();

            if (selection.get() == ButtonType.OK){

                /**
                 * deletes the product from the product table
                 */
                Inventory.deleteProduct(deletedProduct);
            }

        }
    }

    /**
     * code block for the exit button
     * @param actionEvent clicking the exit button
     * ends the program
     */
    public void onExitButton(ActionEvent actionEvent) {
        System.exit(0);
    }




}


