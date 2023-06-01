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
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import static model.Inventory.*;

/**
 * Controls the logic and GUI for the Add Product screen
 * @author Aaron Rose
 */
public class AddProductController implements Initializable {

    private ObservableList<Part> assocParts = FXCollections.observableArrayList();


    public TableView assocPartTable;
    public TableColumn relatePartIdCol;
    public TableColumn relatePartNameCol;
    public TableColumn relatePartInvCol;
    public TableColumn relatePartPriceCol;
    public TableView allPartTable;
    public TableColumn partIdCol;
    public TableColumn partNameCol;
    public TableColumn partInvCol;
    public TableColumn partPriceCol;
    public TextField partSearchText;
    public TextField prodIdText;
    public TextField prodNameText;
    public TextField prodInvText;
    public TextField prodPriceText;
    public TextField prodMaxText;
    public TextField prodMinText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        allPartTable.setItems(getAllParts());
        /**
         * RUNTIME ERROR
         * Tried to initialize and setItems for the assocPartTable here
         * created a new ObservableList of Parts
         * setItems in the addAssociatedParts action
         */

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        relatePartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        relatePartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        relatePartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        relatePartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * This is the logic for the cancel page
     * Sends back to main screen
     */
    public void cancelButtonAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * searches the part table
     * @param actionEvent
     */
    public void onPartSearch(ActionEvent actionEvent) {
        /**
         * @param searchText the text the user has entered in the search field
         * @param partsSearchList an observableList that will contain the search results
         */
        String searchText = partSearchText.getText();
        ObservableList<Part> partSearchList = FXCollections.observableArrayList();

        /**
         * Checks the search text to see if it is the part ID
         */
        if(isInteger(searchText)){
            int searchID = Integer.parseInt(searchText);

            /**
             * Checks that the user has
             */
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
        else{
            allPartTable.setItems(getAllParts());
        }
    }

    /**
     * removes associated parts
     * @param actionEvent
     */
    public void onRemoveAssociatedPart(ActionEvent actionEvent) {
        Part selectedPart = (Part) allPartTable.getSelectionModel().getSelectedItem();

        if(selectedPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No part selected");
            alert.setContentText("A part must be selected to complete this action");
        }
        else{
            assocParts.remove(selectedPart);
        }
    }

    /**
     * adds associated parts to the new Product
     * @param actionEvent
     */
    public void onAddAssociatedPart(ActionEvent actionEvent) {


        /**
         * sets selectedPart from the allPartsTable
         */
        Part selectedPart = (Part) allPartTable.getSelectionModel().getSelectedItem();
        /**
         * checks a part has been selected by the user
         */
        if(selectedPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("No part selected");
            alert.setContentText("A part must be selected to complete this action");
        }
        else{
            /**
             * adds the part to the associateParts list for the product being modified
             */
            assocParts.add(selectedPart);
            assocPartTable.setItems(assocParts);

        }
    }

    public void onSaveProduct(ActionEvent actionEvent) throws IOException {

        /**
         * RUNTIME ERROR
         * users could leave the fields blank
         * solved by creating this check.
         * checks to make sure all fields contain text and are appropriate
         */
        if (prodNameText.getText() == null || prodNameText.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Name Input Error");
            alert.setContentText("Please enter a part name");
            alert.showAndWait();
        } else if (prodPriceText.getText() == null || !Inventory.isDouble(prodPriceText.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Price Input Error");
            alert.setContentText("Please enter a valid price in the format #.##");
            alert.showAndWait();
        } else if (prodInvText.getText() == null || !Inventory.isInteger(prodInvText.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Inventory Input Error");
            alert.setContentText("Please enter a valid number in Inventory");
            alert.showAndWait();
        } else if (prodMaxText.getText() == null || !Inventory.isInteger(prodMinText.getText()) || prodMinText.getText() == null || !Inventory.isInteger(prodMaxText.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Inventory Range Input Error");
            alert.setContentText("Please enter a valid number in Min and Max");
            alert.showAndWait();
        } else {
            String prodName = prodNameText.getText();
            double prodPrice = Double.parseDouble(prodPriceText.getText());
            int prodInv = Integer.parseInt(prodInvText.getText());
            int prodMax = Integer.parseInt(prodMaxText.getText());
            int prodMin = Integer.parseInt(prodMinText.getText());

            /**
             * checks Inventory, Min, and Max are logically correct
             */
            if (prodMax < prodMin) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Inventory Range Error");
                alert.setContentText("The Max cannot be less than the Min");
                alert.showAndWait();
            } else if (prodInv > prodMax || prodInv < prodMin) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Inventory out of range");
                alert.setContentText("Inventory cannot exceed the Min or Max");
                alert.showAndWait();
            }

            /**
             * creates the newProduct as a new Product and calls the addProduct method
             */
            else {

                Product newProduct = new Product(getNextProductId(), prodName, prodPrice, prodInv, prodMin, prodMax);
                Inventory.addProduct(newProduct);

                for (Part part : assocParts) {
                    newProduct.addAssociatedPart(part);
                }

                /**
                 * takes the user back to the main screen
                 */
                Parent root = FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Inventory Management System");
                stage.setScene(scene);
                stage.show();
            }
        }
    }
}
