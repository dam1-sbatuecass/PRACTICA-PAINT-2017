import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

public class VentanaPrincipal {

	/**
	 * IMPORTANTE: CADA HERRAMIENTA TENDRÁ UN CÓDIGO ASOCIADO
	 */
	final static int BOLIGRAFO = 0;
	final static int GOMA = 1;
	// AÑADE AQUÍ TU HERRAMIENTA;
	// TODO: Añadir la herramienta

	JTextField txt, tam;
	JLabel texto, tamaño, tipoLetra;

	JPanel panelSuperiorVentanaTexto, panelInferiorVentanaTexto;

	JRadioButton[] fuentes = new JRadioButton[3];
	JRadioButton[] estilo = new JRadioButton[3];

	String[] nombresFuentes = { "Arial", "Times new Roman", "Calibri" };
	String[] nombresEstilo = { "Negrita", "Cursiva", "ambas" };

	ButtonGroup grupoFuente = new ButtonGroup();
	ButtonGroup grupoEstilo = new ButtonGroup();
	SelectorColor botonColor;
	JButton botonAceptar;

	int herramientaActual = -1; // No hay nada por defecto.

	// La ventana principal, en este caso, guarda todos los componentes:
	JFrame ventana;

	// Una ventana para seleccionar el texto a introducir.
	JFrame ventanaTexto;

	// Paneles:
	JPanel panelSuperior;
	JPanel panelInferior;

	// Variables para dibujo
	JLabel lienzo;
	BufferedImage canvas;

	// Selector de colores;
	SelectorColor selector1;
	SelectorColor selector2;

	// Botones:
	JButton botonNuevo;
	JButton botonBoligrafo;
	JButton botonGoma;

	JButton botonTexto;

	// VARIABLES PROPIAS DE CADA GRUPO:
	// Grupo Jesús:
	int xAnt = -1;
	int yAnt = -1;

	final int strokeGOMA = 10;

	// Constructor, marca el tamaño y el cierre del frame
	public VentanaPrincipal() {
		ventana = new JFrame();
		ventana.setBounds(100, 50, 800, 600);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Método que inicializa todos los componentes de la ventana
	 */
	public void inicializarComponentes() {

		ventana.setLayout(new GridBagLayout());

		// ************************************************
		// PANEL SUPERIOR Y COMPONENTES DE PANEL SUPERIOR
		// ************************************************
		panelSuperior = new JPanel();
		panelSuperior.setLayout(new GridBagLayout());
		panelSuperior.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY),
				"Herramientas", TitledBorder.CENTER, TitledBorder.TOP));

		GridBagConstraints settings;
		settings = new GridBagConstraints();

		settings.gridx = 0;
		settings.gridy = 0;
		settings.weightx = 1;
		settings.ipady = 10;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelSuperior, settings);

		// Botón nuevo
		botonNuevo = new JButton(cargarIconoBoton("Imagenes/nuevo.png"));
		settings = new GridBagConstraints();
		settings.gridx = 0;
		settings.gridy = 0;
		settings.insets = new Insets(0, 10, 0, 0);
		panelSuperior.add(botonNuevo, settings);

		// Selector de color1
		selector1 = new SelectorColor(Color.BLACK);
		settings = new GridBagConstraints();
		settings.gridx = 1;
		settings.gridy = 0;
		settings.insets = new Insets(0, 10, 0, 0);
		settings.fill = GridBagConstraints.BOTH;
		panelSuperior.add(selector1, settings);

		// Selector de color2
		selector2 = new SelectorColor(Color.WHITE);
		settings = new GridBagConstraints();
		settings.gridx = 2;
		settings.gridy = 0;
		settings.insets = new Insets(0, 10, 0, 0);
		settings.fill = GridBagConstraints.BOTH;
		panelSuperior.add(selector2, settings);

		// Herramienta de bolígrafo
		botonBoligrafo = new JButton(cargarIconoBoton("Imagenes/boligrafo.png"));
		settings = new GridBagConstraints();
		settings.gridx = 3;
		settings.gridy = 0;
		settings.insets = new Insets(0, 10, 0, 0);
		panelSuperior.add(botonBoligrafo, settings);

		// Herramienta de borrar
		botonGoma = new JButton(cargarIconoBoton("Imagenes/borrar.png"));
		settings = new GridBagConstraints();
		settings.gridx = 4;
		settings.gridy = 0;
		settings.insets = new Insets(0, 10, 0, 0);
		panelSuperior.add(botonGoma, settings);

		/**
		 * VUESTRAS HERRAMIENTAS AQUÍ
		 */
		// TODO: Insertar un botón e implementar mi herramienta.
		botonTexto = new JButton("Texto");
		settings = new GridBagConstraints();
		settings.gridx = 5;
		settings.gridy = 0;
		settings.insets = new Insets(0, 10, 0, 0);
		panelSuperior.add(botonTexto, settings);

		// Un elemento que ocupe todo el espacio a la derecha:
		JPanel panelEspacioDerecha = new JPanel();
		settings = new GridBagConstraints();
		settings.gridx = 5; /*** OJO ***/
		settings.gridy = 0;
		settings.weightx = 1;
		panelSuperior.add(panelEspacioDerecha, settings);

		// ***************************
		// EL LIENZO DONDE PINTAMOS.
		// ***************************
		panelInferior = new JPanel();
		panelInferior.setBorder(BorderFactory.createLineBorder(Color.RED));
		panelInferior.setLayout(new GridBagLayout());
		settings = new GridBagConstraints();
		settings.gridx = 0;
		settings.gridy = 1;
		settings.weightx = 1;
		settings.weighty = 1;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelInferior, settings);

		lienzo = new JLabel();
		lienzo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lienzo.setHorizontalAlignment(SwingConstants.CENTER);
		settings = new GridBagConstraints();
		settings.gridx = 0;
		settings.gridy = 0;
		settings.fill = GridBagConstraints.BOTH;
		panelInferior.add(lienzo, settings);
		ventana.repaint();

	}

	/**
	 * Método que inicializa todos los listeners del programa.
	 */
	public void inicializarListeners() {

		// Lístener de carga de VentanaPrincipal. Cuando se carga la pantalla es cuando
		// se puede inicializar el canvas.
		ventana.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				actualizarCanvasVacio();
			}
		});

		botonNuevo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actualizarCanvasVacio();
			}
		});

		/**
		 * Cada nueva herramienta que añadas, tendrá un nuevo lístener:
		 */
		botonBoligrafo.addActionListener(anadirListenerHerramienta(BOLIGRAFO));
		botonGoma.addActionListener(anadirListenerHerramienta(GOMA));
		// TODO: Añadir nuevos listeners para las herramientas:

		lienzo.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// Dependiendo de la herramienta...
				switch (herramientaActual) {
				case BOLIGRAFO:
					mouseDraggedBoligrafo(e);
					break;

				case GOMA:
					borraGoma(e);
					break;

				default:
					break;
				}
				lienzo.repaint();
			}
		});

		lienzo.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				xAnt = e.getX();
				yAnt = e.getY();

			}
		});

		botonTexto.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Crea la interfaz
				inicializarComponentesVentanaTexto();

			}

			private void inicializarComponentesVentanaTexto() {

				// Los componentes que vamos a usar.
				ventanaTexto = new JFrame("Selecciona Texto");

				ventanaTexto.setBounds(100, 50, 700, 500);
				ventanaTexto.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				ventanaTexto.setVisible(true);
				ventanaTexto.setLayout(new GridBagLayout());

				panelSuperiorVentanaTexto = new JPanel(new GridBagLayout());
				GridBagConstraints settings = new GridBagConstraints();
				settings.gridx = 0;
				settings.gridy = 0;
				settings.ipadx = 93;
				ventanaTexto.add(panelSuperiorVentanaTexto, settings);

				panelInferiorVentanaTexto = new JPanel(new GridBagLayout());
				settings = new GridBagConstraints();
				settings.gridx = 0;
				settings.gridy = 1;
				ventanaTexto.add(panelInferiorVentanaTexto, settings);

				texto = new JLabel("Introduce el texto:");
				settings = new GridBagConstraints();
				settings.gridx = 0;
				settings.gridy = 0;

				panelSuperiorVentanaTexto.add(texto, settings);

				txt = new JTextField();
				settings = new GridBagConstraints();
				settings.gridx = 0;
				settings.gridy = 1;
				settings.weightx = 2;
				settings.fill = GridBagConstraints.BOTH;
				panelSuperiorVentanaTexto.add(txt, settings);

				tamaño = new JLabel("Tamaño");
				settings = new GridBagConstraints();
				settings.gridx = 0;
				settings.gridy = 0;
				panelInferiorVentanaTexto.add(tamaño, settings);

				tam = new JTextField();
				settings = new GridBagConstraints();
				settings.gridx = 0;
				settings.gridy = 1;
				settings.fill = GridBagConstraints.BOTH;
				panelInferiorVentanaTexto.add(tam, settings);

				botonColor = new SelectorColor(Color.BLACK);
				settings = new GridBagConstraints();
				settings.gridx = 1;
				settings.gridy = 1;
				panelInferiorVentanaTexto.add(botonColor, settings);

				for (int i = 0; i < fuentes.length; i++) {
					fuentes[i] = new JRadioButton(nombresFuentes[i]);
					settings = new GridBagConstraints();
					settings.gridx = 0;
					settings.gridy = (i + 2);
					settings.anchor = GridBagConstraints.WEST;
					grupoFuente.add(fuentes[i]);
					panelInferiorVentanaTexto.add(fuentes[i], settings);

				}

				for (int i = 0; i < estilo.length; i++) {
					estilo[i] = new JRadioButton(nombresEstilo[i]);
					settings = new GridBagConstraints();
					settings.gridx = 1;
					settings.gridy = (i + 2);
					grupoEstilo.add(estilo[i]);
					panelInferiorVentanaTexto.add(estilo[i], settings);

				}

				botonAceptar = new JButton("Aceptar");
				settings = new GridBagConstraints();
				settings.gridx = 0;
				settings.gridy = 5;
				settings.anchor = GridBagConstraints.WEST;
				panelInferiorVentanaTexto.add(botonAceptar, settings);

				botonAceptar.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Graphics graficos = canvas.getGraphics();
						graficos.setColor(botonColor.getColor());//Le damos el color de nuestro botonColor

						Font fuente = new Font("Dialog", Font.BOLD, Integer.parseInt((tam.getText())));//Creamos una fuente
						graficos.setFont(fuente);//Le damos una fuente

						graficos.drawString(txt.getText(), xAnt, yAnt);//Pintamos el texto

						graficos.dispose();
						lienzo.repaint();
						ventanaTexto.hide();

					}
				});
			}
		});

	}

	/**
	 * Método que Borra el canvas para pintarlo completamente en Blanco. El nuevo
	 * canvas se adapta al tamanio del lienzo.
	 */
	public void actualizarCanvasVacio() {
		canvas = new BufferedImage(panelInferior.getWidth(), panelInferior.getHeight(), BufferedImage.TYPE_INT_ARGB);
		lienzo.setIcon(new ImageIcon(canvas));
		Graphics graficos = canvas.getGraphics();
		graficos.setColor(selector2.getColor());
		graficos.fillRect(0, 0, panelInferior.getWidth(), panelInferior.getHeight());
		graficos.dispose();
		lienzo.repaint();
	}

	/**
	 * Método que nos devuelve un icono para la barra de herramientas superior.
	 * NOTA: Sería conveniente colocar una imagen con fondo transparente y que sea
	 * cuadrada, para no estropear la interfaz.
	 * 
	 * @param rutaImagen:
	 *            La ruta de la imagen.
	 * @return El ImageIcon que se utilizará en un botón.
	 */
	public ImageIcon cargarIconoBoton(String rutaImagen) {
		BufferedImage bufferAuxiliar = null;
		try {
			bufferAuxiliar = ImageIO.read(new File(rutaImagen));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ImageIcon(bufferAuxiliar.getScaledInstance(40, 40, BufferedImage.SCALE_SMOOTH));
	}

	/**
	 * Método que devuelve un actionListener que cambia la herramienta Actual a la
	 * que se pasa por parámetros
	 * 
	 * @param herramienta
	 * @return Un action listener que cambia la herramienta actual. Se puede
	 *         utilizar sobre los botones, por ejemplo.
	 */
	public ActionListener anadirListenerHerramienta(int herramienta) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				herramientaActual = herramienta;
			}
		};
	}

	/**
	 * Método que realiza todas las llamadas necesarias para inicializar la ventana
	 * correctamente.
	 */
	public void inicializar() {
		ventana.setVisible(true);
		inicializarComponentes();
		inicializarListeners();
	}

	/*****************************************
	 *****************************************
	 * AQUÍ VAN LOS MÉTODOS DE LOS LISTENERS:
	 *****************************************
	 *****************************************/

	/**
	 * Pinta la línea del bolígrafo al arrastrar.
	 * 
	 * @param e
	 */
	private void mouseDraggedBoligrafo(MouseEvent e) {
		Graphics graficos = canvas.getGraphics();
		graficos.setColor(Color.BLACK);
		// graficos.setColor(selector1.getColor());
		graficos.drawLine(xAnt, yAnt, e.getX(), e.getY());
		graficos.dispose();
		lienzo.repaint();
		xAnt = e.getX();
		yAnt = e.getY();
	}

	/**
	 * Borra donde esté el ratón.
	 * 
	 * @param e
	 */
	private void borraGoma(MouseEvent e) {
		Graphics graficos = canvas.getGraphics();
		graficos.setColor(selector2.getColor());
		graficos.fillOval(e.getX() - (strokeGOMA / 2), e.getY() - (strokeGOMA / 2), strokeGOMA, strokeGOMA);
		graficos.dispose();
	}

}