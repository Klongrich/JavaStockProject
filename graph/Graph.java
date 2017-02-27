package graph;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by klongrich on 2/23/17.
 */

import data.qoutes;

public class Graph extends JFrame implements ActionListener, MouseListener {

    JButton onemin;
    JButton fivemin;
    JButton tenmin;
    JButton fiftenmin;
    JButton thritymin;

    private int width;
    private int height;
    private double max = 0;
    private double min = 0;
    private double xvalue = 0;
    private double yvalue = 0;
    private int numberofdays = 2;
    private int intervals = 60;
    private boolean newchart = true;
    String tick;
    private ArrayList<Double> ypoints = new ArrayList();

    Graph(String tick)
    {
        width = 1000;
        height = 550;
        this.tick = tick;

        ArrayList <Float> x = new ArrayList();
        qoutes data = new qoutes(tick,60,2);
        x = data.smoothed();

        setLayout(null);
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        onemin = createButton("1 Min", 70, 60, 2);
        fivemin = createButton("5 Min", 265, 300, 10);
        tenmin = createButton("10 Min", 465, 600, 20);
        fiftenmin = createButton("15 Min", 645, 900, 30);
        thritymin = createButton("30 Min", 845, 1800, 50);

        addMouseListener(this);
        add(onemin, BorderLayout.PAGE_START);
        add(fivemin);
        add(tenmin);
        add(fiftenmin);
        add(thritymin);
        init(x);
        repaint();
    }

    public JButton createButton(String name, int x, int interval, int days) {
        JButton button = new JButton(name);
        button.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList <Float> x = new ArrayList();
                qoutes data = new qoutes(tick, interval, days);
                x = data.smoothed();
                ypoints.clear();
                numberofdays = days;
                intervals = interval;
                init(x);
            }
        });
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
        button.setLocation(x, 515);
        button.setSize(110, 25);
        button.setBackground(Color.gray);
        button.setForeground(Color.white);
        return (button);
    }


    private double maxValue(ArrayList<Float> array) {
        double max;

        max = array.get(0);
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) > max) {
                max = array.get(i);
            }
        }
        return (max);
    }

    private double minValue(ArrayList<Float> array) {
        double min;

        min = array.get(0);
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) < min) {
                min = array.get(i);
            }
        }
        return (min);
    }

    private ArrayList<Double> minmax(ArrayList<Float> data) {
        ArrayList<Double> x = new ArrayList();

        for (int i = 0; i < data.size(); i++)
            x.add((data.get(i) - min) / (max - min));
        return (x);
    }

    public void init(ArrayList<Float> data) {
        ArrayList<Double> convert = new ArrayList();

        max = maxValue(data);
        min = minValue(data);
        convert = minmax(data);

        for (int i = 0; i < data.size(); i++)
            ypoints.add((((400 * convert.get(i)) - 400) * -1) + 50);

        xvalue = width / data.size();
        yvalue = (maxValue(data) - minValue(data)) / 20;
        repaint();
        setVisible(true);
    }

    private void putprice(Graphics g2) {
        double price;

        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.white);
        for (int i = 50; i < height - 50; i += 20) {
            price = max;
            price = Math.round(price * 100.0) / 100.0;
            g.drawString(Double.toString((price)), 20, i + 8);
            g.fillRect(70, i, 5, 3);
            max -= yvalue;
        }
    }

    public void putline(Graphics2D g)
    {
        int x;

        x = 0;
        g.setColor(Color.red);
        if (xvalue > 1)
        {
            xvalue = 1;
        }
        for (double i = 0; i < ypoints.size() - 1; i += xvalue) {
            g.draw(new Line2D.Double(i + 80, ypoints.get(x), i + 80, ypoints.get(x + 1)));
            x++;
        }

    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        System.out.println(newchart);

        if (newchart == true)
        {
            g.setColor(Color.black);
            g.fillRect(0, 0, width, height);
        }


        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        g.drawString("Ticker: " + tick, 80, 25);
        g.drawString("Days : " + numberofdays, 800, 25);

        g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        g.fillRect(70, 50, 3, height - 100);
        g.fillRect(70, height - 50, width - 100, 3);
        putprice(g);
        putline(g);
    }

    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ArrayList <Float> data = new ArrayList();
        int x;
        int y;

        x = e.getX();
        y = e.getY();

        if (x > 70 && y < 500 && x < 970 && y > 50)
        {
            String name = JOptionPane.showInputDialog(this, "Ticker");
            qoutes stuff = new qoutes(name, intervals, numberofdays);
            data = stuff.smoothed();
            if (!data.isEmpty())
            {
                ypoints.clear();
                tick = name;
                init(data);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}

