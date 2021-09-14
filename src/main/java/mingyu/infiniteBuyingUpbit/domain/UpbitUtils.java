package mingyu.infiniteBuyingUpbit.domain;

public class UpbitUtils {
    public static double getPriceUnit(int price){
        /*
        업비트의 경우 호가단위가 아래와 같다(21년 4월 21일 기준).
        2,000,000원 이상 : 1,000원
        1,000,000원 이상~2,000,000원 미만 : 500원
        100,000원 이상~1,000,000원 미만 : 50원
        10,000원 이상~100,000원 미만 : 10원
        1,000원 이상~10,000원 : 5원
        1,000원 미만 : 1원
        100원 : 0.1원
        10원 미만 : 0.01원
         */
        if(price >= 2000000) return 1000;
        if(price >= 1000000) return 500;
        if(price >= 100000) return 50;
        if(price >= 10000) return 10;
        if(price >= 1000) return 5;
        if(price >= 100) return 1;
        if(price >= 10) return 0.1;
        return 0.01;
    }
    //호가 단위에 가격을 맞춘다.
    public static int setPriceToUnit(int price){
        /*
        ex) 호가단위가 5원일시, 매수가격 2552원에 매수주문을 넣지 못한다. 2550원이나 2555로 단위를 맞춰서 주문을 넣어야 됨. 이를 맞춰 주는 함수
        price : 단위를 맞추려는 가격
        */
        double unit = UpbitUtils.getPriceUnit(price);
        return (int) (Math.floor(price / unit) * unit);
    }

}
