package sales;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class SalesAppTest {

	@Test
	public void testGenerateReport() {
		
		SalesApp salesApp = spy(new SalesApp());

		Sales mSales = mock(Sales.class);
		List<SalesReportData> reportDataList = new ArrayList<>();
		List<String> headers = Arrays.asList("Sales ID", "Sales Name");

		SalesDao mSalesDao = mock(SalesDao.class);
		SalesReportDao mSalesReportDao = mock(SalesReportDao.class);
		EcmService mEcmService = mock(EcmService.class);
		SalesActivityReport mSalesActivityReport = mock(SalesActivityReport.class);

		when(mSalesDao.getSalesBySalesId(anyString())).thenReturn(mSales);
		doReturn(false).when(salesApp).hasEffective(any());
		doReturn(mSalesDao).when(salesApp).createSaleDao();
		doReturn(mSalesReportDao).when(salesApp).createSalesReportDao();
		when(mSalesReportDao.getReportData(any())).thenReturn(reportDataList);
		doReturn(headers).when(salesApp).createHeaders(anyBoolean());
		doReturn(mEcmService).when(salesApp).createEcmService();
		doReturn(mSalesActivityReport).when(salesApp).generateReport(headers, reportDataList);
		when(mSalesActivityReport.toXml()).thenReturn("I'll be mocked!");

		salesApp.generateSalesActivityReport("DUMMY", 0, false, false);

		verify(salesApp).generateReport(headers, reportDataList);
	}

	@Test
	public void testGenerateReportNotHaveEffective() {

		SalesApp salesApp = spy(new SalesApp());

		Sales mSales = mock(Sales.class);
		List<SalesReportData> reportDataList = new ArrayList<>();
		List<String> headers = Arrays.asList("Sales ID", "Sales Name");

		SalesDao mSalesDao = mock(SalesDao.class);
		SalesReportDao mSalesReportDao = mock(SalesReportDao.class);
		EcmService mEcmService = mock(EcmService.class);
		SalesActivityReport mSalesActivityReport = mock(SalesActivityReport.class);

		doReturn(mSalesDao).when(salesApp).createSaleDao();
		doReturn(mSalesReportDao).when(salesApp).createSalesReportDao();
		when(mSalesDao.getSalesBySalesId(anyString())).thenReturn(mSales);
		doReturn(true).when(salesApp).hasEffective(any());

		salesApp.generateSalesActivityReport("DUMMY", 0, false, false);

		verify(salesApp, times(0)).generateReport(headers, reportDataList);
	}
}
