import {Injectable} from '@angular/core';
import {Http, Headers, RequestOptions} from '@angular/http';
import {AuthService} from './auth.service';
import {HttpHeaders, HttpParams} from '@angular/common/http';

@Injectable()
export class ProgramService {

  constructor(private http: Http, private auth: AuthService) {
  }

  retrieveAvailablePrograms() {
    let options = this.auth.buildOptions();
    return this.http.get('http://localhost:5000/programs/userAvailable', options)
      .map(res => res.json());
  }

  retrieveEnrolledPrograms() {
    let options = this.auth.buildOptions();
    return this.http.get('http://localhost:5000/programs/userEnrolled', options)
      .map(res => res.json());
  }

  retrievePendingPrograms() {
    let options = this.auth.buildOptions();
    return this.http.get('http://localhost:5000/programs/userPending', options)
      .map(res => res.json());
  }

  checkEligibility(programId: string) {
    let options = this.auth.buildOptions();
    return this.http.get('http://localhost:5000/programs/checkEligibility/' + programId, options)
      .map(res => res.json());
  }

  viewEnroll(programId: string, servicePointId) {
    let options = this.auth.buildOptions();
    return this.http.get('http://localhost:5000/programs/viewEnroll/' + programId + '/' + servicePointId, options)
      .map(res => res.json());
  }

  enroll(programId: string, selectedAggregator: any, fsl: any, thirdPartyName: any, servicePointId: any) {
    let myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');
    myHeaders.append('Authorization', 'Bearer ' + this.auth.getToken());
    let body = {
      programId: programId,
      selectedAggregator: selectedAggregator,
      fsl: fsl,
      thirdPartyName: thirdPartyName,
      servicePointId: servicePointId
    };
    let options = new RequestOptions({headers: myHeaders});
    console.log('enroll body ', JSON.stringify(body) + body.toString())
    return this.http.post('http://localhost:5000/programs/enroll', JSON.stringify(body), options)
      .map(res => res.json());
  }
}
