import {Component} from '@angular/core';

const CONSUMPTION = true;
const BILLING = false;

@Component({
  selector: 'app-chart-selector',
  templateUrl: 'chart.selector.html'
})
export class ChartSelectorComponent {
  selected = CONSUMPTION;

  onClick(input) {
    this.selected = !this.selected;
  }
}
