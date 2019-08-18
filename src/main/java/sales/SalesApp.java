package sales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SalesApp {

	public void generateSalesActivityReport(String salesId, int maxRow, boolean isNatTrade, boolean isSupervisor) {
		
		SalesDao salesDao = createSaleDao();
		SalesReportDao salesReportDao = createSalesReportDao();
		
		if (salesId == null) {
			return;
		}

		Sales sales = salesDao.getSalesBySalesId(salesId);

		if (hasEffective(sales)) return;

		List<SalesReportData> reportDataList = salesReportDao.getReportData(sales);

		List<SalesReportData> filteredReportDataList = getFilteredReportDataList(isSupervisor, reportDataList);

		filteredReportDataList = getReportDataTemp(maxRow, reportDataList);

		List<String> headers = createHeaders(isNatTrade);

		/* the filteredReportDataList is never used, if it will used in generateReport() ? */
		SalesActivityReport report = this.generateReport(headers, reportDataList);
		
		EcmService ecmService = createEcmService();
		ecmService.uploadDocument(report.toXml());
		
	}

	protected EcmService createEcmService() {
		return new EcmService();
	}

	protected SalesReportDao createSalesReportDao() {
		return new SalesReportDao();
	}

	protected SalesDao createSaleDao() {
		return new SalesDao();
	}

	protected boolean hasEffective(Sales sales) {
		Date today = new Date();
		if (today.after(sales.getEffectiveTo())
				|| today.before(sales.getEffectiveFrom())){
			return true;
		}
		return false;
	}

	protected List<String> createHeaders(boolean isNatTrade) {
		List<String> headers = null;
		if (isNatTrade) {
			return Arrays.asList("Sales ID", "Sales Name", "Activity", "Time");
		}
		return headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");
	}

	protected List<SalesReportData> getReportDataTemp(int maxRow, List<SalesReportData> reportDataList) {
		List<SalesReportData> tempList = new ArrayList<SalesReportData>();
		for (int i=0; i < reportDataList.size() || i < maxRow; i++) {
			tempList.add(reportDataList.get(i));
		}
		return tempList;
	}

	protected List<SalesReportData> getFilteredReportDataList(boolean isSupervisor, List<SalesReportData> reportDataList) {
		List<SalesReportData> filteredReportDataList = new ArrayList<SalesReportData>();
		for (SalesReportData data : reportDataList) {
			if ("SalesActivity".equalsIgnoreCase(data.getType())) {
				if (data.isConfidential()) {
					if (isSupervisor) {
						filteredReportDataList.add(data);
					}
				}else {
					filteredReportDataList.add(data);
				}
			}
		}
		return filteredReportDataList;
	}

	protected SalesActivityReport generateReport(List<String> headers, List<SalesReportData> reportDataList) {
		// TODO Auto-generated method stub
		return null;
	}

}
