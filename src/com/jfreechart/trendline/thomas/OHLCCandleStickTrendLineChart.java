/**
 * 
 */
package com.jfreechart.trendline.thomas;


/**
 * @author Ghanshyam Vaghasiya
 *
 */


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.HighLowRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.Regression;
import org.jfree.data.time.Day;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;


/**
 * Example to combine Candlestick and Line Charts in same chart
 * and also convert candlestick to line.
 * plus to update the timeseries.
 * 
 * @author Ghanshyam Vaghasiya
 *
 */
public class OHLCCandleStickTrendLineChart extends JFrame 
{
	private static final long serialVersionUID = 1L;

	private OHLCDataset dataset;
	public static OHLCSeries ohlcSeries;
	private CandlestickRenderer CandleStickRenderer;
	private DateAxis domainAxis;
	private NumberAxis rangeAxis;
	public static XYPlot plot;
	private JFreeChart jfreechart;
	public ChartPanel chartPanel;
	private JPanel panel;
	private XYLineAndShapeRenderer LineRenderer;
	private HighLowRenderer OHLCRenderer;
	private static final Logger log = LoggerFactory.getLogger(OHLCCandleStickTrendLineChart.class);
	static TimeSeries t1 = new TimeSeries("49-day moving average");


	List<OHLCDataItem> dataItems = new ArrayList<OHLCDataItem>();

	 public static void initializeLoggingContext(){
			initializeLoggingContext("conf/log4j2.xml");
		}
		
		public static void initializeLoggingContext(String logFilePath){
			LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
			File file = new File(logFilePath);
			loggerContext.setConfigLocation(file.toURI());
		}
	
	public OHLCCandleStickTrendLineChart(String symbol, int field, int amount, Interval interval) {
		setBackground(Color.WHITE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		dataset = createDataset(symbol, field, amount, interval);

		JFreeChart chart = createChart(dataset, symbol);
		chartPanel = new ChartPanel(chart);
		chartPanel.setMouseWheelEnabled(true);
		chartPanel.setMouseZoomable(true);
		chartPanel.setMouseZoomable(true, true);

		panel = new JPanel(new BorderLayout());
		panel.add(chartPanel, BorderLayout.CENTER);
		getContentPane().add(panel);

		chart.setBackgroundPaint(Color.WHITE);
		chartPanel.setBackground(Color.WHITE);
		panel.setBackground(Color.WHITE);

		XYPlot xyplot = (XYPlot) jfreechart.getPlot();

		/* Draw 49-day moving average Line */

		TimeSeries dataset3 = MovingAverage.createMovingAverage(t1, "LT", 49, 49);
		TimeSeriesCollection collection = new TimeSeriesCollection();
		collection.addSeries(dataset3);

		/* Draw Fitted Regression (Trend) Line */
		double lowerBound = plot.getDomainAxis().getRange().getLowerBound();
		double upperBound = plot.getDomainAxis().getRange().getUpperBound();
		double regressionParameters[] = Regression.getOLSRegression(dataset, 0);

		LineFunction2D linefunction2d = new LineFunction2D(regressionParameters[0], regressionParameters[1]);
		XYDataset regressionData = DatasetUtilities.sampleFunction2D(linefunction2d, lowerBound, upperBound, 100,"Fitted Regression Line");

		xyplot.setDataset(2, collection);
		xyplot.setDataset(3, regressionData);

		xyplot.setRenderer(2, LineRenderer);
		xyplot.setRenderer(3, LineRenderer);

		xyplot.setRenderer(CandleStickRenderer);

		this.setVisible(true);
		this.setSize(1400, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public OHLCDataset createDataset(String symbol, int field, int amount, Interval interval) {
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(field, amount); // from 1 year ago
		try {
			Stock google = YahooFinance.get(symbol);
			List<HistoricalQuote> googleHistQuotes = google.getHistory(from, to, interval);
			ListIterator<HistoricalQuote> historicalQuoteIterator = googleHistQuotes.listIterator();
			while (historicalQuoteIterator.hasNext()) {
				HistoricalQuote historicalQuote = historicalQuoteIterator.next();
				Date date = historicalQuote.getDate().getTime();
				double open = historicalQuote.getOpen().doubleValue();
				double high = historicalQuote.getHigh().doubleValue();
				double low = historicalQuote.getLow().doubleValue();
				double close = historicalQuote.getClose().doubleValue();
				double volume = historicalQuote.getVolume();
				double adjClose = historicalQuote.getAdjClose().doubleValue();
				// adjust data:
				open = open * adjClose / close;
				high = high * adjClose / close;
				low = low * adjClose / close;

				OHLCDataItem item = new OHLCDataItem(date, open, high, low, adjClose, volume);
				dataItems.add(item);
				t1.add(new Day(date), close);
			}

		} catch (Exception e) {
			log.error("Problem retrieving in createDataset", e);
		}
		Collections.reverse(dataItems);

		OHLCDataItem[] data = dataItems.toArray(new OHLCDataItem[dataItems.size()]);
		OHLCDataset dataset = new DefaultOHLCDataset(symbol, data);

		return dataset;
	}

	@SuppressWarnings("deprecation")
	private JFreeChart createChart(final OHLCDataset dataset, String symbol) {
		CandleStickRenderer = new CandlestickRenderer();
		CandleStickRenderer.setSeriesStroke(0, new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		CandleStickRenderer.setSeriesPaint(0, Color.black);

		LineRenderer = new XYLineAndShapeRenderer(true, false);
		LineRenderer.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		LineRenderer.setSeriesPaint(0, Color.RED);
		LineRenderer.setSeriesPaint(1, Color.BLUE);
		LineRenderer.setSeriesPaint(2, Color.GREEN);

		OHLCRenderer = new HighLowRenderer();
		OHLCRenderer.setSeriesStroke(0, new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		OHLCRenderer.setSeriesPaint(0, Color.black);

		domainAxis = new DateAxis();
		domainAxis.setAutoRange(true);
		domainAxis.setTickLabelsVisible(true);
		domainAxis.setAutoTickUnitSelection(true);

		rangeAxis = new NumberAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
		// rangeAxis.setFixedAutoRange(50.0);
		rangeAxis.setAutoRangeIncludesZero(false);
		rangeAxis.setAutoRange(true);

		plot = new XYPlot(dataset, domainAxis, rangeAxis, LineRenderer);
		plot.setBackgroundPaint(Color.LIGHT_GRAY);
		plot.setDomainGridlinePaint(Color.WHITE);
		plot.setRangeGridlinePaint(Color.WHITE);
		plot.setDomainGridlinesVisible(false);
		plot.setRangeGridlinesVisible(false);

		Stock stock = null;
		try {
			stock = YahooFinance.get(symbol);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Problem retrieving in YahooFinance Data", e);
		}

		String info = "Symbol: " + stock.getSymbol() + " , " + "Name: " + stock.getName() + " , " + "Currency: "
				+ stock.getCurrency() + " , " + "StockExchange: " + stock.getStockExchange();

		jfreechart = new JFreeChart(info, new Font("SansSerif", Font.PLAIN, 15), plot, false);
		return jfreechart;
	}
	


	public static void main(String[] args)
	{
		initializeLoggingContext();
		log.info("Init Start");
		new OHLCCandleStickTrendLineChart("AMZN",Calendar.YEAR, -1,Interval.DAILY);
	}	
}
