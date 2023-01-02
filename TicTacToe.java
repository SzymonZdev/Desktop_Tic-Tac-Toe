package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Random;

import static tictactoe.TicTacToe.BoardCell.*;

public class TicTacToe extends JFrame {
    static boolean xTurn = true;
    static boolean gameOver = false;
    static boolean gameStarted = false;
    JLabel statusLabel = new JLabel();
    static JButton resetStartButton = new JButton();
    static Integer[][] boardStatus = new Integer[3][3];
    static List<BoardCell> cells = new ArrayList<>();
    static JButton player1Button = new JButton();
    static JButton player2Button = new JButton();
    JPanel boardPanel = new BoardClass();

    static String gameState;

    static int count = 0;

    public TicTacToe() {
        SwingUtilities.invokeLater(this::createAndShowGUI);
        updateStatusLabel();
    }

    public void endGame() {
        gameOver = true;
        BoardClass.disableBoard();
    }
    public void endGame(String type, int which) {
        gameOver = true;
        BoardClass.disableBoard();
        highlightWin(type, which);
    }

    private void updateStatusLabel() {
        SwingWorker<String, String> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                while (!gameOver) {
                    updateGameState();
                    statusLabel.setText(gameState);
                }
                updateGameState();
                statusLabel.setText(gameState);
                return null;
            }
        };

        worker.execute();
    }

    private void updateGameState() {
        if (!gameStarted) {
            gameState = "Game is not started";
        } else if (gameOver) {
            StringBuilder endGameState = new StringBuilder();
            if (checkForWin().equals("X")) {
                endGameState.append("The ")
                        .append(player1Button.getText())
                        .append(" Player (X) wins");
            } else if (checkForWin().equals("O")) {
                endGameState.append("The ")
                        .append(player2Button.getText())
                        .append(" Player (O) wins");
            } else if (checkForWin().equals("Draw")) {
                endGameState.append("Draw");
            } else {
                endGameState.append("Game is not started");
            }
            gameState = endGameState.toString();
        } else {
            StringBuilder string = new StringBuilder();
            string.append("The turn of ");
            if (xTurn) {
                string.append(player1Button.getText())
                        .append(" Player (X)");
            } else {
                string.append(player2Button.getText())
                        .append(" Player (O)");
            }
            gameState = string.toString();
        }
    }

    private void createAndShowGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tic Tac Toe");
        setLayout(new BorderLayout(0, 0));
        setSize(400, 420);
        setResizable(false);

        JMenuBar menuBar = new MenuBar();
        menuBar.setSize(400, 20);
        add(menuBar, BorderLayout.NORTH);

        JPanel playerBar = new PlayerBar();
        playerBar.setSize(200, 20);
        playerBar.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        add(playerBar, BorderLayout.NORTH);

        boardPanel.setSize(300, 300);
        add(boardPanel, BorderLayout.CENTER);

        JPanel optionsBar = new OptionsBar();
        optionsBar.setSize(200, 20);
        add(optionsBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    private String checkForWin() {
        // check all vertical possibilities
        for (int i = 0; i < boardStatus.length; i++) {
            int sum = 0;
            for (int j = 0; j < boardStatus[i].length; j++) {
                try {
                    sum += boardStatus[i][j];
                } catch (NullPointerException e) {
                    continue;
                }
                if (sum == 3) {
                    endGame("Column", i);
                    return "X";
                } else if (sum == -3) {
                    endGame("Column", i);
                    return "O";
                }
            }
        }

        // check all horizontal possibilities
        for (int i = 0; i < boardStatus.length; i++) {
            int sum = 0;
            for (int j = 0; j < boardStatus[i].length; j++) {
                try {
                    sum += boardStatus[j][i];
                } catch (NullPointerException e) {
                    continue;
                }
                if (sum == 3) {
                    endGame("Row", i);
                    return "X";
                } else if (sum == -3) {
                    endGame("Row", i);
                    return "O";
                }
            }
        }

        // checks all cross possibilities
        try {
            int sum = boardStatus[0][0] + boardStatus[1][1] + boardStatus[2][2];
            if (sum == -3) {
                endGame("Diagonal", 0);
                return "O";
            }
            if (sum == 3) {
                endGame("Diagonal", 0);
                return "X";
            }
        } catch (NullPointerException ignored) {
        }
        try {
            int sum = boardStatus[2][0] + boardStatus[1][1] + boardStatus[0][2];
            if (sum == -3) {
                endGame("Diagonal", 1);
                return "O";
            }
            if (sum == 3) {
                endGame("Diagonal", 1);
                return "X";
            }
        } catch (NullPointerException ignored) {
        }
        if (count == 9) {
            endGame();
            return "Draw";
        }
        return "None";
    }

    private void highlightWin(String type, int which) {
        List<BoardCell> winningCells = new ArrayList<>();
        switch (type) {
            case "Row" -> {
                switch (which) {
                    case 0 -> {
                        winningCells.add(cells.get(6));
                        winningCells.add(cells.get(7));
                        winningCells.add(cells.get(8));
                        setWinningCells(winningCells);
                    }
                    case 1 -> {
                        winningCells.add(cells.get(3));
                        winningCells.add(cells.get(4));
                        winningCells.add(cells.get(5));
                        setWinningCells(winningCells);
                    }
                    case 2 -> {
                        winningCells.add(cells.get(0));
                        winningCells.add(cells.get(1));
                        winningCells.add(cells.get(2));
                        setWinningCells(winningCells);
                    }
                }
            }
            case "Column" -> {
                switch (which) {
                    case 0 -> {
                        winningCells.add(cells.get(6));
                        winningCells.add(cells.get(3));
                        winningCells.add(cells.get(0));

                        setWinningCells(winningCells);
                    }
                    case 1 -> {
                        winningCells.add(cells.get(7));
                        winningCells.add(cells.get(4));
                        winningCells.add(cells.get(1));
                        setWinningCells(winningCells);
                    }
                    case 2 -> {
                        winningCells.add(cells.get(8));
                        winningCells.add(cells.get(5));
                        winningCells.add(cells.get(2));
                        setWinningCells(winningCells);
                    }
                }
            }
            case "Diagonal" -> {
                switch (which) {
                    case 0 -> {
                        winningCells.add(cells.get(6));
                        winningCells.add(cells.get(4));
                        winningCells.add(cells.get(2));
                        setWinningCells(winningCells);
                    }
                    case 1 -> {
                        winningCells.add(cells.get(0));
                        winningCells.add(cells.get(4));
                        winningCells.add(cells.get(8));
                        setWinningCells(winningCells);
                    }
                }
            }
        }

    }

    private void setWinningCells(List<BoardCell> cells) {
        for (BoardCell cell: cells
             ) {
            cell.setEnabled(true);
            cell.setForeground(Color.RED);
        }
    }

    class BoardClass extends JPanel {
        public static JButton ButtonA1, ButtonA2, ButtonA3, ButtonB1, ButtonB2, ButtonB3, ButtonC1, ButtonC2, ButtonC3;

        BoardClass() {
            super();
            setLayout(new GridLayout(3, 3, 2, 2));
            ButtonA3 = new BoardCell(2, 0);
            ButtonA3.setName("ButtonA3");
            cells.add((BoardCell) ButtonA3);
            add(ButtonA3);
            ButtonB3 = new BoardCell(2, 1);
            ButtonB3.setName("ButtonB3");
            cells.add((BoardCell) ButtonB3);
            add(ButtonB3);
            ButtonC3 = new BoardCell(2, 2);
            ButtonC3.setName("ButtonC3");
            cells.add((BoardCell) ButtonC3);
            add(ButtonC3);

            ButtonA2 = new BoardCell(1, 0);
            ButtonA2.setName("ButtonA2");
            cells.add((BoardCell) ButtonA2);
            add(ButtonA2);
            ButtonB2 = new BoardCell(1, 1);
            ButtonB2.setName("ButtonB2");
            cells.add((BoardCell) ButtonB2);
            add(ButtonB2);
            ButtonC2 = new BoardCell(1, 2);
            ButtonC2.setName("ButtonC2");
            cells.add((BoardCell) ButtonC2);
            add(ButtonC2);

            ButtonA1 = new BoardCell(0, 0);
            ButtonA1.setName("ButtonA1");
            cells.add((BoardCell) ButtonA1);
            add(ButtonA1);
            ButtonB1 = new BoardCell(0, 1);
            ButtonB1.setName("ButtonB1");
            cells.add((BoardCell) ButtonB1);
            add(ButtonB1);
            ButtonC1 = new BoardCell(0, 2);
            ButtonC1.setName("ButtonC1");
            cells.add((BoardCell) ButtonC1);
            add(ButtonC1);
        }

        public static void resetBoard() {
            resetCell((BoardCell) ButtonA1);
            resetCell((BoardCell) ButtonA2);
            resetCell((BoardCell) ButtonA3);

            resetCell((BoardCell) ButtonB1);
            resetCell((BoardCell) ButtonB2);
            resetCell((BoardCell) ButtonB3);

            resetCell((BoardCell) ButtonC1);
            resetCell((BoardCell) ButtonC2);
            resetCell((BoardCell) ButtonC3);
        }

        public static void enableBoard() {
            enableCell((BoardCell) ButtonA1);
            enableCell((BoardCell) ButtonA2);
            enableCell((BoardCell) ButtonA3);

            enableCell((BoardCell) ButtonB1);
            enableCell((BoardCell) ButtonB2);
            enableCell((BoardCell) ButtonB3);

            enableCell((BoardCell) ButtonC1);
            enableCell((BoardCell) ButtonC2);
            enableCell((BoardCell) ButtonC3);
        }

        public static void disableBoard() {
            disableCell((BoardCell) ButtonA1);
            disableCell((BoardCell) ButtonA2);
            disableCell((BoardCell) ButtonA3);

            disableCell((BoardCell) ButtonB1);
            disableCell((BoardCell) ButtonB2);
            disableCell((BoardCell) ButtonB3);

            disableCell((BoardCell) ButtonC1);
            disableCell((BoardCell) ButtonC2);
            disableCell((BoardCell) ButtonC3);
        }
    }

    class BoardCell extends JButton implements EventListener {
        boolean isNotClicked = true;
        int xAxis;
        int yAxis;

        BoardCell(int xAxis, int yAxis) {
            super();
            this.xAxis = xAxis;
            this.yAxis = yAxis;
            setFocusPainted(false);
            setText(" ");
            setBackground(Color.DARK_GRAY);
            setFont(new Font("Arial", Font.BOLD, 30));
            setForeground(Color.WHITE);
            setEnabled(false);

            this.addActionListener(actionEvent -> {
                if (gameStarted && !gameOver) {
                    if (xTurn && isNotClicked) {
                        setText("X");
                        count++;
                        xTurn = false;
                        boardStatus[this.yAxis][this.xAxis] = 1;
                    } else if (isNotClicked) {
                        setText("O");
                        count++;
                        xTurn = true;
                        boardStatus[this.yAxis][this.xAxis] = -1;
                    }
                    this.isNotClicked = false;
                }
                checkForWin();
                ComputerPlayer.makeMove();
            });
        }

        public static void resetCell(BoardCell cell) {
            cell.setText(" ");
            cell.setForeground(Color.WHITE);
            cell.isNotClicked = true;
            cell.setEnabled(false);
        }

        public static void enableCell(BoardCell cell) {
            cell.setEnabled(true);
        }

        public static void disableCell(BoardCell cell) {
            cell.setEnabled(false);
        }
    }

    class OptionsBar extends JPanel {
        OptionsBar() {
            super();
            setLayout(new BorderLayout(0, 0));
            setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

            statusLabel.setSize(100, 20);
            statusLabel.setName("LabelStatus");

            add(statusLabel, BorderLayout.WEST);

            setVisible(true);
        }
    }

    static class MenuBar extends JMenuBar {
        JMenu menuGame;
        JMenuItem menuHumanHuman;
        JMenuItem menuHumanRobot;
        JMenuItem menuRobotHuman;
        JMenuItem menuRobotRobot;

        List<JMenuItem> gamePlayerOptions;
        JMenuItem menuExit;
        MenuBar() {
            super();
            gamePlayerOptions = new ArrayList<>();

            menuGame = new JMenu("Game");
            menuGame.setName("MenuGame");
            menuHumanHuman = new JMenuItem("Human vs Human");
            menuHumanHuman.setName("MenuHumanHuman");
            gamePlayerOptions.add(menuHumanHuman);
            menuHumanRobot = new JMenuItem("Human vs Robot");
            menuHumanRobot.setName("MenuHumanRobot");
            gamePlayerOptions.add(menuHumanRobot);
            menuRobotHuman = new JMenuItem("Robot vs Human");
            menuRobotHuman.setName("MenuRobotHuman");
            gamePlayerOptions.add(menuRobotHuman);
            menuRobotRobot = new JMenuItem("Robot vs Robot");
            menuRobotRobot.setName("MenuRobotRobot");
            gamePlayerOptions.add(menuRobotRobot);

            menuExit = new JMenuItem("Exit");
            menuExit.setName("MenuExit");

            super.add(menuGame);
            menuGame.add(menuHumanHuman);
            menuGame.add(menuHumanRobot);
            menuGame.add(menuRobotHuman);
            menuGame.add(menuRobotRobot);
            menuGame.add(new JSeparator());
            menuGame.add(menuExit);

            addListeners();
            setVisible(true);
        }


        private void addListeners () {
            menuExit.addActionListener(e -> System.exit(0));
            for (JMenuItem item : gamePlayerOptions
            ) {
                item.addActionListener(e -> changePlayer(item));
            }
        }

        private void changePlayer (JMenuItem playerOption){
            if (!gameStarted) {
                if (playerOption.getText().equals("Human vs Human")) {
                    player1Button.setText("Human");
                    player2Button.setText("Human");
                    resetStartButton.doClick();
                } else if (playerOption.getText().equals("Human vs Robot")) {
                    player1Button.setText("Human");
                    player2Button.setText("Robot");
                    resetStartButton.doClick();
                } else if (playerOption.getText().equals("Robot vs Human")) {
                    player1Button.setText("Robot");
                    player2Button.setText("Human");
                    resetStartButton.doClick();
                } else if (playerOption.getText().equals("Robot vs Robot")) {
                    player1Button.setText("Robot");
                    player2Button.setText("Robot");
                    resetStartButton.doClick();
                }
            } else {
                resetStartButton.doClick();
                changePlayer(playerOption);
            }

        }
    }

    class PlayerBar extends JPanel {
        PlayerBar() {
            super();
            setLayout(new BorderLayout(55, 10));
            setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

            resetStartButton.setText("Start");
            resetStartButton.setName("ButtonStartReset");
            resetStartButton.setFocusPainted(false);


            player1Button.setSize(10, 20);
            player1Button.setText("Human");
            player1Button.setName("ButtonPlayer1");
            player1Button.setFocusPainted(false);
            addPlayerButtonListener(player1Button);

            player2Button.setSize(10, 20);
            player2Button.setText("Human");
            player2Button.setName("ButtonPlayer2");
            player2Button.setFocusPainted(false);
            addPlayerButtonListener(player2Button);

            resetStartButton.addActionListener(e -> {
                if (!gameStarted) {
                    BoardClass.enableBoard();
                    gameOver = false;
                    gameStarted = true;
                    player1Button.setEnabled(false);
                    player2Button.setEnabled(false);
                    resetStartButton.setText("Reset");
                    xTurn = true;
                } else {
                    resetStartButton.setText("Start");
                    statusLabel.setText("Game is not started");
                    player1Button.setEnabled(true);
                    player2Button.setEnabled(true);
                    BoardClass.resetBoard();
                    xTurn = true;
                    gameOver = false;
                    gameStarted = false;
                    count = 0;
                    boardStatus = new Integer[3][3];
                    updateStatusLabel();

                }
                ComputerPlayer.makeMove();
            });

            add(resetStartButton, BorderLayout.CENTER);
            add(player1Button, BorderLayout.EAST);
            add(player2Button, BorderLayout.WEST);

            setVisible(true);

        }

        private void addPlayerButtonListener(JButton button) {
            button.addActionListener(e -> {
                if (!gameStarted) {
                    if (button.getText().equals("Human")) {
                        button.setText("Robot");
                    } else {
                        button.setText("Human");
                    }
                }
            });
        }
    }

    class ComputerPlayer {
        public static void makeMove() {
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (!gameOver) {
                    if ((player1Button.getText().contains("Robot") && xTurn || player2Button.getText().contains("Robot") && !xTurn)) {
                        clickRandom();
                    }
                }});

        }

        private static void clickRandom() {
            Random r = new Random();
            int i = r.nextInt(9);
            BoardCell cell = cells.get(i);
            if (cell.isNotClicked) {
                cell.doClick(1);
            } else {
                clickRandom();
            }
        }
    }
}