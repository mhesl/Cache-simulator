public class Set {

    int blockSize ;
    int associativity ;
    int counter;
    Block[] blocks ;

    public Set(int blockSize, int associativity) {
        this.blockSize = blockSize;
        this.associativity = associativity;
        blocks = new Block[associativity];
        for (int i = 0; i < associativity ; i++) {
            blocks[i] =new Block(blockSize);
        }
        this.counter = 0;
    }

    public Block findBlock(int tag){
        Block block = null ;
        for (Block value : blocks) {
            if (value.isValid() && value.getTag() == tag) {
                block = value;
                break;
            }
        }
        return block;
    }

    public void read( int tag , int offset){
        Block currentBlock = findBlock(tag);
        if(currentBlock !=null){
            currentBlock.read(offset ,++counter);
        }else{
            System.out.println("fuck");
        }
    }

    public void write(int tag  ){
        Block currentBlock = findBlock(tag);
        if(currentBlock != null){
            currentBlock.write( ++counter);
        }else{
            System.out.println("we do not have this block here man");
        }
    }



    public Block LRU_policy(){
        Block LRU_block = blocks[0];
        int use_counter = LRU_block.getRecentUse();
        for (Block block : blocks) {
            if (block.getRecentUse() < use_counter) {
                LRU_block = block;
                use_counter = block.getRecentUse();
            }
        }
        return LRU_block;
    }





}
