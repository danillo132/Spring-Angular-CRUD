import { Component, Injectable, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UsuarioService } from 'src/app/service/usuario.service';
import {NgbDateAdapter, NgbDateParserFormatter, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap'
import { UserReport } from 'src/app/model/userReport';



@Injectable()
export class FormatDateAdapter extends NgbDateAdapter<string>{
 
  readonly DELIMITER = '/';

  fromModel(value: string | null): NgbDateStruct | null {
    if(value){
      let date = value.split(this.DELIMITER);
      return {
        day: parseInt(date[0],10),
        month: parseInt(date[1],10),
        year: parseInt(date[2], 10)
      };
  }
    return null;
  }
  toModel(date: NgbDateStruct | null): string | null {
    return date ? validar(date.day) + this.DELIMITER + validar(date.month) + this.DELIMITER + date.year :null;
  }

}

@Injectable()
export class FormataData extends NgbDateParserFormatter{

readonly DELIMITER = '/';

  parse(value: string): NgbDateStruct | null {

      if(value){
          let date = value.split(this.DELIMITER);
          return {
            day: parseInt(date[0],10),
            month: parseInt(date[1],10),
            year: parseInt(date[2], 10)
          };
      }
      return null;

  }

  format(date: NgbDateStruct | null): string {
    
   return date ? validar(date.day) + this.DELIMITER + validar(date.month) + this.DELIMITER + date.year :'';
  }
  
  toModel(date: NgbDateStruct | null) : string | null{
    return date ? validar(date.day) + this.DELIMITER + validar(date.month) + this.DELIMITER + date.year :null;
  }
 
}

function validar(valor){
  if(valor.toString !== '' && parseInt(valor) <= 9){
      return '0' + valor;
  }else{
    return valor;
  }
}

@Component({
  selector: 'app-root',
  templateUrl: './usuario-Report.component.html',
  styleUrls: ['./usuario-Report.component.css'],
  providers: [{provide: NgbDateParserFormatter, useClass : FormataData},
  {provide: NgbDateAdapter, useClass: FormatDateAdapter}]
})
export class UsuarioReportComponent {

 userReport = new UserReport();

  constructor(private routeActive : ActivatedRoute, private userService : UsuarioService) { }

 
  imprimeRelatorio(){
    this.userService.downloadRelatorioParam(this.userReport);
  }
    



  

}
