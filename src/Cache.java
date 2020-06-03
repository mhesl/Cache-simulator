import java.text.DecimalFormat;

public class Cache {

    private int capacity;
    private int associativity;
    private int blockSize;
    private int writeCounter;
    private int dataReadCounter;
    private int writeMissCounter;
    private int dataReadMissCounter;
    private int exitCounter;
    private int copyBacks;
    private int fetch;
    private int insMissReadCounter;
    private int insReadCounter;
    String allocation;
    String writePolicy;
    Set[] cacheSets;



    public Cache(int capacity, int associativity, int blockSize, String allocation, String writePolicy) {
        this.capacity = capacity;
        this.associativity = associativity;
        this.blockSize = blockSize;
        this.allocation = allocation;
        this.writePolicy = writePolicy;
        insReadCounter = 0;
        insMissReadCounter = 0;
        fetch = 0;
        copyBacks = 0;
        writeCounter = 0;
        dataReadCounter = 0;
        writeMissCounter = 0;
        dataReadMissCounter = 0;
        cacheSets = new Set[capacity * 1024 / (associativity * blockSize)];
        System.out.println(cacheSets.length + " ");
        for (int i = 0; i < cacheSets.length; i++) {
            cacheSets[i] = new Set(associativity, blockSize);
        }
    }



    public void allocateInCache(int address) {
        int setIndex = (address / blockSize) % cacheSets.length;
        Set currentSet = cacheSets[setIndex];
        Block currentBlock = currentSet.LRU_policy();
        if (currentBlock.isDirty()) {
            if ((writePolicy.equals("wb") && allocation.equals("wa")))
                copyBacks += 4;
        }

        cacheSets[setIndex].LRU_policy().writeCompleteBlock((address / (cacheSets.length * blockSize)));
    }

    public boolean isBlockInCache(int address) {

        int setIndex = (address / blockSize) % cacheSets.length;
        return cacheSets[setIndex].findBlock((address / (cacheSets.length * blockSize))) != null;
    }


    public void read(int address, int dataOrIns) {
        if (!isBlockInCache(address)) {
            if (dataOrIns == 0) {
                dataReadMissCounter++;
                dataReadCounter++;
            } else {
                insMissReadCounter++;
                insReadCounter++;
            }
            allocateInCache(address);
            fetch += 4;
        }else{
            if(dataOrIns==0)
                dataReadCounter++;
            else
                insReadCounter++;

        }
        int setIndex = (address / blockSize) % cacheSets.length;
        int blockAddress = address % blockSize;
        cacheSets[setIndex].read((address / (cacheSets.length * blockSize)), blockAddress);

    }

    public void write(int address) {
        if ((writePolicy.equals("wt") && allocation.equals("wn"))) {
            writeMissCounter++;
        }

        if (!isBlockInCache(address)) {
            writeMissCounter++;
            if ((writePolicy.equals("wb") && allocation.equals("wa")) || (writePolicy.equals("wt") && allocation.equals("wa"))) {
                allocateInCache(address);
                int setIndex = (address / blockSize) % cacheSets.length;
                cacheSets[setIndex].write((address / (cacheSets.length * blockSize)));
            }
        }
        if (!(writePolicy.equals("wb") && allocation.equals("wa"))) {
            copyBacks += 1;
        }
        writeCounter++;


    }


    public void clearCacheCompletely() {
        for (int i = 0; i < cacheSets.length; i++) {
            for (int j = 0; j < cacheSets[i].blocks.length; j++) {
                if (cacheSets[i].blocks[j].isDirty()) {
                    copyBacks += 4;
                }

            }
        }
    }

//    public void print() {
//        float missRate = (readMissCounter+writeMissCounter)/(writeCounter+readCounter);
//        float hitRate = 1 - missRate;
//        System.out.println("***CACHE SETTINGS***");
//        System.out.println("Unified I- D-cache");
//        System.out.println("Size: " + capacity);
//        System.out.println("Associativity: "+associativity);
//        System.out.println("Block size: " + blockSize);
//        if(writePolicy.equals("wb"))
//            System.out.println("Write policy: WRITE BACK");
//        else if(writePolicy.equals("wt"))
//            System.out.println("WRITE THROUGH");
//        if(allocation.equals("wa"))
//            System.out.println("Allocation policy: WRITE ALLOCATE");
//        else if(allocation.equals("wn"))
//            System.out.println("Allocation policy: WRITE NO ALLOCATE");
//        System.out.println();
//        System.out.println("***CACHE STATISTICS***");
//        System.out.println("INSTRUCTIONS");
//        System.out.println("accesses: " +(writeCounter+readCounter));
//        System.out.println("misses: " + (readMissCounter+writeMissCounter));
//        System.out.println("miss rate: " +missRate +" "+"(" + hitRate + ")" );
//        System.out.println("replace: " );
//    }

    public int getCapacity() {
        return capacity;
    }

    public int getAssociativity() {
        return associativity;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getWriteCounter() {
        return writeCounter;
    }

    public int getDataReadCounter() {
        return dataReadCounter;
    }

    public int getWriteMissCounter() {
        return writeMissCounter;
    }

    public int getDataReadMissCounter() {
        return dataReadMissCounter;
    }

    public int getExitCounter() {
        return exitCounter;
    }

    public int getCopyBacks() {
        return copyBacks;
    }

    public int getFetch() {
        return fetch;
    }

    public int getInsMissReadCounter() {
        return insMissReadCounter;
    }

    public int getInsReadCounter() {
        return insReadCounter;
    }
}
