/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.eightminutes.logic;

import java.io.Serializable;
import java.util.ArrayList;


public class Jogador implements Serializable{
    private String nome;
    private int moedas;
    private int aposta;
    private ArrayList<Carta> cartas = new ArrayList<>();
    private ArrayList<Exercito> listaExercitos = new ArrayList<>();
    private ArrayList<Cidade> listaCidades = new ArrayList<>();    
    private Carta cartaActiva;
    
    public Jogador(){
        
    }
    
    public Jogador(String nome,Cor cor, int moedas, int qtdExercito, int qtdCidades){
        this.nome = nome;
        this.moedas = moedas;
        for(int i=0;i<qtdExercito;i++)
            listaExercitos.add(new Exercito(cor));
        
        for(int m=0;m<qtdCidades;m++)
            listaCidades.add(new Cidade(cor));                   
    }
    
    public void bloqueiaAccaoExtra(){
        if(getCartaActiva()!=null){
            if(!getCartaActiva().isExecutaTodasAccoes())
            {
                for(int i=0;i<getCartaActiva().getAccoes().size();i++){
                    if(getCartaActiva().getAccoes().indexOf(getCartaActiva().getAccaoActiva())==-1)
                        getCartaActiva().getAccoes().get(i).setUsada(true);
                }
            }
        }
    }
    
    public void colocaExercito(Regiao regiao, ArrayList<Exercito> exercitos){
        if(regiao!=null)
        {
            for(int i=0;i<exercitos.size();i++){
                for(int m=0;m<getListaExercitos().size();m++){
                    if(getListaExercitos().get(m)==exercitos.get(i))
                    {
                        getListaExercitos().get(m).colocaExercito(regiao);
                        if(getCartaActiva()!=null)
                            getCartaActiva().getAccaoActiva().setQtd(getCartaActiva().getAccaoActiva().getQtd()-1);                                                    
                        
                        bloqueiaAccaoExtra();
                        break;
                    }
                }                           
            }
        }        
    }
    
    public void moveExercito(Regiao regiao, ArrayList<Exercito> exercitos){
        for(int i=0;i<exercitos.size();i++){
            for(int m=0;m<getListaExercitos().size();m++){
                if(getListaExercitos().get(m)==exercitos.get(i)){
                    getListaExercitos().get(m).moveExercito(regiao);
                    if(getCartaActiva()!=null)
                        getCartaActiva().getAccaoActiva().setQtd(getCartaActiva().getAccaoActiva().getQtd()-1);
                    
                    bloqueiaAccaoExtra();
                    break;
                }
            }                           
        }       
    }
    
    public void moveExercitoAgua(Regiao regiao, ArrayList<Exercito> exercitos){
        for(int i=0;i<exercitos.size();i++){
            for(int m=0;m<getListaExercitos().size();m++){
                if(getListaExercitos().get(m)==exercitos.get(i)){
                    getListaExercitos().get(m).moveExercito(regiao);
                    if(getCartaActiva()!=null)
                        getCartaActiva().getAccaoActiva().setQtd(getCartaActiva().getAccaoActiva().getQtd()-1);
                    
                    bloqueiaAccaoExtra();
                    break;
                }
            }                           
        }       
    }
    
    public void destroiExercito(Exercito exercito){
        int myIdx=getListaExercitos().indexOf(exercito);
        
        if(myIdx!=-1){           
            getListaExercitos().get(myIdx).destroiExercito();
            if(getCartaActiva()!=null)
                getCartaActiva().getAccaoActiva().setQtd(getCartaActiva().getAccaoActiva().getQtd()-1);
                
                bloqueiaAccaoExtra();
        }                                   
    }
    
    public void colocaCidade(Regiao regiao){
        
        for(int m=0;m<getListaCidades().size();m++){
            if(getListaCidades().get(m).getRegiao()==null){
                getListaCidades().get(m).colocaCidade(regiao);
                
                if(getCartaActiva()!=null)
                    getCartaActiva().getAccaoActiva().setQtd(getCartaActiva().getAccaoActiva().getQtd()-1);
                
                bloqueiaAccaoExtra();
                break;
            }
        }                                 
    }             
            
    public void gastaMoedas(int qtd){      
        setMoedas(getMoedas()-qtd);
    }
          
    public void adicionaCarta(Carta carta, int qtdMoedas){        
        getCartas().add(carta);       
        gastaMoedas(qtdMoedas);
        setCartaActiva(carta);
    }

    public Accao getProximaAccao(){
        Accao accao=null;
        for(int i=0;i<getCartaActiva().getAccoes().size();i++){            
            if(getCartaActiva().getAccoes().get(i)!=null){                
                if(!getCartaActiva().getAccoes().get(i).isUsada()){
                    accao = getCartaActiva().getAccoes().get(i);
                }                
            } 
        }
           
        return accao;              
    }
    
    public Cidade getCidade(int idx){
        int cont=0;
        for(int m=0;m<getListaCidades().size();m++){
            if(getListaCidades().get(m).getRegiao()!=null){
                if(cont==idx)
                    return getListaCidades().get(m);
                else
                    cont++;
            }
        }
        
        return null;
    }
    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the moedas
     */
    public int getMoedas() {
        return moedas;
    }

    /**
     * @param moedas the moedas to set
     */
    public void setMoedas(int moedas) {
        this.moedas = moedas;
    }

    /**
     * @return the cartas
     */
    public ArrayList<Carta> getCartas() {
        return cartas;
    }

    /**
     * @param cartas the cartas to set
     */
    public void setCartas(ArrayList<Carta> cartas) {
        this.cartas = cartas;
    }


    /**
     * @return the aposta
     */
    public int getAposta() {
        return aposta;
    }

    /**
     * @param aposta the aposta to set
     */
    public void setAposta(int aposta) {
        this.aposta = aposta;
    }

    /**
     * @return the cartaActiva
     */
    public Carta getCartaActiva() {
        return cartaActiva;
    }

    /**
     * @param cartaActiva the cartaActiva to set
     */
    public void setCartaActiva(Carta cartaActiva) {
        this.cartaActiva = cartaActiva;
    }

    /**
     * @return the listaExercitos
     */
    public ArrayList<Exercito> getListaExercitos() {
        return listaExercitos;
    }

    /**
     * @param listaExercitos the listaExercitos to set
     */
    public void setListaExercitos(ArrayList<Exercito> listaExercitos) {
        this.listaExercitos = listaExercitos;
    }

    /**
     * @return the listaCidades
     */
    public ArrayList<Cidade> getListaCidades() {
        return listaCidades;
    }

    /**
     * @param listaCidades the listaCidades to set
     */
    public void setListaCidades(ArrayList<Cidade> listaCidades) {
        this.listaCidades = listaCidades;
    }
}
