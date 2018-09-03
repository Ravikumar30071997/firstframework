package maven_testing_framework.framework;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import reportgeneration.GenarateHtmlReport;
import reportgeneration.GenerateExcelReport;
import reportgeneration.Screenshots;
import utilities.Browser;
import utilities.ReadFromExcel;

public class Main {

	private String name;
	private String scenarioName;
	private GenerateExcelReport report;
	private String ssPath="\\reportPics\\";
	private ReadFromExcel obData;
	private HashMap<Integer, ArrayList<String>> scenarios;
	private HashMap<Integer, ArrayList<String>> testCases;
	private HashMap<Integer, ArrayList<String>> testMethods;
	//private final String resourcePath="D:\\project reports\\testResources\\";
	//private final String mainReport="D:\\project reports\\selenium\\reportFile\\";
	private String resourcePath="\\testResources\\";
	private String mainPath="\\reportFiles\\";
	private String curDir;
	
	public static void main(String[] args) throws Exception {
		Main call = new Main();
		call.mainControll();
		call.afterTest();
	}
	
	@AfterTest
	public void afterTest() throws IOException
	{
		GenarateHtmlReport ob=new GenarateHtmlReport("newReport.html",curDir);
		ob.generate("testReport.xlsx", "report");
	}
	
	@Test
	public void mainControll() throws Exception
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH_mm_ss");
		LocalDateTime now = LocalDateTime.now();
		
		curDir = System.getProperty("user.dir")+"\\selenium";
		mainPath=curDir+"\\"+dtf.format(now)+mainPath;
		
		resourcePath=curDir+resourcePath;
		ssPath=curDir+"\\"+dtf.format(now)+ssPath;
		
		curDir=curDir+"\\"+dtf.format(now);
		
		File directory = new File(mainPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		directory = new File(ssPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		
		report=new GenerateExcelReport(mainPath+"testReport.xlsx","report");
		report.create();
		obData=new ReadFromExcel();
		scenarios=obData.getData(resourcePath+"Test_Cases.xlsx","Scenarios");
		int i,j;
		for(i=0;i<scenarios.size();i++)
		{
			scenarioName=scenarios.get(i).get(0);
			report.createNewRow();
			report.write(scenarioName);
			this.getTestCases(scenarios.get(i));
			for(j=0;j<testCases.size();j++)
			{
				name=scenarioName+" "+testCases.get(j).get(0);
				report.createNewRow();
				report.write("");
				report.write(testCases.get(j).get(0));
				this.getTestMethods(testCases.get(j));
				this.test1(testMethods);
			}
		}
		report.finish();
	}
	
	public HashMap<Integer, ArrayList<String>> getTestCases(ArrayList<String> scenario) throws IOException
	{
		testCases=obData.getData(resourcePath+"Test_Cases.xlsx", scenario.get(0));
		return testCases;
	}
	
	public HashMap<Integer, ArrayList<String>> getTestMethods(ArrayList<String> testCase) throws IOException
	{
		testMethods=obData.getData(resourcePath+"Test_Cases.xlsx", testCase.get(1));
		return testMethods;
	}

	public void test1(HashMap<Integer, ArrayList<String>> data2) throws Exception {
		
		if(data2==null)
			return;
		
		report.createNewRow();
		report.write("");
		report.write("");

		Class<Browser> Browser2Class = Browser.class;
		Object ob = Browser2Class.newInstance();
		Screenshots ss = new Screenshots(((Browser) ob).getDriver(), ssPath);
		Method m=null;

		int i = 1;
		try {
			report.write("start time");
			report.write(report.timeStamp());
			String mName = "";

			for (i = 1; i < data2.size(); i++) {
				try {
					mName = data2.get(i).get(2);
					//System.out.println(mName)
					m = Browser2Class.getDeclaredMethod(mName, ArrayList.class);
					m.invoke(ob, data2.get(i));
					
				} catch (NoSuchMethodException e) {
					System.out.println("no such method -"+mName);
					continue;
				} catch (Exception e) {
					System.out.println(e);
					throw e;
				}
				if (!("closeBrowser").equals(mName)) {
					ss.capture(name + " sucess " + i);
					report.createNewRow();
					report.write("");
					report.write("");
					report.write(data2.get(i).get(1));
					report.write(ssPath + name + " sucess " + i + ".png");
				}
			}
			report.createNewRow();
			report.write("");
			report.write("");
			report.write("end time");
			report.write(report.timeStamp());
			report.createNewRow();
			report.write("");
			report.write("");
			report.write("result");
			report.write("pass");
		} catch (Exception e) {
			System.out.println(name + " fail " + i+"\n"+data2.get(i));
			ss.capture(name + " fail " + i);
			Thread.sleep(3000);
			m = Browser2Class.getDeclaredMethod("closeBrowser", ArrayList.class);
			m.invoke(ob, data2.get(i));
			report.createNewRow();
			report.write("");
			report.write("");
			report.write(data2.get(i).get(1));
			report.write(ssPath + name + " fail " + i + ".png");

			report.createNewRow();
			report.write("");
			report.write("");
			report.write("end time");
			report.write(report.timeStamp());
			report.createNewRow();
			report.write("");
			report.write("");
			report.write("result");
			report.write("fail");
		}

	}

}
