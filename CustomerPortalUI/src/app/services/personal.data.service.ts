import {Injectable} from '@angular/core';
import {Headers, Http, RequestOptions} from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';
import {AuthService} from './auth.service';
import {PersonalData} from '../model/personal.data.model';

@Injectable()
export class PersonalDataService {

  constructor(private http: Http, private authService: AuthService) {
  }

  updatePersonalData(body: PersonalData) {
    let myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');
    myHeaders.append('Authorization', 'Bearer ' + this.authService.getToken());
    let options = new RequestOptions({headers: myHeaders});
    return this.http.put('http://localhost:5000/profile/data', body, options);
  }
}
