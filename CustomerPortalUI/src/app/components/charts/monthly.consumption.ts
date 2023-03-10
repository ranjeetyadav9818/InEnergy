import {Component} from '@angular/core';
import {InvoiceService} from '../../services/invoice.service';
import {DatePipe} from '@angular/common';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-monthly-consumption',
  templateUrl: 'monthly.consumption.html'
})
export class MonthlyConsumptionComponent {
  monthlyConsumption: any;
  options: any = {
    responsive: true,
    title: {
      display: true,
      text: 'Total Consumption By Month'
    },
    scales: {
      yAxes: [{
        display: true,
        position: 'left',
        ticks: {
          callback: function (label, index, labels) {
            return label + 'Kw/h';
          }
        },
        scaleLabel: {
          display: true,
          labelString: 'Consumption'
        }
      }],
    },
    tooltips: {
      callbacks: {
        label: function(tooltipItem, data) {
          return Number(tooltipItem.yLabel).toFixed(2) + 'Kw/h';
        }
      }
    }
  };

  constructor(private invoiceService: InvoiceService, private datePipe: DatePipe, private authService: AuthService) {

    this.invoiceService.retrieveConsumptionData().subscribe(
      (data) => {
        console.log('Monthly data' + data);
        let labels: string[] = [];
        let datashets = [
          {
            label: 'SA: ' + authService.serviceAgreementId,
            backgroundColor: '#42A5F5',
            borderColor: '#1E88E5',
            data: []
          }
        ];
        data.forEach(monthData => {
          let date: Date = new Date(monthData.month.substr(0, 4) + '-' + monthData.month.substr(4));
          labels.push(datePipe.transform(date, 'MMM yy'));
          datashets[0].data.push(monthData.value);
        });
        this.monthlyConsumption = {
          labels: labels,
          datasets: datashets
        }
      },
      (err) => {},
      () => {
        console.log('Done');
      }
    );
  }
}
