package mingyu.infiniteBuyingUpbit.domain;
import java.util.ArrayList;


//무한매수법 구현
public class infiniteBuyingLogic {

    public String batch(Member member) {
        ArrayList<Coin> coins = member.coins;
        for (Coin coin : coins) {
            int buyingAmount = coin.getBuyingAmount();

        }
    }

    //
    public void reset(Member member, Coin coin){
        coin.setCurrentPeriod(0);
        try {
            //모든 매수, 매도 주문 취소
            for (Order order : coin.buyOrders) {
                UpbitUtils.deleteOrder(member, order);
            }
            for (Order order : coin.sellOrders) {
                UpbitUtils.deleteOrder(member, order);
            }

            //매도 후 처리까지 1초 대기
            Thread.sleep(1000);

            //모든 코인 매도
            UpbitUtils.postOrders(member, coin.getCoinName(), false, coin.getCurrentAmount(), "", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
