package infiniteBuying.infiniteBuyingUpbit.domain;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


//무한매수법 구현
public class infiniteBuyingLogic {

    public static String batch(Member member) {
        for (Coin coin : member.coins.values()) {
            System.out.println("coinName : " + coin.getCoinName() + "period : " + coin.getCurrentPeriod());

            //리셋 주기가 넘었다면 리셋
            if (coin.getRemainingAmount() < coin.getBuyingAmount()) {
                System.out.println("reset " + coin.getCoinName());
                reset(member, coin);
            }


            //period 1 추가
            coin.setCurrentPeriod(coin.getCurrentPeriod() + 1);
        }
        return null;
    }

    //일정 할당량을 구매한다.
    public static void buyQuota(Member member, Coin coin) {
        UpbitUtils.postOrders(member, coin.getCoinName(), true, "", Integer.toString(coin.getBuyingAmount()), true);
    }

    //현재 가격이 평균단가보다 낮을 경우 추가 구매한다.
    public static void buyIfLessThanAveragePrice(Member member, Coin coin) {
        return;
    }

    //리셋 주기가 넘은 코인들을 리셋한다.
    public static void reset(Member member, Coin coin){
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //주기 초기화
        coin.setCurrentPeriod(0);
        //member에 코인 정보 리셋
        coin.resetCoinInfo();
    }
}
