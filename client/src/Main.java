import java.util.ArrayList;
import java.util.Random;
import java.sql.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * created by Tanya Matchenko. Semester project. 
 * Client for delivery service. Works with DB.
 */
public class Main extends Application {

    // поля для соединения с бд
    private static Connection conn;
    private static Statement stmt;
    // остальное
    public static Group root = new Group();
    private static Scene scene;
    private static ArrayList<ProductInCart> catalogueList = new ArrayList<>();
    private static VBox boxCatalogue = new VBox();
    private static ObservableList<String> districts = FXCollections.observableArrayList();
    private static ObservableList<Integer> clients = FXCollections.observableArrayList();
    private static TextArea fieldLog;
    private static int currentOrderId = 0;
    private static Random rnd = new Random();

    /**
     * Connects with database
     */
    private static void initSql() {
        try {
            String url = "jdbc:mysql://localhost:3306/tanya?&serverTimezone=UTC";
            String user = "root";
            // register
            Class.forName("com.mysql.jdbc.Driver");
            // создаем подключение к бд
            conn = DriverManager.getConnection(url, user, "");
            stmt = conn.createStatement();
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            log(e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    /**
     * Spawns UI
     */
    private static void initMenu() {
        scene = new Scene(root, 1200, 600);

        // граница
        Line border1 = new Line(400, 0, 400, 600);
        Line border2 = new Line(800, 0, 800, 600);
        // делаем границу менюшки всегда до конца экрана
        scene.heightProperty().addListener((obs, oldValue, newValue) -> border1.setEndY(newValue.doubleValue()));
        scene.heightProperty().addListener((obs, oldValue, newValue) -> border2.setEndY(newValue.doubleValue()));

        // заголовки
        Text textHeadCatalogue = new Text("Catalogue");
        Text textHeadOrder = new Text("Order");
        Text textHeadAdmin = new Text("Admin");
        // коробки
        VBox boxCataloguePart = new VBox(28, textHeadCatalogue);
        boxCataloguePart.setLayoutX(5);
        VBox boxOrderPart = new VBox(28, textHeadOrder);
        boxOrderPart.setLayoutX(405);
        VBox boxAdminPart = new VBox(28, textHeadAdmin);
        boxAdminPart.setLayoutX(805);

        // #region catalogue
        // catalogue
        ScrollPane spCatalogue = new ScrollPane(boxCatalogue);
        spCatalogue.setStyle("-fx-background: white; -fx-border-color: white;");
        spCatalogue.setMaxHeight(560);
        spCatalogue.setPrefWidth(390);
        spCatalogue.setHbarPolicy(ScrollBarPolicy.NEVER);
        spCatalogue.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        boxCataloguePart.getChildren().addAll(spCatalogue);
        // #endregion

        // #region order
        // total price
        Text textTotalPrice = new Text("total: 0р");
        Button btnRefreshPrice = new Button("refresh");
        btnRefreshPrice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textTotalPrice.setText("total: " + getTotalPrice() + "р");
            }
        });

        // address
        Text textAddress = new Text("Address:");
        // district
        Text textDistrict = new Text("District ");
        ComboBox<String> fieldDistrict = new ComboBox<>(districts);
        fieldDistrict.setVisibleRowCount(4);
        fieldDistrict.setPrefWidth(70);
        TextField fieldAddress = new TextField();
        fieldAddress.setMaxWidth(150);
        fieldAddress.setPromptText("address");
        // set max address length
        fieldAddress.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() < 50)
                fieldAddress.setText(newValue);
            else {
                fieldAddress.setText(oldValue);
                log("Max length of address is 50 symbols!");
            }
        });
        HBox boxAddress = new HBox(10, textDistrict, fieldDistrict, fieldAddress);

        // client
        Text textClient = new Text("Client");
        ComboBox<Integer> fieldClientId = new ComboBox<>(clients);
        fieldClientId.setVisibleRowCount(4);
        fieldClientId.setPrefWidth(150);

        // payment
        Text textPaymentOption = new Text("Payment option");
        ObservableList<String> paymentOptions = FXCollections.observableArrayList("cash", "card");
        ComboBox<String> fieldPayment = new ComboBox<>(paymentOptions);
        fieldPayment.setVisibleRowCount(4);
        fieldPayment.setPrefWidth(90);

        // make order
        Button btnMakeOrder = new Button("ORDER");
        btnMakeOrder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // #region ORDER
                // check address, district, payment, client
                String address = fieldAddress.getText();
                String payment = fieldPayment.getValue();
                String wrhId = fieldDistrict.getValue();
                if (address.equals("") || payment == null || wrhId == null || fieldClientId.getValue() == null) {
                    log("Either address, district, payment or client ID is empty. Aborting process.");
                    return;
                }
                int cltId = fieldClientId.getValue();

                // check if in stock
                try {
                    for (ProductInCart productInCart : catalogueList)
                        if (productInCart.getQuantity() > 0) {
                            ResultSet res = stmt.executeQuery("select quantity from spr where prd_id = "
                                    + productInCart.getId() + " and wrh_id = " + wrhId);
                            if (!res.next()) {
                                log("WRH" + wrhId + " " + productInCart.getName()
                                        + " - out of stock. Aborting proccess.");
                                return;
                            }
                            int stocked = res.getInt("quantity");
                            if (stocked < productInCart.getQuantity()) {
                                log(productInCart.getName() + " - Ordered: " + productInCart.getQuantity()
                                        + " In Stock: " + stocked);
                                log("Aborting process.");
                                return;
                            }
                        }
                } catch (SQLException e) {
                    log("Failed to check stocked products. Aborting process. " + e.getSQLState());
                    e.printStackTrace();
                    return;
                }

                double totalPrice = getTotalPrice();
                textTotalPrice.setText(totalPrice + "");
                if (totalPrice == 0) {
                    log("The shopping cart is empty.");
                    return;
                }
                Timestamp stamp = new Timestamp(System.currentTimeMillis());
                int deliverytime = rnd.nextInt(20) + 25;

                // getting random deliverer
                int dlvId = 0;
                try {
                    ResultSet res = stmt.executeQuery("select id from dlv order by rand() limit 1");
                    res.next();
                    dlvId = res.getInt("id");
                } catch (SQLException e) {
                    log("Couldn't obtain a random deliverer. Aborting process.\n" + e.getSQLState());
                    e.printStackTrace();
                    return;
                }

                // ORDERING
                try {
                    stmt.executeUpdate(
                            "insert into ord (id, cost, address, payment, complete, datetime, deliverytime, dlv_id, clt_id) "
                                    + "values(" + currentOrderId + "," + totalPrice + ",'" + address + "','" + payment
                                    + "','" + "1" + "','" + stamp + "'," + deliverytime + "," + dlvId + "," + cltId
                                    + ")");

                    log("ORDER " + currentOrderId + "\nClient ID: " + cltId + "\nPrice: " + totalPrice + "\nDistrict: "
                            + wrhId + "\nAddress:" + address + "\nPayment option: " + payment
                            + "\nFinished: true\nTimestamp:" + stamp + "\nDelivery time: " + deliverytime
                            + " minutes\nDeliverer ID:" + dlvId + "\n");
                } catch (SQLException e) {
                    log("Failed to make a new order. Aborting process. " + e.getSQLState());
                    e.printStackTrace();
                    try {
                        conn.rollback();
                    } catch (SQLException e1) {
                        log("Failed to rollback. " + e1.getSQLState());
                        e1.printStackTrace();
                    }
                    return;
                }
                // #endregion

                // #region ORDERED PRODUCT
                try {
                    for (ProductInCart product : catalogueList) {
                        if (product.getQuantity() > 0) {
                            stmt.executeUpdate("insert into opr (quantity, ord_id, prd_id) values("
                                    + product.getQuantity() + "," + currentOrderId + "," + product.getId() + ")");
                            log("Added (ordID=" + currentOrderId + ", prdID=" + product.getId() + ", q="
                                    + product.getQuantity() + ") to Ordered Products");
                        }
                    }
                } catch (SQLException e) {
                    log("Failed to add ordered products. " + e.getSQLState());
                    e.printStackTrace();
                    try {
                        conn.rollback();
                    } catch (SQLException e1) {
                        log("Failed to rollback. " + e1.getSQLState());
                        e1.printStackTrace();
                    }
                    return;
                }
                // #endregion

                // #region UNSTOCK
                try {
                    for (ProductInCart product : catalogueList) {
                        if (product.getQuantity() > 0) {

                            ResultSet res = stmt.executeQuery("select quantity from spr where prd_id = "
                                    + product.getId() + " and wrh_id = " + wrhId);
                            res.next();
                            int stocked = res.getInt("quantity");

                            stmt.executeUpdate("update spr set quantity=" + product.getQuantity() + " where prd_id="
                                    + product.getId() + " and wrh_id=" + wrhId);
                            log("WRH" + wrhId + "PRD" + product.getId() + "(" + product.getName() + ")" + " - set to "
                                    + (stocked - product.getQuantity()));
                        }
                    }
                } catch (SQLException e) {
                    log("Failed to unstock products. Aborting proccess. " + e.getSQLState());
                    e.printStackTrace();
                    return;
                }
                // #endregion

                currentOrderId++;
                try {
                    conn.commit();
                } catch (SQLException e) {
                    log("Failed comitting changes. "+e.getSQLState());
                    e.printStackTrace();
                }
            }
        });

        boxOrderPart.getChildren().addAll(textTotalPrice, btnRefreshPrice, textAddress, boxAddress, textClient,
                fieldClientId, textPaymentOption, fieldPayment, btnMakeOrder);
        // #endregion

        // #region admin
        Button btnRefresh = new Button("Refresh info");
        btnRefresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    refreshDistricts();
                    refreshClients();
                } catch (SQLException e) {
                    log("Failed to obtain Districts/Clients. "+e.getSQLState());
                    e.printStackTrace();
                }
            }
        });

        //setting up the log field. it's not editable. it adapts to app's width.
        fieldLog = new TextArea("LOG");
        fieldLog.editableProperty().set(false);
        fieldLog.setPrefSize(390, 370);
        scene.widthProperty().addListener((obs, oldValue, newValue) -> fieldLog.setPrefWidth(newValue.doubleValue()-840));

        Button btnClients = new Button("Show clients");
        btnClients.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    log("CLIENTS:");
                    ResultSet res = stmt.executeQuery("select * from clt");
                    while (res.next()) {
                        log("ID: " + res.getInt("id") + "\nName: " + res.getString("name") + "\nPhone: "
                                + res.getString("phone"));
                    }
                } catch (SQLException e) {
                    log("Failed to obtain clients. "+e.getSQLState());
                    e.printStackTrace();
                }
            }
        });

        boxAdminPart.getChildren().addAll(btnClients, fieldLog);
        // #endregion

        root.getChildren().addAll(border1, border2, boxCataloguePart, boxOrderPart, boxAdminPart);
    }

    /**
     * узнаем номер последнего заказа. 
     * когда будем создавать новый заказ, id будет увеличен
     * @return номер последнего заказа
     * @throws SQLException
     */
    private static int getLastOrderId() throws SQLException {
        ResultSet res = stmt.executeQuery("select max(id) from ord");
        res.next();
        return res.getInt("max(id)");
    }

    /**
     * оставить сообщение в окне лога
     * @param msg сообщение
     */
    public static void log(String msg) {
        fieldLog.setText(fieldLog.getText() + "\n" + msg);
    }

    /**
     * refresh observable list for combobox
     * @throws SQLException
     */
    private static void refreshDistricts() throws SQLException {
        districts.clear();
        ResultSet res = stmt.executeQuery("select id from wrh");
        while (res.next()) {
            districts.add(res.getString("id"));
        }
    }

    /**
     * refresh observable list for combobox
     * @throws SQLException
     */
    private static void refreshClients() throws SQLException {
        clients.clear();
        ResultSet res = stmt.executeQuery("select id from clt");
        while (res.next()) {
            clients.add(res.getInt("id"));
        }
    }

    /**
     * @return total price
     */
    private static double getTotalPrice() {
        double total = 0;
        for (ProductInCart productInCart : catalogueList) {
            if (productInCart.getQuantity() > 0) {
                total += productInCart.getTotalPrice();
            }
        }
        return total;
    }

    /**
     * спрашиваем у бд, какие есть товары. заполняем каталог.
     */
    private static void initCatalogue() {
        try {
            ResultSet res = stmt.executeQuery("select * from prd");
            while (res.next()) {
                ProductInCart tmp = new ProductInCart(res.getInt("id"), res.getString("name"),
                        res.getString("description"), res.getString("category"), res.getDouble("price"));
                catalogueList.add(tmp);
                boxCatalogue.getChildren().add(tmp.getBox());
            }
        } catch (SQLException e) {
            log(e.getSQLState());
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        initMenu();
        initSql();
        initCatalogue();
        try {
            refreshClients();
            refreshDistricts();
        } catch (SQLException e) {
            log(e.getSQLState());
            e.printStackTrace();
        }
        try {
            currentOrderId = getLastOrderId()+1;
        } catch (SQLException e) {
            currentOrderId = 0;
            log("Couldn't get last order's ID!\n" + e.getSQLState());
            e.printStackTrace();
        }

        primaryStage.setTitle("Polydelivery");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}