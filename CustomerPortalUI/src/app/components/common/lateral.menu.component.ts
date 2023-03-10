import {Component} from '@angular/core';
import {Router} from '@angular/router';


@Component({
  selector: 'app-lateral-menu',
  templateUrl: './lateral.menu.component.html'
})
export class LateralMenuComponent {
  message: String;

  constructor(private router: Router) {

  }

  goToHome(): void {
    this.router.navigate(['/homepage']);
  }

  goToInvoices(): void {
    this.router.navigate(['/invoices']);
  }

  goToProgramsSummary(): void {
    this.router.navigate(['/programsSummary']);
  }
}
