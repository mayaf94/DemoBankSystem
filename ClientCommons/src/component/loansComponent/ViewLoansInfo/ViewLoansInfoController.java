package component.loansComponent.ViewLoansInfo;

//import BankActions.Payment;
//import Component.MainComponent.BankController;
import DTOs.LoanDTOs;
import clientController.ClientController;
import common.BankResourcesConstants;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.controlsfx.control.table.TableRowExpanderColumn;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ViewLoansInfoController implements Serializable {
    @FXML private GridPane LoansView;
    @FXML private Label PaymentFrequency;
    @FXML private Label intrest;
    @FXML private Label OriginalIntrestAmount;
    @FXML private Label TotalDuration;
    @FXML private Label originalAmount;
    @FXML private Label status;
    //@FXML private Label LoanDataByStatus;
    @FXML
    private TextArea LoanDataByStatus;
    @FXML private Accordion PaymentsDetails;
    @FXML private Accordion LenderDetails;
    ClientController clientController;

    public void setMainController(ClientController i_clientController) {this.clientController = i_clientController;}

    public void buildLoansTableView(TableView<LoanDTOs> i_LoansData){
        i_LoansData.getItems().clear();
        i_LoansData.refresh();
        TableRowExpanderColumn<LoanDTOs> expanderColumn = new TableRowExpanderColumn<>(param -> {
            try {
                return expandLoanInfo(param);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        expanderColumn.setPrefWidth(45);

        TableColumn<LoanDTOs, String> nameOfLoan = new TableColumn<>("Name of Loan");
        nameOfLoan.setCellValueFactory(new PropertyValueFactory<>("nameOfLoan"));
        nameOfLoan.setPrefWidth(125);


        TableColumn<LoanDTOs, String> nameOfLoaner = new TableColumn<>("Name of loaner");
        nameOfLoaner.setCellValueFactory(new PropertyValueFactory<>("nameOfLoaner"));
        nameOfLoaner.setPrefWidth(125);

        TableColumn<LoanDTOs, String> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<LoanDTOs, String> category = new TableColumn<>("Category");
        category.setCellValueFactory(new PropertyValueFactory<>("category"));

        if(i_LoansData.getColumns().isEmpty())
            i_LoansData.getColumns().addAll(expanderColumn, nameOfLoan, nameOfLoaner, category, status);
    }

    private AnchorPane expandLoanInfo(TableRowExpanderColumn.TableRowDataFeatures<LoanDTOs> param) throws IOException {
        GridPane workSpace = new GridPane();
        workSpace.setHgap(10);
        workSpace.setVgap(5);

        LoanDTOs loan = param.getValue();

        String originalAmount = Integer.toString(loan.getOriginalAmount());
        String paymentFrequency = Integer.toString(loan.getPaymentFrequency());
        String interest = Integer.toString(loan.getInterest());
        String theTotalInterestAmountOfTheLoan =Float.
                toString(loan.getTheAmountOfThePrincipalPaymentPaidOnTheLoanSoFar()
                        + loan.getTheInterestYetToBePaidOnTheLoan());
        String duration = Integer.toString(loan.getDurationOfTheLoan());
        String status = loan.getStatusName();

        FXMLLoader loader = new FXMLLoader();
        URL LoansViewFXML = getClass().getResource(BankResourcesConstants.VIEWLOANSDETAILEXPANDED_RESOURCE_IDENTIFIRE);
        loader.setLocation(LoansViewFXML);
        AnchorPane LoanExpandedDetails = loader.load();
        ViewLoansInfoController expendedLoansDetailsController = loader.getController();
        LoanExpandedDetails.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));

        expendedLoansDetailsController.setLoanDataByStatus(loan);
        expendedLoansDetailsController.setInterestValue(interest);
        expendedLoansDetailsController.setOriginalAmountValue(originalAmount);
        expendedLoansDetailsController.setPaymentFrequencyValue(paymentFrequency);
        expendedLoansDetailsController.setTotalDurationValue(duration);
        expendedLoansDetailsController.setOriginalInterestAmountValue(theTotalInterestAmountOfTheLoan);
        //expendedLoansDetailsController.setStatusValue(status, loan.getNameOfLoan(), mainController);
        expendedLoansDetailsController.setLenderDetails(loan);
        //expendedLoansDetailsController.setPaymentsDetails(loan);//TODO  make it work with payment Dto

        return LoanExpandedDetails;
    }

    public void setPaymentFrequencyValue(String paymentFrequencyVal) {
        PaymentFrequency.setText("Payment Frequency: " + paymentFrequencyVal);
    }

    public void setInterestValue(String i_interestVal) {
        intrest.setText("Interest: " + i_interestVal);
    }

    public void setOriginalInterestAmountValue(String i_originalInterestAmountVal) {
        OriginalIntrestAmount.setText("Total interest to be paid: " + i_originalInterestAmountVal);
    }

    public void setTotalDurationValue(String i_totalDurationVal) {
        TotalDuration.setText("Total Duration: " + i_totalDurationVal);
    }

    public void setOriginalAmountValue(String i_originalAmountVal) {
        originalAmount.setText("The original amount: " + i_originalAmountVal);
    }

    public void setLenderDetails(LoanDTOs  Loan) {
        Map<String,Integer> listOfLenders =  Loan.getListOfLenders();
        TitledPane[] lenders = new TitledPane[listOfLenders.size()];
        ScrollPane scroll=new ScrollPane();
        scroll.setPrefHeight(LenderDetails.getHeight());
        scroll.prefWidth(LenderDetails.getWidth());

        int counter = 0;
        for (Map.Entry<String, Integer> entry : listOfLenders.entrySet()){
            Label curLabel = new Label("Share in loan: " + entry.getValue().toString());
            GridPane grid = new GridPane();
            grid.setVgap(4);
            grid.setPadding(new Insets(5, 5, 5, 5));
            grid.add(new Label("Share in loan: " + entry.getValue().toString()), 0, 0);
            lenders[counter] = new TitledPane(entry.getKey(),grid);
            lenders[counter].setMinSize(50,50);
            counter++;
        }
        LenderDetails.getPanes().addAll(lenders);
    }

/*    public void setPaymentsDetails(LoanDTOs Loan){//TODO change to payment dto and make it work
        List<Payment> listOfPayments = Loan.getLoansPayments();
        TitledPane[] Payments = new TitledPane[listOfPayments.size()];
        int counter = 0;
        for(Payment curPayment : listOfPayments){
            GridPane grid = new GridPane();
            grid.setVgap(4);
            grid.setPadding(new Insets(5, 5, 5, 5));
            Label tmp = new Label(curPayment.PaymentDetails());
            grid.add(new Label(curPayment.PaymentDetails()), 0, 0);
            grid.setMinHeight(tmp.getMinHeight());
            grid.setMinSize(500,500);
            Payments[counter] = new TitledPane(Integer.toString(curPayment.getPaymentDate()),new TextArea(curPayment.PaymentDetails()));
            if(!curPayment.getPaid()) {
                Payments[counter].getContent().setStyle("-fx-background-color: red; -fx-text-fill: red");
                Payments[counter].setStyle("-fx-color: red");
            }
            Payments[counter].setMinSize(100,100);
            counter++;
        }
        PaymentsDetails.getPanes().addAll(Payments);

    }*/

    public void setLoanDataByStatus(LoanDTOs loan){
        LoanDataByStatus.setText("");
        status.setText("");
        switch (loan.getStatusName()){
            case "NEW":
                status.setText("NEW");
                LoanDataByStatus.setText("0$ Raised so far.");
                break;
            case "PENDING":
                status.setText("PENDING");
                LoanDataByStatus.setText(loan.getAmountRaisedSoFar() + "$ Raised so far, \n" + loan.getHowMuchLeftToMakeLoanActive() + "$ left to raise to make the Loan Active.\n");
                break;
            case "ACTIVE":
                status.setText("ACTIVE");
                LoanDataByStatus.setText("Start yaz " + loan.getTheDateTheLoanBecameActive() +  "\n"
                        + "next payment yaz is " + loan.getNextYazPayment() + "\n" + "The total principal paid so far : " + loan.getTheAmountOfThePrincipalPaymentPaidOnTheLoanSoFar() + "\n"
                + "The total interest paid so far : " + loan.getInterestPayedSoFar() + "\n" + "The principal yet to be paid: " + loan.getTheAmountOfTheFundYetToBePaid() + "\n"
                + "The interest yet to be paid: " + loan.getTheInterestYetToBePaidOnTheLoan());
                break;
            case "RISK":
                status.setText("RISK");
                LoanDataByStatus.setText("the loan became active on yaz " + loan.getTheDateTheLoanBecameActive() +  "\n"
                        + "next payment yaz is " + loan.getNextYazPayment() + "\n" + "The total principal paid so far : " + loan.getTheAmountOfThePrincipalPaymentPaidOnTheLoanSoFar() + "\n"
                        + "The total interest paid so far : " + loan.getInterestPayedSoFar() + "\n" + "The principal yet to be paid: " + loan.getTheAmountOfTheFundYetToBePaid() + "\n"
                        + "The interest yet to be paid: " + loan.getTheInterestYetToBePaidOnTheLoan() + "\n" + "total missed payments: " + loan.getTotalMissedYazNeedToPayBack() + "\n" + "Debt: " + loan.getDebt());
                break;
            case "FINISHED":
                status.setText("FINISHED");
                LoanDataByStatus.setText("Start yaz: " + loan.getTheDateTheLoanBecameActive() + "\n" +
                "End yaz: " + loan.getEndDate());
                break;
            default:
                break;

        }
    }
}

