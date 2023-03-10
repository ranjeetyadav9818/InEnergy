import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {PersonalDataService} from '../../services/personal.data.service';
import {FormControl} from '@angular/forms';
import {PersonalData} from '../../model/personal.data.model'
import {Message} from 'primeng/components/common/api';


@Component({
  selector: 'app-profile-component',
  templateUrl: 'profile.component.html'
})
export class ProfileComponent implements OnInit {

  public name: FormControl = new FormControl();
  public phone: FormControl = new FormControl();
  public oldPassword: FormControl = new FormControl();
  public newPassword: FormControl = new FormControl();
  public repeatPassword: FormControl = new FormControl();
  public msgs: Message[] = [];

  constructor(private authService: AuthService, private personalDataService: PersonalDataService) {

  }

  ngOnInit(): void {
    if (this.authService.name != null) {
      this.name.setValue(this.authService.name);
    }
    if (this.authService.phone != null) {
      this.phone.setValue(this.authService.phone);
    }
  }

  public update(): void {
    let personalData: PersonalData = {
      name: this.name.value,
      phone: this.phone.value,
      password: null,
      oldPassword: null
    };

    this.personalDataService.updatePersonalData(personalData).subscribe(
      (data) => {
                  console.log('personal data');
                  console.log(data);
                  this.msgs = [];
                  this.msgs.push({severity: 'info', summary: 'Personal data updated'});
              },
      (error) => console.error(error),
      () => console.log('done')
    );
  }

  public updatePassword(): void {

    if (this.newPassword.value !== this.repeatPassword.value){
      this.msgs = [];
      this.msgs.push({severity: 'error', summary: 'Error trying to change password', detail: 'New password and repeat should be the same'});
      return;
    }

    let personalData: PersonalData = {
      name: null,
      phone: null,
      password: this.newPassword.value,
      oldPassword: this.oldPassword.value
    };

    this.personalDataService.updatePersonalData(personalData).subscribe(
      (data) => {
                  this.msgs = [];
                  this.msgs.push({severity: 'info', summary: 'Password changed'});
              },
      (error) => {
                this.msgs = [];
                this.msgs.push({severity: 'error', summary: 'Error trying to change password', detail: error.json().result});
      },
      () => console.log('done')
    );
  }
}
