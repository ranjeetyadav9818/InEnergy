import {Injectable} from '@angular/core';
import {Headers, Http, RequestOptions} from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';
import {AuthService} from './auth.service';

@Injectable()
export class ServiceAgreementService {

  constructor(private http: Http, private authService: AuthService) {
  }

  getServiceAgreement() {
    let myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/text');
    myHeaders.append('Authorization', 'Bearer ' + this.authService.getToken());
    let options = new RequestOptions({headers: myHeaders});
    return this.http.get('http://localhost:5000/serviceAgreement', options)
      .map(res => res.json());
  }

  getRatePlans() {
    let myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/text');
    myHeaders.append('Authorization', 'Bearer ' + this.authService.getToken());
    let options = new RequestOptions({headers: myHeaders});
    return this.http.get('http://localhost:5000/serviceAgreement/ratePlans', options)
      .map(res => res.json());
  }
}
