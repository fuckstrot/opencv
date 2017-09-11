package javafx;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static javafx.application.Application.launch;
import javafx.scene.layout.Border;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.StageStyle;

public class SalesModule extends Application {
    
    private static final String WIN_ICON = "/graphics/head_ico.png";
    private static final String WIN_TITLE = "1C Продажа товаров";
    private static final String BUTTON_START_TXT = "Начать";
    private static final String BUTTON_END_TXT = "Закончить";
    private static final String BUTTON_VIP = "ВИП";

    public static void main(String[] args) {
        launch(SalesModule.class, args);
    }

    @Override
    public void start(Stage stage) {
        // Use a border pane as the root for scene
        BorderPane border = new BorderPane();
        border.setBorder(Border.EMPTY);
        HBox hbox = addHBox();
        border.setTop(hbox);
        border.setLeft(addVBox());
        // Add a stack to the HBox in the top region
        addStackPane(hbox);

        // To see only the grid in the center, uncomment the following statement
        // comment out the setCenter() call farther down        
        //        border.setCenter(addGridPane());
        // Choose either a TilePane or FlowPane for right region and comment out the
        // one you aren't using        
        border.setRight(addFlowPane());
        //        border.setRight(addTilePane());
        // To see only the grid in the center, comment out the following statement
        // If both setCenter() calls are executed, the anchor pane from the second
        // call replaces the grid from the first call        
        border.setCenter(addAnchorPane(addGridPane()));
        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setTitle(WIN_TITLE);
        stage.getIcons().add(new Image(WIN_ICON));
        //stage.initStyle(StageStyle.TRANSPARENT);
        //stage.setFullScreen(true);
        stage.show();
    }

    /*
 * Creates an HBox with two buttons for the top region
     */
    private HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);   // Gap between nodes
        hbox.setStyle("-fx-background-color: #336699;");
        
        Image imageOk = new Image(getClass().getResourceAsStream("/graphics/vip.png"));
       // imageOk.
        //new ImageView(imageOk).setFitHeight(20);
        ImageView imageOkV =  new ImageView(imageOk);
        imageOkV.setFitHeight(20);
        imageOkV.setFitWidth(100);
        Button buttonCurrent = new Button(BUTTON_START_TXT);
        buttonCurrent.setPrefSize(100, 20);
        Button buttonProjected = new Button(BUTTON_END_TXT);
        buttonProjected.setPrefSize(100, 20);
        Button buttonVip = new Button(BUTTON_VIP);
        buttonVip.setPrefSize(100, 20);
        hbox.getChildren().addAll(buttonCurrent, buttonProjected, buttonVip);
        return hbox;
    }

    private VBox addVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10)); // Set all sides to 10
        vbox.setSpacing(8);       
         vbox.setMinWidth(200);
        // Gap between nodes
        Text title = new Text("Рекомендации");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);
        Hyperlink options[] = new Hyperlink[]{
            new Hyperlink("ФОТО И ВИДЕО"),
            new Hyperlink("АУДИОТЕХНИКА"),
            new Hyperlink("ТЕЛЕВИЗОРЫ"),
            new Hyperlink("СМАРТФОНЫ")};
        for (int i = 0; i < 4; i++) {
            // Add offset to left side to indent from title
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
        }
        return vbox;
    }

    private void addStackPane(HBox hb) {
        StackPane stack = new StackPane();
        Rectangle helpIcon = new Rectangle(30.0, 25.0);
        helpIcon.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop[]{
                    new Stop(0, Color.web("#4977A3")),
                    new Stop(0.5, Color.web("#B0C6DA")),
                    new Stop(1, Color.web("#9CB6CF")),}));
        helpIcon.setStroke(Color.web("#D0E6FA"));
        helpIcon.setArcHeight(3.5);
        helpIcon.setArcWidth(3.5);
        Text helpText = new Text("?");
        helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        helpText.setFill(Color.WHITE);
        helpText.setStroke(Color.web("#7080A0"));
        stack.getChildren().addAll(helpIcon, helpText);
        stack.setAlignment(Pos.CENTER_RIGHT);
        // Add offset to right for question mark to compensate for RIGHT 
        // alignment of all nodes
        StackPane.setMargin(helpText, new Insets(0, 10, 0, 0));
        hb.getChildren().add(stack);
        HBox.setHgrow(stack, Priority.ALWAYS);
    }

    /*
 * Creates a grid for the center region with four columns and three rows
     */
    private GridPane addGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        // Category in column 2, row 1
        //Text category = new Text("Корзина");
        //category.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load("http://mySite.com");
        //grid.add(category, 1, 0);
        // Title in column 3, row 1
//        Text chartTitle = new Text("Current Year");
//        chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
//        grid.add(chartTitle, 2, 0);

        // Subtitle in columns 2-3, row 2
//        Text chartSubtitle = new Text("товары и услуги");
//        grid.add(chartSubtitle, 1, 1, 2, 1);

        // House icon in column 1, rows 1-2
        ImageView imageHouse = new ImageView(new Image(SalesModule.class.getResourceAsStream("/graphics/cart.jpg")));
        imageHouse.setScaleX(0.5);
        imageHouse.setScaleY(0.5);
        grid.add(imageHouse, 0, 0, 1, 2);

//        // Left label in column 1 (bottom), row 3
//        Text goodsPercent = new Text("Goods\n80%");
//        GridPane.setValignment(goodsPercent, VPos.BOTTOM);
//        grid.add(goodsPercent, 0, 2);

        // Chart in columns 2-3, row 3
//        ImageView imageChart = new ImageView(
//                new Image(SalesModule.class.getResourceAsStream("/graphics/piechart.png")));
//        grid.add(imageChart, 1, 2, 2, 1);

        // Right label in column 4 (top), row 3
//        Text servicesPercent = new Text("Services\n20%");
//        GridPane.setValignment(servicesPercent, VPos.TOP);
//        grid.add(servicesPercent, 3, 2);

//        grid.setGridLinesVisible(true);
        return grid;
    }

    /*
 * Creates a horizontal flow pane with eight icons in four rows
     */
    private FlowPane addFlowPane() {

        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setVgap(4);
        flow.setHgap(4);
        flow.setPrefWrapLength(170); // preferred width allows for two columns
        flow.setStyle("-fx-background-color: DAE6F3;");

        ImageView pages[] = new ImageView[8];
        ImageView imgView = null;
        for (int i = 0; i < 8; i++) {
            imgView =  new ImageView(
                    new Image(SalesModule.class.getResourceAsStream(
                            "/graphics/phone_" + (i + 1) + ".jpg")));
            imgView.setFitHeight(90);
            imgView.setFitWidth(80);
            //imgView.setScaleX(0.5);
            //imgView.setScaleY(0.5);
            pages[i] = imgView;
            flow.getChildren().add(pages[i]);
        }

        return flow;
    }

    /*
 * Creates a horizontal (default) tile pane with eight icons in four rows
     */
    private TilePane addTilePane() {
        TilePane tile = new TilePane();
        tile.setPadding(new Insets(5, 0, 5, 0));
        tile.setVgap(4);
        tile.setHgap(4);
        tile.setPrefColumns(2);
        tile.setStyle("-fx-background-color: DAE6F3;");

        ImageView pages[] = new ImageView[8];
        for (int i = 0; i < 8; i++) {
            pages[i] = new ImageView(new Image(SalesModule.class.getResourceAsStream("/graphics/chart_" + (i + 1) + ".jpg")));
            tile.getChildren().add(pages[i]);
        }
        return tile;
    }

    /*
 * Creates an anchor pane using the provided grid and an HBox with buttons
 * 
 * @param grid Grid to anchor to the top of the anchor pane
     */
    private AnchorPane addAnchorPane(GridPane grid) {
        AnchorPane anchorpane = new AnchorPane();
        Button buttonSave = new Button("Сохранить");
        Button buttonCancel = new Button("Отменить");
        HBox hb = new HBox();
        hb.setPadding(new Insets(0, 10, 10, 10));
        hb.setSpacing(10);
        hb.getChildren().addAll(buttonSave, buttonCancel);
        anchorpane.getChildren().addAll(grid, hb);
        // Anchor buttons to bottom right, anchor grid to top
        AnchorPane.setBottomAnchor(hb, 8.0);
        AnchorPane.setRightAnchor(hb, 5.0);
        AnchorPane.setTopAnchor(grid, 10.0);
        return anchorpane;
    }
}
