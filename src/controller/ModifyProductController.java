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
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.getAllParts;
import static model.Inventory.lookupPart;

/**
 * Controls the logic and GUI for the ModifyProduct screen
 * @author Aaron Rose
 */

public class ModifyProductController implements Initializable {

    /**
     * Variables used to receive data from the MainWindow Controller
     */
    private static Product modifyProduct;
    private static int modifyProductIndex;
    private static Part selectedPart;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * declaring the GUI elements
     */
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

    /**
     *
     * @param product the product the user selected on the Main Screen
     */
    public static void setModifyProduct(Product product){
        modifyProduct = product;
    }

    /**
     *
     * @param index the index of the product the user selected on the Main Screen
     */
    public static void setModifyProductIndex(int index) {
        modifyProductIndex = index;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**
         * Initializes the allPartsTable and associatePartsTable
         */
        allPartTable.setItems(getAllParts());
        associatedParts = modifyProduct.getAllAssociatedParts();
        assocPartTable.setItems(associatedParts);


        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        relatePartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        relatePartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        relatePartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        relatePartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        /**
         * sets the text fields to the data received from the main screen
         */
        prodIdText.setText(String.valueOf(modifyProduct.getId()));
        prodNameText.setText(modifyProduct.getName());
        prodPriceText.setText(String.valueOf(modifyProduct.getPrice()));
        prodInvText.setText(String.valueOf(modifyProduct.getStock()));
        prodMaxText.setText(String.valueOf(modifyProduct.getMax()));
        prodMinText.setText(String.valueOf(modifyProduct.getMin()));

    }

    /**
     * code block for the cancel button
     * @param actionEvent clicking the cancel button
     * @throws IOException
     * brings the user back to the main screen
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
     * code block for the part search text field
     * @param actionEvent pressing enter while the part search field is selected
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
        if(Inventory.isInteger(searchText)){
            int searchID = Integer.parseInt(searchText);

            /**
             * Gives error if searched part is not found
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
        else if(Inventory.isInteger(searchText) == false){
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
     * code block for the Remove Associated Part button
     * @param actionEvent click the Remove Associated Part button
     */
    public void onRemoveAssociatedPart(ActionEvent actionEvent) {

        /**
         * sets the selected part from the associated part table
         */
        selectedPart = (Part) assocPartTable.getSelectionModel().getSelectedItem();

        /**
         * checks a part has been selected
         */
        if(selectedPart == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No part selected");
            alert.setContentText("A part must be selected for this action");
            alert.showAndWait();
        }
        /**
         * confirms the users selection and action
         */
        else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Action");
            confirm.setHeaderText("Please Confirm Action");
            confirm.setContentText("Are you sure you want to remove this part? This part will still be removed even if you do not save changes to this product.");
            Optional<ButtonType> selection = confirm.showAndWait();
            if (selection.get() == ButtonType.OK){
                modifyProduct.deleteAssociatedPart(selectedPart);

            }
        }
    }

    /**
     * code block for the Add Associate Part button
     * @param actionEvent Add Associate Part button
     */
    public void onAddAssociatedPart(ActionEvent actionEvent) {
        /**
         * sets selectedPart from the allPartsTable
         */
        selectedPart = (Part) allPartTable.getSelectionModel().getSelectedItem();
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
            modifyProduct.addAssociatedPart(selectedPart);
        }
    }

    /**
     * code block for the save product button
     * @param actionEvent clicking the save product button
     * @throws IOException
     */
    public void onSaveProduct(ActionEvent actionEvent) throws IOException {

        /**
         * checks to make sure all fields contain text and are appropriate
         */
        if(prodNameText.getText() == null || prodNameText.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Name Input Error");
            alert.setContentText("Please enter a part name");
            alert.showAndWait();
        }
        else if(prodPriceText.getText() == null || !Inventory.isDouble(prodPriceText.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Price Input Error");
            alert.setContentText("Please enter a valid price in the format #.##");
            alert.showAndWait();
        }
        else if (prodInvText.getText() == null || !Inventory.isInteger(prodInvText.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Inventory Input Error");
            alert.setContentText("Please enter a valid number in Inventory");
            alert.showAndWait();
        }
        else if(prodMaxText.getText() == null || !Inventory.isInteger(prodMinText.getText()) || prodMinText.getText() == null || !Inventory.isInteger(prodMaxText.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Inventory Range Input Error");
            alert.setContentText("Please enter a valid number in Min and Max");
            alert.showAndWait();
        }
        else {

            /**
             * gets the data from the text fields and stores them in variables
             */
            int prodId = Integer.parseInt(prodIdText.getText());
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
             * creates the modifiedProduct as a new Product and calls the updateProduct method
             */
            else {
                Product modifiedProduct = new Product(prodId, prodName, prodPrice, prodInv, prodMin, prodMax);
                Inventory.updateProduct(modifyProductIndex, modifiedProduct);

                /**
                 * adds all parts in the associatedPartsTable to the newly created modifiedProduct
                 */
                for (Part part : associatedParts) {
                    modifiedProduct.addAssociatedPart(part);
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
