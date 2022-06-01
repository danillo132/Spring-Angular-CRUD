import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/model/user';
import { UsuarioService } from 'src/app/service/usuario.service';

@Component({
  selector: 'app-usuario',
  templateUrl: './usuario.component.html',
  styleUrls: ['./usuario.component.css']
})
export class UsuarioComponent implements OnInit {

  students:Array<User>;
  nome : String;
  p: number = 1;
  total: number;

  constructor(private usuarioService: UsuarioService) { }

  ngOnInit(): void {
    this.usuarioService.getStundentList().subscribe(data => {
      this.students = data.content;
      this.total = data.totalElements;
    });
  }

  deleteUsuario(id: Number, index) {

      if(confirm("Deseja mesmo remover?")){

    

    this.usuarioService.deletarUsuario(id).subscribe(data => {
      //console.log("Retorno do método delete: " + data);
        this.students.splice(index, 1);
     /* this.usuarioService.getStundentList().subscribe(data => {
        this.students = data;
      });*/

    });
  }
  }

  consultarPorNome(){


    if(this.nome === ''){
      this.usuarioService.getStundentList().subscribe(data =>{
        this.students = data.content;
        this.total = data.totalElements;
    });
    }else{
      this.usuarioService.consultarPorNome(this.nome).subscribe(data =>{
        this.students = data.content;
        this.total = data.totalElements;
    });
    }

     
  }

  carregarPagina(pagina){

    if(this.nome !== ''){
      this.usuarioService.consultarPorNomePorPage(this.nome, (pagina -1)).subscribe(data =>{
        this.students = data.content;
        this.total = data.totalElements;
    });
    }else{
      this.usuarioService.getStundentListPage(pagina -1).subscribe(data => {
        this.students = data.content;
        this.total = data.totalElements;
      });
    }

    
  }

  imprimeRelatorio(){
    return this.usuarioService.donwloadPdfRelatorio();
  }

}
