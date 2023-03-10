import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import { MenuItem} from 'primeng/primeng';


@Component({
  templateUrl: './dashboard.component.html'
})
export class DashBoardComponent {
  message: String;

  constructor(private router: Router, private authService: AuthService) {

  }
  onTestRequest(): void {
    this.authService.makeTestRequest().subscribe(
      (data) => {
        console.log(data.json());
        this.message = data.toString();
      },
      (err) => {},
      () => {
        console.log('Done');
        this.router.navigate(['/homepage']);
      }
    );
  }

  goToHome(): void {
    this.router.navigate(['/homepage']);
  }

  goToInvoices(): void {
    this.router.navigate(['/invoices']);
  }
}
