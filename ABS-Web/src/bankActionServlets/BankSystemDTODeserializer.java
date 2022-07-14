package bankActionServlets;

import DTOs.*;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class BankSystemDTODeserializer implements JsonDeserializer<BankSystemDTO> {
    @Override
    public BankSystemDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        // extract raw data
        Integer curYaz = json.getAsJsonObject().get("curYaz").getAsInt();
        String msg = json.getAsJsonObject().get("msg").getAsString();
        //List<CustomerDTOs> Customers = context.deserialize(json.getAsJsonObject().get("Customers"), CustomerDTOs.class);
        List<LoanDTOs> LoansInBank = context.deserialize(json.getAsJsonObject().get("LoansInBank"), LoanDTOs.class);
        CategoriesDTO categories = context.deserialize(json.getAsJsonObject().get("categories"), CategoriesDTO.class);
        Boolean isRewind = json.getAsJsonObject().get("isRewind").getAsBoolean();

        // build object manually
        BankSystemDTO result = new BankSystemDTO();
        result.setCurYaz(curYaz);
        result.setMsg(msg);
        //result.setCustomers(Customers);
        result.setLoansInBank(LoansInBank);
        result.setCategories(categories);
        result.setRewind(isRewind);

        return result;
    }
}

