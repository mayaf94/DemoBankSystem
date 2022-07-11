package DTOs;

public class BankSystemDTO {
    private Integer curYaz;
    private String msg;

    public BankSystemDTO(Integer curYaz, String msg) {
        this.curYaz = curYaz;
        this.msg = msg;
    }

    public Integer getCurYaz() {
        return curYaz;
    }

    public String getMsg() {
        return msg;
    }
}
