package graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.awt.image.*;

/**
 * Created by klongrich on 2/23/17.
 */

import data.qoutes;

public class Graph extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

    double yline = 0;
    double xline = 0;
    boolean newchart = true;
    private int width;
    private int height;
    private double max = 0;
    double maxtemp = 0;
    private double min = 0;
    private double xvalue = 0;
    private double yvalue = 0;
    private int numberofdays = 2;
    private int intervals = 60;
    static String parameters = "14,3";
    String currentinda = "STOCH";
    String tick;
    private ArrayList<Double> ypoints = new ArrayList <Double>();
    static ArrayList<ArrayList<Double>> indactors = new ArrayList <ArrayList<Double>>();
    Timer time;

    ImageIcon icon;
    watchlistbox list;
    Overlays OverlayPanel;
    Indicators inda;
    qoutes data;

    LivePricepanel blah = new LivePricepanel("spy");
    public Graph(String tick)
    {
        blah.setLocation(190, 0);
        add(blah);
        width = 1250;
        height = 710;
        this.tick = tick;
        time = new Timer(100, this);
        time.start();

        list = new watchlistbox();
        list.addticker("SPY");
        list.setLocation(960, 50);
        add(list);

        ArrayList <Double> x = new ArrayList <Double>();
        data = new qoutes(tick,300,10);
        x = data.smoothed();
        OverlayPanel = new Overlays(data.smoothed());
        init(x);

        inda = new Indicators("SPY");

        getContentPane().setBackground(Color.black);
        setLayout(null);
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(inda);
        inda.setVisible(true);
        inda.setLocation(28, 544);

        int offset = 25;
        int space = 170;

        add(createButton("30 Min", offset, 1800, 50));
        offset += space;
        add(createButton("15 Min", offset, 900, 30));
        offset += space;
        add(createButton("10 Min", offset, 600, 20));
        offset += space;
        add(createButton("5 Min", offset, 300, 10));
        offset += space;
        add(createButton("1 Min", offset, 60, 2));

        add(Overlays());
        add(modifyInda());

        int ofx;
        int xrow;

        ofx = 555;
        xrow = 895;
        add(indicat("RSI", "14", xrow, ofx));
        add(indicat("MACD", "12,26,9", xrow,ofx + 30));
        add(indicat("STOCH", "14,3", xrow, ofx + 60));
        add(indicat("ADX", "14", xrow, ofx + 90));
        add(indicat("OBV", "0", xrow, ofx + 120));

        xrow += 120;
        add(indicat("AROON", "14",  xrow, ofx));
        add(indicat("DPO", "20", xrow,ofx + 30));
        add(indicat("COPPOCK", "10,14,11", xrow, ofx + 60));
        add(indicat("ATR", "14",  xrow, ofx + 90));
        add(indicat("ROC", "14",  xrow, ofx + 120));

        xrow += 120;
        add(indicat("%B", "10", xrow, ofx));
        add(indicat("%R", "20", xrow,ofx + 30));
        add(indicat("TSI", "14,12,26", xrow, ofx + 60));
        add(indicat("PVO", "14", xrow, ofx + 90));
        add(indicat("McClellan", "Unkown", xrow, ofx + 120));

        addMouseListener(this);
        addMouseMotionListener(this);
        setVisible(true);
    }

    public JButton createButton(String name, int x, int interval, int days) {
        JButton button = new JButton(name);
        button.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    newchart = true;
                    ArrayList<Double> x = new ArrayList <Double>();
                    qoutes newdata = new qoutes(tick, interval, days);
                    data = newdata;
                    x = data.smoothed();
                    numberofdays = days;
                    intervals = interval;
                    inda.update(currentinda, parameters, data);
                    OverlayPanel.upadate(x);
                    icon = init(x);
                    repaint();
            }
        });
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
        button.setLocation(x, 509);
        button.setSize(110, 25);
        button.setBackground(Color.gray);
        button.setForeground(Color.white);
        return (button);
    }


    public JButton modifyInda()
    {
        JButton button = new JButton("+");
        button.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
        button.setLocation(1130, 510);
        button.setSize(25, 25);
        button.setBackground(Color.gray);
        button.setForeground(Color.white);
        return (button);
    }


    public JButton Overlays()
    {
        JButton button = new JButton("Super Awesome Overlays");
        button.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OverlayPanel.setVisible(true);
            }
        });
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
        button.setLocation(360, 10);
        button.setSize(180, 25);
        button.setBackground(Color.gray);
        button.setForeground(Color.white);
        return (button);

    }

    public JButton indicat(String type, String values, int x, int y)
    {
        JButton button = new JButton(type);
        button.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (values != "0")
                    parameters = JOptionPane.showInputDialog(type, values);
                else
                    parameters = "0";
                inda.update(type, parameters, data);
                currentinda = type;
            }
        });
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
        button.setLocation(x, y);
        button.setSize(100, 25);
        button.setBackground(Color.gray);
        button.setForeground(Color.white);
        return (button);
    }

    private double maxValue(ArrayList<Double> array) {
        double max;

        max = array.get(0);
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) > max) {
                max = array.get(i);
            }
        }
        return (max);
    }

    private double minValue(ArrayList<Double> array) {
        double min;

        min = array.get(0);
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) < min) {
                min = array.get(i);
            }
        }
        return (min);
    }

    private ArrayList<Double> minmax(ArrayList<Double> data) {
        ArrayList<Double> x = new ArrayList <Double>();
        for (int i = 0; i < data.size(); i++)
            x.add((data.get(i) - min) / (max - min));
        return (x);
    }

    public ImageIcon init(ArrayList<Double> data)
    {
        min = minValue(data);
        max = maxValue(data);
        ypoints = points(data);
        xvalue = width / data.size();
        yvalue = (maxValue(data) - minValue(data)) / 20;
        int x;

        x = ypoints.size() - 1;

        BufferedImage image = new BufferedImage(800, 450, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color c =new Color(0.5803922f, 0.6f, 0.6392157f, 0.2f);
        g.setColor(c);
        for (int i = 0; i < 450; i += 20)
        {
            g.drawLine(0, i, 900, i);
        }

        g.setFont(new Font("TimesRoman", Font.PLAIN, 100));
        g.drawString(tick, 280, 230);

        if (xvalue > 1)
            xvalue = 1;
        g.setColor(Color.red);
        for (double i = 800; i > 800 - ypoints.size() + 1 ; i -= xvalue) {
            x--;
            if (ypoints.get(x) > ypoints.get(x + 1))
                g.setColor(Color.green);
            else
                g.setColor(Color.red);
            g.draw(new Line2D.Double(i, ypoints.get(x), i , ypoints.get(x + 1)));
        }

        double test;

        ArrayList <Double> convert = new ArrayList <Double> ();
        g.setColor(Color.green);
        indactors = OverlayPanel.indactors;
        for (int ii = 0; ii < indactors.size(); ii++)
        {
            convert = points(indactors.get(ii));
            x = convert.size() - 1;
            g.setColor(Color.blue);
            for (double i = 800; i > 780 - convert.size() + 21; i -= (width / convert.size())) {
                x--;
                g.draw(new Line2D.Double(i, convert.get(x), i , convert.get(x + 1)));
            }
        }

        //Drawing x-axis
        g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        int j = 11;
        String temp;
        for (int i = 800; i > 0; i--)
        {
            if (i % 79 == 0)
            {
                g.setColor(Color.white);
                temp = "03/" + Integer.toString(j);
                if (i != 790)
                    //g.drawString(temp, i - 5, 440);
                j--;
                g.fillRect(i + 10, 445, 2, 5);
                g.setColor(c);
                g.drawLine(i + 10, 0, i + 10, 500);
            }
        }

        ImageIcon icon = new ImageIcon(image);
        return (icon);

    }

    private void putprice(Graphics g2) {
        double price;

        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.white);
        maxtemp = max;
        for (int i = 50; i < 500; i += 20) {
            price = max;
            price = Math.round(price * 100.0) / 100.0;
            g.drawString(Double.toString((price)), 845, i + 8);
            g.fillRect(831, i, 5, 3);
            max -= yvalue;
        }
        max = maxtemp;
    }


    public ArrayList <Double> points(ArrayList <Double> data)
    {
        ArrayList <Double> convert = new ArrayList <Double>();
        ArrayList <Double> points = new ArrayList <Double>();

        //max = maxValue(data);
        //min = minValue(data);
        convert = minmax(data);

        for (int i = 0; i < data.size(); i++)
            points.add(((400 * convert.get(i)) - 400) * -1);

        return (points);
    }

    public void paint(Graphics g2) {

        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (newchart == true)
        {
            g.setColor(Color.black);
            g.fillRect(840, 48, 70, 450);
            g.fillRect(50, 10, 110, 30);
            g.fillRect(730,10,110, 30);

            //Putting the ticker name and period
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            //g.drawString("Watchlist", 960, 80);
            g.drawString("Indicators", 960, 530);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
            g.drawString("Ticker: " + tick, 50, 25);
            g.drawString("Days : " + numberofdays, 730, 25);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 12));

            //y-axis
            g.fillRect(835, 50, 3, 450);

            //x-axis
            g.fillRect(20, 500, width , 3);
            g.fillRect(20, 540, width , 3);
            putprice(g);
        }

        if (icon != null)
            icon.paintIcon(this, g, 30, 50);

        g.setColor(Color.magenta);
        if (xline > 15 && xline < 825 && yline < 500 && yline > 50)
        {
            g.draw(new Line2D.Double(30.0, yline, 825, yline));
            g.draw(new Line2D.Double(xline, 50, xline, 498));
        }
        list.repaint();
        inda.repaint();
    }

    public void actionPerformed(ActionEvent e) {

    }


    @Override
    public void mouseClicked(MouseEvent e) {
        ArrayList <Double> data = new ArrayList <Double>();
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
                OverlayPanel.upadate(data);
                indactors = OverlayPanel.getinda();
                inda.update(currentinda, parameters, stuff);
                icon = init(data);
                newchart = true;
                blah.update(name);
                repaint();
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
       // System.out.println("X : " + e.getX() * xvalue);
        //System.out.println("Y : " + (((((double)e.getY() - 50) / (500 - 50)) * max) - max) * -1);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.getX() > 30 && e.getX() < 825 && e.getY() < 500 && e.getY() > 50) {
            yline = (double) e.getY();
            xline = (double) e.getX();
            repaint();
        }
        newchart = false;
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        xline = -10;
        yline = -10;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {}
}

