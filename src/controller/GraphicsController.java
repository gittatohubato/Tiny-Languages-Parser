package controller;

import Parser.Node;
import Parser.ParserMain;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import sample.Main;

import java.io.IOException;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.util.ResourceBundle;

public final class GraphicsController implements Initializable{
//    @FXML
//    private BorderPane root_container;
    @FXML
    private ScrollPane root_container;
    @FXML
    private TextArea traversal_textarea;
    @FXML
    private TextField input_field;
    private GraphicsTree graphicsTree , graphicsTree1;

    public GraphicsController() {
    }

    public void initialize(URL location, ResourceBundle resources) {
        //////////////////////////////////////////////////////////////////////////////////////////
        //Forming Tree:
        //-------------

        Node parseTree = null;
        parseTree = new ParserMain(Main.filePath).syntaxTree().root;

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Initialization:
        //---------------
        GraphicsTree.xExtreme = 0;
        GraphicsTree.yMaxRight = 0;
        GraphicsTree.xExtremeCircle = 0;
        GraphicsTree.yMaxRightCircle = 0;
        ////////////////////////////////////////////////////////////////////////////////////////////

        HBox hBox = new HBox(new GraphicsTree(parseTree).canvas);

        this.root_container.setContent(hBox);
    }
}
