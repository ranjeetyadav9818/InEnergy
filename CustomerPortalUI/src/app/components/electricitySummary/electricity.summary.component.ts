import {Component, OnInit} from '@angular/core';
import {InvoiceService} from '../../services/invoice.service';

@Component({
    selector: 'app-electricity-summary',
    templateUrl: 'electricity.summary.component.html'
  }
)
export class ElectricitySummaryComponent implements OnInit {

  data: any = {
    currentBalance: {},
    lastBill: {},
    lastPayment: {}
  }


  constructor(private invoiceService: InvoiceService) {
  }

  ngOnInit(): void {

    this.invoiceService.retrieveBalanceData().subscribe(
      (data) => {
        this.data.currentBalance = data;
        console.log(data);
      });

    this.invoiceService.retrieveLastPayment().subscribe(
      (data) => {
        this.data.lastPayment = data;
        console.log(data);
      });

    this.invoiceService.retrieveLastInvoice().subscribe(
      (data) => {
        this.data.lastBill = data;
        console.log(data);
      });

  }


}
