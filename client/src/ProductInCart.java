import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created by Tanya Matchenko. 
 * A class for box in the catalogue - a product.
 */
public class ProductInCart {
    private GridPane box = new GridPane();
    private String name;
    private String description;
    private String category;
    private double price;
    private double totalPrice;
    private int id;
    private int quantity;

    private TextField fldQuantity;
    private Text textName;
    private Text textDesc; 
    private Text textPrice;
    private Text textTotalPrice;

    public ProductInCart(int id, String name, String description, String category, double price) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price; totalPrice = 0;
        this.id = id;
        quantity = 0;

        //name, description, price, total price...
        textName = new Text(name); textName.setFont(Font.font(19));
        textDesc = new Text(description+"\n"+category); textDesc.setFont(Font.font(9));
        textPrice = new Text(price + "р"); textPrice.setFont(Font.font(19));
        textTotalPrice = new Text("total:"+totalPrice+"р"); textTotalPrice.setFont(Font.font(19));
        
        //setting up a field for quantity input
        fldQuantity = new TextField(); fldQuantity.setMaxSize(40, 30);
        fldQuantity.textProperty().addListener( (observable, oldValue, newValue) -> {   
            //validating int, setting value
            if ( newValue.matches("\\d{1,2}")){
                fldQuantity.setText(newValue);
                quantity = Integer.parseInt(newValue);
                totalPrice = price*quantity;
                textTotalPrice.setText("total:"+totalPrice+"р");
            }
            else if (newValue.length()==0) {
                quantity = 0;
                totalPrice = 0;
                textTotalPrice.setText("total:0р");
            }
            else fldQuantity.setText(oldValue);
        });

        textTotalPrice.setLayoutX(300);
        box.add(new VBox(textName,textDesc), 0, 0);
        box.add(textPrice, 1, 0);
        box.add(fldQuantity, 2, 0);
        box.add(textTotalPrice, 3, 0);
        ColumnConstraints colCon0 = new ColumnConstraints();
        colCon0.setPrefWidth(200);
        ColumnConstraints colCon1 = new ColumnConstraints();
        colCon1.setPrefWidth(50);
        box.getColumnConstraints().addAll(colCon0,colCon1);
    }

    /**
     * returns the product's wrapper - GridPane
     * @return
     */
    public GridPane getBox(){
        return this.box;
    }
    public int getId() {
        return id;
    }
    public String getName(){
        return name;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public int getQuantity(){
        return quantity;
    }
    /**
     * sets quantity
     * @param quantity
     */
    public void setQuantity(int quantity){
        fldQuantity.setText(quantity+"");
        this.quantity = quantity;
        totalPrice = price*quantity;
        textTotalPrice.setText("total:"+totalPrice+"р");
    }
    /**
     * sets info
     * @param name
     * @param description
     * @param category
     * @param price
     */
    public void setInfo(String name, String description, String category, double price) {
        this.name = name; textName.setText(name);
        this.description = description; textDesc.setText(description+"\n"+category);
        this.price = price; textPrice.setText(price+"р");
        totalPrice = price*quantity;
        textTotalPrice.setText("total:"+totalPrice+"р");
    }
}