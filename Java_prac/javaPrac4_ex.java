/*-< 演習：Ex1_05_1 >-------------------------------------------------------------------
以下①～⑤のデータを格納する変数を定義したい。
それぞれに適した型で変数宣言&初期化を行ってください。
なお、変数名はJavaの一般的に規則に従っていることはもちろん、
「どのようなデータが格納されているか一目でわかる名前」になるよう意識してつけてください。
① IT業界の生涯賃金（調べてください）
② IT業界の市場規模（調べてください）
③ 自分の好きな漢字1文字 ※char型を使いましょう
④ 自分のなりたい職業
⑤ 自分が既卒かどうか ※boolean型を使いましょう
--------------------------------------------------------------------------------------*/
class Ex1_05_1 {
	public static void main (String[] args) {
        int annual_income = 60000000; //IT業界の生涯賃金(調べてください)
        System.out.println("IT業界の生涯賃金は" + annual_income + "円");

        String market_size = "13兆5,500億円";
        System.out.println("IT業界の市場規模は" + market_size + "円");

		char favorite_kanji = '残';
        System.out.println("自分の好きな漢字は" + favorite_kanji + "です");

        String career = "プログラマー";
        System.out.println("自分のなりたい職業は" + career + "です");

        boolean graduate = true;
        if (graduate == true){
            System.out.println("自分は既卒です。");
        }
        else{
            System.out.println("自分は既卒ではありません。");
        }
		


		
		
		
		
		
		
		
		
	}
}