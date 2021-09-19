package infiniteBuying.infiniteBuyingUpbit.domain;

import java.util.ArrayList;

public class Coin {
    public String coinName;
    private int buyingAmount; //1회 구매량
    private int totalAmount; //설정된 코인 전체 구매량 상한(KRW).
    private int remainingAmount; //설정된 구매 목표 총량에서 현재 남은 잔량(KRW). 이 값이 0이 되면 리셋함.
    private int currentPeriod; //시행일수
    private String currentAmount; //현재 코인 구매량(코인 단위) - ex) 0.01
    public ArrayList<Order> buyOrders = new ArrayList<Order>();
    public ArrayList<Order> sellOrders = new ArrayList<Order>();





    public Coin() {
        setRemainingAmount(0);
    }

    public void resetCoinInfo() {
        setRemainingAmount(getTotalAmount());
        buyOrders = new ArrayList<Order>();
        sellOrders = new ArrayList<Order>();
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


    public String getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(String currentAmount) {
        this.currentAmount = currentAmount;
    }
    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(int remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public int getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(int currentPeriod) {
        this.currentPeriod = currentPeriod;
    }
}
