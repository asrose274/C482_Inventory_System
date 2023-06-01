package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls the logic and GUI for the ModifyPart screen
 * @author Aaron Rose
 */

public class ModifyPartController implements Initializable {

    /**
     * creates variables to help receive data from the MainWindow controller
     */
    private static Part modifyPart;
    private static int modifyPartIndex;

    /**
     * declares the FXML GUI elements
     */
    public Label partIdNameLabel;
    public ToggleGroup togglePartType;
    public TextField partIdText;
    public TextField partNameText;
    public TextField partInvText;
    public TextField partPriceText;
    public TextField partMaxText;
    public TextField partMinText;
    public TextField partIdNameText;
    public RadioButton inHouseRadioButton;
    public RadioButton outsourcedRadioButton;


    /**
     * Method to set data received from MainWindowController
     * @param part the part selected on the MainWindow
     */
    public static void setModifyPart(Part part){
        modifyPart = part;
    }

    /**
     * Method to set data received from MainWindowController
     * @param index the index in the Parts ObservableList selected by user
     */
    public static void setModifyPartIndex(int index) {
        modifyPartIndex = index;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**
         * Sets data from selected Part to text fields
         */
        partIdText.setText(String.valueOf(modifyPart.getId()));
        partNameText.setText(modifyPart.getName());
        partPriceText.setText(String.valueOf(modifyPart.getPrice()));
        partInvText.setText(String.valueOf(modifyPart.getStock()));
        partMaxText.setText(String.valueOf(modifyPart.getMax()));
        partMinText.setText(String.valueOf(modifyPart.getMin()));

        /**
        *checks the type of Part that has been selected and adjusts the partIdNameLabel as needed
        */
        if(modifyPart instanceof InHouse){
            inHouseRadioButton.setSelected(true);
            partIdNameText.setText(String.valueOf(((InHouse) modifyPart).getMachineId()));


        }
        else if(modifyPart instanceof Outsourced){
            outsourcedRadioButton.setSelected(true);
            partIdNameLabel.setText("Company Name");
            partIdNameText.setText(((Outsourced) modifyPart).getCompanyName());
        }

    }

    /**
     *code block for the cancel button
     * @param actionEvent clicking the cancel button
     * @throws IOException
     */
    public void onCancelAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * InHouse Radio Button selected
     * @param actionEvent clicking the inHouseRadioButton
     */
    public void onInHouseRadioButton(ActionEvent actionEvent) {
        partIdNameLabel.setText("Machine ID");
    }

    /**
     * Outsourced Radio Button selected
     * @param actionEvent clicking the outsourcedRadioButton
     */
    public void onOutsourcedRadioButton(ActionEvent actionEvent) {
        partIdNameLabel.setText("Company Name");
    }

    /**
     * Save button code block
     * @param actionEvent clicking the save button
     * @throws IOException
     */
    public void onSaveModify(ActionEvent actionEvent) throws IOException {

        /**
         * checks to make sure all fields contain text and are appropriate
         */
        if(partNameText.getText() == null || partNameText.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Name Input Error");
            alert.setContentText("Please enter a part name");
            alert.showAndWait();
        }
        else if(partPriceText.getText() == null || !Inventory.isDouble(partPriceText.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Price Input Error");
            alert.setContentText("Please enter a valid price in the format #.##");
            alert.showAndWait();
        }
        else if (partInvText.getText() == null || !Inventory.isInteger(partInvText.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Inventory Input Error");
            alert.setContentText("Please enter a valid number in Inventory");
            alert.showAndWait();
        }
        else if(partMaxText.getText() == null || !Inventory.isInteger(partMinText.getText()) || partMinText.getText() == null || !Inventory.isInteger(partMaxText.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Inventory Range Input Error");
            alert.setContentText("Please enter a valid number in Min and Max");
            alert.showAndWait();
        }
        else {

            /**
             * Gets data from text fields and stores them in variables
             */
            int partID = Integer.parseInt(partIdText.getText());
            String partName = partNameText.getText();
            double partPrice = Double.parseDouble(partPriceText.getText());
            int partInv = Integer.parseInt(partInvText.getText());
            int partMin = Integer.parseInt(partMinText.getText());
            int partMax = Integer.parseInt(partMaxText.getText());

            /**
             * checks that Inventory, Min and Max are logically acceptable
             */
            if (partMax < partMin) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Inventory Range Error");
                alert.setContentText("The Max cannot be less than the Min");
                alert.showAndWait();
            } else if (partInv > partMax || partInv < partMin) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Inventory out of range");
                alert.setContentText("Inventory cannot exceed the Min or Max");
                alert.showAndWait();
            }

            /**
             * Determines what type of Part the part is and creates it
             * Calls the updatePart method located in the Inventory Class
             * After updating the part the user is brought back to the MainWindow
             */
            if (togglePartType.getSelectedToggle().equals(inHouseRadioButton)) {
                InHouse updatedPart = new InHouse(partID, partName, partPrice, partInv, partMin, partMax, Integer.parseInt(partIdNameText.getText()));
                Inventory.updatePart(modifyPartIndex, updatedPart);
            } else {
                Outsourced updatedPart = new Outsourced(partID, partName, partPrice, partInv, partMin, partMax, partIdNameText.getText());
                Inventory.updatePart(modifyPartIndex, updatedPart);
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

