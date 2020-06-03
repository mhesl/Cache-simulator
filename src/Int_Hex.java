public class Int_Hex {


    public static String int_to_hex(int integer){
        String hex = Integer.toHexString(integer);
        while(hex.length() < 8){
            hex = "0" + hex;
        }
        return hex ;
    }

    public static int hex_to_int(String hex){
        String digits ="0123456789ABCDEF" ;
        hex = hex.toUpperCase();
        int integer = 0;
        for (int i = 0; i < hex.length(); i++) {
            char c = hex.charAt(i) ;
            int d = digits.indexOf(c);
            integer = integer*16 + d ;
        }
        return integer ;
    }



}
