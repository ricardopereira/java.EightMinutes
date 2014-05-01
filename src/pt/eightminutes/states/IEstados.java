/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.eightminutes.states;

import java.util.ArrayList;
import pt.eightminutes.logic.*;

public interface IEstados {
    IEstados defineNumJogadores(int numJogadores);
    IEstados defineDadosJogadores(String nome, Cor cor);
    IEstados comecaApostas();
    IEstados defineApostasJogadores(int numMoedas);
    IEstados comecaJogo();    
    IEstados escolheCartas(int idx);    
    IEstados verificaPontuacao();
    IEstados compraCarta();
    IEstados escolheAccao(Accao accao);
    IEstados mudaAccao();
    IEstados passaVez();
    IEstados colocaCidade(Regiao regiao);
    IEstados colocaExercito(Regiao regiao, ArrayList<Exercito> exercitos);
    IEstados colocaExercitoCont(Regiao regiao, ArrayList<Exercito> exercitos);
    IEstados moveExercito(Regiao regiao, ArrayList<Exercito> exercitos);
    IEstados moveExercitoCont(Regiao regiao, ArrayList<Exercito> exercitos);
    IEstados destroiExercito(Exercito exercito);
    IEstados moveExercitoAgua(Regiao regiao, ArrayList<Exercito> exercitos);
    IEstados moveExercitoAguaCont(Regiao regiao, ArrayList<Exercito> exercitos);
    IEstados abandonaJogo();
    IEstados gravarJogo();
    IEstados retomarJogo();
}