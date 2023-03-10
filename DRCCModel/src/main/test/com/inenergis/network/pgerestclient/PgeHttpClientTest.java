package com.inenergis.network.pgerestclient;

import com.inenergis.entity.ProductType;
import com.inenergis.entity.ServiceAgreement;
import com.inenergis.entity.bidding.Bid;
import com.inenergis.entity.bidding.Segment;
import com.inenergis.entity.locationRegistration.IsoResource;
import com.inenergis.entity.locationRegistration.RegistrationSubmissionStatus;
import com.inenergis.network.pgerestclient.model.RequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Disabled //These are integration tests
public class PgeHttpClientTest {

    private Properties properties = new Properties();
    private PgeLayer7 pgeLayer7;

    @BeforeEach
    public void before() throws Exception {
        properties.setProperty("pge.api.username", "DemandResponseDRCC");
        properties.setProperty("pge.api.password", "Layer7ControlCenter");
        properties.setProperty("pge.certificate.location", "/opt/certificates/pgeCertificate.p12");
        properties.setProperty("pge.certificate.password", "yw4ZZUx5DL9p5edN");
        properties.setProperty("pge.api.url", "https://apiqa.pge.com/tst/demandresponse/v1/meterdata/availability");
        properties.setProperty("pge.api.numberOfDaysToCheck", "50");
        properties.setProperty("pge.api.availableDaysToBeReady", "45");
        properties.setProperty("pge.api.peakdemand.url", "https://apiqa.pge.com/tst/demandresponse/v1/meterdata/peakdemand");
        properties.setProperty("pge.api.resourceregistration.url", "https://apiqa.pge.com/tst/demandresponse/v1/resource/registration");
        properties.setProperty("pge.api.bid.url", "https://apiqa.pge.com/tst/demandresponse/v1/bid");
        properties.setProperty("pge.api.bidstatus.url", "https://apiqa.pge.com/tst/demandresponse/v1/bid/status");

        pgeLayer7 = new PgeLayer7(properties);
    }

    @Test
    public void validateMeterData() throws Exception {
        System.out.println(pgeLayer7.validateMeterData("3312212385", RequestModel.IdType.SAID));
    }

    @Test
    public void getPeakDemandSingle() throws Exception {
        ServiceAgreement serviceAgreement = new ServiceAgreement();
        serviceAgreement.setSaUuid("4168409005");
        System.out.println(pgeLayer7.getPeakDemand(Collections.singletonList(serviceAgreement), LocalDateTime.now().minusYears(1), LocalDateTime.now()).toString());
    }

    @Test
    public void getPeakDemandArray() throws Exception {
        ServiceAgreement serviceAgreement1 = new ServiceAgreement();
        ServiceAgreement serviceAgreement2 = new ServiceAgreement();
        serviceAgreement1.setSaUuid("4168409005");
        serviceAgreement2.setSaUuid("4168409005");
        System.out.println(pgeLayer7.getPeakDemand(Arrays.asList(serviceAgreement1, serviceAgreement2), LocalDateTime.now().minusYears(1), LocalDateTime.now()).toString());
    }

    @Test
    public void registerResource() throws Exception {
        // Mock Test IsoResource object <<<
        RegistrationSubmissionStatus registrationSubmissionStatus = new RegistrationSubmissionStatus();
        registrationSubmissionStatus.setId(2L);
        registrationSubmissionStatus.setRegistrationStatus(RegistrationSubmissionStatus.RegistrationStatus.REGISTERED);
        registrationSubmissionStatus.setActiveStartDate(Date.from(LocalDateTime.now().minusYears(1).atZone(ZoneId.systemDefault()).toInstant()));
        registrationSubmissionStatus.setActiveEndDate(Date.from(LocalDateTime.now().plusMonths(1).atZone(ZoneId.systemDefault()).toInstant()));
        IsoResource isoResource = new IsoResource();
        isoResource.setName("PGSB_1_RDRR02");
        isoResource.setType(ProductType.RDRR);
        isoResource.setRegistrations(Collections.singletonList(registrationSubmissionStatus));
        registrationSubmissionStatus.setIsoResource(isoResource);
        // >>>

        System.out.println(pgeLayer7.registerRegistration(registrationSubmissionStatus));
    }

    @Test
    public void bid() throws Exception {
        Bid bidMock = new Bid();
        List<Segment> segmentList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            Segment segment = new Segment();
            segment.setName(Integer.toString(i));
            segment.setCapacityHe1(10L);
            segment.setCapacityHe2(20L);
            segment.setCapacityHe3(30L);
            segment.setCapacityHe4(40L);
            segment.setCapacityHe5(50L);
            segment.setCapacityHe6(60L);
            segment.setCapacityHe7(70L);
            segment.setCapacityHe8(80L);
            segment.setCapacityHe9(90L);
            segment.setCapacityHe10(100L);
            segment.setCapacityHe11(110L);
            segment.setCapacityHe12(120L);
            segment.setCapacityHe13(130L);
            segment.setCapacityHe14(140L);
            segment.setCapacityHe15(150L);
            segment.setCapacityHe16(160L);
            segment.setCapacityHe17(170L);
            segment.setCapacityHe18(180L);
            segment.setCapacityHe19(190L);
            segment.setCapacityHe20(200L);
            segment.setCapacityHe21(210L);
            segment.setCapacityHe22(220L);
            segment.setCapacityHe23(230L);
            segment.setCapacityHe24(240L);
            segment.setPriceHe1(1L);
            segment.setPriceHe2(2L);
            segment.setPriceHe3(3L);
            segment.setPriceHe4(4L);
            segment.setPriceHe5(5L);
            segment.setPriceHe6(6L);
            segment.setPriceHe7(7L);
            segment.setPriceHe8(8L);
            segment.setPriceHe9(9L);
            segment.setPriceHe10(10L);
            segment.setPriceHe11(11L);
            segment.setPriceHe12(12L);
            segment.setPriceHe13(13L);
            segment.setPriceHe14(14L);
            segment.setPriceHe15(15L);
            segment.setPriceHe16(16L);
            segment.setPriceHe17(17L);
            segment.setPriceHe18(18L);
            segment.setPriceHe19(19L);
            segment.setPriceHe20(20L);
            segment.setPriceHe21(21L);
            segment.setPriceHe22(22L);
            segment.setPriceHe23(23L);
            segment.setPriceHe24(24L);

            segmentList.add(segment);
        }

        bidMock.setSegments(segmentList);
        IsoResource isoResourceMock = new IsoResource();
        isoResourceMock.setName("Test");
        RegistrationSubmissionStatus registrationSubmissionStatusMock = new RegistrationSubmissionStatus();
        registrationSubmissionStatusMock.setIsoResource(isoResourceMock);
        bidMock.setTradeDate(new java.util.Date());

        System.out.println(pgeLayer7.bid(bidMock, "2017-02-07T10:25:36.436Z"));
    }

    @Test
    public void bidStatus() throws Exception {
        System.out.println(pgeLayer7.getBidStatus("1", "2017-02-07T10:25:36.436Z"));
    }
}