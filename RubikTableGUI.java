package Rubik;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;

public class RubikTableGUI {
    private int maxSize;
    private int rubikSize;
    private int moves;
    private RubikTable rubikTable;
    private JFrame frame;
    private JPanel tableSizeSetterButtonPanel;
    private JPanel upShiftButtonsPanel;
    private JPanel downShiftButtonsPanel;
    private JPanel rubikRowsPanel;
    private ArrayList<ArrayList<JPanel>> rubikTablePanels;
    private JFrame victoryFrame;

    public RubikTableGUI(){
        this.maxSize = 6;

        startNewGame();
    }

    private void startNewGame(){
        this.frame = new JFrame("Rubik Table");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.moves = 0;

        //TableSizeSetterButtonPanel
        this.tableSizeSetterButtonPanel = new JPanel();
        this.tableSizeSetterButtonPanel.setLayout(new GridLayout(this.maxSize/2,1));
        this.frame.add(this.tableSizeSetterButtonPanel);

        for(int i = 2; i <= this.maxSize; i += 2){
            JButton sizeButton = new JButton(i + " x " + i);
            sizeButton.addActionListener(new RubikTableSizeSetterButtonActionListener());
            this.tableSizeSetterButtonPanel.add(sizeButton);
        }

        this.frame.pack();
        this.frame.setVisible(true);
    }

    private void drawGame(){
        this.upShiftButtonsPanel = new JPanel();
        this.downShiftButtonsPanel = new JPanel();

        //UpButtons
        for(int i = 0; i < this.rubikSize; ++i){
            JButton upButton = new JButton("Up");
            upButton.addActionListener(new RubikShiftButtonActionListener(Direction.UP, i));
            this.upShiftButtonsPanel.add(upButton);
        }

        //SideButtonsAndRubikTable
        this.rubikRowsPanel = new JPanel();
        this.rubikRowsPanel.setLayout(new GridLayout(rubikSize,rubikSize+2));
        this.rubikRowsPanel.setBackground(Color.BLACK);

        this.rubikTablePanels = new ArrayList<>(this.rubikSize);

        for(int i = 0; i < this.rubikSize; ++i) {
            JButton leftButton = new JButton("Left");
            leftButton.addActionListener(new RubikShiftButtonActionListener(Direction.LEFT, i));
            this.rubikRowsPanel.add(leftButton);
            this.rubikTablePanels.add(new ArrayList<JPanel>());

            char id;

            for(int j = 0; j < this.rubikSize; ++j) {
                JPanel panel = new JPanel();
                id = this.rubikTable.get(i,j);
                panel.setBackground(RubikTable.idColorPairs.get(id));
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                this.rubikTablePanels.get(i).add(panel);
                this.rubikRowsPanel.add(panel);
            }

            JButton rightButton = new JButton("Right");
            rightButton.addActionListener(new RubikShiftButtonActionListener(Direction.RIGHT, i));
            this.rubikRowsPanel.add(rightButton);
        }

        //DownButtons
        for(int i = 0; i < this.rubikSize; ++i){
            JButton downButton = new JButton("Down");
            downButton.addActionListener(new RubikShiftButtonActionListener(Direction.DOWN, i));
            this.downShiftButtonsPanel.add(downButton);
        }

        this.frame.getContentPane().add(BorderLayout.NORTH, this.upShiftButtonsPanel);
        this.frame.getContentPane().add(BorderLayout.CENTER, this.rubikRowsPanel);
        this.frame.getContentPane().add(BorderLayout.SOUTH, this.downShiftButtonsPanel);
        this.frame.pack();
    }

    private void updateRubikRow(int index){
        for(int i = 0; i < this.rubikSize; ++i) {
            char id = this.rubikTable.get(index,i);
            this.rubikTablePanels.get(index).get(i).setBackground(RubikTable.idColorPairs.get(id));
        }
    }

    private void updateRubikColumn(int index){
        for(int i = 0; i < this.rubikSize; ++i) {
            char id = this.rubikTable.get(i,index);
            this.rubikTablePanels.get(i).get(index).setBackground(RubikTable.idColorPairs.get(id));
        }
    }

    private void drawVictoryWindow(){
        this.victoryFrame = new JFrame("Victory!");
        JPanel panel = new JPanel();
        JLabel label1 = new JLabel("Congratulations!", SwingConstants.CENTER);
        JLabel label2 = new JLabel("You made " + moves + " moves.", SwingConstants.CENTER);
        JLabel label3 = new JLabel("Close this window to start a new game.", SwingConstants.CENTER);

        panel.setLayout(new GridLayout(3,1));
        panel.add(label1);
        panel.add(label2);
        panel.add(label3);

        this.victoryFrame.getContentPane().add(BorderLayout.CENTER,panel);
        this.victoryFrame.setPreferredSize(new Dimension(300,200));
        this.victoryFrame.addWindowListener(new VictoryWindowListener());
        this.victoryFrame.pack();
        this.victoryFrame.setVisible(true);
    }

    enum Direction{
        UP, DOWN, LEFT, RIGHT;
    }

    class RubikTableSizeSetterButtonActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            rubikSize = Integer.parseInt(actionEvent.getActionCommand().split("x")[0].trim());
            rubikTable = new RubikTable(rubikSize);
            tableSizeSetterButtonPanel.setVisible(false);
            drawGame();
        }
    }

    class RubikShiftButtonActionListener implements ActionListener{
        Direction buttonDirection;
        int index;

        public RubikShiftButtonActionListener(Direction dir, int index){
            this.buttonDirection = dir;
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            switch(this.buttonDirection){
                case UP:
                    rubikTable.shiftColumnUp(index);
                    updateRubikColumn(index);
                    break;

                case DOWN:
                    rubikTable.shiftColumnDown(index);
                    updateRubikColumn(index);
                    break;

                case LEFT:
                    rubikTable.shiftRowLeft(index);
                    updateRubikRow(index);
                    break;

                case RIGHT:
                    rubikTable.shiftRowRight(index);
                    updateRubikRow(index);
                    break;
            }

            ++moves;

            if(rubikTable.checkWinCondition()){
                drawVictoryWindow();
            }
        }
    }

    class VictoryWindowListener extends WindowAdapter{
        @Override
        public void windowClosing(WindowEvent windowEvent) {
            frame.dispose();
            startNewGame();
        }
    }
}
