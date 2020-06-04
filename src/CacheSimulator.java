import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class CacheSimulator {

    public static Cache dataCache;
    public static Cache instructionCache;
    public static String allocation;
    public static String policy;
    public static int unifiedOrNot ;
    public static DecimalFormat df = new DecimalFormat("0.0000");
    public static void main(String[] args) throws IOException {
        ArrayList<String[]> addresses = new ArrayList<>();
        int dataCacheSize = 0;
        int instructionCacheSize = 0;
        int blockSize = 0;
        int associativity = 0;

        String path = "";
        Scanner input = new Scanner(System.in);
        String[] cacheDetails = input.nextLine().split(" - ");
        blockSize = Integer.parseInt(cacheDetails[0]);
        unifiedOrNot = Integer.parseInt(cacheDetails[1]);
        associativity = Integer.parseInt(cacheDetails[2]);
        policy = cacheDetails[3];
        allocation = cacheDetails[4];
        if (unifiedOrNot == 0) {
            dataCacheSize = Integer.parseInt(input.nextLine());
            instructionCacheSize = dataCacheSize;
        } else if (unifiedOrNot == 1) {
            String[] cacheSize = input.nextLine().split(" - ");
            dataCacheSize = Integer.parseInt(cacheSize[0]);
            instructionCacheSize = Integer.parseInt(cacheSize[1]);
        }
        String str = input.nextLine();
        while (!str.equals("")) {

            addresses.add(str.split(" "));
            str = input.nextLine();
        }

        dataCache = new Cache(dataCacheSize, associativity, blockSize, allocation, policy);
        instructionCache = new Cache(instructionCacheSize, associativity, blockSize, allocation, policy);
        for (int i = 0; i < addresses.size(); i++) {
            int condition = Integer.parseInt(addresses.get(i)[0]);
            int address = Int_Hex.hex_to_int(addresses.get(i)[1]);
            if (condition == 0)
                dataCache.read(address, 0);
            else if (condition == 1)
                dataCache.write(address);
            else if (condition == 2) {
                if (unifiedOrNot == 1)
                    instructionCache.read(address, 1);
                else if (unifiedOrNot == 0)
                    dataCache.read(address, 1);
            }
        }
        if (policy.equals("wb"))
            dataCache.clearCacheCompletely();
        instructionCache.clearCacheCompletely();
        print();
    }


    static double getRate(double num){
        num *= 10000;
        num = Math.round(num);
        num /= 10000;
        return num;
    }
    public static void print() {
        float instructionMissRate;
        float instructionHitRate;
        float dataMissRate;
        float dataHitRate;
        if ((dataCache.getInsReadCounter() + instructionCache.getInsReadCounter()) == 0) {
            instructionMissRate = 0;
            instructionHitRate = 0;
        } else {
            instructionMissRate = (float) (instructionCache.getInsMissReadCounter() + dataCache.getInsMissReadCounter()) / (float) (instructionCache.getInsReadCounter() + dataCache.getInsReadCounter());
            instructionHitRate = 1 - instructionMissRate;
        df.setRoundingMode(RoundingMode.FLOOR);
        }
        dataMissRate = (float) (dataCache.getDataReadMissCounter() + dataCache.getWriteMissCounter()) / (float) (dataCache.getWriteCounter() + dataCache.getDataReadCounter());
        dataHitRate = 1 - dataMissRate;
        System.out.println("***CACHE SETTINGS***");
        if(unifiedOrNot==0)
            System.out.println("Unified I- D-cache");
        else
            System.out.println("Split I- D-cache");
        if(unifiedOrNot == 0){
            System.out.println("Size: " + dataCache.getCapacity());
        }
        else{
            System.out.println("I-cache size: " + instructionCache.getCapacity());
            System.out.println("D-cache size: " + dataCache.getCapacity());
        }
        System.out.println("Associativity: " + instructionCache.getAssociativity());
        System.out.println("Block size: " + instructionCache.getBlockSize());
        if (policy.equals("wb"))
            System.out.println("Write policy: WRITE BACK");
        else if (policy.equals("wt"))
            System.out.println("WRITE THROUGH");
        if (allocation.equals("wa"))
            System.out.println("Allocation policy: WRITE ALLOCATE");
        else if (allocation.equals("nw"))
            System.out.println("Allocation policy: WRITE NO ALLOCATE");
        System.out.println();
        System.out.println("***CACHE STATISTICS***");
        System.out.println("INSTRUCTIONS");
        System.out.println("accesses: " + (instructionCache.getWriteCounter() + instructionCache.getInsReadCounter() + dataCache.getInsReadCounter()));
        System.out.println("misses: " + (instructionCache.getInsMissReadCounter() + dataCache.getInsMissReadCounter()));
        System.out.printf("miss rate: %.4f (hit rate %.4f)\n", getRate(instructionMissRate), 1-getRate(instructionMissRate));
        if (unifiedOrNot == 1){
            System.out.println("replace: " + instructionCache.getInstructionReplaceCounter());
        }else{
            System.out.println("replace: " + dataCache.getInstructionReplaceCounter());
        }

        System.out.println("DATA");
        System.out.println("accesses: " + (dataCache.getWriteCounter() + dataCache.getDataReadCounter()));
        System.out.println("misses: " + (dataCache.getWriteMissCounter() + dataCache.getDataReadMissCounter()));
        System.out.printf("miss rate: %.4f (hit rate %.4f)\n", getRate(dataMissRate), 1-getRate(dataMissRate));
        System.out.println("replace: " + dataCache.getDataReplaceCounter());
        System.out.println("TRAFFIC (in words)");
        System.out.println("demand fetch: " + (dataCache.getFetch() + instructionCache.getFetch()));
        System.out.println("copies back: " + (dataCache.getCopyBacks() + instructionCache.getCopyBacks()));
    }
}
