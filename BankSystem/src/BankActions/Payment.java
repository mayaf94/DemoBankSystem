package BankActions;

import java.io.Serializable;

public class Payment  implements Serializable {
    private int paymentDate;
    private int principalPayment;
    private int interestPayment;
    private int totalAmountPaid;
    private Boolean paid;

    public Payment(int paymentDate, int principalPayment, int interestComponent, int totalAmountPaid,boolean paid) {
        this.paymentDate = paymentDate;
        this.principalPayment = principalPayment;
        this.interestPayment = interestComponent;
        this.totalAmountPaid = totalAmountPaid;
        this.paid = paid;
    }

    public int getPaymentDate() {
        return paymentDate;
    }

    public int getPrincipalPayment() {
        return principalPayment;
    }

    public int getInterestPayment() {
        return interestPayment;
    }

    public int getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public Boolean getPaid() {
        return paid;
    }

    public String PaymentDetails()
    {
        String res = (
                "The amount without the interest: " + principalPayment + "\n" +
                "The interest component: " + interestPayment +  "\n" +
                "The total amount is :" + totalAmountPaid + "\n");
        return res;
    }

    public void printPaymentTotalAmount(){
        System.out.println("The payment at yaz: " + paymentDate);
        System.out.println("The total amount paid: " + totalAmountPaid);
    }
}
