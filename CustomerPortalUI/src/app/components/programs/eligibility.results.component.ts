import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-eligibility-results',
  templateUrl: 'eligibility.results.component.html'
})
export class EligibilityResultsComponent implements OnInit {

  @Input()
  userSelectedProgramsEligibility: any[] = [];

  @Output()
  nextButtonEmitter = new EventEmitter();

  @Output()
  cancelButtonEmitter = new EventEmitter();


  ngOnInit(): void {
  }

  nextButton(programId, servicePointId) {
    console.log('EligibilityResultsComponent : nextButton programId ' + programId + ' servicePointId ' + servicePointId);
    this.nextButtonEmitter.emit( programId + ',' + servicePointId);
  }

  cancelButton(programId) {
    console.log('EligibilityResultsComponent: cancelButton ', event);
    this.cancelButtonEmitter.emit(programId);
  }
}

