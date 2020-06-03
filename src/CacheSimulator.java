import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CacheSimulator {

    public static Cache dataCache;
    public static Cache instructionCache;
    public static String allocation;
    public static String policy;

    public static void main(String[] args) throws IOException {
        ArrayList<String[]> addresses = new ArrayList<>();
        int unifiedOrNot = 0;
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
        if (policy.equals("wb") && allocation.equals("wa"))
            dataCache.clearCacheCompletely();
        instructionCache.clearCacheCompletely();
        print();
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

        }
        dataMissRate = (float) (dataCache.getDataReadMissCounter() + dataCache.getWriteMissCounter()) / (float) (dataCache.getWriteCounter() + dataCache.getDataReadCounter());
        dataHitRate = 1 - dataMissRate;
        System.out.println("***CACHE SETTINGS***");
        System.out.println("Unified I- D-cache");
        System.out.println("Size: " + instructionCache.getCapacity());
        System.out.println("Associativity: " + instructionCache.getAssociativity());
        System.out.println("Block size: " + instructionCache.getBlockSize());
        if (policy.equals("wb"))
            System.out.println("Write policy: WRITE BACK");
        else if (policy.equals("wt"))
            System.out.println("WRITE THROUGH");
        if (allocation.equals("wa"))
            System.out.println("Allocation policy: WRITE ALLOCATE");
        else if (allocation.equals("wn"))
            System.out.println("Allocation policy: WRITE NO ALLOCATE");
        System.out.println();
        System.out.println("***CACHE STATISTICS***");
        System.out.println("INSTRUCTIONS");
        System.out.println("accesses: " + (instructionCache.getWriteCounter() + instructionCache.getInsReadCounter() + dataCache.getInsReadCounter()));
        System.out.println("misses: " + (instructionCache.getInsMissReadCounter() + dataCache.getInsMissReadCounter()));
        System.out.println("miss rate: " + instructionMissRate + " " + "(" + instructionHitRate + ")");
        System.out.println("replace: ");

        System.out.println("DATA");
        System.out.println("accesses: " + (dataCache.getWriteCounter() + dataCache.getDataReadCounter()));
        System.out.println("misses: " + (dataCache.getWriteMissCounter() + dataCache.getDataReadMissCounter()));
        System.out.println("miss rate: " + dataMissRate + " " + "(" + dataHitRate + ")");
        System.out.println("replace: ");
        System.out.println("TRAFFIC (in words)");
        System.out.println("demand fetch: " + (dataCache.getFetch() + instructionCache.getFetch()));
        System.out.println("copies back: " + (dataCache.getCopyBacks() + instructionCache.getCopyBacks()));
    }
}
