package com.kane.FXDataMonitor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

/**
 * Created by
 *
 * @author kane
 * @date 1/12/19
 */
@RunWith(MockitoJUnitRunner.class)
public class MonitorTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private Monitor monitor;

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.invokeMethod(monitor, "initURLs");
    }

    @Test
    public void When_OneExDataChange_Expect_OneChangedDataString() {

        String url = "https://eodhistoricaldata.com/api/real-time/AUDUSD.FOREX?api_token=null&fmt=json";
        RealExDataResp realExDataResp = new RealExDataResp();
        realExDataResp.setChange(0.1);
        realExDataResp.setCode("AUDUSD");
        realExDataResp.setClose(0.65);

        Mockito.when(restTemplate.getForObject(url, RealExDataResp.class)).thenReturn(realExDataResp);
        String result = ReflectionTestUtils.invokeMethod(monitor, "fetchChangedFXData");
        Assert.assertTrue(result.contains("\"AUDUSD\":0.65"));

    }

    @Test
    public void When_MultiExDataChange_Expect_MultiChangedDataString(){

        String AUDUSDURL = "https://eodhistoricaldata.com/api/real-time/AUDUSD.FOREX?api_token=null&fmt=json";
        String AUDJPYURL = "https://eodhistoricaldata.com/api/real-time/AUDJPY.FOREX?api_token=null&fmt=json";
        RealExDataResp realAUDUSDExDataResp = new RealExDataResp();
        realAUDUSDExDataResp.setChange(0.1);
        realAUDUSDExDataResp.setCode("AUDUSD");
        realAUDUSDExDataResp.setClose(0.65);

        RealExDataResp realAUDJPYExDataResp = new RealExDataResp();
        realAUDJPYExDataResp.setChange(0.1);
        realAUDJPYExDataResp.setCode("AUDJPY");
        realAUDJPYExDataResp.setClose(73.5);

        Mockito.when(restTemplate.getForObject(AUDUSDURL, RealExDataResp.class)).thenReturn(realAUDUSDExDataResp);
        Mockito.when(restTemplate.getForObject(AUDJPYURL, RealExDataResp.class)).thenReturn(realAUDJPYExDataResp);

        String result = ReflectionTestUtils.invokeMethod(monitor, "fetchChangedFXData");
        Assert.assertTrue(result.contains("\"AUDUSD\":0.65"));
        Assert.assertTrue(result.contains("\"AUDJPY\":73.5"));
    }

    @Test
    public void When_NoExDataChange_Expect_NoChangedDataString() {

        String url = "https://eodhistoricaldata.com/api/real-time/AUDUSD.FOREX?api_token=null&fmt=json";
        RealExDataResp realExDataResp = new RealExDataResp();
        realExDataResp.setChange(0);
        realExDataResp.setCode("AUDUSD");

        Mockito.when(restTemplate.getForObject(url, RealExDataResp.class)).thenReturn(realExDataResp);
        String result = ReflectionTestUtils.invokeMethod(monitor, "fetchChangedFXData");
        Assert.assertNull(result);
    }

    @Test
    public void When_RestClientException_Expect_Null() {
        String url = "https://eodhistoricaldata.com/api/real-time/AUDUSD.FOREX?api_token=null&fmt=json";
        Mockito.when(restTemplate.getForObject(url, RealExDataResp.class)).thenThrow(RestClientException.class);
        String result = ReflectionTestUtils.invokeMethod(monitor, "fetchChangedFXData");
        Assert.assertNull(result);
    }
}