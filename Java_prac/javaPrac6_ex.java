public class javaPrac6_ex {
    public static void main (String[] args) {
        int answer1 =  3 + ( 7 % 4 ) / 2  ;
        System.out.println( answer1 );
        //4

        int calc2 =  10  ;
        int answer2 = ++calc2 ;
        System.out.println(calc2 + "," + answer2 );
        //11,11

        int calc3 =  10  ;
        int answer3 = calc3++ ;
        System.out.println( calc3 + "," + answer3 );
        //11,10

        int calc4 =  10  ;
        int answer4 = calc4 + 1;
        System.out.println( calc4 + "," + answer4 );
        //10,11

        System.out.println( "1" + "6" );
        //16

        /*
        boolean answer9 = ( 5 >= 3 + 2 || 2 + 8 != 9 ) && !( 6 == 2 + 4 );
        System.out.println( answer9 );
        //
        */
    }
}