import {Component, OnInit} from '@angular/core';

import {ServiceAgreementService} from '../../services/service.agreement.service';

@Component({
  selector: 'app-service-agreement',
  templateUrl: 'service.agreement.component.html'
})
export class ServiceAgreementComponent implements OnInit {
  serviceAgreement: any;
  servicePoints: any;
  constructor(private serviceAgreementService: ServiceAgreementService) {

  }

  ngOnInit(): void {
    this.serviceAgreementService.getServiceAgreement().subscribe(
      (data) => {
        this.serviceAgreement = data;
        this.servicePoints = data.servicePoints;
        console.log(data);
      });
  }
}
