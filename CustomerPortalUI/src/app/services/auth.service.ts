import {Injectable} from '@angular/core';
import {User} from '../model/user.model';
import {Http, Headers, RequestOptions} from '@angular/http';
import {JwtHelper} from 'angular2-jwt';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';

@Injectable()
export class AuthService {

  jwtHelper: JwtHelper = new JwtHelper();

  constructor(private http: Http) {
  }

  login(user: User) {

    // const body = {username: user.username.value, password: user.password.value};

    let body = new FormData();
    body.append('username', user.username.value);
    body.append('password', user.password.value);

    return this.http.post('http://localhost:5000/auth/login', body)
      .map(res => res.json())
      .do(data => {
        this.saveJwt(data.access_token);
        this.saveUserData(data.name, data.phone, data.service_agreement);
      });
  }

  getToken(): string {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return this.getToken() !== null && !this.jwtHelper.isTokenExpired(this.getToken(), 55);
  }

  logout(): void {
    localStorage.removeItem('token');
  }

  saveJwt(token: string) {
    localStorage.setItem('token', token);
  }

  saveUserData(name: string, phone: string, serviceAgreementId: string) {
    if (name != null) {
      localStorage.setItem('name', name);
    }
    if (phone != null) {
      localStorage.setItem('phone', phone);
    }
    localStorage.setItem('serviceAgreementId', serviceAgreementId);
  }

  get name(): string {
    return localStorage.getItem('name')
  }

  get phone(): string {
    return localStorage.getItem('phone')
  }

  get serviceAgreementId(): string {
    return localStorage.getItem('serviceAgreementId')
  }

  makeTestRequest() {
    let options = this.buildOptions();
    return this.http.get('http://localhost:5000/test', options);
  }

  public buildOptions() {
    let myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/text');
    myHeaders.append('Authorization', 'Bearer ' + this.getToken());
    let options = new RequestOptions({headers: myHeaders});
    return options;
  }
}
