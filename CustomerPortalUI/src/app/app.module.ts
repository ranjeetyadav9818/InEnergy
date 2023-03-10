import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {AppComponent} from './app.component';
import {LoginComponent} from './components/login/login.component';
import {DashBoardComponent} from './components/homepage/dashboard.component';
import {InvoicesComponent} from './components/invoices/invoices.component';
import {HeaderComponent} from './components/header/header.component';
import {RoutingModule} from './app.routes';
import {AuthService} from './services/auth.service';
import {PersonalDataService} from './services/personal.data.service';
import {ServiceAgreementComponent} from './components/serviceAgreement/service.agreement.component';
import {DefaultValueIfNullPipe} from './components/common/default.value.if.null.pipe';
import {AddressComponent} from 'app/components/common/address.component';
import {LateralMenuComponent} from 'app/components/common/lateral.menu.component';
import {ServiceAgreementService} from 'app/services/service.agreement.service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ProfileComponent} from './components/profile/profile.component';
import {MonthlyConsumptionComponent} from './components/charts/monthly.consumption';
import {ChartModule} from 'primeng/components/chart/chart';
import {InvoiceService} from './services/invoice.service';
import {MonthlyBillingComponent} from 'app/components/charts/monthly.billing';
import {ChartSelectorComponent} from './components/charts/chart.selector';
import {InputSwitchModule} from 'primeng/components/inputswitch/inputswitch';
import {CurrencyPipe, DatePipe} from '@angular/common';
import {ElectricitySummaryComponent} from './components/electricitySummary/electricity.summary.component';
import {RatePlanService} from './services/rate.plan.service';
import {RatePlanSummaryComponent} from './components/ratePlanSummary/rate.plan.summary.component';
import {InvoicesYearlyComponent} from './components/invoices/invoices.yearly.component';
import {
  AccordionModule,
  ButtonModule,
  DropdownModule,
  GrowlModule,
  InputTextModule,
  MenuModule,
  PanelModule,
  SharedModule,
  SplitButtonModule,
  TreeTableModule,
  DataListModule,
  PanelMenuModule,
  DataScrollerModule, PickListModule
} from 'primeng/primeng';
import {ProgramsSummaryComponent} from './components/programs/programs.summary.component';
import {ProgramService} from 'app/services/program.service';
import {StepsModule} from 'primeng/components/steps/steps';
import {EligibilityPickListComponent} from './components/programs/eligibility.pick.list.component';
import {EligibilityResultsComponent} from './components/programs/eligibility.results.component';
import {EnrollConfirmationComponent} from 'app/components/programs/enroll.confirmation.component';
import {EnrollCompleteComponent} from './components/programs/enroll.complete.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashBoardComponent,
    InvoicesComponent,
    HeaderComponent,
    ServiceAgreementComponent,
    DefaultValueIfNullPipe,
    ProfileComponent,
    AddressComponent,
    LateralMenuComponent,
    MonthlyConsumptionComponent,
    MonthlyBillingComponent,
    ChartSelectorComponent,
    ElectricitySummaryComponent,
    RatePlanSummaryComponent,
    InvoicesYearlyComponent,
    ProgramsSummaryComponent,
    EligibilityPickListComponent,
    EligibilityResultsComponent,
    EnrollConfirmationComponent,
    EnrollCompleteComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    RoutingModule,
    InputTextModule,
    ButtonModule,
    GrowlModule,
    AccordionModule,
    DropdownModule,
    PanelModule,
    MenuModule,
    TreeTableModule,
    SplitButtonModule,
    SharedModule,
    BrowserAnimationsModule,
    DataListModule,
    ChartModule,
    InputSwitchModule,
    PanelMenuModule,
    DataScrollerModule,
    StepsModule,
    PickListModule,
    DataListModule,
    PanelModule
  ],
  providers: [AuthService, DefaultValueIfNullPipe, ServiceAgreementService, InvoiceService, DatePipe,
    PersonalDataService, InvoiceService, CurrencyPipe, RatePlanService, ProgramService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
