import {Component, HostListener, OnInit} from '@angular/core';
import {AuthService} from './services/auth.service';
import {Router} from '@angular/router';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  loggedIn: boolean;

  constructor(public authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    this.loggedIn = this.authService.isLoggedIn();
    if (!this.loggedIn) {
      this.router.navigate(['/login']);
    }
  }

  @HostListener('window:scroll', ['$event'])
  @HostListener('window:resize', ['$event'])
  onWindowScroll($event) {
    let i = 0;
    let elementsByClassName = document.getElementsByClassName('future-fixed');
    while (elementsByClassName.length > i) {
      let element = <HTMLElement>elementsByClassName[i];
      let elementBottom = element.offsetTop + element.offsetHeight;
      let windowBottom = window.innerHeight + window.pageYOffset;
      if (element.hasAttribute('previousSize')) {
        elementBottom = Number(element.getAttribute('previousSize'));
      }
      if (windowBottom > elementBottom) {
        let bottom = window.innerHeight - element.offsetHeight;
        if (bottom < 0) {
          bottom = 0;
        }
        element.style.cssText = 'position:fixed;bottom:' + bottom.toString() + ';width:' + element.offsetWidth + 'px !important';
        element.setAttribute('previousSize', elementBottom.toString());
      } else {
        element.style.cssText = '';
      }
      i++
    }
  }

  isUserLoggedIn(): boolean {
    this.loggedIn = this.authService.isLoggedIn();
    if (!this.loggedIn && this.router.url !== '/login') {
      this.authService.logout();
      this.router.navigate(['/login']);
    }
    return this.authService.isLoggedIn();
  }
}
