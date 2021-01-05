package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main extends Application {

    public static String filePath;  //Path that is given to parser
    public static Text error=new Text();
    private static final String WRITE_PATH = "Code.txt";

    @Override
    public void start(Stage primaryStage) throws Exception{

        int x=600,y=40;
        primaryStage.setTitle("Parser");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        //Setting the Scene
        Group root = new Group();

        //GridPane root2=new GridPane();
        TextField path = new TextField();
        TextArea code=new TextArea();
        Button button = new Button("Run");
        Text name =new Text(50,50,"           Enter The Code File Path");
        button.setLayoutX(450);
        button.setLayoutY(100);
        path.setLayoutX(100);

        path.setLayoutY(100);
        path.setPrefWidth(300);
        code.setLayoutY(150);
        code.setLayoutX(100);
        error.setLayoutY(50);
        error.setLayoutX(400);
        error.setFont(Font.font(20));

        name.setFill(Color.AQUA);
        name.setFont(Font.font(20));

        button.setOnAction(action -> {

            String pathcode=path.getText().toString();

            Group group = new Group();
            Text text = new Text(200,200,"//    HERE is the TREE"); //demoy

            String codePath = code.getText();


            if(code.getText().trim().equals("")) {
                if(path.getText().trim().equals("")){
                    pathcode="false";
                    error.setFill(Color.RED);
                    error.setText("Invalid Inputs");
                }
                else filePath = path.getText();
            }

            if(pathcode!="false") {

                try {

                    if(!codePath.trim().equals("")) {
                        filePath = WRITE_PATH;
                        codePath = codePath.replaceAll("\n", "\r\n");
                        FileWriter file = new FileWriter(WRITE_PATH);
                        Files.write(Paths.get(WRITE_PATH), codePath.getBytes(), StandardOpenOption.APPEND);
                        file.close();
                    }

                    error.setText("");
                    ScrollPane scrollPane1 = (ScrollPane) FXMLLoader.load(this.getClass().getResource("/fxml/FXMLGraphicsPanel.fxml"));
                    Stage primaryStageruncode = new Stage();
                    primaryStageruncode.setScene(new Scene(scrollPane1));
                    if(error.getText().equals(""))
                        primaryStageruncode.show();
                }
                catch(Exception e) {
                    error.setFill(Color.RED);
                }
            }

        });

        root.getChildren().addAll(button,path,name,code,error);

        scrollPane.setContent(root);

        primaryStage.setScene(new Scene(root, 700, 400,Color.rgb(45,45,45)));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

