package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getExtent(){
        return extent;
    }

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance("Report/Automation-Report.html");
        }
        return extent;
    }

    public static void createInstance(String filePath) {
        ExtentSparkReporter reporter = new ExtentSparkReporter(filePath);
        reporter.config().setTheme(Theme.STANDARD);
        reporter.config().setDocumentTitle("Automation Test Report");
        reporter.config().setReportName("Test Execution Report");

        extent = new ExtentReports();
        extent.attachReporter(reporter);

        extent.setSystemInfo("OS", System.getProperty("Windows"));
        extent.setSystemInfo("System Under Test", System.getProperty("Takealot"));
        extent.setSystemInfo("Tester", "Solly");

    }

    public static void pdfReport(List<TestResult> testResults, Duration executionTime) {
        try {

            Document document = new Document();
            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get("Report/Automation-Report.pdf")));
            document.open();

            // Add title
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.BLUE);
            Paragraph title = new Paragraph("API Automation Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            Font infoFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.DARK_GRAY);
            String reportDateStr = "Report Date: " + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Paragraph reportDate = new Paragraph(reportDateStr, infoFont);
            reportDate.setAlignment(Element.ALIGN_CENTER);
            reportDate.setSpacingAfter(5);
            document.add(reportDate);

            String reportOwnerStr = "Report Owner: Solomon Mathebula";
            Paragraph reportOwner = new Paragraph(reportOwnerStr, infoFont);
            reportOwner.setAlignment(Element.ALIGN_CENTER);
            reportOwner.setSpacingAfter(20);
            document.add(reportOwner);

            long pass = testResults.stream()
                    .filter(t -> "PASS".equalsIgnoreCase(t.getStatus()))
                    .count();
            long fail = testResults.size() - pass;

            DefaultPieDataset<String> ds = new DefaultPieDataset<>();
            ds.setValue("Passed", pass);
            ds.setValue("Failed", fail);

            JFreeChart chart = ChartFactory.createPieChart(
                    "Pass : Fail ratio", ds, true, true, false);

            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setSectionPaint("Passed", new java.awt.Color(0, 128, 0));  // dark green
            plot.setSectionPaint("Failed", new java.awt.Color(178, 34, 34)); // firebrick red

            byte[] chartPng = ChartUtils.encodeAsPNG(chart.createBufferedImage(450, 300));
            com.lowagie.text.Image chartImg = com.lowagie.text.Image.getInstance(chartPng);
            chartImg.setAlignment(Image.ALIGN_CENTER);
            document.add(chartImg);

            // Add table
            PdfPTable table = new PdfPTable(4); // Columns: Scenario, Status, Duration, Message
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            addTableHeader(table);
            for (TestResult result : testResults) {
                addRow(table, result);
            }

            document.add(table);

            Duration run = executionTime;
            Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
            Paragraph exec = new Paragraph(
                    String.format("Execution time: %02d:%02d:%02d (hh:mm:ss)",
                            run.toHoursPart(), run.toMinutesPart(), run.toSecondsPart()),
                    bold);
            exec.setSpacingAfter(12);
            document.add(exec);


            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addTableHeader(PdfPTable table) {
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Stream.of("Scenario", "Status", "Duration", "Message")
                .forEach(header -> {
                    PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                    cell.setBackgroundColor(Color.LIGHT_GRAY);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                });
    }

    private static void addRow(PdfPTable table, TestResult result) {
        Font defaultFont = new Font(Font.HELVETICA, 11, Font.NORMAL);
        Font statusFont = new Font(Font.HELVETICA, 11,
                result.getStatus().equalsIgnoreCase("PASS") ? Font.BOLD : Font.BOLDITALIC,
                result.getStatus().equalsIgnoreCase("PASS") ? Color.GREEN : Color.RED);

        table.addCell(new Phrase(result.getScenario(), defaultFont));
        table.addCell(new Phrase(result.getStatus(), statusFont));
        table.addCell(new Phrase(result.getDuration(), defaultFont));
        table.addCell(new Phrase(result.getMessage(), defaultFont));
    }
}
