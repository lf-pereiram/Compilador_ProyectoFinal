package vistas;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import controlador.Compilador;
import controlador.ParseException;

public class Main {
	Compilador compilador;

	private JFrame 		frame;
	private JFileChooser jFileChooser;
	private JTextArea 	textArea1;
	private String 		nombreArchivo;
	private JTable 		tableSimbolos;

	final static String compiler 	= "Compilador";
	final static String sinCambios 	= "< No se han guardado los Cambios >";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		compilador = new Compilador();
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(50, 50, 900, 750);
		frame.setTitle("Compilador");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		/**
		 * Parte del menu
		 */
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 900, 22);
		frame.getContentPane().add(menuBar);

		{
			JMenu menu1 = new JMenu();
			menu1.setText("Archivo");
			menuBar.add(menu1);

			// MenuItem para llamar el metodo de crear un nuevo archivo
			JMenuItem menuItem11 = new JMenuItem();
			menuItem11.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			menuItem11.setText("Nuevo Archivo");
			menu1.add(menuItem11);

			// MenuItem para llamar el metodo de abrir y cargar archivos
			JMenuItem menuItem12 = new JMenuItem();
			menuItem12.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CargarArchivo();
				}
			});
			menuItem12.setText("Abrir Archivo");
			menu1.add(menuItem12);

			// MenuItem para guardar los avances de un codigo creado
			JMenuItem menuItem13 = new JMenuItem();
			menuItem13.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			menuItem13.setText("Guardar");
			menu1.add(menuItem13);

			// MenuItem para guardar un archivo con otro nombre o en otro lugar
			JMenuItem menuItem14 = new JMenuItem();
			menuItem14.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				}
			});
			menuItem14.setText("Guardar como...");
			menu1.add(menuItem14);

			// MenuItem para salir del software
			JMenuItem menuItem15 = new JMenuItem();
			menuItem15.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Salir();
				}
			});
			menuItem15.setText("Salir");
			menu1.add(menuItem15);

			JMenu menu2 = new JMenu();
			menu2.setText("Mas");
			menuBar.add(menu2);

			// MenuItem para mostrar informacion de los creadores del software
			JMenuItem menuItem21 = new JMenuItem();
			menuItem21.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Acerca();
				}
			});
			menuItem21.setText("Acerca de...");
			menu2.add(menuItem21);
		}

		/**
		 * Parte del panel principal
		 */
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setBounds(0, 22, 900, 706);
		frame.getContentPane().add(panelPrincipal);
		panelPrincipal.setLayout(null);

		{
			/**
			 * Panel de Codigo Fuente
			 */
			JPanel panelCodigoFuente = new JPanel();
			panelCodigoFuente.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panelCodigoFuente.setBounds(10, 10, 550, 510);
			panelPrincipal.add(panelCodigoFuente);
			panelCodigoFuente.setLayout(null);

			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 10, 530, 450);
			panelCodigoFuente.add(scrollPane);

			textArea1 = new JTextArea();
			textArea1.setFont(new Font("Consolas", Font.PLAIN, 13));
			TextLineNumber tlnCodFuente = new TextLineNumber(textArea1);
			scrollPane.setViewportView(textArea1);
			scrollPane.setRowHeaderView(tlnCodFuente);

			Label lblcodfuente = new Label();
			lblcodfuente.setAlignment(Label.CENTER);
			lblcodfuente.setText(sinCambios);
			lblcodfuente.setBounds(10, 470, 400, 30);
			panelCodigoFuente.add(lblcodfuente);

			/**
			 * Boton para Compilar...
			 */
			JButton btnCompilar = new JButton("COMPILAR");
			btnCompilar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CompilarCodFuente();
				}
			});
			btnCompilar.setBounds(416, 470, 124, 30);
			panelCodigoFuente.add(btnCompilar);

			/**
			 * Panel de Tabla de Simbolos
			 */
			JPanel panelTablaSimb = new JPanel();
			panelTablaSimb.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panelTablaSimb.setBounds(570, 10, 320, 250);
			panelPrincipal.add(panelTablaSimb);
			panelTablaSimb.setLayout(null);

			Label lblTablaSimb = new Label();
			lblTablaSimb.setFont(new Font("Tahoma", Font.BOLD, 20));
			lblTablaSimb.setAlignment(Label.CENTER);
			lblTablaSimb.setSize(300, 30);
			lblTablaSimb.setLocation(10, 10);
			lblTablaSimb.setText("Tabla de Simbolos");
			panelTablaSimb.add(lblTablaSimb);

			JScrollPane scrollPaneTablaSimb = new JScrollPane();
			scrollPaneTablaSimb.setBounds(10, 50, 300, 190);
			panelTablaSimb.add(scrollPaneTablaSimb);
			
			tableSimbolos = new JTable();
			scrollPaneTablaSimb.setViewportView(tableSimbolos);

			/**
			 * Panel de Arbol de Derivacion
			 */
			JPanel panelArbolDer = new JPanel();
			panelArbolDer.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panelArbolDer.setBounds(570, 270, 320, 250);
			panelPrincipal.add(panelArbolDer);
			panelArbolDer.setLayout(null);

			Label lblArbolDer = new Label();
			lblArbolDer.setFont(new Font("Tahoma", Font.BOLD, 20));
			lblArbolDer.setAlignment(Label.CENTER);
			lblArbolDer.setSize(300, 30);
			lblArbolDer.setLocation(10, 10);
			lblArbolDer.setText("Árbol de Derivación");
			panelArbolDer.add(lblArbolDer);

			JScrollPane scrollPaneArbolDer = new JScrollPane();
			scrollPaneArbolDer.setBounds(10, 50, 300, 190);
			panelArbolDer.add(scrollPaneArbolDer);
			
			JTextArea textArea3 = new JTextArea();
			scrollPaneArbolDer.setViewportView(textArea3);

			/**
			 * Panel de Errores
			 */
			JPanel panelErrores = new JPanel();
			panelErrores.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panelErrores.setBounds(10, 530, 880, 170);
			panelPrincipal.add(panelErrores);
			panelErrores.setLayout(null);

			Label lblErrores = new Label();
			lblErrores.setFont(new Font("Tahoma", Font.BOLD, 20));
			lblErrores.setSize(860, 30);
			lblErrores.setLocation(10, 10);
			lblErrores.setText("Errores");
			panelErrores.add(lblErrores);

			JScrollPane scrollPaneErrores = new JScrollPane();
			scrollPaneErrores.setBounds(10, 50, 860, 110);
			panelErrores.add(scrollPaneErrores);

			JTextArea textArea4 = new JTextArea();
			scrollPaneErrores.setViewportView(textArea4);

		}

	}

	/**
	 * **************************************************************
	 * METODOS LLAMADOS DESDE LOS ACTIONPERFORMED
	 * **************************************************************
	 */

	/**
	 * 
	 */
	public void NuevoArchivo() 
	{
		
		if (textArea1.getText().equals("")) {
			frame.setTitle(compiler);
			textArea1.setText("");
		} else {
			
		}
	}

	/**
	 * 
	 */
	public void CargarArchivo() {

		try {
			/** llamamos el metodo que permite cargar la ventana */
			jFileChooser = new JFileChooser();
			FileNameExtensionFilter filtro = new FileNameExtensionFilter("SHTML", "shtml");
			jFileChooser.setFileFilter(filtro);
			jFileChooser.showOpenDialog(null);
			/** abrimos el archivo seleccionado */
			File abre = jFileChooser.getSelectedFile();
			String texto = "";
			String aux = "";
			/**
			 * recorremos el archivo, lo leemos para plasmarlo en el area de
			 * texto
			 */
			if (abre != null) {
				nombreArchivo = "";
				nombreArchivo = abre.getName();
				FileReader archivos;

				archivos = new FileReader(abre);

				BufferedReader lee = new BufferedReader(archivos);
				while ((aux = lee.readLine()) != null) {
					texto += aux + "\u005cn";
				}
				lee.close();
			}
			aux ="";
			
			frame.setTitle(compiler + " - " + nombreArchivo);
			
			textArea1.setText(texto);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(jFileChooser, 
					"ERROR! - Archivo No Encontrado o No Existente!");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(jFileChooser, 
					"ERROR! - No se puede Leer el Archivo!");
			e.printStackTrace();
		} 

	}

	/**
	 * 
	 */
	public void GuardarArchivo() {

	}

	/**
	 * 
	 */
	public void GuardarComoArchivo() {

	}

	/**
	 * 
	 */
	public void Salir() {
		System.exit(0);
	}

	/**
	 * 
	 */
	public void Acerca() {
		String[] lista = { "Software creado por:", " ", 
				"* Juliana Andrea Davila", 
				"* Luisa Fernanda Pereira",
				"* Roger Fabian Cordoba Garcia" };
		JOptionPane.showMessageDialog(frame, lista);
	}

	/**
	 * 
	 */
	public void CompilarCodFuente() {
		if (textArea1.getText().isEmpty()) {
			JOptionPane.showMessageDialog(frame, "No hay codigo para analizar... D=");
		} else {
			JOptionPane.showMessageDialog(frame, "Compilando...");

			InputStream stream = new ByteArrayInputStream(textArea1.getText().getBytes(StandardCharsets.UTF_8));

			System.out.println(
					"---------- INICIANDO AN\u00c1LISIS L\u00c9XICO PARA EL ARCHIVO " + nombreArchivo + " ----------");
			System.out.println("Ingrese el c\u00f3digo a analizar:");

			@SuppressWarnings("unused")
			Compilador parser = new Compilador(stream);
			// parser.TokenList();
			try {
				Compilador.Programa();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Analisis terminado:");
			System.out.println("no se han hallado errores l\u00e9xicos");

			LlenarTablaArbol( compilador.getListaSimbolos() );
		}
	}
	
	/**
	 * 
	 */
	private void LlenarTablaArbol(ArrayList<String> lista){
		
		DefaultTableModel modelo = new DefaultTableModel();
		modelo.addColumn("Variable");
		modelo.addColumn("Lexema");
		
		for (String obj : lista) {
			String[] elementos = obj.split(";");
			
			modelo.addRow(elementos);
		}
		
		tableSimbolos.getModel();
	}
}
