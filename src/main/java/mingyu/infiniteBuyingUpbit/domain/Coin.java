package mingyu.infiniteBuyingUpbit.domain;

import java.util.ArrayList;

public class Coin {
    public String coinName;
    private int buyingAmount;
    private int resetPeriod;
    private int currentPeriod;

    public ArrayList<Order> buyOrders = new ArrayList<Order>();
    public ArrayList<Order> sellOrders = new ArrayList<Order>();



    private String currentAmount;

    public Coin() {
        setResetPeriod(40);
        setCurrentPeriod(0);
    }

    public int getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(int currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public int getBuyingAmount() {
        return buyingAmount;
    }

    public void setBuyingAmount(int buyingAmount) {
        this.buyingAmount = buyingAmount;
    }

    public int getResetPeriod() {
        return resetPeriod;
    }

    public void setResetPeriod(int resetPeriod) {
        this.resetPeriod = resetPeriod;
    }
    public String getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(String currentAmount) {
        this.currentAmount = currentAmount;
    }

}
