import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-eligibility-pick-list',
  templateUrl: 'eligibility.pick.list.component.html'
})
export class EligibilityPickListComponent implements OnInit {

  @Input()
  availablePrograms: any[];
  @Input()
  userSelectedPrograms: any[];
  @Input()
  activeIndex: any;

  @Output()
  nextButtonEmitter = new EventEmitter();

  @Output()
  cancelButtonEmitter = new EventEmitter();

  constructor() {
  }

  ngOnInit(): void {
    console.log('Active index', this.activeIndex);
  }

  nextButton(event) {
    console.log('nextButton ', event);
    this.nextButtonEmitter.emit(event);
  }

  cancelButton(event) {
    console.log('cancelButton ', event);
    this.cancelButtonEmitter.emit(event);
  }
}
