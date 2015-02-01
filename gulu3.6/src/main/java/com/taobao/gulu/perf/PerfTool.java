package com.taobao.gulu.perf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

/**
 * <p>
 * Title: PerfTool.java
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author: gongyuan.cz
 * @email: gongyuan.cz@taobao.com
 * @blog: 100continue.iteye.com
 */
public class PerfTool {

	private static Logger logger = Logger.getLogger(PerfTool.class);
	private Connection conn;
	private Session session;

	public void doPerfTest(PerfInfo perfInfo) throws Exception {

		// create Workbook
		WritableWorkbook wwb = Workbook.createWorkbook(new File(perfInfo
				.getXlsxFilePath()));
		WritableSheet sheet = wwb.createSheet("value", 0);

		String concurrencyList[] = perfInfo.getConcurrencyList().split(" ");
		int row = 1;
		int num = concurrencyList.length;

		for (String concurrency : concurrencyList) {
			String cmd = "";
			cmd = cmd + perfInfo.getABSenderPath() + " -c " + concurrency + " "
					+ perfInfo.getPerfCommand();

			if (isExecuteRemoteCommand(perfInfo)) {

				UserAuthentication server = new UserAuthentication(
						perfInfo.getHost(), perfInfo.getUsername(),
						perfInfo.getPassword());

				doExecuteCommand(server, cmd);
				collectPerfResult(session.getStdout(), row, num, sheet);
				doStopExecute();
			} else {
				String cmdList[] = cmd.split(" ");
				ProcessBuilder builder = new ProcessBuilder(cmdList);
				Process p = builder.start();
				collectPerfResult(p.getInputStream(), row, num, sheet);
				logger.info(builder.command().toString() + " exit_code="
						+ p.waitFor());
			}
			row++;
			TimeUnit.SECONDS.sleep(10);
		}
		wwb.write();
		wwb.close();
	}

	public void processData(ArrayList<PerfInfo> perfInfoArray, String filePath)
			throws IOException, RowsExceededException, WriteException,
			BiffException {
		WritableWorkbook wwb = Workbook.createWorkbook(new File(filePath));
		WritableSheet sheet = wwb.createSheet("value", 0);
		// WritableSheet sheet2 = wwb.createSheet("average_chart",1);
		initComment(perfInfoArray.get(0), sheet);
		int count = 1;

		for (PerfInfo perfInfo : perfInfoArray) {
			editSheet(perfInfo, sheet, count);
			count++;
		}
		wwb.write();
		wwb.close();

	}

	@SuppressWarnings("deprecation")
	public void doChart(String xlsFileName, String fileName,
			int concurrency_num, int perf_num) throws IOException,
			BiffException {
		Workbook wb = Workbook.getWorkbook(new File(xlsFileName));
		Sheet readSheet = wb.getSheet(0);
		DecimalFormat decimalformat1 = new DecimalFormat("##.##");
		int column = 1;
		int row = 1;
		int row_tmp = 1;
		int tmp = row;

		// write Resquest per seconds png file
		DefaultCategoryDataset rpsdataset = new DefaultCategoryDataset();
		for (; column <= perf_num; column++) {
			row = tmp;
			row_tmp = 1;
			for (; row <= concurrency_num; row++, row_tmp++)
				rpsdataset.addValue(Double.parseDouble(readSheet.getCell(
						column, row).getContents()),
						readSheet.getCell(column, 0).getContents(), readSheet
								.getCell(row_tmp, 1 + ((concurrency_num) * 6))
								.getContents());
		}
		JFreeChart rpschart = ChartFactory.createLineChart(
				"Requests Per Second", // chart title
				"Concurrency Level", // domain axis label
				"Requests per second:  [#/sec]", // range axis label
				rpsdataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
				);
		CategoryPlot rpsplot = rpschart.getCategoryPlot();
		// customise the range axis...
		NumberAxis rpsrangeAxis = (NumberAxis) rpsplot.getRangeAxis();
		rpsrangeAxis.setAutoRangeIncludesZero(true);
		rpsrangeAxis.setUpperMargin(0.20);

		LineAndShapeRenderer rpsrenderer = (LineAndShapeRenderer) rpsplot
				.getRenderer();
		rpsrenderer
				.setItemLabelGenerator(new StandardCategoryItemLabelGenerator(
						"{2}", decimalformat1));
		rpsrenderer.setItemLabelsVisible(true);
		rpsrenderer.setBaseItemLabelsVisible(true);
		rpsrenderer.setShapesFilled(Boolean.TRUE);
		rpsrenderer.setShapesVisible(true);

		FileOutputStream rpspng = new FileOutputStream(fileName + "_RPS.png");
		ChartUtilities.writeChartAsPNG(rpspng, rpschart, 1000, 500);
		rpspng.close();

		// write Time per request(mean) png file
		column = 1;
		row_tmp = 1;
		tmp = row;
		row++;
		DefaultCategoryDataset tpsudataset = new DefaultCategoryDataset();
		for (; column <= perf_num; column++) {
			row = tmp + 1;
			row_tmp = 1;
			for (; row <= (tmp + concurrency_num); row++, row_tmp++)
				tpsudataset.addValue(Double.parseDouble(readSheet.getCell(
						column, row).getContents()),
						readSheet.getCell(column, 0).getContents(), readSheet
								.getCell(row_tmp, 1 + ((concurrency_num) * 6))
								.getContents());
		}
		JFreeChart tpsuchart = ChartFactory.createLineChart(
				"Time per request(mean)", // chart title
				"Concurrency Level", // domain axis label
				"Time per request:  [ms] (mean)", // range axis label
				tpsudataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
				);
		CategoryPlot tpsuplot = tpsuchart.getCategoryPlot();
		// customise the range axis...
		NumberAxis tpsurangeAxis = (NumberAxis) tpsuplot.getRangeAxis();
		tpsurangeAxis.setAutoRangeIncludesZero(true);
		tpsurangeAxis.setUpperMargin(0.20);

		LineAndShapeRenderer tpsurenderer = (LineAndShapeRenderer) tpsuplot
				.getRenderer();
		tpsurenderer
				.setItemLabelGenerator(new StandardCategoryItemLabelGenerator(
						"{2}", decimalformat1));
		tpsurenderer.setItemLabelsVisible(true);
		tpsurenderer.setBaseItemLabelsVisible(true);
		tpsurenderer.setShapesFilled(Boolean.TRUE);
		tpsurenderer.setShapesVisible(true);
		FileOutputStream tpsupng = new FileOutputStream(fileName + "_TPSU.png");
		ChartUtilities.writeChartAsPNG(tpsupng, tpsuchart, 1000, 500);
		tpsupng.close();

		// write Time per request([ms] mean, across all concurrent requests) png
		// file
		column = 1;
		row_tmp = 1;
		tmp = row;
		row++;
		DefaultCategoryDataset tpssdataset = new DefaultCategoryDataset();
		for (; column <= perf_num; column++) {
			row = tmp + 1;
			row_tmp = 1;
			for (; row <= (tmp + concurrency_num); row++, row_tmp++)
				tpssdataset.addValue(Double.parseDouble(readSheet.getCell(
						column, row).getContents()),
						readSheet.getCell(column, 0).getContents(), readSheet
								.getCell(row_tmp, 1 + ((concurrency_num) * 6))
								.getContents());
		}
		JFreeChart tpsschart = ChartFactory
				.createLineChart(
						"Time per request(mean, across all concurrent requests))", // chart
																					// title
						"Concurrency Level", // domain axis label
						"Time per request:  [ms] (mean, across all concurrent requests)", // range
																							// axis
																							// label
						tpssdataset, // data
						PlotOrientation.VERTICAL, // orientation
						true, // include legend
						true, // tooltips
						false // urls
				);
		CategoryPlot tpssplot = tpsschart.getCategoryPlot();
		// customise the range axis...
		NumberAxis tpssrangeAxis = (NumberAxis) tpssplot.getRangeAxis();
		tpssrangeAxis.setAutoRangeIncludesZero(true);
		tpssrangeAxis.setUpperMargin(0.20);

		LineAndShapeRenderer tpssrenderer = (LineAndShapeRenderer) tpssplot
				.getRenderer();
		tpssrenderer
				.setItemLabelGenerator(new StandardCategoryItemLabelGenerator(
						"{2}", decimalformat1));
		tpssrenderer.setItemLabelsVisible(true);
		tpssrenderer.setBaseItemLabelsVisible(true);
		tpssrenderer.setShapesFilled(Boolean.TRUE);
		tpssrenderer.setShapesVisible(true);

		FileOutputStream tpsspng = new FileOutputStream(fileName + "_TPSS.png");
		ChartUtilities.writeChartAsPNG(tpsspng, tpsschart, 1000, 500);
		tpsspng.close();

		// write ransfer rate(received) png file
		column = 1;
		row_tmp = 1;
		tmp = row;
		row++;
		DefaultCategoryDataset rrdataset = new DefaultCategoryDataset();
		for (; column <= perf_num; column++) {
			row = tmp + 1;
			row_tmp = 1;
			for (; row <= (tmp + concurrency_num); row++, row_tmp++)
				rrdataset.addValue(Double.parseDouble(readSheet.getCell(column,
						row).getContents()), readSheet.getCell(column, 0)
						.getContents(),
						readSheet.getCell(row_tmp, 1 + ((concurrency_num) * 6))
								.getContents());
		}
		JFreeChart rrchart = ChartFactory.createLineChart(
				"Ransfer Rate(received)", // chart title
				"Concurrency Level", // domain axis label
				"Ransfer Rate([Kbytes/sec] received)", // range axis label
				rrdataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
				);
		CategoryPlot rrplot = rrchart.getCategoryPlot();
		// customise the range axis...
		NumberAxis rrrangeAxis = (NumberAxis) rrplot.getRangeAxis();
		rrrangeAxis.setAutoRangeIncludesZero(true);
		rrrangeAxis.setUpperMargin(0.20);

		LineAndShapeRenderer rrrenderer = (LineAndShapeRenderer) rrplot
				.getRenderer();
		rrrenderer
				.setItemLabelGenerator(new StandardCategoryItemLabelGenerator(
						"{2}", decimalformat1));
		rrrenderer.setItemLabelsVisible(true);
		rrrenderer.setBaseItemLabelsVisible(true);
		rrrenderer.setShapesFilled(Boolean.TRUE);
		rrrenderer.setShapesVisible(true);

		FileOutputStream rrpng = new FileOutputStream(fileName + "_RR.png");
		ChartUtilities.writeChartAsPNG(rrpng, rrchart, 1000, 500);
		rrpng.close();
	}

	private void editSheet(PerfInfo perfInfo, WritableSheet sheet, int column)
			throws BiffException, IOException, RowsExceededException,
			WriteException {
		Workbook wb = Workbook
				.getWorkbook(new File(perfInfo.getXlsxFilePath()));
		Sheet readSheet = wb.getSheet(0);
		String concurrencyList[] = perfInfo.getConcurrencyList().split(" ");
		int num = concurrencyList.length;

		Label title = new Label(column, 0, perfInfo.getPerfTestTitle());
		sheet.addCell(title);

		for (int tmp = 1; tmp <= (num) * 6; tmp++) {
			Cell cell = readSheet.getCell(0, tmp);
			jxl.write.Number number = new jxl.write.Number(column, tmp,
					Double.parseDouble(cell.getContents()));
			sheet.addCell(number);
		}

		wb.close();
	}

	private void initComment(PerfInfo perfInfo, WritableSheet sheet)
			throws RowsExceededException, WriteException {
		// init comment
		String concurrencyList[] = perfInfo.getConcurrencyList().split(" ");
		int num = concurrencyList.length;
		int tmp = 1;

		// Requests per second:
		Label rps = new Label(0, tmp, "Requests per second: ");
		sheet.addCell(rps);

		// Time per request([ms] mean):
		Label tpru = new Label(0, tmp + num, "Time per request([ms] mean): ");
		sheet.addCell(tpru);

		// Time per request([ms] mean, across all concurrent requests):
		Label tprs = new Label(0, tmp + (num * 2),
				"Time per request([ms] mean, across all concurrent requests): ");
		sheet.addCell(tprs);

		// Transfer rate([Kbytes/sec] received):
		Label tr = new Label(0, tmp + (num * 3),
				"Transfer rate([Kbytes/sec] received): ");
		sheet.addCell(tr);

		// Time taken for tests(seconds):
		Label ttft = new Label(0, tmp + (num * 4),
				"Time taken for tests(seconds): ");
		sheet.addCell(ttft);

		// Complete requests:
		Label cr = new Label(0, tmp + (num * 5), "Complete requests: ");
		sheet.addCell(cr);

		// concurrency info:
		Label ci = new Label(0, tmp + (num * 6), "concurrency info: ");
		sheet.addCell(ci);

		// tmp
		for (int column = 0; column <= (num - 1); column++) {

			sheet.addCell(new jxl.write.Number(column + 1, tmp + (num * 6),
					Double.parseDouble(concurrencyList[column])));
		}

	}

	private boolean isExecuteRemoteCommand(PerfInfo perfInfo) {
		return !(perfInfo.getHost() == null || perfInfo.getUsername() == null || perfInfo
				.getPassword() == null);
	}

	private void initSession(UserAuthentication server) throws Exception {
		conn = new Connection(server.getHost());
		conn.connect();
		boolean success = conn.authenticateWithPassword(server.getUsername(),
				server.getPassword());
		if (success) {
			logger.info("initialize session SUCCESS");
		} else {
			logger.info("initialize session FAIL, Host: " + server.getHost());
			throw new Exception("initialize session FAIL");
		}
		session = conn.openSession();
	}

	private void doExecuteCommand(UserAuthentication server, String cmd)
			throws Exception {
		logger.info("execute remote command with result");
		initSession(server);
		logger.info("execute remote command : " + cmd);
		session.execCommand(cmd);
	}

	private void doStopExecute() {
		session.waitForCondition(ChannelCondition.EXIT_STATUS, 0);
		int status = session.getExitStatus();
		logger.info("exit_code : " + status);
		session.close();
		conn.close();
	}

	private void collectPerfResult(InputStream inputStream, int row, int num,
			WritableSheet sheet) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				inputStream));
		StringBuffer sb = new StringBuffer();
		String stringArray[];
		String line = br.readLine();
		while (line != null) {
			sb.append(line);
			System.out.println(line);
			if (line.contains("Requests per second")) {
				stringArray = line.split(":");
				stringArray = stringArray[1].split("\\[");
				double rps = Double.parseDouble(stringArray[0]);
				jxl.write.Number number = new jxl.write.Number(0, row, rps);
				sheet.addCell(number);
			}
			if (line.contains("[ms] (mean)")) {
				stringArray = line.split(":");
				stringArray = stringArray[1].split("\\[");
				double tpru = Double.parseDouble(stringArray[0]);
				jxl.write.Number number = new jxl.write.Number(0, row + num,
						tpru);
				sheet.addCell(number);
			}
			if (line.contains("[ms] (mean, across all concurrent requests)")) {
				stringArray = line.split(":");
				stringArray = stringArray[1].split("\\[");
				double tprs = Double.parseDouble(stringArray[0]);
				jxl.write.Number number = new jxl.write.Number(0, row
						+ (num * 2), tprs);
				sheet.addCell(number);
			}
			if (line.contains("Transfer rate:")) {
				stringArray = line.split(":");
				stringArray = stringArray[1].split("\\[");
				double tr = Double.parseDouble(stringArray[0]);
				jxl.write.Number number = new jxl.write.Number(0, row
						+ (num * 3), tr);
				sheet.addCell(number);
			}
			if (line.contains("Time taken for tests")) {
				stringArray = line.split(":");
				stringArray = stringArray[1].split("s");
				double ttft = Double.parseDouble(stringArray[0]);
				jxl.write.Number number = new jxl.write.Number(0, row
						+ (num * 4), ttft);
				sheet.addCell(number);
			}
			if (line.contains("Complete requests")) {
				stringArray = line.split(":");
				double cr = Double.parseDouble(stringArray[1]);
				jxl.write.Number number = new jxl.write.Number(0, row
						+ (num * 5), cr);
				sheet.addCell(number);
			}

			line = br.readLine();
		}
	}

}
