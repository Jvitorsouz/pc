public class Contador{

    private int count;

    public Contador(){
        this.count = 0;
    }

    public void setCount(){
        this.count++;
    }

    public int getCount(){
        return this.count++;
    }
}