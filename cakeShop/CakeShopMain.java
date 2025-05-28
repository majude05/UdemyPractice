package cakeShop;

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap; // 商品の表示順を維持するために使用
import java.util.ArrayList;
import java.util.List;

public class CakeShopMain {

    // 商品情報を定義 (LinkedHashMap を使用して追加順を保持)
    private static final Map<String, Integer> PRODUCTS = new LinkedHashMap<>();
    static {
        PRODUCTS.put("ショートケーキ", 320);
        PRODUCTS.put("モンブラン", 350);
        PRODUCTS.put("チョコレートケーキ", 370);
        PRODUCTS.put("イチゴのタルト", 400);
        PRODUCTS.put("チーズケーキ", 300);
    }

    private static final double SALES_TAX_RATE = 0.08; // 消費税率8%
    private static final int DISCOUNT_THRESHOLD = 1000; // 割引が適用される金額の閾値 (円)
    private static final double DISCOUNT_PERCENTAGE = 0.20; // 割引率20% (2割引)

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // 注文情報を保持するMap (商品名, 個数)
        Map<String, Integer> order = new HashMap<>();

        System.out.println("🍰 ケーキ屋さんへようこそ！ご注文をどうぞ。 🍰");
        System.out.println("==========================================");

        // 商品リストの表示と番号付け
        System.out.println("--- 🌸 本日のケーキメニュー 🌸 ---");
        List<String> productNames = new ArrayList<>(PRODUCTS.keySet()); // 注文時の選択用に商品名のリストを作成
        for (int i = 0; i < productNames.size(); i++) {
            String productName = productNames.get(i);
            System.out.println((i + 1) + ". " + productName + ": " + PRODUCTS.get(productName) + "円");
        }
        System.out.println("----------------------------------");

        // 注文受付ループ
        while (true) {
            System.out.println("\n▶ ご注文のケーキの番号を入力してください (注文を終了する場合は「0」を入力):");
            String input = scanner.nextLine().trim(); // 入力値の前後の空白を除去
            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ 無効な入力です。番号で入力してください。");
                continue;
            }

            if (choice == 0) {
                if (order.isEmpty()) {
                    System.out.println("ご注文はありませんでした。またのご利用をお待ちしております。");
                    scanner.close();
                    return;
                }
                break; // 注文終了
            }

            if (choice < 1 || choice > productNames.size()) {
                System.out.println("⚠️ 無効な商品番号です。メニューの番号から選んでください。");
                continue;
            }

            String selectedCakeName = productNames.get(choice - 1);

            System.out.println("▶ " + selectedCakeName + " を何個ご注文されますか？ (個数を入力):");
            input = scanner.nextLine().trim();
            int quantity;

            try {
                quantity = Integer.parseInt(input);
                if (quantity <= 0) {
                    System.out.println("⚠️ 1個以上で入力してください。");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ 無効な入力です。数値で個数を入力してください。");
                continue;
            }

            // 注文情報を更新 (既にあれば個数を加算、なければ新規追加)
            order.put(selectedCakeName, order.getOrDefault(selectedCakeName, 0) + quantity);
            System.out.println("🛒 " + selectedCakeName + " " + quantity + "個をカートに追加しました。");
            System.out.print("現在のカート: ");
            if (order.isEmpty()) {
                System.out.println("空です");
            } else {
                List<String> cartItems = new ArrayList<>();
                for(Map.Entry<String, Integer> entry : order.entrySet()){
                    cartItems.add(entry.getKey() + " x" + entry.getValue());
                }
                System.out.println(String.join(", ", cartItems));
            }
        }

        // --- 会計処理 ---
        System.out.println("\n==========================================");
        System.out.println("📝 ご注文内容の確認とお会計");
        System.out.println("==========================================");

        // 税抜き合計金額の計算
        int subtotal = 0;
        for (Map.Entry<String, Integer> entry : order.entrySet()) {
            String cakeName = entry.getKey();
            int quantity = entry.getValue();
            int price = PRODUCTS.get(cakeName);
            int itemTotal = price * quantity;
            System.out.println(String.format("  %-15s %3d円 x %2d個 = %5d円", cakeName, price, quantity, itemTotal));
            subtotal += itemTotal;
        }
        System.out.println("------------------------------------------");
        System.out.println(String.format("小計: %29d円", subtotal));

        // 消費税の計算 (小数点以下切り捨て)
        int tax = (int) Math.floor(subtotal * SALES_TAX_RATE);
        System.out.println(String.format("消費税 (%.0f%%): %23d円", SALES_TAX_RATE * 100, tax));
        System.out.println("------------------------------------------");

        // 税込み合計金額 (割引前)
        int totalBeforeDiscount = subtotal + tax;

        // 割引の適用と最終会計金額の表示
        if (totalBeforeDiscount >= DISCOUNT_THRESHOLD) {
            System.out.println(String.format("お会計 %,d円です。", totalBeforeDiscount)); // 割引前金額
            System.out.println("🎉 本日は特売期間のため、お会計が２割引きになります！ 🎉");
            // 2割引後の金額を計算 (小数点以下切り捨て)
            int finalTotal = (int) Math.floor(totalBeforeDiscount * (1.0 - DISCOUNT_PERCENTAGE));
            System.out.println(String.format("お会計 %,d円です。", finalTotal)); // 割引後金額
        } else {
            System.out.println(String.format("お会計 %,d円です。", totalBeforeDiscount)); // 割引なし
        }

        System.out.println("\nありがとうございました！またのご来店をお待ちしております。🙇");
        scanner.close();
    }
}

/*注文を受け付け、会計額を表示するケーキ屋プログラムを作成したいです。

言語はJavaです。

商品は以下の5種類です。
・ショートケーキ:320円
・モンブラン:350円
・チョコレートケーキ:370円
・イチゴのタルト:400円
・チーズケーキ:300円

その他要件
ケーキ名、個数を注文情報とする。
消費税は8パーセントとし、小数点以下切り捨てで会計額を算出する
特売期間のため、会計金額が1000円以上である場合は2割引きをする。

→その際の表示は
お会計〇〇円です。
本日は特売期間のため、お会計が２割引きになります。
お会計△△です。
このような表示にしてください。

ちなみに
〇〇→2割引前の金額
△△→2割引後の金額
*/