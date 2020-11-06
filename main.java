class Main {
  public static void main(String[] args) {
    System.out.println("Hello world!");

    int len = 0, index = 0;
    char[] chars = new char[30];

    chars[index ++] = 0x02;     // STX
    len ++;
    chars[index ++] = 0x00;     // Length position
    len ++;
    chars[index ++] = 0xF0;     // command
    len ++;
    for (int i = 0; i < info.getBleDeviceMac().length(); i++)
      // BLE Device MAC Address
      chars[index ++] = info.getBleDeviceMac().charAt(i);
    len += info.getBleDeviceMac().length() / 2;         // 12 chars but count 6
    chars[index ++] = 0x03;
    len ++;
    chars[1] = (char)(++ len);     // add lrc length
    chars[index] = make_lrc(chars, index);

    Formatter formatter = new Formatter();
    for (int i = 0; i < 3; i ++) {
      // STX, LEN, CMD
      formatter.format("%02x", (int) chars[i]);
    }
    for (int j = 3; j < 15; j ++) {
      // BLE Device MAC Address
      formatter.format("%c", chars[j]);
    }
    for (int k = 15; k < index + 1; k ++) {
      // ETX, LRC
      formatter.format("%02x", (int) chars[k]);
    }

    String result = formatter.toString();
    formatter.close();
    System.out.println(result);
  }
}