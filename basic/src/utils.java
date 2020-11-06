public class utils {
    public static char make_lrc(char[] pBuf, int size) {
        char lrc = 0;

        for (int i = 0; i < size; i++) {
            lrc ^= pBuf[i];
        }

        return lrc;
    }

    public static int char2hex(char s) {
        return ((s>= '0' && s<='9') ? (s-'0') : ((s>= 'a' && s<='f') ? (s-'a'+0xa) :(((s>= 'A' && s<='F') ? (s-'A'+0xa) :0xffff))));
    }

    public static char[] strVal2charArr(String p) {
        final int len = p.length();

        if((len % 2) != 0) throw new IllegalArgumentException("HexBinary need to even length : " + p);
        char[] tempDest = new char[len / 2];

        for(int i = 0 ; i < len ; i += 2) {
            int high_4bits = char2hex(p.charAt(i));
            int low_4bits = char2hex(p.charAt(i + 1));
            if (high_4bits == -1 || low_4bits == -1) {
                throw new IllegalArgumentException("Contains illegal character for hexBinary : " + p);
            }
            tempDest[i / 2] = (char) (high_4bits * 16 + low_4bits);
        }

        return tempDest;
    }

    public static String unHex(String arg) {

        StringBuilder str = new StringBuilder();
        for(int i = 0; i < arg.length(); i += 2) {
            String s = arg.substring(i, (i + 2));
            int decimal = Integer.parseInt(s, 16);
            str.append((char) decimal);
        }
        return str.toString();
    }
}