package infiniteBuying.infiniteBuyingUpbit.domain;

import java.util.ArrayList;

public class Coin {
    public String coinName;
    private int minimumBuying; //1일 최소 구매량(KRW). 하루에 최소 이만큼은 구매하게 된다.
    private int totalBudget; //설정된 코인 전체 구매량 상한(KRW).
    private int remainingBudget; //설정된 구매 목표 총량에서 현재 남은 잔량(KRW). 이 값이 minimumBuying값보다 작아지면 리셋.
    private int currentPeriod; //시행일수
    private double currentAmount; //현재 코인 구매량(코인 단위) - ex) 0.01
    private ArrayList<Order> buyOrders = new ArrayList<Order>();
    private ArrayList<Order> sellOrders = new ArrayList<Order>();

    public ArrayList<Order> getBuyOrders() {
        return buyOrders;
    }

    public ArrayList<Order> getSellOrders() {
        return sellOrders;
    }


    public void resetCoinInfo() {
        setCurrentAmount(0.0);
        setRemainingBudget(getTotalBudget());
        buyOrders = new ArrayList<Order>();
        sellOrders = new ArrayList<Order>();
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public int getMinimumBuying() {
        return minimumBuying;
    }

    public void setMinimumBuying(int minimumBuying) {
        this.minimumBuying = minimumBuying;
    }


    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public int getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(int totalBudget) {
        this.totalBudget = totalBudget;
    }

    public int getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(int remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public int getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(int currentPeriod) {
        this.currentPeriod = currentPeriod;
    }
}
