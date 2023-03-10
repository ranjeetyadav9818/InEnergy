import {Component, OnInit} from '@angular/core';
import {User} from '../../model/user.model';
import {AuthService} from '../../services/auth.service';

import {Message} from 'primeng/primeng';
import {Router} from '@angular/router';
import {FormControl} from '@angular/forms';

@Component({
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {
  message: String;
  public username: FormControl = new FormControl();
  public password: FormControl = new FormControl();
  public msgs: Message[] = [];

  constructor(private authService: AuthService, private router: Router) {

  }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/homepage']);
    }
  }

  public showToken(): void {
    console.log(this.authService.getToken());
  }

  public login(): void {
    let user: User = {
      username: this.username,
      password: this.password
    };

    this.authService.login(user).subscribe(
      (data) => {
        console.log('test');
      },
      (err) => {
        console.log('Error login ', err);
        this.message = 'Unknown user / password';
      },
      () => {
        console.log('Done');
        this.router.navigate(['/homepage']);
      }
    );
  }
}
