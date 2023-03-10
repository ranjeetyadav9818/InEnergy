package com.inenergis.oneShot.service;

import com.inenergis.oneShot.dao.InvoiceDao;
import com.inenergis.entity.billing.Invoice;
import com.inenergis.model.ElasticInvoice;
import com.inenergis.model.ElasticInvoiceConverter;
import io.searchbox.client.JestClient;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static com.inenergis.model.ElasticInvoice.INVOICE;


/**
 * Created by egamas on 05/09/2017.
 */
@Component
public class ElasticInvoiceService {

    private static final Logger log = LoggerFactory.getLogger(ElasticInvoiceService.class);


    @Autowired
    private InvoiceDao invoiceDao;

    @Transactional
    public boolean sendInvoiceToElastic(JestClient client, int page, int size) {
        try{
            Page<Invoice> all = invoiceDao.findAll(new PageRequest(page, size));
            if(all.getNumberOfElements()>0){
                Bulk.Builder builder = new Bulk.Builder();
                builder.defaultIndex("energy_array").defaultType(INVOICE);
                for (Invoice invoice : all) {
                    ElasticInvoice entity = ElasticInvoiceConverter.convert(invoice);
                    Index index = new Index.Builder(entity).index("energy_array").type(INVOICE).id(invoice.getId().toString()).build();
                    builder.addAction(index);
                }
                client.execute(builder.build());
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            log.error("IOException in the scheduled task", e);
            return false;
        } finally {
            client.shutdownClient();
        }
    }
}
