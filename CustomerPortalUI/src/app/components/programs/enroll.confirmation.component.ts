import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ProgramService} from '../../services/program.service';
import {EnrollResult} from './enroll.result';

@Component({
  selector: 'app-enroll-confirmation',
  templateUrl: 'enroll.confirmation.component.html'
})
export class EnrollConfirmationComponent implements OnInit {
  @Input()
  activeIndex = 0;
  @Input()
  selectedProgram: any;
  @Input() finalEnrollResult: any;
  @Output() finalEnrollResultChange = new EventEmitter<EnrollResult>();
  @Input()
  userAppliedPrograms: any;
  @Input()
  aggregators: any;
  @Input()
  servicePointId: any;
  @Input()
  availablePrograms: any;


  @Output()
  cancelButtonEmitter = new EventEmitter();

  selectedAggregator: any;
  fsl = '';
  thirdPartyName = '';

  constructor(private programService: ProgramService) {
  }

  ngOnInit(): void {
    if (this.aggregators && this.aggregators.length > 0) {
      this.selectedAggregator = this.aggregators[0];
      console.log('Selected aggregator', JSON.stringify(this.selectedAggregator));
    } else {
      console.log('Selected aggregator null because aggregtors are empty', JSON.stringify(this.aggregators));
    }
  }

  cancelButton(event) {
    this.cancelButtonEmitter.emit(event);
    console.log('cancelButton ', event);
  }

  onSubmit() {
    this.programService.enroll(this.selectedProgram.id, this.selectedAggregator.value, this.fsl, this.thirdPartyName,
      this.servicePointId).subscribe(
      data => {
        this.finalEnrollResult = data;
        if (this.finalEnrollResult.messages.length === 0) {
          this.userAppliedPrograms.push(this.selectedProgram);
        } else {
          this.availablePrograms.push(this.selectedProgram);
        }
        this.finalEnrollResultChange.emit(this.finalEnrollResult);
      });
  }

}
