import {Component, OnInit} from '@angular/core';
import {InvoiceService} from '../../services/invoice.service';

@Component({
    selector: 'app-invoices-yearly',
    templateUrl: 'invoices.yearly.component.html'
  }
)
export class InvoicesYearlyComponent implements OnInit {

  invoices = [];

  constructor(private invoiceService: InvoiceService) {
  }

  ngOnInit(): void {
    this.invoiceService.retrieveBillingData().subscribe(
      (data) => {
        console.log(data);
        if (data.length > 0) {
          let currentYear = data[0].date.substring(0, 4);
          data.unshift({year: currentYear});
          i = 1;
          while (i < data.length) {
            let newYear = data[i].date.substring(0, 4);
            if (currentYear !== newYear) {
              currentYear = newYear;
              data.splice(i, 0, {year: currentYear});
            }
            i++;
          }
        }
        this.invoices = data;
      });
    let i = 0;
    let elementsByClassName = document.getElementsByClassName('ui-datascroller-content ui-widget-content');
    while (elementsByClassName.length > i) {
      let element = <HTMLElement>elementsByClassName[i];
      element.style.cssText = 'border:0px;background-color: transparent;padding: 0px;';
      i++;
    }
  }
}
