package com.LMCloud.android.MyStocks.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.LMCloud.android.MyStocks.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistQuotesRequest;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

// the spinner part is inspired by http://www.mysamplecode.com/2012/03/android-spinner-arrayadapter.html
// YahooFinance-api http://grepcode.com/search/usages?type=method&id=repo1.maven.org%24maven2@com.yahoofinance-api%24YahooFinanceAPI@1.3.0@yahoofinance%24histquotes@HistQuotesRequest@%3Cinit%3E%28java.lang.String%2Cjava.util.Calendar%2Cjava.util.Calendar%2Cyahoofinance.histquotes.Interval%29&k=u

public class DetailedStockActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailedStockActivity.class.getSimpleName();
    public static String symbol;
    Calendar mTo = Calendar.getInstance();
    //private RelativeLayout mChartHolder;
    //Date date = new Date();
    private LinearLayout mDetailLayout;// = (LinearLayout) findViewById(R.id.container_detail);
    /**
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                List<HistoricalQuote> results  = (List<HistoricalQuote>) msg.obj;
                drawChart(results);
            }
        }
    };

     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        //mChartHolder = (RelativeLayout)findViewById(R.id.container_detail);
        symbol = getIntent().getExtras().getString("symbol");
        getSupportActionBar().setTitle(symbol);

        //Dynamically generate a spinner data
        createSpinnerDropDown();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //Add ranges into spinner dynamically
    private void createSpinnerDropDown() {
        //get reference to the spinner from the XML layout
        Spinner spinner = (Spinner) findViewById(R.id.range_spinner);
        //Array list of animals to display in the spinner
        ArrayList<String> ranges = new ArrayList<>();
        ranges.add(getString(R.string.year));
        ranges.add(getString(R.string.week));
        ranges.add(getString(R.string.month));
        //create an ArrayAdaptar from the String Array
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, ranges);
        //set the view for the Drop down list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the ArrayAdapter to the spinner
        spinner.setAdapter(dataAdapter);
        //attach the listener to the spinner
        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            String selectedItem = parent.getItemAtPosition(pos).toString();
            //Locale localeUS = new Locale("en", "US");
            //DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", localeUS);
            //String startDate = formatter.format(System.currentTimeMillis());
            //Date date = new Date();


            //check which spinner triggered the listener
            switch (selectedItem) {
                case "Past Week":

                    Thread tW = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Calendar fromW = Calendar.getInstance();
                            fromW.add(Calendar.WEEK_OF_YEAR, -1); // from 1 week ago
                            HistQuotesRequest hist_week = new HistQuotesRequest(symbol, fromW, mTo, Interval.DAILY);
                            List<HistoricalQuote> results = null;
                            try {
                                results = hist_week.getResult();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d(LOG_TAG, "stock_quote", e);
                            }
                            drawChart(results, "Day");
                            //List<HistoricalQuote> results = YahooFinance.get(symbol).getHistory(fromY, to, Interval.DAILY);
                        }
                    });
                    tW.start();
                    /**
                    Calendar fromW = Calendar.getInstance();
                    fromW.add(Calendar.WEEK_OF_YEAR, -1); // from 1 week ago
                    //HistQuotesRequest hist_week = new HistQuotesRequest(symbol, from, to, Interval.DAILY);
                    try {
                        //List<HistoricalQuote> results = hist_week.getResult();
                        List<HistoricalQuote> results = YahooFinance.get(symbol).getHistory(fromW, mTo, Interval.DAILY);
                        drawChart(results);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                     */
                    break;
                case "Past Month":
                    Thread tM = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Calendar fromM = Calendar.getInstance();
                            fromM.add(Calendar.MONTH, -1); // from 1 year ago
                            HistQuotesRequest hist_month = new HistQuotesRequest(symbol, fromM, mTo, Interval.DAILY);
                            List<HistoricalQuote> results = null;
                            try {
                                results = hist_month.getResult();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d(LOG_TAG, "stock_quote", e);
                            }
                            drawChart(results, "Day");
                            //List<HistoricalQuote> results = YahooFinance.get(symbol).getHistory(fromY, to, Interval.DAILY);

                        }
                    });
                    tM.start();
                    /**
                    Calendar fromM = Calendar.getInstance();
                    fromM.add(Calendar.MONTH, -1); // from 1 month ago
                    //HistQuotesRequest hist_month = new HistQuotesRequest(symbol, fromM, to, Interval.DAILY);
                    try {
                        //List<HistoricalQuote> results = hist_month.getResult();
                        List<HistoricalQuote> results = YahooFinance.get(symbol).getHistory(fromM, mTo, Interval.DAILY);
                        drawChart(results);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                     */
                    break;
                case "Past Year":

                    // reference: http://grepcode.com/search/usages?type=type&id=repo1.maven.org%24maven2@com.yahoofinance-api%24YahooFinanceAPI@1.3.0@yahoofinance%24histquotes@HistoricalQuote&k=u
                    //try {
                    Thread tY = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Calendar fromY = Calendar.getInstance();
                                fromY.add(Calendar.YEAR, -1); // from 1 year ago
                                //HistQuotesRequest hist_year = new HistQuotesRequest(symbol, fromY, mTo, Interval.WEEKLY);
                                List<HistoricalQuote> results = null;
                                try {
                                    results = YahooFinance.get(symbol).getHistory(fromY, mTo, Interval.WEEKLY);//hist_year.getResult();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.d(LOG_TAG, "stock_quote", e);
                                }
                                drawChart(results, "Week");
                                //List<HistoricalQuote> results = YahooFinance.get(symbol).getHistory(fromY, to, Interval.DAILY);
                                /**
                                if (results != null) {
                                    Message msg = new Message();
                                    msg.obj = results;
                                    msg.what = 0;
                                    mHandler.sendMessage(msg);
                                }
                                 */
                            }
                        });
                    tY.start();









                        //Stock google = YahooFinance.get("GOOG");
                        //List<HistoricalQuote> results = google.getHistory(fromY, to, Interval.DAILY);
                        //List<HistoricalQuote> results = YahooFinance.get(symbol).getHistory(fromY, to, Interval.DAILY);
                        //drawChart(results);
                    //} catch (Exception e) {
                    //    e.printStackTrace();
                   // }
                    break;
            }
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }

    /**
     * draw the line chart
     *
     * @param results
     */
    private void drawChart(List<HistoricalQuote> results, String step)  {
        // Still not able to remove the previous chart and show the new chart automatically,
        // user need to touch the screen for the new chart to appear
        mDetailLayout = (LinearLayout) findViewById(R.id.container_detail);
        LineChart mChart = (LineChart) findViewById(R.id.chart);

        mChart.setDrawGridBackground(true);
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // creating list of entry
        ArrayList<Entry> entries = new ArrayList<>();
        int num_points = results.size();
        String[] labels = new String[num_points];
        float minQuote = 1000000;
        float maxQuote = 0;
        float[] closes = new float[num_points];
        for (int i = 0; i < num_points; i++) {
            //labels[i] = results.get(i).getDate().toString();
            labels[i] = String.valueOf(i);
            closes[i] = results.get(i).getClose().floatValue();
            entries.add(new Entry(closes[i], i));
            if (closes[i] > maxQuote) maxQuote = closes[i];
            if (closes[i] < minQuote) minQuote = closes[i];
        }
        maxQuote *= 1.3;
        minQuote *= 0.8;
        LineDataSet dataSet = new LineDataSet(entries, step);
        LineData data = new LineData(labels, dataSet);
        //dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        // set XAxis style
        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setTextSize(10f);
        xAxis.setSpaceBetweenLabels(num_points/5);
        xAxis.setTextColor(Color.RED);

        // set the vertical range of the chart
        YAxis leftAxis = mChart.getAxisLeft();
        YAxis rightAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaxValue(maxQuote);
        rightAxis.setAxisMaxValue(maxQuote);
        leftAxis.setAxisMinValue(minQuote);
        rightAxis.setAxisMinValue(minQuote);

        // set the data points
        mChart.setData(data);

        // set the description
        mChart.setDescription("Close prices");
        //LineChart preChart = (LineChart) findViewById(R.id.chart);
        //if (preChart != null) {
         //   mDetailLayout.removeView(preChart);
       // }
       // mDetailLayout.addView(mChart);
    }
}

