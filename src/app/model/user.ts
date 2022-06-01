import { Profissao } from "./profissao";
import { Telefone } from "./telefone";

export class User {

    id: Number;
    login: String;
    email: String;
    senha: String;
    nome: String;
    cpf: String;
    dataNascimento: String;
    salario: DoubleRange;

    profissao: Profissao = new Profissao();

    telefones: Array<Telefone>;
}
