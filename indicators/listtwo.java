package indicators;

import java.util.ArrayList;

/**
 * Created by klongrich on 3/9/17.
 */
public class listtwo {

    listone x = new listone();

    public ArrayList<Double> macd(ArrayList<Double> data, int period1, int period2)
    {
        ArrayList <Double> answer = new ArrayList <Double>();
        ArrayList <Double> ema1 = new ArrayList<Double>();
        ArrayList <Double> ema2 = new ArrayList<Double>();
        int len;

        ema1 = x.ema(data, period1);
        ema2 = x.ema(data, period2);
        if (ema1.size() < ema2.size())
            len = ema1.size();
        else
            len = ema2.size();
        for (int i = 0; i < len; i++)
        {
            answer.add(ema1.get(i) - ema2.get(i));
        }
        return (answer);
    }

    //Room for speed up.
    public ArrayList <Double> stoch(ArrayList <Double> data, int period)
    {
        ArrayList <Double> answer = new ArrayList <Double>();
        double ll;
        double hh;
        double temp;

        for (int i = period; i < data.size(); i++)
        {
            ll = 0;
            hh = 0;
            for (int x = 0; x < period; x++)
            {
                temp = data.get(i - x);
                if (temp > hh)
                    hh = temp;
                if (temp < ll)
                    ll = temp;
            }
            answer.add(((data.get(i) - ll) / (hh - ll)) * 100);
        }
        return (answer);
    }

    public ArrayList <Double> rsi(ArrayList <Double> data, int period)
    {
        ArrayList <Double> answer = new ArrayList<Double>();
        double gain;
        double loss;
        double rs;
        double change;

        for (int i = period; i < data.size(); i++)
        {
            gain = 0;
            loss = 0;
            for (int x = 0; x < period - 1; x++) {
                if (data.get(i - x) > data.get(i - x - 1))
                    gain += data.get(i - x);
                else
                    loss += data.get(i - x);
            }
            gain /= (double)period;
            loss /= (double)period;
            rs = gain / loss;
            answer.add(100 - (100/( rs + 1)));
        }
        return (answer);
    }

    public ArrayList <Double> obv(ArrayList <Double> data, ArrayList <Double> volume)
    {
        ArrayList <Double> answer = new ArrayList <Double>();
        double temp;

        temp = 0;
        for (int i = 1; i < data.size(); i++)
        {
            if (data.get(i) > data.get(i - 1))
                temp += volume.get(i);
            else
                temp -= volume.get(i);
            answer.add(temp);
        }
        return (answer);
    }

    public ArrayList <Double> roc(ArrayList <Double> data, int period)
    {
        ArrayList <Double> answer = new ArrayList <Double>();
        double prev;

        for (int i = period; i < data.size(); i++)
        {
            prev = data.get(i - period);
            answer.add(((data.get(i) - prev ) / prev) * 100);
        }
        return (answer);
    }

    public ArrayList <Double> wma(ArrayList <Double> data, int period) {
        ArrayList<Double> answer = new ArrayList<Double>();
        double d;
        double temp;

        d = 0;
        for (double x = 0; x <= period; x++)
        {
            d += x;
        }
        for (int i = period; i < data.size(); i++)
        {
            temp = 0;
            for (int n = 0; n < period; n++)
            {
                temp += data.get(i - n) *(n / d);
            }
            answer.add(temp);
        }
        return (answer);
    }

    public ArrayList<Double> cc (ArrayList <Double> data, int p1, int p2, int p3)
    {
        ArrayList <Double> answer = new ArrayList<Double>();
        ArrayList <Double> r1 = new ArrayList<Double>();
        ArrayList <Double> r2 = new ArrayList<Double>();
        ArrayList <Double> w = new ArrayList<Double>();

        r1 = roc(data, p2);
        r2 = roc(data, p3);
        w = wma(r1, p1);
        for (int i = 0; i < w.size(); i++)
        {
            answer.add(w.get(i) + r2.get(i));
        }
        return (answer);
    }

    public ArrayList<Double> w(ArrayList <Double> data, int period)
    {
        ArrayList <Double> answer = new ArrayList<Double>();
        double temp;
        double hh;
        double ll;

        hh = data.get(0);
        ll = data.get(0);
        for (int i = period; i < data.size(); i++)
        {
            for (int j = 0; j < period; j++)
            {
                temp = data.get(i - j);
                if (temp > hh) {
                    hh = temp;
                }
                else if(temp < ll)
                {
                    ll = temp;
                }
            }
            answer.add(((hh - data.get(i)) / (hh - ll)) * -100);
        }
        return (answer);
    }
}


