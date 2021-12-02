pre-requisite
------------

Have installed java 8 JDK with JavaFX (prefer oracle JDK)
-- note : JavaFX is bundled with JRE 8 and JDK 8. The JavaFX jar is jfxrt.jar and resides in the ext folder.

Have installed maven (prefer version apache-maven version 3.6.0)


open terminal on project folder (example on windows c:\\workspace\JfreeChart )

type/execute each command below to run on terminal to be test :

mvn clean package exec:java -Dexec.mainClass=com.jfreechart.trendline.thomas.CombinedView.MultiChartView

-- to stop on terminal for windows Ctr+ Z or Ctr+ C

try the another below :

mvn clean package exec:java -Dexec.mainClass=com.jfreechart.trendline.thomas.OHLCCandleStickTrendLineChart

mvn clean package exec:java -Dexec.mainClass=com.jfreechart.trendline.thomas.CombinedView.OHLCMultiSymbolTrendLineChart

taking note already check the result identical with your refrence this site https://www.tradingview.com/chart/  CMIIW..

please review..

thanks