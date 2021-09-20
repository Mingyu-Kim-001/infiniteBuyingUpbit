package infiniteBuying.infiniteBuyingUpbit.domain;

public class Asset {
    private String currency;
    private double balance;
    private double locked;
    private double avg_buy_price;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getLocked() {
        return locked;
    }

    public void setLocked(double locked) {
        this.locked = locked;
    }

    public double getAvg_buy_price() {
        return avg_buy_price;
    }

    public void setAvg_buy_price(double avg_buy_price) {
        this.avg_buy_price = avg_buy_price;
    }
}
