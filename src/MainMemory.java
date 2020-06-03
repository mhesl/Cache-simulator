public class MainMemory {

    int[] mainMemoryData ;
    static final int MEMORYSIZE  = 0;

    public MainMemory() {
        this.mainMemoryData = new int[4194304];
        for (int i = 0; i < mainMemoryData.length; i++) {
            mainMemoryData[i] = i;
        }
    }

    public void write(int offset , int data){
        mainMemoryData[offset] = data;
    }

    public int read(int address){
        int result = mainMemoryData[address];
        return result;
    }

}
