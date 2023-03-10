import {Component, NgModule} from '@angular/core';
import {FormControl} from '@angular/forms';
import {SplitButtonModule, MenuItem, SelectItem} from 'primeng/primeng';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html'
})
export class HeaderComponent {
  customerName = 'Jane Green';
  serviceAgreements: SelectItem[];
  items: MenuItem[];
  selectedServiceAgreement: number;
  titleHeader: FormControl = new FormControl({value: 'Customer Name', disabled: true});

  constructor(private authService: AuthService, private router: Router) {
    this.customerName = authService.name;
    this.serviceAgreements = [];
    this.serviceAgreements.push({
      label: 'SA ' +
      authService.serviceAgreementId, value: authService.serviceAgreementId
    });
    this.items = [
      {
        label: 'Logout', icon: 'fa fa-sign-out', command: () => {
        this.onLogout();
      }
        ,
        items: []
      },
    ];
  }

  onLogout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  goToProfile(): void {
    this.router.navigate(['/profile']);
  }
}
