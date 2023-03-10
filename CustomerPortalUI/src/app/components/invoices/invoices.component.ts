import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';


@Component({
  templateUrl: './invoices.component.html'
})
export class InvoicesComponent  {
  message: String;

  constructor() {

  }
}
