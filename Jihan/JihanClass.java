package Jihan;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class JihanClass {
    // 使用可能なお金の種類を定義します
    private static final int[] VALID_MONEY = {1, 5, 10, 50, 100, 500, 1000, 5000, 10000};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int totalAmount = 0; // 投入された合計金額
        List<Integer> insertedMoney = new ArrayList<>(); // 投入されたお金のリスト

        System.out.println("自動販売機へようこそ！");
        System.out.println("お金を投入してください。投入を終了する場合は「0」を入力してください。");
        System.out.println("利用可能なお金: 1円, 5円, 10円, 50円, 100円, 500円, 1000円, 5000円, 10000円");

        while (true) {
            System.out.print("投入するお金 (終了は0): ");
            if (scanner.hasNextInt()) {
                int money = scanner.nextInt();

                if (money == 0) {
                    break; // 投入終了
                }

                boolean isValidMoney = false;
                for (int valid : VALID_MONEY) {
                    if (money == valid) {
                        isValidMoney = true;
                        break;
                    }
                }
                
                if (money == 1 || money == 5) {
                    System.out.println("1円または5円は投入できません。");
                }else if (isValidMoney) {
                    totalAmount += money;
                    insertedMoney.add(money);
                    System.out.println(money + "円が投入されました。現在の合計金額: " + totalAmount + "円");
                } else {
                    System.out.println("無効なお金です。指定されたお金を投入してください。");
                }
            } else {
                System.out.println("数値を入力してください。");
                scanner.next(); // 不正な入力を消費
            }
        }

        System.out.println("お金の投入を終了しました。");
        System.out.println("投入された合計金額: " + totalAmount + "円");
        System.out.println("投入されたお金の内訳: " + insertedMoney);
        scanner.close();
    }
}

/*
作成したプロンプト
Javaを使用して演習課題にとりくみたいです。
演習で取り組むのは自販機プログラムです。
使用できるお金は
1円、5円、10円、50円、100円、500円、1000円、5000円、10000円
です。
package jihan;
public class jihan_class {
    public static void main(String[] args) {
    }
}
この枠組みの中にコードを組み込んで全文生成してください。

 */