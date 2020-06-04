public
class Block {
    private int size ;
    private int tag ;
    private int recentUse ;
    private boolean isDirty ;
    private boolean isValid ;

    public Block(int size) {
        this.size = size;
        isValid = false ;
        isDirty = false ;
        recentUse = 0 ;
        tag = -1;
    }

    public void read(int address , int recentlyUseCounter){
        recentUse = recentlyUseCounter ;

    }

    public void write( int recentlyUseCounter){
        recentUse = recentlyUseCounter ;
        isValid = true;
        isDirty = true;
    }

    public void writeCompleteBlock( int tag){
        this.tag = tag;
        isDirty=false;
        isValid=true;
    }

    public int getSize() {
        return size;
    }

    public int getTag() {
        return tag;
    }

    public int getRecentUse() {
        return recentUse;
    }


    public boolean isDirty() {
        return isDirty;
    }

    public boolean isValid() {
        return isValid;
    }
}