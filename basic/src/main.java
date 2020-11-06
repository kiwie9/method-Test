class Main {
    public static void main(String[] args) {
        String bleDeviceMac = "f46df362d01e";
        String[] tlvString = {"020ff844657631f46df362d01e0356", "0211f0f46df362d01e38332e3638340339"};

        System.out.println("Hello world!");

        System.out.println(TLVBuild.buildDeviceControlCommand(bleDeviceMac, 1));
        System.out.println(TLVBuild.buildDeviceReqWattCommand(bleDeviceMac));
        System.out.println(TLVBuild.parseResponseData(tlvString[1]));
        System.out.println(TLVBuild.parseDeviceData(tlvString[0]));
    }
}