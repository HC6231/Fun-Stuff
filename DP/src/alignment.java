import java.util.*;
public class alignment {
    static void Highest_Score_Alignment(String x, String y, int[][] optimal) {
        //instantiation
        int match = 3;
        int mismatch = -3;
        int gap = -2;
        optimal[0][0] = 0;
        //fill the 2 x 2 matrix with scores of three situations:
        //       1.x <-> y;       2. x <-> -;        3. y <-> -;
        // two for loops are used to record the smaller problems
        for (int i = 0; i <= x.length(); i++){
            for (int j = 0; j <= y.length(); j++){
                if(i == 0){
                    optimal[i][j] = j * gap;
                } 
                else if(j == 0){
                    optimal[i][j] = i * gap;
                } 
                else{
                    //match
                    if(x.charAt(i - 1) == y.charAt(j - 1)){
                        optimal[i][j] = optimal[i-1][j-1] + match;
                    }
                    //not match
                    else{
                        //not match has three situations
                        //x != y , x = - , y = -
                        optimal[i][j] = Math.max(Math.max(optimal[i - 1][j - 1] + mismatch, optimal[i - 1][j] + gap), optimal[i][j - 1] + gap);
                    }
                }
            }
        }

        //  x - - - - - - -
        // y
        // |
        // |
        // |
        // |
        // |             opti

        //the optimal score is already stored at the bottom of the matrix
        System.out.println(optimal[x.length()][y.length()]);

        //trace the optimal alignment
        int i = x.length();
        int j = y.length();
        String fout = "";
        String sout = "";
        while(true){
            if(i==0 && j==0){
                break;
            }
            else if(i==0 && j!=0){
                fout = '-' + fout;
                sout = y.charAt(j-1) + sout;
                j--;
            }
            else if(x.charAt(i-1)==y.charAt(j-1)){
                fout= x.charAt(i - 1) + fout;
                sout = y.charAt(j - 1) + sout;
                i--;
                j--;
            }
            //i - 1, j - 1
            else if(optimal[i-1][j-1] + mismatch == optimal[i][j]){
                fout = x.charAt(i - 1) + fout;
                sout = y.charAt(j - 1) + sout;
                i--;
                j--;
            }
            //go left for opt
            else if(optimal[i-1][j] + gap == optimal[i][j]){
                fout = x.charAt(i - 1) + fout;
                sout = '-' + sout;
                i--;
            }
            //go up for opt
            else if(optimal[i][j-1] + gap == optimal[i][j]){
                fout = '-' + fout;
                sout = y.charAt(j - 1) + sout;
                j--;
            }
        }
        System.out.println(fout);
        System.out.println(sout);
    }

    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);
        String a = scanner.nextLine();
        String b = scanner.nextLine();

        int[][] table = new int[a.length() + 1][b.length() + 1];

        Highest_Score_Alignment(a, b, table);

    }
}
