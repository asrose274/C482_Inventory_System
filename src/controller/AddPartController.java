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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls the logic and GUI for the Add Part screen
 * @author Aaron Rose
 */

public class AddPartController implements Initializable {

    /**
     * declares the FXML GUI parts
     */
    public Label partIdNameLabel;
    public TextField partIdText;
    public TextField partNameText;
    public TextField partInvText;
    public TextField partPriceText;
    public TextField partMaxText;
    public TextField partIdNameText;
    public ToggleGroup togglePartType;
    public RadioButton inHouseRadioButton;
    public RadioButton OutsourcedRadioButton;
    public TextField partMinText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     *
     * @param actionEvent event that triggers the code block
     * @throws IOException
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
     *
     * @param actionEvent event that triggers the code block
     */

    public void onInHouseRadioButton(ActionEvent actionEvent) {
        partIdNameLabel.setText("Machine ID");
    }

    /**
     *
     * @param actionEvent event that trigger the code block
     */
    public void onOutsourcedRadioButton(ActionEvent actionEvent) {
        partIdNameLabel.setText("Company Name");
    }

    /**
     *
     * @param actionEvent event that triggers the code block
     * @throws IOException
     */
    public void onSaveButton(ActionEvent actionEvent) throws IOException {

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
             * gets the data from the text fields
             */
            String partName = partNameText.getText();
            double partPrice = Double.parseDouble(partPriceText.getText());
            int partInv = Integer.parseInt(partInvText.getText());
            int partMin = Integer.parseInt(partMinText.getText());
            int partMax = Integer.parseInt(partMaxText.getText());

            /**
             * Checks if the Min is greater than the Max and gives and alert if it is
             */
            if (partMax < partMin) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Inventory Range Error");
                alert.setContentText("The Max cannot be less than the Min");
                alert.showAndWait();
            }
            /**
             * Checks if the Inventory amount is greater than the Max or less than the Min
             */
            else if (partInv > partMax || partInv < partMin) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Inventory out of range");
                alert.setContentText("Inventory cannot exceed the Min or Max");
                alert.showAndWait();
            }

            /**
             * If it passes the previous check then it will check the radio button to determine which type of part to create
             */
            else {

                if (togglePartType.getSelectedToggle().equals(inHouseRadioButton)) {
                    InHouse newPart = new InHouse(Inventory.getNextPartId(), partName, partPrice, partInv, partMin, partMax, Integer.parseInt(partIdNameText.getText()));
                    Inventory.addPart(newPart);
                } else {
                    Outsourced newPart = new Outsourced(Inventory.getNextPartId(), partName, partPrice, partInv, partMin, partMax, partIdNameText.getText());
                    Inventory.addPart(newPart);
                }

                /**
                 * This brings the user back to the Main Screen were the new part will show in the Parts Table
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
