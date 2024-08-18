package Canvas;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class LifeCanvas extends JPanel implements Runnable {
    int width;
    int height;
    int cellSize;

    int cellCountX;
    int cellCountY;
    int[][] canvas;

    boolean isRunning = false;
    int delayMs = 50;
    LifeRule rule = new LifeRule(new int[]{3}, new int[]{2, 3});

    public int getCellCountX() {
        return cellCountX;
    }
    public int getCellCountY() {
        return cellCountY;
    }

    public boolean isRunning() {
        return isRunning;
    }


    public void setDelayMs(int delayMs) {
        this.delayMs = delayMs;
    }

    public void setRule(LifeRule rule) {
        this.rule = rule;
    }

    public int[] getCellFromCoords(int x, int y) {
        int[] result = new int[2];
        result[0] = x / cellSize;
        result[1] = y / cellSize;
        return result;
    }

    public LifeCanvas(int width, int height, int cellSize) {
        super();

        // Добавляем слушатель событий мыши
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int[] cellIDS = getCellFromCoords(e.getX(), e.getY());
                System.out.println(cellIDS[0] + " " + cellIDS[1]);

                switchCell(cellIDS[0], cellIDS[1]);
                repaint();
            }
        });

        if ((cellSize > width) | (cellSize > height)) {
            throw new IllegalArgumentException("Ты тупой");
        }

        // Делим на размер клетки чтобы в окно входило целое число клеток
        this.width = width / cellSize * cellSize;
        this.height = height / cellSize * cellSize;
        this.cellSize = cellSize;
        this.setSize(this.width, this.height);

        // Число клеток по осям
        this.cellCountX = this.width / cellSize;
        this.cellCountY = this.height / cellSize;
        canvas = new int[cellCountY][cellCountX];

        // Заполняем массив нулями
        for (int i = 0; i < cellCountY; i++) {
            for (int j = 0; j < cellCountX; j++) {
                canvas[i][j] = 0;

            }
        }
    }

    @Override
    public void paint(Graphics g) {
        int pivotY = 0;
        for (int i = 0; i < cellCountY; i++) {
            int pivotX = 0;

            for (int j = 0; j < cellCountX; j++) {
                if (canvas[i][j] == 1) {
                    g.setColor(Color.GREEN);
                } else if (canvas[i][j] == 0) {
                    g.setColor(Color.BLACK);

                    // Для тестов
                } else if (canvas[i][j] == 2) {
                    g.setColor(Color.GREEN);
                } else if (canvas[i][j] == 3) {
                    g.setColor(Color.RED);
                }

                g.fillRect(pivotX, pivotY, cellSize, cellSize);
                pivotX += cellSize;
            }
            pivotY += cellSize;
        }
    }

    public void killCell(int x, int y) {
        if (canvas[y][x] != 0) {
            canvas[y][x] = 0;
        }
    }
    public void realiveCell(int x, int y) {
        if (canvas[y][x] != 1) {
            canvas[y][x] = 1;
        }
    }
    public void switchCell(int x, int y) {
        if (canvas[y][x] == 0) {
            canvas[y][x] = 1;
        } else {
            canvas[y][x] = 0;
        }
    }

    public void randomise() {
        Random r = new Random();

        for (int i = 0; i < cellCountY; i++) {
            for (int j = 0; j < cellCountX; j++) {
                canvas[i][j] = r.nextInt(0, 2);
            }
        }
        this.repaint();
    }

    public void clear() {
        for (int i = 0; i < cellCountY; i++) {
            for (int j = 0; j < cellCountX; j++) {
                canvas[i][j] = 0;
            }
        }
        this.repaint();
    }

    // Запускает симуляцию в отдельном потоке
    public void startTheSimulation() {
        isRunning = true;
        Thread simulationThread = new Thread(this);
        simulationThread.setDaemon(true);
        simulationThread.start();
        System.out.println("Simulating thread is started");
    }

    @Override
    public void run() {
        try {
            while (this.isRunning) {
                nextGeneration();
                this.repaint();
                Thread.sleep(delayMs);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void pauseTheSimulation() {
        this.isRunning = false;
        System.out.println("Paused");
    }
    public void unPauseTheSimulation() {
        this.isRunning = true;
        this.startTheSimulation();
        System.out.println("Unpaused");
    }

    public void nextGeneration() {
        int[][] subCanvas = new int[cellCountY][cellCountX];
        int currentNeighbors;
        for (int i = 0; i < cellCountY; i++) {
            for (int j = 0; j < cellCountX; j++) {
                currentNeighbors = countNeighbors(j, i);
                if (canvas[i][j] == 1) {
                    if (rule.checkIfStays(currentNeighbors)) {
                        subCanvas[i][j] = 1;
                    } else {
                        subCanvas[i][j] = 0;
                    }
                } else {
                    if (rule.checkIfBorn(currentNeighbors)) {
                        subCanvas[i][j] = 1;
                    } else {
                        subCanvas[i][j] = 0;
                    }
                }
            }
        }
        canvas = subCanvas.clone();
    }

    // Считает живых соседей у одной клетки
    public int countNeighbors(int x, int y) {
        int alive = 0;

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int currentX = x + j;
                int currentY = y + i;

                // Скип если попали в проверяемую клетку
                if (currentX == x && currentY == y) {
                    // canvas[currentY][currentX] = 3;
                    continue;
                }

                // Проверка на границы
                if (currentX < 0) {
                    currentX = cellCountX - 1;
                }
                if (currentX >= cellCountX) {
                    currentX = 0;
                }
                if (currentY < 0) {
                    currentY = cellCountY - 1;
                }
                if (currentY >= cellCountY) {
                    currentY = 0;
                }

                if (canvas[currentY][currentX] == 1) {
                    alive++;
                }
            }
        }
        return alive;
    }
}

