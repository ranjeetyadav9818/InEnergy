import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-enroll-complete',
  templateUrl: 'enroll.complete.component.html'
})
export class EnrollCompleteComponent implements OnInit {

  @Input()
  finalEnroll: any;

  @Input()
  activeIndex = 0;

  @Output()
  cancelButtonEmitter = new EventEmitter();


  ngOnInit(): void {
  }

  cancelButton(programId) {
    console.log('EnrollCompleteComponent: cancelButton ', event);
    this.cancelButtonEmitter.emit(programId);
  }
}

