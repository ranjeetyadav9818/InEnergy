import {NgModule}     from '@angular/core';
import {CommonModule} from '@angular/common';
import {HeaderComponent} from './header.component';
import {SplitButtonModule} from 'primeng/primeng';

@NgModule({
  imports: [
    CommonModule,
    SplitButtonModule,
  ],
  declarations: [
    HeaderComponent
  ]
})
export class HeaderModule{}
