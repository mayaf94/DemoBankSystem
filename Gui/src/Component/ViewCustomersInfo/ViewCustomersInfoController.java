package Component.ViewCustomersInfo;
import DTOs.LoanDTOs;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ViewCustomersInfoController implements Serializable {

    @FXML private Label LoansAsLoaner;
    @FXML private Label LoansAsLender;

    //num of loans in status for customer as loaner
    @FXML private Label PendingAsLoaner;
    @FXML private Label activeAsLoaner;
    @FXML private Label riskAsLoaner;
    @FXML private Label finishedAsLoaner;
    @FXML private Label newAsLender;

    //num of loans in status for customer as lender
    @FXML private Label pendingAsLender;
    @FXML private Label activeAsLender;
    @FXML private Label riskAsLender;
    @FXML private Label finishedAsLender;
    @FXML private Label newAsLoaner;


    public void SetLoansAsLoanerByStatusLabels(List<LoanDTOs> loansAsLoaner){
        if(loansAsLoaner.size() != 0) {
            Map<String, List<LoanDTOs>> LoansByStatus = loansAsLoaner
                    .stream()
                    .collect(
                            Collectors.groupingBy(LoanDTOs::getStatusName)
                    );
            int pendingNum,activenNum,riskNum,FinishedNum,NewNum;
            pendingNum = activenNum = riskNum = FinishedNum = NewNum = 0;
            if(LoansByStatus.containsKey("PENDING")){
                pendingNum = LoansByStatus.get("PENDING").size();
            }
            if(LoansByStatus.containsKey("NEW")){
                NewNum = LoansByStatus.get("NEW").size();
            }
            if(LoansByStatus.containsKey("FINISHED")){
                FinishedNum = LoansByStatus.get("FINISHED").size();
            }
            if(LoansByStatus.containsKey("ACTIVE")){
                activenNum = LoansByStatus.get("ACTIVE").size();
            }
            if(LoansByStatus.containsKey("RISK")){
                riskNum = LoansByStatus.get("RISK").size();
            }

            newAsLoaner.setText("New: " + NewNum);
            PendingAsLoaner.setText("Pending: " + pendingNum);
            activeAsLoaner.setText("Active: " + activenNum);
            riskAsLoaner.setText("Risk : " + riskNum);
            finishedAsLoaner.setText("Finished: " + FinishedNum);
        }
    }

    public void SetLoansAsLenderByStatusLabels(List<LoanDTOs> loansAsLender){//TODO: list of propertys that will have the amount of loans per status that will be baind to the label
        if(loansAsLender.size() != 0) {
            Map<String, List<LoanDTOs>> LoansByStatus = loansAsLender
                    .stream()
                    .collect(
                            Collectors.groupingBy(LoanDTOs::getStatusName)
                    );
            int pendingNum,activenNum,riskNum,FinishedNum,NewNum;
            pendingNum = activenNum = riskNum = FinishedNum = NewNum = 0;
            if(LoansByStatus.containsKey("PENDING")){
                pendingNum += LoansByStatus.get("PENDING").size();
            }
            if(LoansByStatus.containsKey("NEW")){
                NewNum += LoansByStatus.get("NEW").size();
            }
            if(LoansByStatus.containsKey("FINISHED")){
                FinishedNum += LoansByStatus.get("FINISHED").size();
            }
            if(LoansByStatus.containsKey("ACTIVE")){
                activenNum += LoansByStatus.get("ACTIVE").size();
            }
            if(LoansByStatus.containsKey("RISK")){
                riskNum += LoansByStatus.get("RISK").size();
            }

            pendingAsLender.setText("Pending: " + pendingNum);
            activeAsLender.setText("Active: " + activenNum);
            riskAsLender.setText("Risk : " + riskNum);
            finishedAsLender.setText("Finished: " + FinishedNum);
            newAsLender.setText("New: " + NewNum);
           // newAsLender.textProperty().bind(propertyMapToBind.get(LoanStatus.NEW));
        }
/*        pendingAsLender.textProperty().bind(propertyMapToBind.get(LoanStatus.PENDING));
        activeAsLender.textProperty().bind(propertyMapToBind.get(LoanStatus.ACTIVE));
        riskAsLender.textProperty().bind(propertyMapToBind.get(LoanStatus.RISK));
        finishedAsLender.textProperty().bind(propertyMapToBind.get(LoanStatus.FINISHED));
        newAsLender.textProperty().bind(propertyMapToBind.get(LoanStatus.NEW));*/

    }

}
