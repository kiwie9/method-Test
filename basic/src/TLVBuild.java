import java.util.Formatter;

public class TLVBuild {
    public static String buildDeviceControlCommand(String bleDeviceMac, int value) {
        int len = 0, index = 0;
        char[] chars = new char[30];

        chars[index ++] = 0x02;     // STX
        len ++;
        chars[index ++] = 0x00;
        len ++;
        chars[index ++] = 0xF6;
        len ++;
        for (int i = 0; i < bleDeviceMac.length(); i ++)
            chars[index ++] = bleDeviceMac.charAt(i);
        len += bleDeviceMac.length() / 2;
        chars[index ++] = (char) value;      // on-off
        len ++;
        chars[index ++] = 0x03;     // ETX
        len ++;
        chars[1] = (char)(++ len);     // update length include lrc
        chars[index] = utils.make_lrc(chars, index);

        Formatter formatter = new Formatter();
        for (int i = 0; i < 3; i ++) {
            // STX, Len, Cmd
            formatter.format("%02x", (int) chars[i]);
        }
        for (int j = 3; j < 15; j ++) {
            // BLE Device MAC Address 12 chars
            formatter.format("%c", chars[j]);
        }
        for (int k = 15; k < index + 1; k ++) {
            // Value, ETX, LRC
            formatter.format("%02x", (int) chars[k]);
        }

        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String buildDeviceReqWattCommand(String bleDeviceMac) {
        int len = 0, index = 0;
        char[] chars = new char[30];

        chars[index ++] = 0x02;     // STX
        len ++;
        chars[index ++] = 0x00;
        len ++;
        chars[index ++] = 0xF0;
        len ++;
        for (int i = 0; i < bleDeviceMac.length(); i ++)
            chars[index ++] = bleDeviceMac.charAt(i);
        len += bleDeviceMac.length() / 2;
        chars[index ++] = 0x03;     // ETX
        len ++;
        chars[1] = (char)(++ len);     // update length include lrc
        chars[index] = utils.make_lrc(chars, index);

        Formatter formatter = new Formatter();
        for (int i = 0; i < 3; i ++) {
            // STX, Len, Cmd
            formatter.format("%02x", (int) chars[i]);
        }
        for (int j = 3; j < 15; j ++) {
            // BLE Device MAC Address 12 chars
            formatter.format("%c", chars[j]);
        }
        for (int k = 15; k < index + 1; k ++) {
            // Value, ETX, LRC
            formatter.format("%02x", (int) chars[k]);
        }

        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String parseResponseData(String tlvString) {
        int loopa = 0;
        char[] tempBuf = new char[70];
        for (int i = 0; i < (tlvString.length() / 2); i ++) {
            tempBuf[i] = (char) Integer.parseInt(tlvString.substring(loopa, loopa + 2), 16);
            loopa += 2;
        }

        int loop1=0;

        // check STX
        if (Integer.parseInt(tlvString.substring(loop1, loop1 + 2)) != 0x02) return "Error STX";
        loop1 += 2;

        // check length
        int length = Integer.parseInt(tlvString.substring(loop1, loop1 + 2), 16);
        //if (length != tlvString.length()) return _bleDeviceInfos;
        loop1 += 2;

        // check LRC
        char lrc = 0;
        for (int i = 0; i < length - 1; i ++) {
            lrc ^= tempBuf[i];
        }
        if (lrc != tempBuf[length - 1]) {
            return "Error LRC";
        }

        // command check
        if (Integer.parseInt(tlvString.substring(loop1, loop1 + 2), 16) != 0xf0) return "Error command";
        loop1 += 2;

        String macAddress = tlvString.substring(loop1, loop1 + 12);
        loop1 += 12;

        return String.copyValueOf(utils.strVal2charArr(tlvString.substring(loop1, loop1 + 12)));
    }

    public static String parseDeviceData(String tlvString) {
        int loopa = 0;
        char[] tempBuf = new char[70];
        for (int i = 0; i < (tlvString.length() / 2); i ++) {
            tempBuf[i] = (char) Integer.parseInt(tlvString.substring(loopa, loopa + 2), 16);
            loopa += 2;
        }

        int loopb = 0;
        // check STX
        if (Integer.parseInt(tlvString.substring(loopb, loopb + 2), 16) != 0x02) return "Error STX";
        loopb += 2;

        // check length
        int length = Integer.parseInt(tlvString.substring(loopb, loopb + 2), 16);
        //if (length != (tlvString.length() / 2)) return bleDeviceInfos;
        loopb += 2;

        // check ETX

        // check LRC
        char lrc = 0;
        for (int i = 0; i < length - 1; i ++) {
            lrc ^= tempBuf[i];
        }
        if (lrc != tempBuf[length - 1]) {
            return "Error LRC";
        }

        // check command
        if (Integer.parseInt(tlvString.substring(loopb, loopb + 2), 16) != 0xf8) return "Error command";
        loopb += 2;

        String bleDeviceName = utils.unHex(tlvString.substring(loopb, loopb + 8));
        if (bleDeviceName == null) return "Error BLE Device Name";
        loopb += 8;
        String macAddress = tlvString.substring(loopb, loopb + 12);
        loopb += 12;
        
        return bleDeviceName + macAddress;
    }
}