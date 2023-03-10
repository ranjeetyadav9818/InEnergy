import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {ProgramService} from '../../services/program.service';
import {MenuItem, Message} from 'primeng/primeng';
import {Router} from '@angular/router';
import {EnrollResult} from './enroll.result';

@Component({
  selector: 'app-programs-summary',
  templateUrl: 'programs.summary.component.html'
})
export class ProgramsSummaryComponent implements OnInit {

  availablePrograms: any[] = [];
  userEnrolledPrograms: any[] = [];
  userSelectedPrograms: any[] = [];
  userSelectedProgramsEligibility: any[] = [];
  userAppliedPrograms: any[] = [];
  enrollResult: any;
  finalEnroll: any;
  // Vars for items
  items: MenuItem[];
  activeIndex = 0;
  selectedProgram: any;
  aggregators: any = [];

  constructor(private auth: AuthService, private programService: ProgramService, private router: Router) {
  }

  ngOnInit(): void {
    this.programService.retrieveAvailablePrograms().subscribe(
      data => {
        this.availablePrograms = data;
      }
    );

    this.programService.retrieveEnrolledPrograms().subscribe(
      data => {
        this.userEnrolledPrograms = data;
      }
    );
    this.programService.retrievePendingPrograms().subscribe(
      data => {
        this.userAppliedPrograms = data;
      }
    );

    this.items = [{
      label: 'Overview'
    },
      {
        label: 'Eligibility Check'
      },
      {
        label: 'Enroll Confirmation'
      },
      {
        label: 'Complete'
      }
    ];
  }

  checkEligibility() {
    this.userSelectedProgramsEligibility = [];
    this.userSelectedPrograms.forEach(
      program => {
        this.programService.checkEligibility(program.id).subscribe(data => {
            this.userSelectedProgramsEligibility.push({'program': program, 'eligibility': data});
            // console.log('Eligibility ', JSON.stringify(this.userSelectedProgramsEligibility));
          }
        );
      }
    );
    this.activeIndex = 1;
    console.log('active index ' + this.activeIndex);
  }

  cancel() {
    if (this.activeIndex === 0) {
      this.router.navigate(['/homepage']);
    } else if (this.activeIndex === 3) {
      if (this.selectedProgram.id) {
        let index = this.userSelectedPrograms.map(p => p.id).indexOf(this.selectedProgram.id);
        if (index !== -1) {
          this.userSelectedPrograms.splice(index, 1);
        }
      }
      this.activeIndex = 0;
    } else {
      this.activeIndex--;
    }
  }

  viewEnroll(programIdAndServicePoint: string) {
    console.log(' viewEnroll(programIdAndServicePoint: string) ' + programIdAndServicePoint);
    let params = programIdAndServicePoint.split(',');
    let programId = params[0].trim();
    let servicePointId = params[1].trim();
    this.selectedProgram = this.userSelectedPrograms.find(p => p.id.toString() === programId.toString());
    this.programService.viewEnroll(programId, servicePointId).subscribe(
      data => {
        this.enrollResult = data;
        if (this.enrollResult.servicePointsEligibility.length > 0) {
          this.aggregators = [];
          this.enrollResult.servicePointsEligibility[0].aggregators.forEach(aggregator =>
            this.aggregators.push({label: aggregator.name, value: aggregator.id}));
        }
      });
    this.activeIndex = 2;
  }

  onCompleteEnroll(result: EnrollResult) {
    this.activeIndex = 3;
    this.finalEnroll = result;
    console.log('onCompleteEnroll :::', JSON.stringify(this.finalEnroll));
  }
}
