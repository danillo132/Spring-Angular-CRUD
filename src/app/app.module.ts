import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule} from "@angular/common/http";
import { HomeComponent } from './home/home.component'; //Requisições ajax
import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';
import { LoginComponent } from './login/login.component';
import { HttpInterceptorModule } from './service/header-interceptor.service';
import { UsuarioComponent } from './componentes/usuario/usuario/usuario.component';
import { UsuarioAddComponent } from './componentes/usuario/usuarioadd/usuario-add.component';
import { GuardiaoGuard } from './service/guardiao.guard';
import { NgxMaskModule, IConfig} from 'ngx-mask';
import { NgxPaginationModule} from 'ngx-pagination';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxCurrencyModule } from 'ngx-currency';
import { ChartsModule } from 'ng2-charts';
import { UsuarioReportComponent } from './componentes/usuario-report/usuario-report.component';
import { BarChartComponent } from './componentes/bar-chart/bar-chart.component';


export const appRouters: Routes = [
  {path : 'home', component : HomeComponent, canActivate: [GuardiaoGuard] },
  {path : 'login', component : LoginComponent},
  {path : '', component : LoginComponent},
  {path : 'usuariolist', component : UsuarioComponent, canActivate: [GuardiaoGuard]},
  {path : 'usuarioAdd', component : UsuarioAddComponent, canActivate: [GuardiaoGuard]},
  {path : 'usuarioAdd/:id', component : UsuarioAddComponent, canActivate: [GuardiaoGuard]},
  {path : 'userReport', component : UsuarioReportComponent, canActivate: [GuardiaoGuard]},
  {path : 'chart', component : BarChartComponent, canActivate: [GuardiaoGuard]}
]; 

export const routes : ModuleWithProviders<any> = RouterModule.forRoot(appRouters);

  export const optionMask : Partial<IConfig> | (() => Partial<IConfig>) = {};

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    UsuarioComponent,
    UsuarioAddComponent,
    UsuarioReportComponent,
    BarChartComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgxMaskModule.forRoot(optionMask),
    routes,
    HttpInterceptorModule,
    NgxPaginationModule,
    BrowserAnimationsModule,
    NgbModule,
    NgxCurrencyModule,
    ChartsModule
   
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
