package Component.AdminView;
    import Component.MainComponent.BankController;
    import Component.ViewLoansInfo.ViewLoansInfoController;
    import Component.ViewCustomersInfo.ViewCustomersInfoController;
    import DTOs.CustomerDTOs;
    import DTOs.LoanDTOs;
    import SystemExceptions.InccorectInputType;
    import common.BankResourcesConstants;
    import javafx.beans.property.SimpleBooleanProperty;
    import javafx.beans.property.SimpleStringProperty;
    import javafx.collections.FXCollections;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.geometry.Insets;
    import javafx.scene.control.*;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.layout.*;
    import javafx.scene.paint.Color;
    import javafx.stage.FileChooser;
    import org.controlsfx.control.table.TableRowExpanderColumn;

    import java.io.File;
    import java.io.IOException;
    import java.io.Serializable;
    import java.net.URL;
    import java.util.List;

public class AdminViewController implements Serializable {

    @FXML BankController mainController;
    @FXML private Button IncreaseYazBtn;
    @FXML private Button LoadFileBtn;
    @FXML private GridPane MainButtonsBox;
    @FXML private TableView<LoanDTOs> LoansData = new TableView<>();
    @FXML private TableView<CustomerDTOs> CustomerData = new TableView<>();
    @FXML private BorderPane viewByAdminContainer;

    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileSelected;

    public AdminViewController() {
        this.selectedFileProperty = new SimpleStringProperty();
        this.isFileSelected = new SimpleBooleanProperty(false);
    }

    @FXML private void initialize(){
       IncreaseYazBtn.disableProperty().bind(isFileSelected.not());
        MainButtonsBox.prefWidthProperty().bind(viewByAdminContainer.widthProperty());
    }

    public SimpleStringProperty getSelectedFileProperty() {
        return selectedFileProperty;
    }

    public SimpleStringProperty selectedFilePropertyProperty() {
        return selectedFileProperty;
    }

    public void setMainController(BankController m_mainController){
        this.mainController = m_mainController;
    }

    @FXML private void clickOnLoadFile(ActionEvent event) throws InccorectInputType {
        List<LoanDTOs> allLoans = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Bank Data File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        File selectedFile = mainController.ShowFileChooserDialog(fileChooser);
        if(selectedFile == null){
            return;
        }
        String absolutePath = selectedFile.getAbsolutePath();
        StringBuilder newAbsolutePath = new StringBuilder();
        selectedFileProperty.set(absolutePath);
        isFileSelected.set(mainController.LoadFileActivation(absolutePath, newAbsolutePath));//TODO add exception
        if(isFileSelected.getValue()){
            CustomerData.getItems().clear();
            LoansData.getItems().clear();
            ViewLoansInfoController loansInfoController = new ViewLoansInfoController();
            loansInfoController.setMainController(mainController);
            loansInfoController.buildLoansTableView(LoansData,mainController.getSystemLoans());
            buildCustomersTableView();
            selectedFileProperty.set(newAbsolutePath.toString());
        }
    }

    public void clearLoansTables(){
        LoansData.getItems().clear();
        CustomerData.getItems().clear();
    }

    public void updateLoansInBankInAdminView(){
        CustomerData.getItems().clear();
        LoansData.getItems().clear();
        ViewLoansInfoController loansInfoController = new ViewLoansInfoController();
        loansInfoController.setMainController(mainController);
        loansInfoController.buildLoansTableView(LoansData,mainController.getSystemLoans());
        buildCustomersTableView();
    }

    private void buildCustomersTableView(){
        CustomerData.getItems().clear();
        List<CustomerDTOs> allCustomers = mainController.getSystemCustomers();
        TableRowExpanderColumn<CustomerDTOs> expanderColumn = new TableRowExpanderColumn<>(param -> {
            try {
                return expandCustomerInfo(param);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });

        expanderColumn.setPrefWidth(45);
        TableColumn<CustomerDTOs, String> nameOfCustomer = new TableColumn<>("Name of customer");
        nameOfCustomer.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameOfCustomer.setPrefWidth(125);

        TableColumn<CustomerDTOs, String> balance = new TableColumn<>("Balance");
        balance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        balance.setPrefWidth(125);

        TableColumn<CustomerDTOs, String> loansAsLoaner = new TableColumn<>("Loans as Borrower");
        loansAsLoaner.setCellValueFactory(new PropertyValueFactory<>("numOfLoansAsBorrower"));

        TableColumn<CustomerDTOs, String> loansAsLender = new TableColumn<>("Loans as lender");
        loansAsLender.setCellValueFactory(new PropertyValueFactory<>("numOfLoansAsLender"));

        if(CustomerData.getColumns().isEmpty())
            CustomerData.getColumns().addAll(expanderColumn, nameOfCustomer, balance, loansAsLoaner, loansAsLender);
        CustomerData.getItems().addAll(FXCollections.observableArrayList(allCustomers));

    }

    private GridPane expandCustomerInfo(TableRowExpanderColumn.TableRowDataFeatures<CustomerDTOs> param) throws IOException {
        GridPane workSpace = new GridPane();
        workSpace.setHgap(10);
        workSpace.setVgap(5);

        CustomerDTOs customer = param.getValue();

        FXMLLoader loader = new FXMLLoader();
        URL CustomerViewFXML = getClass().getResource(BankResourcesConstants.VIEWCUSTOMERDATAEXPANDED_RESOURCE_IDENTIFIRE);
        loader.setLocation(CustomerViewFXML);
        GridPane CustomerExpandedDetails = loader.load();
        ViewCustomersInfoController customersInfoController = loader.getController();
        CustomerExpandedDetails.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));

        customersInfoController.SetLoansAsLenderByStatusLabels(mainController.getSystemCustomerLoansByListOfLoansName(customer.getLoansAsALender()));
        customersInfoController.SetLoansAsLoanerByStatusLabels(mainController.getSystemCustomerLoansByListOfLoansName(customer.getLoansAsABorrower()));//TODO: change the map

        return CustomerExpandedDetails;
    }

    @FXML private void clickOnIncreaseYaz(ActionEvent event){
        mainController.increaseYazActivation();
    }


}
