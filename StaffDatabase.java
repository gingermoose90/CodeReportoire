import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** 
 * 
 * @author andrew
 * This program uses a JavaFX UI and a MySQL relational database to accept and store information
 * on users added to the database. 
 * Users can either add a new entry to the database, search for an entry, update an existing entry
 * or delete an existing entry.
 */

public class StaffDatabase extends Application {

  // initialise instance variables
  private Connection connection;
  private PreparedStatement preparedStatement;
  private ResultSet results;
  private TextField tfId = new TextField();
  private TextField tfFirstName = new TextField();
  private TextField tfLastName = new TextField();
  private TextField tfMi = new TextField();
  private TextField tfAddress = new TextField();
  private TextField tfCity = new TextField();
  private TextField tfState = new TextField();
  private TextField tfTelephone = new TextField();
  private Label lblStatus = new Label();

  @Override
  public void start(Stage primaryStage) {
    // set up database
    initialiseDB();

    // initialise four buttons
    Button btInsert = new Button("Insert");
    Button btUpdate = new Button("Update");
    Button btView = new Button("View");
    Button btClear = new Button("Clear");

    // create six horizontal boxes for layout containing labels and text fields
    HBox hBox1 = new HBox(5);
    hBox1.getChildren().addAll(new Label("ID"), tfId);
    HBox hBox2 = new HBox(5);
    hBox2.getChildren().addAll(new Label("Last Name"), tfLastName, new Label("First Name"),
        tfFirstName, new Label("MI"), tfMi);
    HBox hBox3 = new HBox(5);
    hBox3.getChildren().addAll(new Label("Address"), tfAddress);
    HBox hBox4 = new HBox(5);
    hBox4.getChildren().addAll(new Label("City"), tfCity, new Label("State"), tfState);
    HBox hBox5 = new HBox(5);
    hBox5.getChildren().addAll(new Label("Telephone"), tfTelephone);
    HBox hBox6 = new HBox(6);
    hBox6.getChildren().addAll(btView, btInsert, btUpdate, btClear);
    hBox6.setAlignment(Pos.CENTER);

    // place horizontal boxes into single vertical box
    VBox vBox = new VBox(10);
    vBox.getChildren().addAll(hBox1, hBox2, hBox3, hBox4, hBox5, hBox6, lblStatus);
    for (Node n : vBox.getChildren()) {
      VBox.setMargin(n, new Insets(0, 0, 0, 5));
    }

    // set length of text field objects
    tfId.setPrefColumnCount(10);
    tfLastName.setPrefColumnCount(10);
    tfFirstName.setPrefColumnCount(10);
    tfMi.setPrefColumnCount(2);
    tfAddress.setPrefColumnCount(15);
    tfCity.setPrefColumnCount(13);
    tfState.setPrefColumnCount(3);
    tfTelephone.setPrefColumnCount(8);
    lblStatus.setText("Please view or insert a new record");

    // assign actions on button press
    btView.setOnAction(e -> view());
    btInsert.setOnAction(e -> insert());
    btUpdate.setOnAction(e -> update());
    btClear.setOnAction(e -> clear());

    Scene scene = new Scene(vBox, 480, 230);
    primaryStage.setTitle("Staff Database");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void initialiseDB() {
    try {
      // load driver
      Class.forName("com.mysql.cj.jdbc.Driver");
      System.out.println("Driver loaded");

      // establish connection
      connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/assignment3", "andrew",
          "boulderdash");
      System.out.println("Database connected");

    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    } catch (ClassNotFoundException ex) {
      System.out.println(ex.getMessage());
    }
    if (connection == null) {
    	System.out.println("connection is null");
    }
  }

  /**
   * This method inserts a new entry into the relational database. All fields must have a value
   * entered into them except for the <CODE>tfId</CODE>, which has an automatically generated value
   * passed to it. The <CODE>checkInput</CODE> utility method first checks the validity of each
   * input. If there is an invalid input, the <CODE>lblStatus</CODE> object reports an error and the
   * user can try again.
   * <dt><b>Postcondition:</b>
   * <dd>A new entry is added to the relational database.
   */
  private void insert() {
    if (checkInput()) {
      try {
        String insertString =
            "insert into Staff (id, lastName, firstName, mi, address, city, state, telephone)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?);";
        preparedStatement = connection.prepareStatement(insertString);
        preparedStatement.setString(1, generateId());
        preparedStatement.setString(2, tfLastName.getText());
        preparedStatement.setString(3, tfFirstName.getText());
        preparedStatement.setString(4, tfMi.getText());
        preparedStatement.setString(5, tfAddress.getText());
        preparedStatement.setString(6, tfCity.getText());
        preparedStatement.setString(7, tfState.getText());
        preparedStatement.setString(8, tfTelephone.getText());
        preparedStatement.execute();

        lblStatus.setText("New entry successfully added.");
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    } else {
      lblStatus.setText("Invalid input. Please try again.");
    }
  }

  /**
   * This method displays all details of an existing entry. The entry is searched by its ID, i.e.
   * primary key. The user inputs the ID into the <CODE>tfId</CODE> and presses the 'view' button.
   * The details are displayed in the relevant text fields.
   * <dt><b>Postcondition:</b>
   * <dd>An existing entry has its details displayed in the GUI text fields.
   */
  private void view() {
    if (tfId.getText() == null) {
      lblStatus.setText("Please enter an ID");
    }
    try {
      String viewString = "select * from Staff where id = ?;";
      preparedStatement = connection.prepareStatement(viewString);
      preparedStatement.setString(1, tfId.getText());
      results = preparedStatement.executeQuery();
      if (results.next()) {
        tfLastName.setText(results.getString("lastName"));
        tfFirstName.setText(results.getString("firstName"));
        tfMi.setText(results.getString("mi"));
        tfAddress.setText(results.getString("address"));
        tfCity.setText(results.getString("city"));
        tfState.setText(results.getString("state"));
        tfTelephone.setText(results.getString("telephone"));

        lblStatus.setText("Entry found. Details updated in text fields.");
      } else {
        lblStatus.setText("No entry found.");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * This method uses a prepared statement to update the details of an existing entry in the
   * relational database. All fields must have an entry, otherwise the <CODE>checkInput</CODE>
   * method will prevent the method from entering the try statement. If the <CODE>tfID</CODE> text
   * does not match an existing entry then the label indicates the search failure.
   * <dt><b>Postcondition:</b>
   * <dd>The relational database has an existing entry's fields updated. If no matching ID is found,
   * then no action is taken.
   */
  private void update() {
    if (checkInput()) {
      try {
        String updateString = "update Staff " + "set lastName = ?" + ", firstName = ?" + ", mi = ?"
            + ", address = ?" + ", city = ?" + ", state = ?" + ", telephone = ?" + " where id = ?;";
        preparedStatement = connection.prepareStatement(updateString);
        preparedStatement.setString(1, tfLastName.getText());
        preparedStatement.setString(2, tfFirstName.getText());
        preparedStatement.setString(3, tfMi.getText());
        preparedStatement.setString(4, tfAddress.getText());
        preparedStatement.setString(5, tfCity.getText());
        preparedStatement.setString(6, tfState.getText());
        preparedStatement.setString(7, tfTelephone.getText());
        preparedStatement.setString(8, tfId.getText());
        int result = preparedStatement.executeUpdate();
        if (result >= 1) {
          lblStatus.setText("Details updated");
        } else {
          lblStatus.setText("No entry found for that ID. Please enter a different ID");
        }
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    } else {
      lblStatus.setText("Invalid input. Please try again.");
    }
  }

  /**
   * Clears all of the text field objects.
   */
  private void clear() {
    tfId.clear();
    tfLastName.clear();
    tfFirstName.clear();
    tfMi.clear();
    tfAddress.clear();
    tfCity.clear();
    tfState.clear();
    tfAddress.clear();
    tfTelephone.clear();
    lblStatus.setText("Please view or insert a new record");
  }

  /**
   * Utility method that checks whether the text fields inputs are valid for the relational
   * database.
   * 
   * @return boolean indicating whether or not the input values are valid
   */
  private boolean checkInput() {
    if (tfLastName.getText() == null || tfLastName.getText().length() > 15) {
      return false;
    } else if (tfFirstName.getText() == null || tfFirstName.getText().length() > 15) {
      return false;
    } else if (tfMi.getText() == null || tfMi.getText().length() != 1) {
      return false;
    } else if (tfAddress.getText() == null || tfAddress.getText().length() > 20) {
      return false;
    } else if (tfCity.getText() == null || tfCity.getText().length() > 20) {
      return false;
    } else if (tfState.getText() == null || tfState.getText().length() > 3) {
      return false;
    } else if (tfTelephone.getText() == null) {
      return false;
    } else if (tfTelephone.getText() != null) {
      for (char c : tfTelephone.getText().toCharArray()) {
        if (!Character.isDigit(c)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Utility method that generates a random 9 digit number that acts as a primary key in the
   * relational database. The number is generated through the <CODE>Math.random</CODE> utility and
   * the <CODE>System.nanoTime</CODE> utility which is then converted into a String object.
   * 
   * @return a String object of 9 digits
   */
  private String generateId() {
    long timeSeed = System.nanoTime();
    double randSeed = Math.random() * 1000;
    long midSeed = (long) (timeSeed * randSeed);
    String s = midSeed + "";
    String id = s.substring(0, 9);
    return id;
  }

  public static void main(String[] args) {
    Application.launch(args);
  }

}
