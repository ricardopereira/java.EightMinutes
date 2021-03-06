package pt.eightminutes.logic;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import pt.eightminutes.states.AguardaOpcoesJogo;
import pt.eightminutes.states.AguardaPreparaJogo;
import pt.eightminutes.states.IEstados;
import pt.eightminutes.states.EstadoListener;
import pt.eightminutes.utils.utils;

public class Jogo extends Base implements Serializable {
    
    private IEstados estadoActual;
    private IEstados estadoAnterior;
    private int numJogadores;
    private Baralho baralho;
    private ArrayList<Carta> cartasViradas = new ArrayList<>();
    private Mapa mapa;
    private ArrayList<Jogador> jogadores = new ArrayList<>();
    private Jogador jogadorActivo;
    
    // Eventos
    private transient List<EstadoListener> listeners = null;

    public Jogo() {
        mapa = new Mapa();
    }

    public void verificarDependencias() throws FileNotFoundException, IOException {
        // Trajectos possiveis
        mapa.lerTrajectos();   
    }
    
    public Jogador getJogadorVencedor() {
        Jogador jogadorAux = null;
        int myPontos;
        int myPontosMax = 0;
        for(int i = 0; i < getJogadores().size(); i++) {
            myPontos = getJogadores().get(i).getPontuacao(this);
            if (myPontos > myPontosMax) {
                jogadorAux = getJogadores().get(i);
            }
            else
            if (myPontos == myPontosMax) {
                jogadorAux = null;
            }    
        }
        return jogadorAux;
    }
       
    public void gravaInstanciaJogo(String fileName) throws IOException {
        FileOutputStream fo = new FileOutputStream(fileName);
        ObjectOutputStream oo = new ObjectOutputStream(fo);
        oo.writeObject(this);
        oo.close();
    }
    
    public Jogo carregaInstanciaJogo(String fileName) throws FileNotFoundException {
        Jogo jogoAux= this;
        try {         
            FileInputStream fi = new FileInputStream(fileName);
            ObjectInputStream oi = new ObjectInputStream(fi);
            jogoAux = ((Jogo) oi.readObject());       
            oi.close();
        }
        catch (FileNotFoundException e) {
            throw e;
        }
        catch (IOException | ClassNotFoundException e) {
            // Ignora
        }
        return jogoAux;
    }
    
    public void addListener(EstadoListener toAdd) {
        getListeners().add(toAdd);
    }
    
    public void setEstado(IEstados estado)
    {
        this.setEstadoAnterior(this.estadoActual);
        this.setEstadoActual(estado);
        
        if (debugMode)
            System.out.println((new Date())+ ": setEstado - " + estado.getClass().getSimpleName());
        
        // Notifica a todos os listeners o setEstado
        for (EstadoListener event : getListeners())
            event.onSetEstado();
    }
    
    public void opcoesJogo() {
        // Primeiro estado da máquina
        setEstado(new AguardaOpcoesJogo(this));
    }

    public void novoJogo() {
        setEstado(estadoActual.novoJogo());
    }
    
    public Jogo carregaJogo() throws FileNotFoundException {
        // ToDo: Amanhã rever este procedimento: "está a ficar pedra"
        try {
            IEstados proximoEstado = estadoActual.carregaJogo();
            setEstado(proximoEstado);
            // Nova instancia do Jogo
            return proximoEstado.getJogo();
        }
        catch (FileNotFoundException e) {
            throw e;
        }
        catch (IOException e) {
            // Ignora
        }
        return null;
    }
    
    public void gravaJogo() {
        setEstado(estadoActual.gravaJogo());
    }
    
    public void terminaJogo() {
        setEstado(null);
    }
    
    public boolean verificaJogadoresFimDoJogo(){
        boolean myResult = true;
        
        for(int i=0;i< getJogadores().size();i++){
            if(getJogadores().get(i).getCartas().size()< getNumCartasFinal())
                myResult = false;
        }
        return myResult;
    }
    
    public void iniciaJogo(){
        defineCartasViradas();
        colocaExercitoInicial();
    }
    
    public void defineNumJogadores(int numJogadores) {
        setNumJogadores(numJogadores);
        baralho = new Baralho(numJogadores);
        setEstado(estadoActual.defineNumJogadores(numJogadores));
    }
    
    public void defineDadosJogadores(String nome, Color cor) {
        setEstado(estadoActual.defineDadosJogadores(nome,cor));
    }
    
    public void comecaApostas() {
        setEstado(estadoActual.comecaApostas());
    }
    
    public Jogador criaJogador(String nome, Color cor) {
        if (!nome.isEmpty()) {
            jogadores.add(new Jogador(this, nome, cor, getMoedasPorJogador(), 14, 3, jogadores.size()));
            return jogadores.get(jogadores.size()-1);
        }
        else
            return null;
    }
   
    public void comecaJogo() {
        escolheJogadorInicial();
        iniciaJogo();
        setEstado(estadoActual.comecaJogo());
    }
    
    public void passaVez() {
        mudaJogador();        
        setEstado(estadoActual.passaVez());
    }
    
    public void defineApostasJogadores(Jogador jogador, int aposta) {
        setEstado(estadoActual.defineApostasJogadores(jogador,aposta));
    }
            
    public void apostaJogador(Jogador jogador, int aposta){        
        jogador.setAposta(aposta);  
    }
    
    public void escolheJogadorInicial() {
        int apostaMax=0;       
        int apostaJog=0;
        ArrayList<Jogador> jogadoresAux = new ArrayList<>();
        
        for(int i=0;i<getJogadores().size();i++){
            apostaJog = getJogadores().get(i).getAposta();
            if(apostaJog > apostaMax){                
                jogadoresAux.clear();
                jogadoresAux.add(getJogadores().get(i));
                apostaMax = apostaJog;
            }
            else
            if(apostaJog == apostaMax){
                jogadoresAux.add(getJogadores().get(i));
            }
        }
        
        if(jogadoresAux.size()>1){
            int i = utils.randInt(0, jogadoresAux.size()-1);
        
            setJogadorActivo(jogadoresAux.get(i));
        }
        else
            setJogadorActivo(jogadoresAux.get(0));
     
        getJogadorActivo().gastaMoedas(apostaMax);
    }
    
    private void mudaJogador(){
        int idxJog = getJogadores().indexOf(getJogadorActivo());
        
        if(idxJog<getJogadores().size()-1)
            setJogadorActivo(getJogadores().get((idxJog+1)));
        else
            setJogadorActivo(getJogadores().get(0));       
    }
    
    public void escolheCarta(int idx) {
        setEstado(estadoActual.escolheCartas(idx));
    }
    
    public boolean validaNumCartasFinal(){
        boolean myValida=true;
        for(int i=0;i<getJogadores().size();i++){
            if(getJogadores().get(i).getCartas().size()<getNumCartasFinal()){
                myValida = false;
                break;
            }
        } 
        return myValida;
    }
    
    public void compraCarta(int idx){
        for(int i=0;i<6;i++){
            if(cartasViradas.get(i)!=null)
            {
                if(i==idx){
                    getJogadorActivo().adicionaCarta(cartasViradas.get(i),getCustoPorIdx(idx));   
                    cartasViradas.get(i).setComprada(true);    
                    baralho.getCartas().remove(cartasViradas.get(i));
                    break;
                }                                             
            }
        }  
        defineCartasViradas();
    }
    
    public void escolheAccao(Accao accao) {
        if (accao == null){            
            mudaJogador();
            setEstado(estadoActual.compraCarta());
        }
        else
            setEstado(estadoActual.escolheAccao(accao));
    }
    
    public void mudaAccao() {
        setEstado(estadoActual.mudaAccao());
    }
    
    public void getListaExercitosTodosUtilizadores(ArrayList<Exercito> exercitos) {
        if (exercitos == null) return;
        exercitos.clear();
        
        for (Jogador jogador : getJogadores()) {
            for (Exercito exercito : jogador.getListaExercitos()) {
                if (exercito.getRegiao() != null)
                    exercitos.add(exercito);
            }
        }
    }
    
    public int getNumCartasFinal() {
        int numJog = getNumJogadores();
        int numCartasFinal=0;
        
        switch (numJog) {
            case 2: numCartasFinal = 13;
                    break;
            case 3: numCartasFinal = 10;
                    break;                
            case 4: numCartasFinal = 8;
                    break;
            case 5: numCartasFinal = 7;
                    break;
            default: numCartasFinal = 0;
                     break;
        }
        
        return numCartasFinal;
    }
    
    public void colocaCidade(Regiao regiao){                
        setEstado(estadoActual.colocaCidade(regiao));
    }
    
    public void colocaExercito(Regiao regiao, int qtd){                
        setEstado(estadoActual.colocaExercito(regiao, qtd));
    }
        
    public void moveExercito(Regiao regiao, ArrayList<Exercito> exercitos){                
        setEstado(estadoActual.moveExercito(regiao, exercitos));
    }
    
    public void moveExercitoAgua(Regiao regiao, ArrayList<Exercito> exercitos){                
        setEstado(estadoActual.moveExercitoAgua(regiao, exercitos));
    }
    
    
    public void destroiExercito(Exercito exercito){                
        setEstado(estadoActual.destroiExercito(exercito));
    }
    
    public void defineRecurso(Carta carta, Recurso recurso){                
        setEstado(estadoActual.defineRecurso(carta, recurso));
    }
    
    public void mostraPontuacao(){                
        setEstado(estadoActual.mostraPontuacao());
    }
    
    public Regiao escolheRegiao(Continente continente,int idx){
        if(continente!=null){
            if(idx<continente.getRegioes().size())
                return continente.getRegioes().get(idx);
            else
                return null;
        }
        else           
            return null;        
    }
    
    public Continente escolheContinente(int idx){
        if(idx<getMapa().getContinentes().size())
            return getMapa().getContinentes().get(idx);
        else
            return null;
    }
    
    public void getRegioesPossiveisTerra(Regiao regiao, int qtdMovimentos, ArrayList <Regiao> regioesPossiveis){        
        //guardar as regioes possiveis num map com o valor de distancia?
        if (regiao == null)
            return;
        
        if (qtdMovimentos > 0)
        {
            qtdMovimentos--;
            
            for(int i = 0; i < regiao.getRegioesVizinhas().size();i++)
            {
                Regiao regiaoVizinha = regiao.getRegioesVizinhas().get(i);
                if (regiaoVizinha.getContinente() == regiao.getContinente()) {
                    if ((regioesPossiveis.indexOf(regiaoVizinha) == -1) && (regiaoVizinha != regiao))
                    {
                        regioesPossiveis.add(regiaoVizinha);
                    }
                    
                    getRegioesPossiveisTerra(regiaoVizinha,qtdMovimentos,regioesPossiveis);
                }
            }
        }        
    }
    
    public void getRegioesPossiveisAgua(Regiao regiao, int qtdMovimentos, ArrayList<Regiao> regioesPossiveis){
        //guardar as regioes possiveis num map com o valor de distancia?
        if (regiao == null)
            return;
        
        if (qtdMovimentos > 0)
        {
            qtdMovimentos--;
            
            for (int i = 0; i < regiao.getRegioesVizinhas().size(); i++)
            {
                Regiao regiaoVizinha = regiao.getRegioesVizinhas().get(i);
                if ((regioesPossiveis.indexOf(regiaoVizinha) == -1) && (regiaoVizinha != regiao))
                {
                    regioesPossiveis.add(regiaoVizinha);                    
                }
                
                getRegioesPossiveisAgua(regiaoVizinha,qtdMovimentos,regioesPossiveis);
            }            
        }        
    }
    
    public void colocaExercitoInicial(){
        ArrayList<Exercito> exercitoAux = new ArrayList<>();
        
        for (int i = 0; i < jogadores.size(); i++)
        {            
            exercitoAux.clear();
            for (int m = 0; m < jogadores.get(i).getListaExercitos().size(); m++) {
                exercitoAux.add((Exercito)jogadores.get(i).getListaExercitos().get(m));                
                
                if (exercitoAux.size() == 3)
                    break;
            }
            
            jogadores.get(i).colocaExercito(mapa.getRegiaoInicial(),exercitoAux);
        }
    }
    
    public int getMoedasPorJogador(){
        int numJogadoresAux = getNumJogadores();
        
        if(numJogadoresAux==5)
            return 8;
        else
        if(numJogadoresAux==4)
            return 9;
        else
        if(numJogadoresAux==3)
            return 11;
        else
        if(numJogadoresAux==2)
            return 14;
        else
            return 0;
    }

    public void defineCartasViradas(){
        cartasViradas.clear();
        
        for(int i=0;i<baralho.getCartas().size();i++){
            if(baralho.getCartas().get(i)!=null)
            {
                if(!baralho.getCartas().get(i).isComprada()){
                    if(baralho.getCartas().get(i).getAccoes().get(0).getQtd()>0)
                    {
                        if(cartasViradas.size()<6){
                            cartasViradas.add(baralho.getCartas().get(i));                    
                        }
                        else
                            break;
                    }                                                     
                }
            }
        }
    }
    
    public int getCustoPorIdx(int idx){
        int myCusto = 0;
        
        switch (idx) {
            case 0: myCusto = 0;
                    break;
            case 1: myCusto = 1;
                    break;
            case 2: myCusto = 1;
                    break;
            case 3: myCusto = 2;
                    break;
            case 4: myCusto = 2;
                    break;
            case 5: myCusto = 3;
                    break;
            default: myCusto= 0;
                     break;
        }
            
        return myCusto;        
    }
    
    public boolean isJokersAtribuidos(){  
        
        boolean result = true;
        
        for(int i=0;i< getJogadores().size();i++)
        {
            if(!getJogadores().get(i).getListaCartaJokers().isEmpty())
            {
                result = false;
                break;
            }            
        }
        return result;
    }
    
    public ArrayList<Jogador> getListaJogadoresJokers(){
        ArrayList<Jogador> jogadoresAux = new ArrayList<>();
        //Listar jogadores
        for(int i=0;i<getJogadores().size();i++){
            //Listar cartas por jogador
            for(int m=0;m<getJogadores().get(i).getListaCartaJokers().size();m++){
                //Adiciona se tiverem jokers                
                jogadoresAux.add(getJogadores().get(i));                
            }
        }
        return jogadoresAux;
    }
    
    /**
     * @return the numJogadores
     */
    public int getNumJogadores() {
        return numJogadores;
    }

    /**
     * @param numJogadores the numJogadores to set
     */
    public void setNumJogadores(int numJogadores) {
        this.numJogadores = numJogadores;
    }

    /**
     * @return the baralho
     */
    public Baralho getBaralho() {
        return baralho;
    }

    /**
     * @param baralho the baralho to set
     */
    public void setBaralho(Baralho baralho) {
        this.baralho = baralho;
    }

    /**
     * @return the mapa
     */
    public Mapa getMapa() {
        return mapa;
    }

    /**
     * @param mapa the mapa to set
     */
    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }

    /**
     * @return the jogadores
     */
    public ArrayList<Jogador> getJogadores() {
        return jogadores;
    }

    /**
     * @param jogadores the jogadores to set
     */
    public void setJogadores(ArrayList<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

    /**
     * @return the cartasViradas
     */
    public ArrayList<Carta> getCartasViradas() {
        return cartasViradas;
    }

    /**
     * @param cartasViradas the cartasViradas to set
     */
    public void setCartasViradas(ArrayList<Carta> cartasViradas) {
        this.cartasViradas = cartasViradas;
    }
    
     @Override
    public String toString() {
        String s = "";
        s += "Cartas viradas :" + getCartasViradas().size()+"\n";
        for(int i=0;i<getCartasViradas().size();i++)
        {
            s += "  Carta: "+i+"\n Caracteristicas:\n"+
                 "      Recurso:"+getCartasViradas().get(i).getRecurso().getNome() +"\n"+
                 "      Qtd Recurso:"+getCartasViradas().get(i).getQtdRecurso()+"\n";
                    
            for(int m=0;m<getCartasViradas().get(i).getAccoes().size();m++)
            {
               if(getCartasViradas().get(i).getAccoes().get(m) != null) 
                   s+="      Acções:"+getCartasViradas().get(i).getAccoes().get(m).getNome() +"\n";
            }           
        }
        return s;
    } 

    /**
     * @return the estadoActual
     */
    public IEstados getEstadoActual() {
        return estadoActual;
    }

    /**
     * @param estadoActual the estadoActual to set
     */
    public void setEstadoActual(IEstados estadoActual) {
        this.estadoActual = estadoActual;
    }

    /**
     * @return the jogadorActivo
     */
    public Jogador getJogadorActivo() {
        return jogadorActivo;
    }

    /**
     * @param jogadorActivo the jogadorActivo to set
     */
    public void setJogadorActivo(Jogador jogadorActivo) {
        this.jogadorActivo = jogadorActivo;
    }

    /**
     * @return the estadoAnterior
     */
    public IEstados getEstadoAnterior() {
        return estadoAnterior;
    }

    /**
     * @param estadoAnterior the estadoAnterior to set
     */
    public void setEstadoAnterior(IEstados estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }
    
    public boolean isEstadoAnterior(Class estado) {
        return estadoAnterior != null && estadoAnterior.getClass() == estado;
    }
    
    public List<EstadoListener> getListeners() {
        if (listeners == null)
        {
            listeners = new ArrayList<>();
        }
        return listeners;
    }

}
