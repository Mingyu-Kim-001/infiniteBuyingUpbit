package infiniteBuying.infiniteBuyingUpbit.domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


//무한매수법 구현
@Component
public class infiniteBuyingLogic {
    private final Member member;
    @Autowired
    public infiniteBuyingLogic(Member member) {
        this.member = member;
    }

    //하루에 한 번씩 매수 로직에 따라 매수한다.
    @Scheduled(cron = "0 30 00 * * *")
    public String batch() {
        System.out.println("batch");
        for (Coin coin : member.getCoins().values()) {
            if(!coin.isActivated()){
                continue;
            }
            System.out.println("coinName : " + coin.getCoinName() + " period : " + coin.getCurrentPeriod() + " start");
            System.out.println("remaining Budget " + coin.getRemainingBudget() + " minimum buying " + coin.getMinimumBuying());

            //리셋 주기가 넘었다면 리셋
            if (coin.getRemainingBudget() < coin.getMinimumBuying()) {
                System.out.println("reset " + coin.getCoinName());
                reset(member, coin);
            }

            //코인 구매
            buyQuota(member, coin);
            if (coin.getCurrentPeriod() > 0) {
                buyIfLessThanAveragePrice(member, coin);
            }

        //period 1 추가
        coin.setCurrentPeriod(coin.getCurrentPeriod() + 1);
    }
        return null;
}

    //일일 최소 할당량을 매수한다. 첫 주기 때는, 한번에 일 최대량(최소량의 두 배)을 매수한다.
    public static void buyQuota(Member member, Coin coin) {
        double buyingAmount = UpbitUtils.setPriceToUnit(coin.getMinimumBuying()) * (coin.getCurrentPeriod() == 0 ? 1 : 2);
        String result = UpbitUtils.postOrders(member, coin.getCoinName(), true, "", Double.toString(buyingAmount), true);
        System.out.println(result);
        Order buyOrder = new Order(result);

        //주문 후 처리까지 3초 대기
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(UpbitUtils.getOrder(member, buyOrder));
        System.out.println("buyQuota " + coin.getCoinName() + " at price " + buyOrder.getPrice() + " and volume " + buyOrder.getExecuted_volume() + " uuid " );

        //성사된 거래 업데이트
        buyOrder = UpbitUtils.updateOrder(member, buyOrder);
        System.out.println("buyQuota " + coin.getCoinName() + " at price " + buyOrder.getPrice() + " and volume " + buyOrder.getExecuted_volume() + " uuid " );
        coin.setCurrentVolume(coin.getCurrentVolume() + Double.parseDouble(buyOrder.getExecuted_volume()));
        coin.setRemainingBudget(coin.getRemainingBudget() - Double.parseDouble(buyOrder.getPrice()));
    }

    //현재 가격이 평균단가보다 낮을 경우 추가 구매한다.
    public static void buyIfLessThanAveragePrice(Member member, Coin coin) {
        double avgPrice = (coin.getTotalBudget() - coin.getRemainingBudget()) / coin.getCurrentVolume();
        double currentPrice = UpbitUtils.getCurrentPrice(coin.getCoinName());
        double margin = UpbitUtils.getPriceUnit(currentPrice) * 4; //시차 등을 감안하여 계산시 어느 정도의 마진을 둔다.
        double buyingAmount = UpbitUtils.setPriceToUnit(coin.getMinimumBuying());
        if (avgPrice > currentPrice - margin) {
            String result = UpbitUtils.postOrders(member, coin.getCoinName(), true, "", Double.toString(buyingAmount), true);
            Order buyOrder = new Order(result);

            //주문 후 처리까지 3초 대기
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //성사된 거래 업데이트
            buyOrder = UpbitUtils.updateOrder(member, buyOrder);

            System.out.println("buyIfLessThanAveragePrice " + coin.getCoinName() + " at price " + buyOrder.getPrice() + " and volume " + buyOrder.getExecuted_volume());
            coin.setCurrentVolume(coin.getCurrentVolume() + Double.parseDouble(buyOrder.getExecuted_volume()));
            coin.setRemainingBudget(coin.getRemainingBudget() - Double.parseDouble(buyOrder.getPrice()));
        }

    }

    //리셋 주기가 넘은 코인들을 리셋한다.
    public static void reset(Member member, Coin coin){
        try {
            //모든 매수, 매도 주문 취소
            for (Order order : coin.getBuyOrders()) {
                UpbitUtils.deleteOrder(member, order);
            }
            for (Order order : coin.getSellOrders()) {
                UpbitUtils.deleteOrder(member, order);
            }

            //매도 후 처리까지 1초 대기
            Thread.sleep(1000);

            //모든 코인 매도
            UpbitUtils.postOrders(member, coin.getCoinName(), false, Double.toString(coin.getCurrentVolume()), "", true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //member에 코인 정보 리셋
        coin.resetCoinInfo();
    }


}
