package attendanceCode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * WorkingResult.csv（一か月分の勤務実績ファイル）を読み込み、
 * 1か月間の給与の総額を算出して出力するクラスです。
 */
public class MonthlySalaryCalculator {

    // --- 定数定義 ---
    /** WorkingResult.csvのファイル名 (プログラムと同じディレクトリに配置想定) */
    private static final String WORKING_RESULT_FILE_NAME = "C:\\work\\ForDevelop\\Attendance\\data\\WorkingResult.csv";
    /** CSVファイルの区切り文字 */
    private static final String COMMA = ",";
    /** 時給（円） */
    private static final int HOURLY_WAGE = 900;
    /** 残業代の割増率 */
    private static final double OVERTIME_RATE = 1.25;

    /** 1時間の分数 */
    private static final int MINUTES_IN_HOUR = 60;
    /** 通常勤務時間の上限（分）= 8時間 */
    private static final int REGULAR_WORK_MINUTES_THRESHOLD = 8 * MINUTES_IN_HOUR;

    /** 休憩時間発生の閾値1（分）= 6時間 */
    private static final int BREAK_THRESHOLD_1_MINUTES = 6 * MINUTES_IN_HOUR;
    /** 休憩時間（分）（労働時間が6時間以上8時間以下の場合） */
    private static final int BREAK_TIME_1_MINUTES = 45;
    /** 休憩時間（分）（労働時間が8時間を超える場合） */
    private static final int BREAK_TIME_2_MINUTES = 60;


    public static void main(String[] args) {
        List<String> workingRecords = new ArrayList<>();
        double totalMonthlySalary = 0;

        // 1. WorkingResult.csvを読み込む
        try {
            File workingResultFile = new File(WORKING_RESULT_FILE_NAME);
            // ファイルが存在しない場合はエラーメッセージを表示して終了
            if (!workingResultFile.exists()) {
                System.err.println("エラー: " + WORKING_RESULT_FILE_NAME + " が見つかりません。プログラムと同じディレクトリに配置してください。");
                return;
            }
            BufferedReader br = new BufferedReader(new FileReader(workingResultFile));

            String line = br.readLine();
            // ヘッダー行と思われる行をスキップ (例: "日付,出勤時間,退勤時間")
            if (line != null && (line.contains("日付") || line.contains("出勤時間") || line.contains("退勤時間"))) {
                line = br.readLine(); // 次の行から実データ
            }

            while (line != null) {
                if (!line.trim().isEmpty()) { // 空行は無視
                    workingRecords.add(line);
                }
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.err.println("ファイルの読み込み中にエラーが発生しました: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // ArrayListから1行ずつ取り出して給与を計算
        for (String record : workingRecords) {
            String[] fields = record.split(COMMA);
            if (fields.length < 3) {
                System.err.println("警告: 不正な形式のレコードです（列不足）: " + record);
                continue; // この行をスキップ
            }

            // CSVから日付、出勤時間、退勤時間を取得
            // String dateStr = fields[0]; // 日付文字列 (今回は給与計算に直接使用しない)
            Time startTime;
            Time finishTime;

            try {
                // CSVの時刻形式が "HH:mm" の場合、":00" を補って "HH:mm:ss" 形式にする
                String startTimeStr = fields[1].trim();
                String finishTimeStr = fields[2].trim();

                if (startTimeStr.matches("\\d{1,2}:\\d{2}")) {
                    startTimeStr += ":00";
                }
                if (finishTimeStr.matches("\\d{1,2}:\\d{2}")) {
                    finishTimeStr += ":00";
                }

                startTime = Time.valueOf(startTimeStr);
                finishTime = Time.valueOf(finishTimeStr);
            } catch (IllegalArgumentException e) {
                System.err.println("警告: 時刻の形式が正しくありません: " + record + " エラー: " + e.getMessage());
                continue; // この行をスキップ
            }

            // 2. 日次勤務データの処理
            // 2a. 総労働時間の算出 (ミリ秒単位 -> 分単位)
            long workingTimeMillis = finishTime.getTime() - startTime.getTime();
            // 日付をまたぐ深夜勤務は考慮しない前提（例: 22:00～02:00のような勤務）
            // もし退勤時刻が出勤時刻より早い場合はエラーまたは0分として扱う
            if (workingTimeMillis < 0) {
                System.err.println("警告: 退勤時間が出勤時間より前です。この日の労働時間は0分として計算します: " + record);
                workingTimeMillis = 0;
            }
            long totalWorkingMinutes = workingTimeMillis / (1000 * 60); // ミリ秒を分に変換

            // 2b. 休憩時間の決定
            int breakTimeMinutes = 0;
            if (totalWorkingMinutes > REGULAR_WORK_MINUTES_THRESHOLD) { // 労働時間が8時間 (480分) を超える場合
                breakTimeMinutes = BREAK_TIME_2_MINUTES; // 60分休憩
            } else if (totalWorkingMinutes >= BREAK_THRESHOLD_1_MINUTES) { // 労働時間が6時間 (360分) 以上、8時間 (480分) 以下の場合
                breakTimeMinutes = BREAK_TIME_1_MINUTES; // 45分休憩
            }
            // 労働時間が6時間未満の場合は休憩なし (breakTimeMinutes = 0)

            // 2c. 実労働時間の算出
            long actualWorkingMinutes = totalWorkingMinutes - breakTimeMinutes;
            if (actualWorkingMinutes < 0) {
                actualWorkingMinutes = 0; // 休憩が労働時間を超える異常ケースを考慮
            }

            // 2d. 日当の計算
            double dailySalary = 0;
            if (actualWorkingMinutes > 0) {
                double minuteRate = (double) HOURLY_WAGE / MINUTES_IN_HOUR; // 分給

                long overtimeMinutes = 0;
                long regularTimeMinutes = actualWorkingMinutes;

                // 実労働時間が8時間 (REGULAR_WORK_MINUTES_THRESHOLD) を超える場合、超過分を残業時間とする
                if (actualWorkingMinutes > REGULAR_WORK_MINUTES_THRESHOLD) {
                    overtimeMinutes = actualWorkingMinutes - REGULAR_WORK_MINUTES_THRESHOLD;
                    regularTimeMinutes = REGULAR_WORK_MINUTES_THRESHOLD;
                }

                // 通常給与
                double regularPay = regularTimeMinutes * minuteRate;
                // 残業給与
                double overtimePay = overtimeMinutes * minuteRate * OVERTIME_RATE;

                dailySalary = regularPay + overtimePay;
            }

            // 小数点以下切り捨て
            dailySalary = Math.floor(dailySalary);

            // 3. 月給総額の集計
            totalMonthlySalary += dailySalary;

            // (任意) 日ごとの詳細を出力する場合
            // System.out.printf("日付: %s, 総労働: %d分, 休憩: %d分, 実労働: %d分, 日当: %.0f円%n",
            // dateStr, totalWorkingMinutes, breakTimeMinutes, actualWorkingMinutes, dailySalary);
        }

        // 4. 結果の出力
        System.out.printf("1ヶ月間の給与の総額: %.0f円%n", totalMonthlySalary);
    }
}