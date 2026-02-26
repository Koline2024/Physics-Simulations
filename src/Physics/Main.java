package Physics;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application{
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        Scene scene = new Scene(createContent(), 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Region createContent(){
        TextField inputTextField = new TextField("");
        HBox inputRow = new HBox(new Label("Name: "), inputTextField);
        inputRow.setSpacing(6);
        inputRow.setAlignment(Pos.CENTER);
        Label outputLabel = new Label("");
        Button actionButton = new Button("Hello");
        actionButton.setOnAction(e -> outputLabel.setText("Hello " + inputTextField.getText()));
        VBox results = new VBox(20, inputRow, outputLabel, actionButton);
        results.setAlignment(Pos.CENTER);
        return results;
    }


}
