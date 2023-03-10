import {Injectable} from '@angular/core';
import {Http, Headers, RequestOptions} from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/Rx';
import {AuthService} from './auth.service';

@Injectable()
export class RatePlanService {

  constructor(private http: Http, private auth: AuthService) {
  }

  retrieveRatePlan() {
    let options = this.auth.buildOptions();
    return this.http.get('http://localhost:5000/ratePlan', options)
      .map(res => res.json());
  }

}
