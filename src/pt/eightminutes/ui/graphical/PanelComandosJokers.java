package pt.eightminutes.ui.graphical;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import pt.eightminutes.logic.Carta;
import pt.eightminutes.logic.Jogador;
import pt.eightminutes.logic.Recurso;
import pt.eightminutes.logic.RecursoAlimento;
import pt.eightminutes.logic.RecursoFerro;
import pt.eightminutes.logic.RecursoJoia;
import pt.eightminutes.logic.RecursoMadeira;
import pt.eightminutes.logic.RecursoUtensilio;
import pt.eightminutes.states.AguardaJokers;

public class PanelComandosJokers extends PanelBase implements Observer{
    Carta joker;
    
    public PanelComandosJokers(PanelBase owner, DataController controller) {
        super(owner,controller);
        
        Jogador jogador = getJogo().getJogadorActivo();      
        
        this.setLayout(new FlowLayout());
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(600,100));
        this.setMinimumSize(new Dimension(600,100));
        this.setMaximumSize(new Dimension(600,100));
        
        if(!getJogo().isJokersAtribuidos())
        {
                    
            if(!jogador.getListaCartaJokers().isEmpty())
            {
              this.add(new JLabel("Escolha de Jokers"), BorderLayout.CENTER);
              this.add(new JLabel("Escolha o Joker:"+ String.format("%s", jogador.getListaCartaJokers().get(0))), BorderLayout.CENTER);
              joker = jogador.getListaCartaJokers().get(0);
              showRecursos();
            } 
            else
            {
                getJogo().passaVez();
            }
        }
        else
        {            
            String msg = "E o VENCEDOR é........ "+String.format("%s",getJogo().getJogadorVencedor().getNome());            
            JOptionPane.showMessageDialog(null,msg,"Vencedor",JOptionPane.WARNING_MESSAGE);
            getJogo().mostraPontuacao();
        }
    }
    
    public void showRecursos() {               
        // Lista de acções da carta seleccionada        
        ButtonRecurso btRecurso;
        Jogador jogador= getJogo().getJogadorActivo();
        Carta carta = jogador.getCartaActiva();
        Recurso itemRecurso;
        
        
        JPanel panelRecurso;
        panelRecurso = new JPanel();
        panelRecurso.setBackground(this.getBackground());
        this.add(panelRecurso, BorderLayout.CENTER); 

        //Alimento
        itemRecurso = new RecursoAlimento();
        btRecurso = addButtonRecurso(panelRecurso,itemRecurso);            
        btRecurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clique na recurso               
                Recurso recurso= ((ButtonRecurso)e.getSource()).getRecurso(); 
                
                getJogo().defineRecurso(joker, recurso);
            }
        });
        
        //Ferro
        itemRecurso = new RecursoFerro();
        btRecurso = addButtonRecurso(panelRecurso,itemRecurso);            
        btRecurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clique na recurso               
                Recurso recurso= ((ButtonRecurso)e.getSource()).getRecurso(); 
                getJogo().defineRecurso(joker, recurso);
            }
        });

        panelRecurso = new JPanel();
        panelRecurso.setBackground(this.getBackground());
        this.add(panelRecurso, BorderLayout.CENTER);         
        
        
        //Joia
        itemRecurso = new RecursoJoia();
        btRecurso = addButtonRecurso(panelRecurso,itemRecurso);            
        btRecurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clique na recurso               
                Recurso recurso= ((ButtonRecurso)e.getSource()).getRecurso(); 
                getJogo().defineRecurso(joker, recurso);
            }
        });
        
        //Madeira
        itemRecurso = new RecursoMadeira();
        btRecurso = addButtonRecurso(panelRecurso,itemRecurso);            
        btRecurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clique na recurso               
                Recurso recurso= ((ButtonRecurso)e.getSource()).getRecurso(); 
                getJogo().defineRecurso(joker, recurso);
            }
        });
        
        panelRecurso = new JPanel();
        panelRecurso.setBackground(this.getBackground());
        this.add(panelRecurso, BorderLayout.CENTER);     
        
        //Utensilios
        itemRecurso = new RecursoUtensilio();
        btRecurso = addButtonRecurso(panelRecurso,itemRecurso);            
        btRecurso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clique na recurso               
                Recurso recurso= ((ButtonRecurso)e.getSource()).getRecurso(); 
                getJogo().defineRecurso(joker, recurso);
            }
        });
        // Refrescar
        validate();
        repaint();
    }

    protected static Image createImage(String path) {

        BufferedImage imagem = null;
        try {
            imagem = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println(" erro ao carregar as imagens");
        }
        return imagem;
    }
    
    private ButtonRecurso addButtonRecurso(JPanel panel,Recurso recurso)
    {
        ButtonRecurso btRecurso = new ButtonRecurso(recurso);
        
        
        Image imgRecurso = createImage("src/pt/eightminutes/ui/recursos/rec"+String.format("%s",recurso.getNome())+".jpg");
        
        if (imgRecurso != null) {
            // Formatar botão consoante a imagem
            btRecurso.setMargin(new Insets(0,0,0,0));
            btRecurso.setContentAreaFilled(false);
            btRecurso.setFocusPainted(false);
            btRecurso.setBorder(BorderFactory.createEmptyBorder());
            btRecurso.setIcon(new ImageIcon(imgRecurso));
            btRecurso.setText(String.format("Qtd: %d",getJogo().getJogadorActivo().getQtdRecurso(recurso)));
        }
        else
            btRecurso.setText("Recurso "+String.format("%s",recurso.getNome()));
        String imgUrl="";
        
        if(recurso.getClass()==RecursoAlimento.class)
            imgUrl = "src/pt/eightminutes/ui/cards/card"+String.format("%03d",3)+".jpg";
        else
        if(recurso.getClass()==RecursoFerro.class)
            imgUrl = "src/pt/eightminutes/ui/cards/card"+String.format("%03d",1)+".jpg";
        else
        if(recurso.getClass()==RecursoUtensilio.class)
            imgUrl = "src/pt/eightminutes/ui/cards/card"+String.format("%03d",6)+".jpg";
        else
        if(recurso.getClass()==RecursoJoia.class)
            imgUrl = "src/pt/eightminutes/ui/cards/card"+String.format("%03d",28)+".jpg";
        else
        if(recurso.getClass()==RecursoMadeira.class)
            imgUrl = "src/pt/eightminutes/ui/cards/card"+String.format("%03d",18)+".jpg";
        
        btRecurso.setToolTipText(
         "<html><img src=\"" +
            ButtonRecurso.class.getResource(imgUrl) +
        "\"> Recurso "
         );
             
        panel.add(btRecurso);
        panel.add(Box.createRigidArea(new Dimension(15,15)));
        
        return btRecurso;
    }

    @Override
    public void update(Observable o, Object arg) 
    {
        clear();
        showRecursos();
        // Escolha da carta
        if (getJogo().getEstadoActual().getClass() == AguardaJokers.class) {
            setEnabled(true);
        }
        else {
            setEnabled(false);
        }
    }
    
}
