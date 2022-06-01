import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { document } from 'ngx-bootstrap/utils';
import { Observable } from 'rxjs';
import { AppConstants } from '../app-constants';
import { UserReport } from '../model/userReport';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor(private http: HttpClient) { }

  getStundentList(): Observable<any>{
    return this.http.get<any>(AppConstants.baseUrl + "listar");
    
  }

  getStundentListPage(pagina): Observable<any>{
    return this.http.get<any>(AppConstants.baseUrl + "page/" + pagina);
    
  }

  getStundent(id) : Observable<any>{
    return this.http.get<any>(AppConstants.baseUrl + id);
  }

  getProfissaoList(): Observable<any>{
      return this.http.get<any>(AppConstants.baseUrlPath + 'profissao/');
  }

  deletarUsuario(id: Number) : Observable<any>{

    return this.http.delete(AppConstants.baseUrl + id, {responseType : 'text'});
  }

  consultarPorNome(nome: String) : Observable<any>{
    return this.http.get(AppConstants.baseUrl + "usuarioPorNome/" + nome);
  }

  consultarPorNomePorPage(nome: String, page: Number) : Observable<any>{
    return this.http.get(AppConstants.baseUrl + "usuarioPorNome/" + nome + "/page/" + page);
  }



  salvarUsuario(user) : Observable<any>{
    return this.http.post<any>(AppConstants.baseUrl + "cadastrar", user);
  }

  updateUsuario(user) : Observable<any>{
    return this.http.put<any>(AppConstants.baseUrl + "atualizar", user);
  }

  userAutenticado(){
    if(localStorage.getItem('token') != null && localStorage.getItem('token')?.toString().trim() != null){
      return true;
  }else{
    return false;
  }
  }

  removertelefone(id) : Observable<any>{

      return this.http.delete(AppConstants.baseUrl + "removerTelefone/" + id, {responseType:'text'});
  }


  donwloadPdfRelatorio(){
    return this.http.get(AppConstants.baseUrl + 'relatorio', {responseType: 'text'}).subscribe(data => {
        document.querySelector('iframe').src = data;
    });
  }

  downloadRelatorioParam(UserReport: UserReport){
    return this.http.post(AppConstants.baseUrl + 'relatorio/',UserReport ,{responseType: 'text'}).subscribe(data => {
        document.querySelector('iframe').src = data;
    });
  }


  carregarGrafico(): Observable<any>{
    return this.http.get(AppConstants.baseUrl + 'grafico');
  }
}
