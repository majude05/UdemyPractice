import ex.Ex1_16_1b;
import ex.Ex1_16_1c;

public class Ex1_16_1a {
    public static void main (String[] args) {

        int totalListPrice = 0 ;                                //購入した商品の合計金額（定価）

        //コマンドライン引数から購入した商品の合計金額（定価）を取得
        for(int i = 0 ; i < args.length ; i++ ){
            totalListPrice += Integer.parseInt( args[i] );
        }

        //Ex1_16_1bクラスのcalcTaxPriceメソッドを使って購入した商品の合計金額（税込）を取得
        int totalTaxPrice = Ex1_16_1b.calcTaxPrice( totalListPrice );  //購入した商品の合計金額（税込）

        //Ex1_16_1cクラスのdiscountメソッドを使って割引後の支払金額を取得
        int payPrice = Ex1_16_1c.discount( totalTaxPrice );            //割引後の支払金額

        //割引後の支払金額を表示
        System.out.println("割引後の支払金額：" + payPrice + "円" );

    }
}