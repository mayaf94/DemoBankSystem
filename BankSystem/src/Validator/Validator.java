package Validator;

import SystemExceptions.InccorectInputType;

import java.io.Serializable;

public class Validator implements Serializable {
    public static void isTheStringANumber(String userInput) throws InccorectInputType {
        int userAnswerInInt = Integer.parseInt(userInput);
    }

    public static boolean checkIfTheBalanceIsEnoughForInvesment(int num, int accountBalance) {
        if (num > accountBalance || num < 0) {
            return false;
        }
        return true;
    }

    public static void CheckIfChoicesIsInRange(int[] nums, int maxChoices) throws InccorectInputType {
        for (int num : nums) {
            if (!(num <= maxChoices && num >= 0)) {
                    throw new InccorectInputType("");
            }
            if (num == 0 && !(nums.length == 1)) {
                throw new InccorectInputType("");
            }
        }
    }

    public static void CheckIfGreaterThenZero(int num,boolean canBeZero) throws InccorectInputType {
        if(num < 0){
            throw new InccorectInputType(InccorectInputType.getGreaterThenZero());
        }
        if(num == 0 && !canBeZero){
            throw new InccorectInputType(InccorectInputType.getGreaterThenZero());
        }
    }
}
