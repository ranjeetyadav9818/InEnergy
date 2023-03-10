import {Component} from '@angular/core';
import {InvoiceService} from '../../services/invoice.service';
import {DatePipe} from '@angular/common';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-monthly-billing',
  templateUrl: 'monthly.billing.html'
})
export class MonthlyBillingComponent {
  monthlyBilling: any = {};
  options: any = {
    responsive: true,
    title: {
      display: true,
      text: 'Total Billing By Month'
    },
    scales: {
      yAxes: [{
        display: true,
        position: 'left',
        ticks: {
          callback: function (label, index, labels) {
            return '$' + ( (label / 100 ).toFixed(0) ); // To format labels for y axis
          }
        },
        scaleLabel: {
          display: true,
          labelString: 'Billing'
        }
      }],
    },
    tooltips: {
      callbacks: {
        label: function (tooltipItem, data) {
          return '$' + ( (Number(tooltipItem.yLabel) / 100 ).toFixed(2) );
        }
      }
    }
  };
  aggregatedByDate: Map<String, Number>;

  constructor(private invoiceService: InvoiceService, private datePipe: DatePipe, private authService: AuthService) {

    this.invoiceService.retrieveBillingData().subscribe(
      (data) => {
        let aggregated = new Map<String, Number>();
        console.log('invoices size ', data.length)
        data.forEach(invoice => {
          let date = new Date(invoice.dateTo);
          let monthStartingAt1 = ( date.getMonth() + 1);
          let key = date.getFullYear() + '' +
            (monthStartingAt1 <= 9 ? '0' + monthStartingAt1 : monthStartingAt1);
          // Padding the month number to match format expected by date
          if (aggregated.has(key)) {
            let value = aggregated.get(key) + invoice.total;
            aggregated.set(key, value);
          } else {
            aggregated.set(key, invoice.total);
          }
        });
        let keys = Array.from(aggregated.keys());
        keys.sort((date1, date2) => {
          let n1 = Number(date1);
          let n2 = Number(date2);
          if (n1 < n2) {
            return -1;
          } else if (n1 > n2) {
            return n2;
          }
          return 0;
        });

        this.aggregatedByDate = aggregated;
        let labels: string[] = [];
        let datashets = [
          {
            label: 'SA: ' + authService.serviceAgreementId,
            backgroundColor: '#9CCC65',
            borderColor: '#7CB342',
            data: []
          }
        ];
        keys.forEach(
          (key) => {
            let date: Date = new Date(key.substr(0, 4) + '-' + key.substr(4));
            let label = datePipe.transform(date, 'MMM yy');
            labels.push(label);
            datashets[0].data.push(this.aggregatedByDate.get(key));
          }
        );
        this.monthlyBilling = {
          labels: labels,
          datasets: datashets
        }
      },
      (err) => {
      },
      () => {
        console.log('Done ', JSON.stringify(this.monthlyBilling));
      }
    );
  }
}
