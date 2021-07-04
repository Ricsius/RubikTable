package Rubik;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RubikTable {
    private static char[] ids = {'R', 'G', 'B', 'W', 'Y', 'O'};
    public static HashMap<Character, Color> idColorPairs;

    private int n;
    private ArrayList< ArrayList<Character> > table;

    static {
        RubikTable.idColorPairs = new HashMap<>();
        RubikTable.idColorPairs.put('R', Color.RED);
        RubikTable.idColorPairs.put('G', Color.GREEN);
        RubikTable.idColorPairs.put('B', Color.BLUE);
        RubikTable.idColorPairs.put('W', Color.WHITE);
        RubikTable.idColorPairs.put('Y', Color.YELLOW);
        RubikTable.idColorPairs.put('O', Color.ORANGE);
    }

    public RubikTable(int n){
        this.n = n;

        createRandomizedTable();

        while(checkWinCondition()){
            createRandomizedTable();
        }
    }

    public int getSize(){
        return n;
    }

    public char get(int ri, int ci){
        return (this.table.get(ri).get(ci));
    }

    public void shiftRowRight(int ri){
        ArrayList<Character> row = this.table.get(ri);
        Character last = row.get(this.n-1);
        Character tmp1 = row.get(0);
        Character tmp2;

        for(int i = 1; i < this.n; ++i){
            tmp2 = row.get(i);
            row.set(i, tmp1);
            tmp1 = tmp2;
        }

        row.set(0, last);
    }

    public void shiftRowLeft(int ri){
        ArrayList<Character> row = this.table.get(ri);
        Character first = row.get(0);
        Character tmp1 = row.get(this.n-1);
        Character tmp2;

        for(int i = this.n-2; i > -1; --i){
            tmp2 = row.get(i);
            row.set(i, tmp1);
            tmp1 = tmp2;
        }

        row.set(this.n-1, first);
    }

    public void shiftColumnUp(int ci){
        ArrayList<Character> column = new ArrayList<>(this.n);

        for(int i = 0; i < this.n; ++i){
            column.add(this.table.get(i).get(ci));
        }

        Character first = column.get(0);
        Character tmp1 = column.get(this.n-1);
        Character tmp2;

        for(int i = this.n-2; i > -1; --i){
            tmp2 = column.get(i);
            column.set(i, tmp1);
            tmp1 = tmp2;
        }

        column.set(this.n-1, first);

        for(int i = 0; i < this.n; ++i){
            this.table.get(i).set(ci, column.get(i));
        }
    }

    public void shiftColumnDown(int ci){
        ArrayList<Character> column = new ArrayList<>(this.n);

        for(int i = 0; i < this.n; ++i){
            column.add(this.table.get(i).get(ci));
        }

        Character last = column.get(this.n-1);
        Character tmp1 = column.get(0);
        Character tmp2;

        for(int i = 1; i < this.n; ++i){
            tmp2 = column.get(i);
            column.set(i, tmp1);
            tmp1 = tmp2;
        }

        column.set(0, last);

        for(int i = 0; i < this.n; ++i){
            this.table.get(i).set(ci, column.get(i));
        }
    }

    public boolean checkWinCondition(){
        return isAllRowsEq() || isAllColumnsEq();
    }
    
    private void createRandomizedTable(){
        this.table = new ArrayList<>(n);

        ArrayList<Character> tmp = new ArrayList<>(n*n);

        for(int i = 0; i < n; ++i){
            for(int j = 0; j < n; ++j){
                tmp.add(RubikTable.ids[i]);
            }
        }

        int randi;
        char ch;

        for(int i = 0; i < n; ++i){
            ArrayList<Character> row = new ArrayList<>();
            for(int j = 0; j < n; ++j){
                randi = (int)(Math.random() * tmp.size());
                ch = tmp.get(randi);
                row.add(ch);
                tmp.remove(randi);
            }
            this.table.add(row);
        }
    }

    private boolean isAllRowsEq(){
        int ri = 0;
        int ci;
        boolean ret = true;

        while (ri < n && ret){
            ci = 1;
            char first = get(ri,0);
            while (ci < n && ret){
                ret = first == get(ri,ci);
                ++ci;
            }
            ++ri;
        }

        return ret;
    }

    private boolean isAllColumnsEq(){
        int ri;
        int ci = 0;
        boolean ret = true;

        while (ci < n && ret){
            ri = 1;
            char first = get(0,ci);
            while (ri < n && ret){
                ret = first == get(ri,ci);
                ++ri;
            }
            ++ci;
        }

        return ret;
    }
}

