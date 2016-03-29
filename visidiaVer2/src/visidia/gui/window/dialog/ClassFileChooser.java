package visidia.gui.window.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import visidia.io.ClassFileFilter;
import visidia.io.VisidiaIO;
import visidia.misc.ClassIdentifier;
import visidia.misc.ImageHandler;
import visidia.misc.SpringUtilities;
import visidia.misc.VisidiaSettings;
import visidia.simulation.process.SensorMover;
import visidia.simulation.process.agent.Agent;
import visidia.simulation.process.agent.AgentMover;
import visidia.simulation.process.agent.SynchronousAgent;
import visidia.simulation.process.algorithm.Algorithm;
import visidia.simulation.process.algorithm.SensorSyncAlgorithm;
import visidia.simulation.process.algorithm.SynchronousAlgorithm;
import visidia.simulation.process.synchronization.SynchronizationAlgorithm;

// TODO: Auto-generated Javadoc
/**
 * This class is a custom file chooser for compiled Java classes.
 */
public class ClassFileChooser extends JDialog {

	private static final long serialVersionUID = 713051922219015658L;

	/** The main panel. */
	private JPanel mainPanel;

	/** The button parent folder. */
	private JButton buttonParentFolder;
	
	/** The button recently opened. */
	//private JButton buttonRecentlyOpened;
	
	/** The button visidia. */
	private JButton buttonVisidia;
	
	/** The button user. */
	private JButton buttonUser;
	
	/** The button add new. */
	private JButton buttonAddNew;
	
	/** The button remove. */
	private JButton buttonRemove;
	
	/** The table. */
	private JTable table;
	
	/** The file table. */
	FileTable fileTable;
	
	/** The sorter. */
	private TableRowSorter<DefaultTableModel> sorter;
	
	/** The filter text. */
	private JTextField filterText;
	
	/** The description text. */
	private JTextArea descriptionText;
	
	/** The button cancel. */
	private JButton buttonCancel;
	
	/** The button load. */
	private JButton buttonLoad;

	/** The root. */
	private String root;
	
	/** The boolean indicating if we are dealing with a jar file. */
	private boolean inJarFile;
	
	/** The base directory. */
	private String baseDir;
	
	/** The current directory. */
	private String currentDir;
		
	/** The loader class. */
	private Class<?> loaderClass;
	
	/** The return value. */
	private int returnValue = JFileChooser.CANCEL_OPTION;
	
	/** The selected object. */
	private Object selectedObject = null;

	/** The selected object identifier. */
	private ClassIdentifier selectedObjectIdentifier = ClassIdentifier.emptyClassId;
	
	/** The model id. */
	private int modelId;

	// maps the loaded objects to their index in the table model
	/** The loaded objects. */
	Hashtable<Integer, Object> loadedObjects = new Hashtable<Integer, Object>();

	// maps the loaded object identifiers to their index in the table model
	/** The loaded object identifiers. */
	Hashtable<Integer, ClassIdentifier> loadedObjectIdentifiers = new Hashtable<Integer, ClassIdentifier>();

	/** The accessory. */
	private JComponent accessory = null;
	
	/**
	 * Instantiates a new class file chooser.
	 * Operates on files located either on disk or in a jar.
	 * In the former case, root is an absolute path and class files to load are: root/baseDir/myClass
	 * In the latter case, root is a jar file name and class files to load are: /baseDir/myClass in the jar file
	 * 
	 * @param owner the owner
	 * @param root the root directory or jar file
	 * @param baseDir the base directory in root
	 * @param loaderClass the loader class
	 */
	public ClassFileChooser(Frame owner, String root, String baseDir, Class<?> loaderClass, JComponent accessory) {
		super(owner, "Load compiled Java class", true); // modal dialog
		this.inJarFile = root.endsWith(".jar");
		this.root = root;
		this.baseDir = baseDir;
		this.loaderClass = loaderClass;
		this.accessory = accessory;
		buildGUI();
		setLocationRelativeTo(owner);
	}

	/**
	 * Show dialog.
	 * 
	 * @return the return state of the file chooser on popdown: JFileChooser.CANCEL_OPTION, JFileChooser.APPROVE_OPTION
	 */
	public int showDialog() {
		setVisible(true);
		return returnValue;
	}

	/**
	 * Gets the selected object.
	 * 
	 * @return the selected object
	 */
	public Object getSelectedObject() {
		return selectedObject;
	}

	/**
	 * Gets the selected object identifier.
	 * 
	 * @return the selected object identifier
	 */
	public ClassIdentifier getSelectedObjectIdentifier() {
		return (selectedObjectIdentifier.equals(ClassIdentifier.emptyClassId) ? null : selectedObjectIdentifier);
	}

	/**
	 * Display user objects.
	 */
	private void displayUserObjects() {
		currentDir = null;
		Vector<String> userFilePaths = (Vector<String>) VisidiaSettings.getInstance().getObject(VisidiaSettings.Constants.USER_FILE_PATHS);
		if (userFilePaths == null) {
			fileTable.clearTable();
			return;
		}
		File[] userFiles = new File[userFilePaths.size()];
		for (int i = 0; i < userFilePaths.size(); ++i) userFiles[i] = new File(userFilePaths.elementAt(i));
		fileTable.load(userFiles);					
	}

	/**
	 * Builds the GUI.
	 */
	private void buildGUI() {
		ActionListener buttonListener = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				descriptionText.setText("");
				filterText.setText("");
				JButton source = (JButton) e.getSource();
				if (source.equals(buttonParentFolder)) {
					if (currentDir == null || currentDir.equals(baseDir)) return;
					String dirToGo = new File(currentDir).getParent();
					dirToGo = dirToGo.replace(File.separator, "/");
					fileTable.load(dirToGo);
				//} else if (source.equals(buttonRecentlyOpened)) {
					// TODO
				} else if (source.equals(buttonVisidia)) {
					fileTable.load(baseDir);
				} else if (source.equals(buttonUser)) {
					displayUserObjects();
				} else if (source.equals(buttonAddNew)) {
					JFileChooser fc = new JFileChooser(".");
					javax.swing.filechooser.FileFilter classFileFilter = new ClassFileFilter();
					fc.addChoosableFileFilter(classFileFilter);
					fc.setFileFilter(classFileFilter);
					fc.setMultiSelectionEnabled(true);

					int returnVal = fc.showOpenDialog(mainPanel);
					if (returnVal == JFileChooser.CANCEL_OPTION) return;
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File[] newFiles = fc.getSelectedFiles();
						for (int i = 0; i < newFiles.length; ++i) {
							Object newObj = fileTable.loadObject(newFiles[i]);
							if (newObj == null) {
								descriptionText.setText("Unable to load file: " + newFiles[i].getAbsolutePath());
								continue;
							}

							// check for object existence in both visidia and user object lists
							String newFilePath = newFiles[i].getAbsolutePath();
							newFilePath = newFilePath.replace(File.separator + "." + File.separator, File.separator);
							if (newFilePath.startsWith(root+File.separator+baseDir)) {
								descriptionText.setText(newFilePath + " is already loaded in ViSiDiA.");
								continue;					
							}
							Vector<String> userFilePaths = (Vector<String>) VisidiaSettings.getInstance().getObject(VisidiaSettings.Constants.USER_FILE_PATHS);
							if (userFilePaths == null) userFilePaths = new Vector<String>();
							else if (userFilePaths.contains(newFilePath)) {
								descriptionText.setText(newFilePath + " is already contained in user preferences.");
								continue;							
							}
							String absolutePath = newFiles[i].getAbsolutePath();
							int index = absolutePath.lastIndexOf(File.separator);
							try {
								fileTable.putObjectInTable(newObj, newFiles[i].getName(), (index > 0) ? absolutePath.substring(0, index) : "", new URL("file:"+newFiles[i].getAbsolutePath()));
							} catch (MalformedURLException exc) {
							}
							
							// add object to user preferences
							userFilePaths.add(newFilePath);
							VisidiaSettings.getInstance().set(VisidiaSettings.Constants.USER_FILE_PATHS, userFilePaths);
							displayUserObjects();
						}						
					}
				} else if (source.equals(buttonRemove)) {
					if (selectedObject == null) return;
					Vector<String> userFilePaths = (Vector<String>) VisidiaSettings.getInstance().getObject(VisidiaSettings.Constants.USER_FILE_PATHS);
					if (userFilePaths == null) return;

					int viewRow = table.getSelectedRow();
					if (viewRow < 0) return;
					int modelRow = table.convertRowIndexToModel(viewRow);
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					String filePath = (String) model.getValueAt(modelRow, 5);
					String fileName = filePath + File.separator + (String) model.getValueAt(modelRow, 0) + ".class";
					if (userFilePaths.remove(fileName)) {
						VisidiaSettings.getInstance().set(VisidiaSettings.Constants.USER_FILE_PATHS, userFilePaths);
						displayUserObjects();
					}
				} else if (source.equals(buttonCancel)) {
					returnValue = JFileChooser.CANCEL_OPTION;
					setVisible(false);
					dispose();
				} else if (source.equals(buttonLoad)) {
					if (selectedObject != null) {
						if (selectedObject instanceof String) { // this is a directory to explore
							String dirToGo = (String) selectedObject;
							if (dirToGo.endsWith("/")) dirToGo = dirToGo.substring(0, dirToGo.length()-1);
							fileTable.load(currentDir+"/"+dirToGo);}
						else {
						returnValue = JFileChooser.APPROVE_OPTION;
						setVisible(false);
						dispose();}
					} else descriptionText.setText("Please select a file!");
				}
			}
		};

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel hPanel = new JPanel();
		hPanel.setLayout(new BoxLayout(hPanel, BoxLayout.X_AXIS));

		JPanel fcPanel = new JPanel();
		fcPanel.setLayout(new BoxLayout(fcPanel, BoxLayout.Y_AXIS));

		// The button box
		buttonParentFolder = new JButton(ImageHandler.getInstance().createImageIcon("/parent_folder.png"));
		buttonParentFolder.setToolTipText("Parent folder");
		buttonParentFolder.addActionListener(buttonListener);
		//buttonRecentlyOpened = new JButton("Recently opened");
		//buttonRecentlyOpened.addActionListener(buttonListener);
		buttonVisidia = new JButton("ViSiDiA");
		buttonVisidia.addActionListener(buttonListener);
		buttonUser = new JButton("User");
		buttonUser.addActionListener(buttonListener);
		buttonAddNew = new JButton("Add new");
		buttonAddNew.addActionListener(buttonListener);
		buttonRemove = new JButton("Remove");
		buttonRemove.addActionListener(buttonListener);

		JPanel buttonPanel = new JPanel(new SpringLayout());
		buttonPanel.add(buttonParentFolder);
		//buttonPanel.add(buttonRecentlyOpened);
		buttonPanel.add(buttonVisidia);
		buttonPanel.add(buttonUser);
		buttonPanel.add(buttonAddNew);  
		buttonPanel.add(buttonRemove);        
		SpringUtilities.makeCompactGrid(buttonPanel, 1, buttonPanel.getComponentCount(), 6, 6, 6, 6);
		fcPanel.add(buttonPanel);

		// The file table
		fileTable = new FileTable();
		table = fileTable.getTable();
		configure(table);
		fileTable.load(baseDir);
		table.setFillsViewportHeight(true);

		//For the purposes of this example, better to have a single selection.
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//When selection changes, provide user with row numbers for both view and model.
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent event) {
						int viewRow = table.getSelectedRow();
						selectedObject = null;
						if (viewRow < 0) {
							//Selection got filtered away.
							descriptionText.setText("");
						} else {
							int modelRow = table.convertRowIndexToModel(viewRow);
							Object obj = loadedObjects.get(modelRow);
							selectedObject = obj;
							selectedObjectIdentifier = loadedObjectIdentifiers.get(modelRow);
							if (obj instanceof Algorithm) descriptionText.setText(((Algorithm) obj).getDescription());
							else if (obj instanceof Agent) descriptionText.setText(((Agent) obj).getDescription());
							else if (obj instanceof AgentMover) descriptionText.setText(((AgentMover) obj).getDescription());
							else if (obj instanceof SensorMover) descriptionText.setText(((SensorMover) obj).getDescription());
							else descriptionText.setText("");
						}
					}
				}
		);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Point p = e.getPoint();

					try {
						int row = table.convertRowIndexToModel(table.rowAtPoint(p));
						int column = table.columnAtPoint(p);
						if (row >= 0 && column >= 0) {
							Object obj = loadedObjects.get(row);

							if (obj instanceof String) { // this is a directory to explore
								String dirToGo = (String) obj;
								if (dirToGo.endsWith("/")) dirToGo = dirToGo.substring(0, dirToGo.length()-1);
								fileTable.load(currentDir+"/"+dirToGo);
							} else { // this is a file to open
								if (selectedObject != null) {
									returnValue = JFileChooser.APPROVE_OPTION;
									setVisible(false);
									dispose();
								}
							}
						}
					} catch (IndexOutOfBoundsException exc) {
					}
				}
			}
		});

		//Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);
		//Add the scroll pane to this panel.
		fcPanel.add(scrollPane);

		//Create a separate form for filterText and statusText
		JPanel form = new JPanel(new SpringLayout());
		JLabel l1 = new JLabel("Filter Text:", SwingConstants.TRAILING);
		form.add(l1);
		filterText = new JTextField();
		//Whenever filterText changes, invoke newFilter.
		filterText.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						newFilter();
					}
					public void insertUpdate(DocumentEvent e) {
						newFilter();
					}
					public void removeUpdate(DocumentEvent e) {
						newFilter();
					}
				});
		l1.setLabelFor(filterText);
		form.add(filterText);
		SpringUtilities.makeCompactGrid(form, 1, 2, 6, 6, 6, 6);
		fcPanel.add(form);


		// The class description
		JPanel descriptionPanel = new JPanel(new SpringLayout());
		descriptionPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),
				BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Class description"))
		);
		descriptionText = new JTextArea(6, 10);
		descriptionText.setEditable(false);
		descriptionText.setLineWrap(true);
		descriptionText.setWrapStyleWord(true);
		JScrollPane descriptionScrollPane = new JScrollPane(descriptionText);

		descriptionPanel.add(descriptionScrollPane);
		SpringUtilities.makeCompactGrid(descriptionPanel, 1, 1, 6, 6, 6, 6);
		fcPanel.add(descriptionPanel);

		hPanel.add(fcPanel);

		// the accessory
		if (accessory != null) hPanel.add(accessory);
		
		mainPanel.add(hPanel);
		
		// The load/cancel buttons
		buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(buttonListener);
		buttonLoad = new JButton("Load");
		buttonLoad.addActionListener(buttonListener);

		JPanel buttonPanel2 = new JPanel(new SpringLayout());
		buttonPanel2.add(buttonCancel);
		buttonPanel2.add(buttonLoad);  
		SpringUtilities.makeCompactGrid(buttonPanel2, 1, buttonPanel2.getComponentCount(), 6, 6, 6, 6);
		mainPanel.add(buttonPanel2);

		setContentPane(mainPanel);
		pack();
		//Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		//setResizable(false);
		setVisible(false);
	}

	/**
	 * Update the row filter regular expression from the expression in the text box.
	 */
	private void newFilter() {
		RowFilter<DefaultTableModel, Object> rf = null;
		//If current expression doesn't parse, don't update.
		try {
			rf = RowFilter.regexFilter(filterText.getText(), 0);
		} catch (java.util.regex.PatternSyntaxException e) {
			return;
		}
		sorter.setRowFilter(rf);
	}

	/**
	 * Configure the table.
	 * 
	 * @param table the table
	 */
	private void configure(JTable table) {
		String[] colIds = { "Name", "Type", "mode", "For sensors?", "Status", "Path" };
		((DefaultTableModel) table.getModel()).setColumnIdentifiers(colIds);
		table.setIntercellSpacing(new Dimension(6, 2));
		table.getTableHeader().setReorderingAllowed(false);

		int[] colWidths = { 150, 80, 80, 50, 50, 200 };
		Dimension d = new Dimension();
		for (int j = 0; j < colWidths.length; j++) d.width += colWidths[j];
		d.height = 250;
		table.setPreferredScrollableViewportSize(d);
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnModel colModel = table.getColumnModel();
		for (int j = 0; j < colModel.getColumnCount(); j++) {
			TableColumn col = colModel.getColumn(j);
			col.setPreferredWidth(colWidths[j]);
		}
		table.getColumnModel().getColumn(0).setCellRenderer(new NameColumnRenderer());
	}


	/**
	 * The Class FileTable.
	 */
	private class FileTable {
		
		/** The table. */
		JTable table;

		/**
		 * Instantiates a new file table.
		 */
		public FileTable() {
			createTable();
		}

		/**
		 * Gets the table.
		 * 
		 * @return the table
		 */
		public JTable getTable() {
			return table;
		}

		/**
		 * Gets the extension.
		 * 
		 * @param file the file
		 * 
		 * @return the extension
		 */
		private String getExtension(File file) {
			String s = file.toString();
			int dot = s.lastIndexOf(".");
			return s.substring(dot+1);
		}

		/**
		 * Clear table.
		 */
		private void clearTable() {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			loadedObjects.clear();
			loadedObjectIdentifiers.clear();
			for (int i = model.getRowCount()-1; i >= 0; --i) model.removeRow(i);
		}

		/**
		 * Put object in table.
		 * 
		 * @param obj the object
		 * @param fileName the file name
		 * @param filePath the file path
		 */
		private void putObjectInTable(Object obj, String fileName, String filePath, URL url) {
			if (obj == null) return;

			// add a row in table
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Object[] data = new Object[model.getColumnCount()];
			Object[] properties = getProperties(obj);
			int index = fileName.lastIndexOf(".");
			data[0] = (index >= 0) ? fileName.substring(0, index) : fileName;
			data[1] = (String) properties[0];
			data[2] = (String) properties[1];
			data[3] = (properties[2] == null) ? "" : ((Boolean) properties[2]).toString();
			data[4] = "";
			data[5] = filePath;
			model.addRow(data);
			loadedObjects.put(modelId, obj);
			ClassIdentifier classId = (url.toExternalForm().startsWith("jar:") ? new ClassIdentifier(url, filePath, fileName) : new ClassIdentifier(url));
			loadedObjectIdentifiers.put(modelId, classId);
			modelId ++;
		}

		/**
		 * Load object.
		 * 
		 * @param f the file
		 * 
		 * @return the object
		 */
		private Object loadObject(File f) {
			if (!getExtension(f).equals("class")) return null;

			Constructor<?> constructor;
			VisidiaIO loaderObject;
			try {
				constructor = loaderClass.getConstructor(ClassIdentifier.class);
				loaderObject = (VisidiaIO) constructor.newInstance(new ClassIdentifier(new URL("file:"+f.getAbsolutePath())));
			} catch (Exception e) {
				return null;
			}
			
			return loaderObject.load();
		}

		/**
		 * Load from jar.
		 */
		private void loadFromJar() {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			clearTable();
			modelId = 0;
			Object[] data = new Object[model.getColumnCount()];

			URL url = null;
			JarFile jarFile = null;
			try {
				String jarFileName = "jar:" + (root.startsWith("http://") ? "" : "file:") + root + "!/";
				url = new URL(jarFileName);			
				JarURLConnection connection = (JarURLConnection) url.openConnection();
				jarFile = connection.getJarFile();
			} catch (Exception e) {
				return;
			}

			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				
				if (entry.getName().startsWith(currentDir)) {
					int index = entry.getName().lastIndexOf(".class");
					if (index == -1) continue;
					String className = entry.getName().substring(currentDir.length()+1, index);
					boolean isDirectory = className.contains("/");
					if (isDirectory) {
						data[0] = className.substring(0, className.indexOf("/")+1);
						if (loadedObjects.contains((String) data[0])) continue;
						data[1] = data[2] = data[3] = data[4] = data[5] = "";
						model.addRow(data);
						loadedObjects.put(modelId, (String) data[0]);
						loadedObjectIdentifiers.put(modelId, ClassIdentifier.emptyClassId);
						modelId ++;
						continue;
					} else {
						try {
							VisidiaIO loaderObject;
							try {
								Constructor<?> constructor = loaderClass.getConstructor(ClassIdentifier.class);
								loaderObject = (VisidiaIO) constructor.newInstance(new ClassIdentifier(url, currentDir, className));
							} catch (Exception e) {
								continue;
							}

							Object obj = loaderObject.load();						
							putObjectInTable(obj, className, currentDir, url);
						} catch (Exception e) {
							continue;
						}
					}	        	
				}					
			}

			try {
				jarFile.close();
			} catch (Exception e) {
			}
			if (modelId > 0) model.fireTableRowsInserted(0, modelId-1);

			// create a sorter
			sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel) table.getModel());
			table.setRowSorter(sorter);		
		}

		/**
		 * Load.
		 * 
		 * @param filePath the file path
		 */
		protected void load(String filePath) {
			currentDir = filePath;
			if (inJarFile) loadFromJar();	
			else load(new File(root+File.separator+currentDir).listFiles());
		}

		/**
		 * Load.
		 * 
		 * @param files the files
		 */
		protected void load(File[] files) {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			clearTable();
			if (files == null) return;
			modelId = 0;
			Object[] data = new Object[model.getColumnCount()];
			for (int j = 0; j < files.length; j++) {
				if (files[j].isDirectory()) {
					data[0] = files[j].getName() + "/";
					data[1] = data[2] = data[3] = data[4] = data[5] = "";
					model.addRow(data);
					loadedObjects.put(modelId, (String) data[0]);
					loadedObjectIdentifiers.put(modelId, ClassIdentifier.emptyClassId);
					modelId ++;
					continue;
				} else {
					try {
						Object obj = loadObject(files[j]);
						String absolutePath = files[j].getAbsolutePath();
						int index = absolutePath.lastIndexOf(File.separator);
						putObjectInTable(obj, files[j].getName(), (index > 0) ? absolutePath.substring(0, index) : "", new URL("file:"+files[j].getAbsolutePath()));
					} catch (Exception e) {
					}
				}	        	
			}
			if (modelId > 0) model.fireTableRowsInserted(0, modelId-1);

			// create a sorter
			sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel) table.getModel());
			table.setRowSorter(sorter);
		}

		/**
		 * Gets the properties.
		 * 
		 * @param object the object
		 * 
		 * @return the properties
		 */
		private Object[] getProperties(Object object) {
			Object[] properties = new Object[3]; // type, mode, forSensors?
			properties[0] = new String("unknown");
			properties[1] = new String("unknown");
			properties[2] = null;

			if (object instanceof SynchronizationAlgorithm) return properties;

			if (object instanceof SensorSyncAlgorithm) {
				properties[0] = new String("algorithm");
				properties[1] = new String("synchronous");
				properties[2] = new Boolean(true);
			} else if (object instanceof SynchronousAlgorithm) {
				properties[0] = new String("algorithm");
				properties[1] = new String("synchronous");
				properties[2] = new Boolean(false);
			} else if (object instanceof Algorithm) {
				properties[0] = new String("algorithm");
				properties[1] = new String("asynchronous");
				properties[2] = new Boolean(false);
			} else if (object instanceof SynchronousAgent) {
				properties[0] = new String("agent");
				properties[1] = new String("synchronous");
				properties[2] = new Boolean(false);
			} else if (object instanceof Agent) {
				properties[0] = new String("agent");
				properties[1] = new String("asynchronous");
				properties[2] = new Boolean(false);
			} else if (object instanceof SensorMover) {
				properties[0] = new String("mover");
				properties[2] = new Boolean(true);
			}

			return properties;
		}

		/**
		 * Creates the table.
		 */
		private void createTable() {
			DefaultTableModel model = new DefaultTableModel() {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			table = new JTable(model);
			table.setShowGrid(false);
			//table.setFont(table.getFont().deriveFont(Font.BOLD));
		}
	}


	/**
	 * The Class NameColumnRenderer.
	 */
	public class NameColumnRenderer extends DefaultTableCellRenderer {
		
		/** The leaf icon. */
		Icon leafIcon;
		
		/** The closed icon. */
		Icon closedIcon;

		/**
		 * Instantiates a new name column renderer.
		 */
		public NameColumnRenderer() {
			leafIcon = UIManager.getIcon("Tree.leafIcon");
			closedIcon = UIManager.getIcon("Tree.closedIcon");
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
		 */
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			Icon icon = null;
			if (value != null) {
				boolean isFolder = ((String) value).contains("/");
				icon = isFolder ? closedIcon : leafIcon;
			}
			setIcon(icon);
			return this;
		}
	}

}
