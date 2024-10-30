import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame implements ActionListener {

    private int xScore, oScore, moveCounter;
    private boolean isPlayerOne;
    private JLabel turnLabel, scoreLabel, resultLabel;
    private JButton [][] board;
    private JDialog resultDialog;
    public GUI(){
        super("XoXo (Java Swing)");
        setSize(Constants.FRAME_SIZE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Constants.BACKGROUNG_COLOR);

        createResultDialog();

        // init vars
        board = new JButton[3][3];

        //player x starts first
        isPlayerOne = true;

        addGuiComponent();
    }

    private void addGuiComponent(){
        JLabel barLabel = new JLabel();
        barLabel.setOpaque(true);
        barLabel.setBackground(Constants.BAR_COLOR);
        barLabel.setBounds(0, 0, Constants.FRAME_SIZE.width, 25);

        turnLabel = new JLabel(Constants.X_LABEL);
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        turnLabel.setFont(new Font("Dialog", Font.PLAIN, 40));
        turnLabel.setPreferredSize(new Dimension(100, turnLabel.getPreferredSize().height));
        turnLabel.setOpaque(true);
        turnLabel.setBackground(Constants.X_COLOR);
        turnLabel.setForeground(Constants.BOARD_COLOR);
        turnLabel.setBounds(
                (Constants.FRAME_SIZE.width - turnLabel.getPreferredSize().width)/2,
                0,
                turnLabel.getPreferredSize().width,
                turnLabel.getPreferredSize().height
        );

        scoreLabel = new JLabel(Constants.SCORE_LABEL);
        scoreLabel.setFont(new Font("Dialog", Font.PLAIN, 40));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setForeground(Constants.BOARD_COLOR);
        scoreLabel.setBounds(0,
                turnLabel.getY() + turnLabel.getPreferredSize().height + 25,
                Constants.FRAME_SIZE.width,
                scoreLabel.getPreferredSize().height
                );

        GridLayout gridLayout = new GridLayout(3,3);
        JPanel boardPanel = new JPanel(gridLayout);
        boardPanel.setBounds(0,
                scoreLabel.getY() + scoreLabel.getPreferredSize().height + 35,
                Constants.BOARD_SIZE.width,
                Constants.BOARD_SIZE.height
                );

        // create board
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
                JButton button = new JButton();
                button.setFont(new Font("Dialog", Font.PLAIN, 180));
                button.setPreferredSize(Constants.BOARD_SIZE);
                button.setBackground(Constants.BACKGROUNG_COLOR);
                button.addActionListener(this);
                button.setBorder(BorderFactory.createLineBorder(Constants.BOARD_COLOR));


                // add button board
                board [i][j] = button;
                boardPanel.add(board[i][j]);

            }


        }

        // reset button
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Dialog", Font.PLAIN, 24));
        resetButton.addActionListener(this);
        resetButton.setBackground(Constants.BOARD_COLOR);
        resetButton.setBounds(
                (Constants.FRAME_SIZE.width - resetButton.getPreferredSize().width)/2,
                Constants.FRAME_SIZE.height - 100,
                resetButton.getPreferredSize().width,
                resetButton.getPreferredSize().height
        );

        getContentPane().add(turnLabel);
        getContentPane().add(barLabel);
        getContentPane().add(scoreLabel);
        getContentPane().add(boardPanel);
        getContentPane().add(resetButton);


    }

    private void createResultDialog(){
        resultDialog = new JDialog();
        resultDialog.getContentPane().setBackground(Constants.BACKGROUNG_COLOR);
        resultDialog.setResizable(false);
        resultDialog.setTitle("Result");
        resultDialog.setSize(Constants.RESULT_DIALOG_SIZE);
        resultDialog.setLocationRelativeTo(this);
        resultDialog.setModal(true);
        resultDialog.setLayout(new GridLayout(2, 1));
        resultDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                resetGame();
            }
        });

        // result label
        resultLabel = new JLabel();
        resultLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        resultLabel.setForeground(Constants.BOARD_COLOR);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);


        //restart button
        JButton restartButton = new JButton("Play again");
        restartButton.setBackground(Constants.BOARD_COLOR);
        restartButton.addActionListener(this);

        resultDialog.add(resultLabel);
        resultDialog.add(restartButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Reset") || command.equals("Play again")){

            // reset the game
            resetGame();

            if(command.equals("Reset"))
                xScore = oScore = 0;

            if (command.equals("Play again"))
                resultDialog.setVisible(false);
        }else {
            JButton button = (JButton) e.getSource();
            if(button.getText().equals("")){
                moveCounter++;

                if (isPlayerOne){
                    button.setText(Constants.X_LABEL);
                    button.setForeground(Constants.X_COLOR);

                    turnLabel.setText(Constants.O_LABEL);
                    turnLabel.setBackground(Constants.O_COLOR);

                    isPlayerOne = false;
                }else {
                    button.setText(Constants.O_LABEL);
                    button.setForeground(Constants.O_COLOR);

                    turnLabel.setText(Constants.X_LABEL);
                    turnLabel.setBackground(Constants.X_COLOR);

                    isPlayerOne = true;
                }

                if (isPlayerOne){
                    checkOWin();
                }{
                    checkXWin();
                }

                // check for draw conditions
                checkDraw();

                // update score label
                scoreLabel.setText("X : " + xScore + " | 0 :" + oScore);


            }

            repaint();
            revalidate();
        }
    }

    private void checkXWin(){
        String result = "X wins";

        // chech rows
        for (int row = 0; row < board.length; row++){
            if (board[row][0].getText().equals("X") && board[row][1].getText().equals("X") && board[row][2].getText().equals("X")){
               resultLabel.setText(result);

               // display result dialog
                resultDialog.setVisible(true);

                // update score
                xScore++;
            }
        }

        // check columns
        for (int col = 0; col < board.length; col++){
                if (board[0][col].getText().equals("X") && board[1][col].getText().equals("X") && board[2][col].getText().equals("X")) {
                    resultLabel.setText(result);

                    // display result dialog
                    resultDialog.setVisible(true);

                    // update score
                    xScore++;
                }
        }
        // check diagonals
        if (board[0][0].getText().equals("X") && board[1][1].getText().equals("X") && board[2][2].getText().equals("X")){
            resultLabel.setText(result);

            // display result dialog
            resultDialog.setVisible(true);

            // update score
            xScore++;
        }
        if (board[2][0].getText().equals("X") && board[1][1].getText().equals("X") && board[0][2].getText().equals("X")){
            resultLabel.setText(result);

            // display result dialog
            resultDialog.setVisible(true);

            // update score
            xScore++;
        }

    }
    private void checkOWin(){
        String result = "O wins";

        // chech rows
        for (int row = 0; row < board.length; row++){
            if (board[row][0].getText().equals("O") && board[row][1].getText().equals("O") && board[row][2].getText().equals("O")){
                resultLabel.setText(result);

                // display result dialog
                resultDialog.setVisible(true);

                // update score
                oScore++;
            }
        }

        // check columns
        for (int col = 0; col < board.length; col++){
            if (board[0][col].getText().equals("O") && board[1][col].getText().equals("O") && board[2][col].getText().equals("O")) {
                resultLabel.setText(result);

                // display result dialog
                resultDialog.setVisible(true);

                // update score
                oScore++;
            }
        }
        // check diagonals
        if (board[0][0].getText().equals("O") && board[1][1].getText().equals("O") && board[2][2].getText().equals("O")){
            resultLabel.setText(result);

            // display result dialog
            resultDialog.setVisible(true);

            // update score
            oScore++;
        }
        if (board[2][0].getText().equals("O") && board[1][1].getText().equals("O") && board[0][2].getText().equals("O")){
            resultLabel.setText(result);

            // display result dialog
            resultDialog.setVisible(true);

            // update score
            oScore++;
        }
    }

    private void checkDraw(){
        if (moveCounter >= 9){
            resultLabel.setText("Draw!");
            resultDialog.setVisible(true);

        }
    }

    // reset player back on x
    private void resetGame(){
        isPlayerOne = true;
        turnLabel.setText(Constants.X_LABEL);
        turnLabel.setBackground(Constants.X_COLOR);

        // reset score
        scoreLabel.setText(Constants.SCORE_LABEL);

        // rest move counter
        moveCounter = 0;

        // reset board
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
                board[i][j].setText("");
            }
        }
    }



}
