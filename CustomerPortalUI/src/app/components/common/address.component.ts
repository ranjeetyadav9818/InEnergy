import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-sa-address',
  templateUrl: 'address.component.html'
})

export class AddressComponent implements OnInit {


  @Input() public address1: string;
  @Input() public address2: string;
  @Input() public city: string;
  @Input() public postal: string;
  @Input() public state: string;
  public stateTransformed: string;

  ngOnInit(): void {
    if (this.state === 'CA') {
      this.stateTransformed = 'California';
    } else {
      this.stateTransformed = 'Unkonwn State';
    } // todo add states by code
  }
}
