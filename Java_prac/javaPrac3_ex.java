/*
 *以下は、練習問題です
 * 九九の計算をする練習問題です。
*/

class Ex1_04_1 {
    public static void main (String[] args) {
        for(int i = 1 ; i <= 9  ; i++){
            String disp3 = "";for(int j = 1 ; j <= 9  ; j++){
                disp3 = disp3 + i*j + " ";}
                if(args.length == 1){
                    if(i == Integer.parseInt(args[0])){
                        System.out.println(disp3);
                    }
                }else{
                    System.out.println(disp3);
                }
            }
        }
    }
