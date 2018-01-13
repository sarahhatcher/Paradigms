import java.util.Arrays;
class a1{

  public static void main(String[] args){
    //create a penality array with hard constraints forbidden mach. ,
    //and forced assignment


    table table1 = new table();
    for(int i=1;i<9;i++)
    {
      table1.applyForbiden(i,i);
    }
    table1.print();

    //create a tree
    //preform a depth first search
    //itterate through the tree until fully explored
    //return the assignment with the lowest pen.
  }

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


/*
* table with penalties. Contains error checking for forced partial assignment.
* Does not deal with "invalid machine/task" errors.
* does not deal with "machine penalty error".
*/
  public static class table{
    public int[][] penArray = new int[8][8];
    // constructor for testing only. Should find a way to actualy use input
    public table(){
      for(int i = 0; i < 8;i++){
        Arrays.fill(penArray[i], 1);
      }
    }

    //method for applying forbiden machine

    public void applyForbiden(int m, int t){
      this.penArray[m-1][t-1] = -1;
    }

    // display the array for error checking. Do not use in final assignment
    public void print(){
      for(int i = 0;i<8;i++){
        System.out.println(Arrays.toString(this.penArray[i]));
      }
    }
  }

}
