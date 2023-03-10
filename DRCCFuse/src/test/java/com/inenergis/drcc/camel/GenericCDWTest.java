package com.inenergis.drcc.camel;

import com.inenergis.drcc.camel.csv.customer.Customer;
import com.inenergis.drcc.camel.csv.genericCustomer.NewCustomer;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.ini4j.Profile;
import org.ini4j.Wini;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GenericCDWTest {

    Wini ini;

    @BeforeEach
    void init() throws IOException {
        ini = new Wini(this.getClass().getResourceAsStream("/cdw.ini"));
    }

    @Test
    void testCdwProperties() throws IOException {
        String uri = "file:work/drcc/customer/delta/unzip/?charset=%s&delete=%s";
        final Profile.Section cdwSection = ini.get("cdw");
        testProperties(uri, cdwSection);

    }

    private void testProperties(String uri, Profile.Section cdwSection) {
        Assertions.assertNotNull(cdwSection);
        final String charset = cdwSection.get("charset");
        Assertions.assertNotNull(charset);
        final String delete = cdwSection.get("delete");
        Assertions.assertNotNull(cdwSection.get("deleteFtp"));
        Assertions.assertNotNull(cdwSection.get("seda_failedCustomerRow"));
        Assertions.assertNotNull(cdwSection.get("idDeadletter"));
        Assertions.assertNotNull(cdwSection.get("customerRecordFolder"));
        Assertions.assertNotNull(cdwSection.get("customerRecordUnzipFolder"));
        Assertions.assertNotNull(cdwSection.get("idFTPDownload"));
        Assertions.assertNotNull(cdwSection.get("idDeltaUnzipper"));
        Assertions.assertNotNull(cdwSection.get("idCustomerDeltaDataRoute"));
        Assertions.assertNotNull(cdwSection.get("nodeUnenrollDueToCdwChange"));
        Assertions.assertNotNull(cdwSection.get("nodeProcessDeltaCustomer"));
        Assertions.assertNotNull(cdwSection.get("idVModelDeltaSaving"));
        Assertions.assertNotNull(cdwSection.get("idUnenrollDueToCDWChange"));
        Assertions.assertNotNull(cdwSection.get("idCustomerDataRoute"));
        Assertions.assertNotNull(cdwSection.get("idVModelSaving"));
        Assertions.assertNotNull(cdwSection.get("idEnrollmentSaving"));
        String formatteduri = String.format(uri, charset, delete);
        Assertions.assertEquals("file:work/drcc/customer/delta/unzip/?charset=UTF-8&delete=true", formatteduri);
    }

    @Test
    public void testOldCustomerUnmarshall() throws Exception {
        final String line= "20¶6632327884¶6632327863¶2378991214¶9802305155¶5172422751¶OAKWOOD CORP HOUSING¶4750 TASSAJARA RD APT 5107¶¶DUBLIN¶CA¶945684629¶(602) 427-2822¶PO BOX 84470¶¶PHOENIX¶AZ¶850714470¶E1¶Y¶UNKNOWN¶014052101¶2378991205¶N¶N¶N¶03/04/2016 00:00:00¶¶RES¶¶¶N¶¶¶¶¶0¶¶¶NA¶¶6632327322¶03/27/2016 13:15:48¶0¶5319720808¶¶03/04/2016¶¶¶K¶RES¶E Res Individually Metered¶N¶CCB¶¶C&I¶N¶JN¶X¶1006586505¶SM-Read¶01/26/2010¶¶SSN¶SM-15-SIMPLE¶15¶GE¶N¶N¶E-RES¶¶¶PG&E, ESP, Bundled Service¶K¶S¶6632325688¶03/04/2016¶E¶NA¶0¶MIS¶2101¶01405¶ALAMEDA¶Z12¶EBA¶72795¶37.708781¶-121.872283¶316028444300¶¶1K¶¶¶PGE-FS¶\n";

        BindyCsvDataFormat oldCustomerCsv = new BindyCsvDataFormat(Customer.class);
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .onException(Exception.class).log("Exception: ${exception.stacktrace}").to("direct:end").end()
                        .unmarshal(oldCustomerCsv);
            }
        });

        try {
            context.start();
            ProducerTemplate producer = context.createProducerTemplate();
            Customer oldOne = producer.requestBody("direct:start",line,Customer.class);
            oldOne.solveBooleanValues();
            Assert.assertEquals("20",oldOne.getSA_STATUS());
            Assert.assertEquals("PGE-FS",oldOne.getCUSTOMER_LSE_CODE());

        } finally {
            context.stop();
        }

    }


    @Test
    public void testNewCustomerUnmarshall() throws Exception {
        final String lineWithoutPGEFields = "20¶6632327884¶6632327863¶2378991214¶9802305155¶5172422751¶OAKWOOD CORP HOUSING¶4750 TASSAJARA RD APT 5107¶¶DUBLIN¶CA¶945684629¶(602) 427-2822¶PO BOX 84470¶¶PHOENIX¶AZ¶850714470¶E1¶Y¶UNKNOWN¶014052101¶2378991205¶N¶N¶03/04/2016 00:00:00¶¶RES¶0¶¶NA¶¶6632327322¶03/27/2016 13:15:48¶0¶5319720808¶¶03/04/2016¶¶¶K¶RES¶E Res Individually Metered¶N¶CCB¶¶C&I¶N¶JN¶X¶1006586505¶SM-Read¶01/26/2010¶¶SSN¶SM-15-SIMPLE¶15¶N¶E-RES¶¶¶PG&E, ESP, Bundled Service¶K¶S¶6632325688¶03/04/2016¶E¶NA¶0¶MIS¶2101¶01405¶ALAMEDA¶Z12¶EBA¶72795¶37.708781¶-121.872283¶316028444300¶¶1K¶¶¶PGE-FS¶";

        BindyCsvDataFormat newCustomerCsv = new BindyCsvDataFormat(NewCustomer.class);
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("direct:start")
                        .onException(Exception.class).log("Exception: ${exception.stacktrace}").to("direct:end").end()
                        .unmarshal(newCustomerCsv);
            }
        });

        try {
            context.start();

            ProducerTemplate producer2 = context.createProducerTemplate();
            NewCustomer newOne = producer2.requestBody("direct:start",lineWithoutPGEFields,NewCustomer.class);
            Assert.assertEquals("20",newOne.getSA_STATUS());
            Assert.assertEquals("PGE-FS",newOne.getCUSTOMER_LSE_CODE());;
        } finally {
            context.stop();
        }

    }

}
