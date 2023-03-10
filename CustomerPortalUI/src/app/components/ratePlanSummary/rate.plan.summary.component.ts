import {Component, OnInit} from '@angular/core';
import {ServiceAgreementService} from '../../services/service.agreement.service';

@Component({
  selector: 'app-rate-plan-summary',
  templateUrl: 'rate.plan.summary.component.html'
})
export class RatePlanSummaryComponent implements OnInit {
  ratePlans: any;

  constructor(private serviceAgreementService: ServiceAgreementService) {
  }

  ngOnInit(): void {
    this.serviceAgreementService.getRatePlans().subscribe(
      (data) => {
        this.ratePlans = data;
        console.log(data);
      });
  }
}
