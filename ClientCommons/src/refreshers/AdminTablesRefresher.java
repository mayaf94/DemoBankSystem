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
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static util.Constants.GSON_INSTANCE;

public class AdminTablesRefresher extends TimerTask {
    private final Consumer<List<LoanDTOs>> loansTableUpdate;
    private final Consumer<List<CustomerDTOs>> customersTableUpdate;
    private final Boolean shouldUpdate;


    public AdminTablesRefresher(Boolean isRewind, Consumer<List<LoanDTOs>> listOfLoans, Consumer<List<CustomerDTOs>> customerUpdate) {
        this.shouldUpdate = isRewind;
        this.loansTableUpdate = listOfLoans;
        this.customersTableUpdate = customerUpdate;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                    .parse(Constants.refreshAdminTables)
                    .newBuilder()
                    .addQueryParameter("isRewind", shouldUpdate.toString())
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
                loansTableUpdate.accept(bankSystemDTO.getLoansInBank());
                customersTableUpdate.accept(bankSystemDTO.getCustomers());
            }
        });
    }
}
