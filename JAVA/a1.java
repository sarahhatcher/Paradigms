import java.util.Arrays;
class a1{

  //the nodes of the binary tree
  public class node {
    public node children[];
    public int mach;
    public int task;
    public int flag;
    public int pen;

    public void node(int m,int t,int f,int p){
      this.mach = m;
      this.task = t;
      this.flag = f;
      this.pen = p;
    }

  }

  public static class table{
    int[][] penArray = new int[8][8];
    public void table(){
      for(int i = 0; i < 8;i++){
        Arrays.fill(penArray[i], 1);
      }
    }
    public void print(){
      for(int i = 0;i<8;i++){
        System.out.println(Arrays.toString(this.penArray[i]));
      }
    }
  }
  public static void main(String args[]){
    //create a penality array with hard constraints forbidden mach. ,
    //and forced assignment


    table table1 = new table();
    table1.print();

    //create a tree
    //preform a depth first search
    //itterate through the tree until fully explored
    //return the assignment with the lowest pen.
  }
}