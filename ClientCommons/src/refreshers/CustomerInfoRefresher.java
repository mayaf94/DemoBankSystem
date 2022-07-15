package refreshers;

import DTOs.BankSystemDTO;
import DTOs.CustomerDTOs;
import DTOs.LoanDTOs;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;


public class CustomerInfoRefresher extends TimerTask {
    private final Consumer<List<LoanDTOs>> updateTableLoansAsLoaner;
    private final Consumer<List<LoanDTOs>> updateTableLoansAsLender;
    private final Consumer<List<LoanDTOs>> updateTableLoansToSellTable;
    private final Consumer<List<LoanDTOs>> updateTableLoansToBuyTable;
    private final Consumer<List<CustomerDTOs>> updateTableNotificationsView;
    private final Consumer<List<CustomerDTOs>> updateTransactionTable;
    private final Consumer<Integer> updateYazLB;
    private final Consumer<Boolean> disableBT;
    private final Consumer<List<String>> updateCategories;
    private final Consumer<List<CustomerDTOs>> updatebalanceLB;

    public CustomerInfoRefresher(Consumer<List<LoanDTOs>> updateTableLoansAsLoaner, Consumer<List<LoanDTOs>> updateTableLoansAsLender,
                                 Consumer<List<LoanDTOs>> updateTableLoansToSellTable, Consumer<List<LoanDTOs>> updateTableLoansToBuyTable,
                                 Consumer<List<CustomerDTOs>> updateTableNotificationsView, Consumer<List<CustomerDTOs>> updateTransactionTable,
                                 Consumer<Integer> updateYazLB, Consumer<Boolean> disableBT, Consumer<List<String>> updateCategories, Consumer<List<CustomerDTOs>> updatebalanceLB) {
        this.updateTableLoansAsLoaner = updateTableLoansAsLoaner;
        this.updateTableLoansAsLender = updateTableLoansAsLender;
        this.updateTableLoansToSellTable = updateTableLoansToSellTable;
        this.updateTableLoansToBuyTable = updateTableLoansToBuyTable;
        this.updateTableNotificationsView = updateTableNotificationsView;
        this.updateTransactionTable = updateTransactionTable;
        this.updateYazLB = updateYazLB;
        this.disableBT = disableBT;
        this.updateCategories = updateCategories;
        this.updatebalanceLB = updatebalanceLB;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.refreshTables)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonBankSystem = response.body().string();
                BankSystemDTO bankSystemDTO = GSON_INSTANCE.fromJson(jsonBankSystem, BankSystemDTO.class);

                updateTableLoansAsLoaner.accept(bankSystemDTO.getLoansInBank());
                updateTableLoansAsLender.accept(bankSystemDTO.getLoansInBank());
                updateTableLoansToSellTable.accept(bankSystemDTO.getLoansInBank());
                updateTableLoansToBuyTable.accept(bankSystemDTO.getLoansInBank());
                updateTableNotificationsView.accept(bankSystemDTO.getCustomers());
                updateTransactionTable.accept(bankSystemDTO.getCustomers());
                updateYazLB.accept(bankSystemDTO.getCurYaz());
                disableBT.accept(bankSystemDTO.getRewind());
                updatebalanceLB.accept(bankSystemDTO.getCustomers());
                updateCategories.accept(bankSystemDTO.getCategories().getCategories());
            }
        });
    }
}
