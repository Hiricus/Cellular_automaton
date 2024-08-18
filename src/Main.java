import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Canvas.LifeCanvas;
import Canvas.LifeRule;


public class Main {
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        int frameHeight = 1300;
        int frameWidth = 1500;

        JFrame frame = new JFrame();
        frame.setLocation(screenWidth/2 - frameWidth/2, screenHeight/2 - frameHeight/2);
        frame.setSize(frameWidth, frameHeight);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);

        // Сцена с клетками
        LifeCanvas canvas = new LifeCanvas(1100, 1100, 5);
        canvas.setLocation(0, 0);
        canvas.setBackground(Color.PINK);
        frame.add(canvas);

        // Панель с кнопками
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(null);
        controlPanel.setBounds(1165, 350, 250, 280);
        controlPanel.setBackground(Color.LIGHT_GRAY);
        frame.add(controlPanel);

        // Кнопки
        // Пауза
        JButton pauseButton = new JButton("Pause");
        pauseButton.setBounds(50, 25, 150, 50);
        controlPanel.add(pauseButton);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (canvas.isRunning()) {
                    canvas.pauseTheSimulation();
                    pauseButton.setText("Unpause");
                } else {
                    canvas.unPauseTheSimulation();
                    pauseButton.setText("Pause");
                }
            }
        });

        // Очистить
        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(50, 100, 150, 50);
        controlPanel.add(clearButton);
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.clear();
            }
        });

        // Рандомизировать
        JButton randomiseButton = new JButton("Randomise");
        randomiseButton.setBounds(50, 175, 150, 50);
        controlPanel.add(randomiseButton);
        randomiseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.randomise();
            }
        });

        // Слайдеры
        JSlider timeSlider = new JSlider(JSlider.HORIZONTAL, 15, 250, 35);
        timeSlider.setBounds(50, 250, 150, 15);
        controlPanel.add(timeSlider);
        timeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                canvas.setDelayMs(timeSlider.getValue());
            }
        });


//        canvas.randomise();
        canvas.setRule(new LifeRule(new int[]{3}, new int[]{2, 3, 4}));

        double bpm = 270;
        int delay = (int) Math.round(60.0 / bpm * 1000);
        canvas.setDelayMs(35);
        canvas.startTheSimulation();
    }
}
