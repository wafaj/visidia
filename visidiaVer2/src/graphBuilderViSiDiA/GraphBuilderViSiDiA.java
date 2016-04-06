package graphBuilderViSiDiA;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import visidia.graph.Edge;
import visidia.graph.Graph;
import visidia.graph.Vertex;
import visidia.io.gml.GMLGraphFileFilter;
import visidia.io.gml.GMLGraphIO;



public class GraphBuilderViSiDiA {
	private static BuildGraphFrame windowBuildGraph;

	public static void main(String[] args) {
		windowBuildGraph = new BuildGraphFrame();
		windowBuildGraph.setVisible(true);
		windowBuildGraph.btnGenererGraph
				.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (windowBuildGraph.validInput()) {
							System.out
									.println("le bouton dans l'autre fenetre build graphe cliqu� avec "
											+ windowBuildGraph.getDensite());
							generateGraph();
							// dispose();

						} else
							JOptionPane.showMessageDialog(new JFrame(),
									"erreur de saisie !", "Dialog",
									JOptionPane.ERROR_MESSAGE);
					}

					private boolean generateGraph() {
						if (windowBuildGraph.validInput()) {
							System.out
									.println("vous pouvez g�n�rer votre graphe");
							if (windowBuildGraph.getSelectedTopology().equals(
									"Grille  nxn")) {
								return genereateGrille(
										windowBuildGraph.getMaxRange(),
										windowBuildGraph.getNbNodes());

							} else if (windowBuildGraph.getSelectedTopology()
									.equals("Rectangle")) {
								return genereateRectangle(
										windowBuildGraph.getMaxRange(),
										windowBuildGraph.getNbNodes(),
										windowBuildGraph.getDensite(),
										windowBuildGraph.getD1(),
										windowBuildGraph.getD2()

								);

							} else if (genereateGraphe(
									windowBuildGraph.getSelectedTopology(),
									windowBuildGraph.getMaxRange(),
									windowBuildGraph.getNbNodes(),
									windowBuildGraph.getDensite())) {

								System.out
										.println("G�ration du graphe r�ussit ");
								return true;
							} else {
								System.out
										.println("G�n�ration du graphe echou� ");
								return false;
							}

						} else {
							System.out
									.println("vous ne pouvez pas g�n�rer le graphe, erreur de saisie");
							return false;
						}
					}

					private boolean genereateRectangle(int maxRange, int nbNodes, int densite,
							int D1, int D2) {

						Graph graph = new Graph();
						double nbRegions = (D1 * D2);
						double a = Math
								.sqrt(((double) nbNodes / ((double) ((double) densite / 1000) * (double) nbRegions)));
						int unitee = (int) Math.ceil(a);
						System.out.println("unit� =ceil(a)=" + unitee);
						int i = 0;
						Random rn = new Random(System.currentTimeMillis());
						int x = 0;
						int y = 0;
						do {
							x = rn.nextInt(unitee * D1);
							y = rn.nextInt(unitee * D2);

							System.out.println("New Vertex" + x + "_" + y + "____"
									+ (nbNodes / nbNodes));
							Vertex v = graph.createVertex(i);
							v.setPos(x,y);
							v.setLabel("A");
							i++;
						} while (i < nbNodes);

						for (i = 0; i < nbNodes; i++) {
							for (int j = i + 1; j < nbNodes; j++) {
								if(graph.getVertex(i).getPos().distance(graph.getVertex(j).getPos())<=maxRange){

								//if ((graph.getVertex(i).distanceFrom(graph.getVertex(j))) <= maxRange) {
									//Edge edge = new Edge(graph.getVertex(i),graph.getVertex(j), false);
									graph.getVertex(i).linkTo(graph.getVertex(j), false);
								}
							}
						}

						// ////////////////////////*********************************//////////////////////////:
						JFileChooser fc = new JFileChooser(".");
						javax.swing.filechooser.FileFilter gmlFileFilter = new GMLGraphFileFilter();
						fc.addChoosableFileFilter(gmlFileFilter);
						fc.setFileFilter(gmlFileFilter);
						fc.setAcceptAllFileFilterUsed(false);

						int returnVal = fc.showSaveDialog(windowBuildGraph);
						if (returnVal == JFileChooser.CANCEL_OPTION)
							return false;
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File fFile = fc.getSelectedFile();

							if (fFile.exists()) {
								int response = JOptionPane.showConfirmDialog(null,
										"Overwrite existing file?", "Confirm Overwrite",
										JOptionPane.OK_CANCEL_OPTION,
										JOptionPane.QUESTION_MESSAGE);
								if (response == JOptionPane.CANCEL_OPTION)
									return false;
							}

							String filename = fFile.getName();

							if (!filename.endsWith(".gml"))
								fFile = new File(filename + ".gml");

							new GMLGraphIO(fFile).save(graph);

						}
						return true;

					}

					private boolean genereateGrille(int maxRange, int nbNodes) {
						Graph graph = new Graph();
						System.out.println("NbNodes=" + nbNodes);
						double nbRang = Math.sqrt(nbNodes);
						int nbRangInt = (int) Math.ceil(nbRang);
						int numero = 0;// ;
						for (int i = 0; i < nbRangInt; i++) {
							for (int j = 0; j < nbRangInt; j++) {
								if (numero < nbNodes) {
									int x = j * maxRange;
									int y = i * maxRange;
									Vertex v = graph.createVertex(numero);// Vertex();
									v.setPos(x, y);
									v.setLabel("A");
									numero++;
								}
							}
						}

						for (int i = 0; i < nbNodes; i++) {
							for (int j = i + 1; j < nbNodes; j++) {
								if(graph.getVertex(i).getPos().distance(graph.getVertex(j).getPos())<=maxRange){
									graph.getVertex(i).linkTo(graph.getVertex(j), false);
								}
							}
						}
						for (int i = 0; i < nbNodes; i++) {
							System.out.println(graph.getVertex(i).getPos());
						}
						System.out.println("graph [\n"+"\tcomment \"Generated by ViSiDiA\"\n"+"\tdirected 0\n");
						for (int i = 0; i < nbNodes; i++) {
							System.out.println(" node [\n"+"\t\tid "+graph.getVertex(i).getId()+"\n\t\tlabel \"A\""+
									"\n\t\tgraphics ["+
									"\n\t\t\tx "+(int)graph.getVertex(i).getPos().getX()+
									"\n\t\t\ty "+(int)graph.getVertex(i).getPos().getY()+
									"\n\t\t]"+
									"\n\t]");
							
						}
						for (int i = 0; i < nbNodes; i++) {
							;//String string = args[i];
							
						}
						// ////////////////////////*********************************//////////////////////////:
						JFileChooser fc = new JFileChooser(".");
						javax.swing.filechooser.FileFilter gmlFileFilter = new GMLGraphFileFilter();
						fc.addChoosableFileFilter(gmlFileFilter);
						fc.setFileFilter(gmlFileFilter);
						fc.setAcceptAllFileFilterUsed(false);

						int returnVal = fc.showSaveDialog(windowBuildGraph);
						if (returnVal == JFileChooser.CANCEL_OPTION)
							return false;
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File fFile = fc.getSelectedFile();

							if (fFile.exists()) {
								int response = JOptionPane.showConfirmDialog(
										null, "Overwrite existing file?",
										"Confirm Overwrite",
										JOptionPane.OK_CANCEL_OPTION,
										JOptionPane.QUESTION_MESSAGE);
								if (response == JOptionPane.CANCEL_OPTION)
									return false;
							}

							String filename = fFile.getName();

							if (!filename.endsWith(".gml"))
								fFile = new File(filename + ".gml");

							new GMLGraphIO(fFile).save(graph);

						}
						return true;

					}

					private boolean genereateGraphe(String selectedTopology,
							int maxRange, int nbNodes, int densite) {
						int indexTopology = -1;
						int nbRegions = 1;
						if (selectedTopology.equals("Thin H")) {
							indexTopology = 0;
							nbRegions = 30;

						} else if (selectedTopology.equals("Thin Cross")) {
							indexTopology = 1;
							nbRegions = 20;
						} else if (selectedTopology.equals("S")) {
							indexTopology = 2;
							nbRegions = 11;
						} else if (selectedTopology.equals("Large Cross")) {
							indexTopology = 3;
							nbRegions = 5;
						} else if (selectedTopology.equals("L")) {
							indexTopology = 4;
							nbRegions = 4;
						} else if (selectedTopology.equals("Large H")) {
							indexTopology = 5;
							nbRegions = 12;
						} else if (selectedTopology.equals("Large H")) {
							indexTopology = 5;
							nbRegions = 12;
						}

						Graph graph = new Graph();
						double a = Math
								.sqrt(((double) nbNodes / ((double) ((double) densite / 1000) * (double) nbRegions)));
						int unitee = (int) Math.ceil(a);
						System.out.println("unit� =ceil(a)=" + unitee);
						int i = 0;
						Random rn = new Random(System.currentTimeMillis());
						int x = 0;
						int y = 0;
						do {
							x = rn.nextInt(unitee);
							y = rn.nextInt(unitee);

							System.out.println("New Vertex" + x + "_" + y
									+ "____" + (nbNodes / nbNodes));
							Vertex v = graph.createVertex(i);
							v.setPos(deplace(x,dx(unitee, nbNodes, i, indexTopology,nbRegions), unitee),
									deplace(y,dy(unitee, nbNodes, i, indexTopology,nbRegions), unitee));
							v.setLabel("A");
							i++;
							/* positinner */
							System.out.println("New Vertex" + x + "_" + y
									+ "    " + (nbNodes / 30));
							// if (appartient(x,y,unitee,indexTopology)){
							// v.setId(i);
							// }
						} while (i < nbNodes);

						for (i = 0; i < nbNodes; i++) {
							for (int j = i + 1; j < nbNodes; j++) {
								if(graph.getVertex(i).getPos().distance(graph.getVertex(j).getPos())<=maxRange){
								//if ((graph.getVertex(i).distanceFrom(graph.getVertex(j))) <= maxRange) {
									//Edge edge = new Edge(graph.getVertex(i),graph.getVertex(j), false);
									graph.getVertex(i).linkTo(graph.getVertex(j), false);
								}
							}
						}

						// ////////////////////////*********************************//////////////////////////:
						JFileChooser fc = new JFileChooser(".");
						javax.swing.filechooser.FileFilter gmlFileFilter = new GMLGraphFileFilter();
						fc.addChoosableFileFilter(gmlFileFilter);
						fc.setFileFilter(gmlFileFilter);
						fc.setAcceptAllFileFilterUsed(false);

						int returnVal = fc.showSaveDialog(windowBuildGraph);
						if (returnVal == JFileChooser.CANCEL_OPTION)
							return false;
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File fFile = fc.getSelectedFile();

							if (fFile.exists()) {
								int response = JOptionPane.showConfirmDialog(
										null, "Overwrite existing file?",
										"Confirm Overwrite",
										JOptionPane.OK_CANCEL_OPTION,
										JOptionPane.QUESTION_MESSAGE);
								if (response == JOptionPane.CANCEL_OPTION)
									return false;
							}

							String filename = fFile.getName();

							if (!filename.endsWith(".gml"))
								fFile = new File(filename + ".gml");

							new GMLGraphIO(fFile).save(graph);

						}
						return true;

					}

					private int dy(int unitee, int nbNodes, int i,
							int indexTopology, int nbRegions) {

						switch (indexTopology) {
						case 0:
							return dyThinH(unitee, nbNodes, i, nbRegions);
						case 1:
							return dyThinCross(unitee, nbNodes, i, nbRegions);// ok
						case 2:
							return dyS(unitee, nbNodes, i, nbRegions);
						case 3:
							return dyLargeCross(unitee, nbNodes, i, nbRegions);
						case 4:
							return dyL(unitee, nbNodes, i, nbRegions);
						case 5:
							return dyLargeH(unitee, nbNodes, i, nbRegions);
						default:
							return -1;
						}
					}

					private int dyLargeH(int unitee, int nbNodes, int rang,
							int nbRegions) {
						int tab[][] = { { 1, 8 }, { 2, 9 }, { 3, 6, 7, 10 },
								{ 4, 11 }, { 5, 12 } };
						int i = 0;
						for (i = 0; i < 5; i++) {
							if (appartient(
									(int) Math.ceil(rang
											/ (nbNodes / nbRegions)), tab[i]))
								return i;
						}
						return i - 1;

					}

					private int dyL(int unitee, int nbNodes, int rang,
							int nbRegions) {
						int tab[][] = { { 1 }, { 2 }, { 3, 4 } };
						int i = 0;
						for (i = 0; i < 4; i++) {
							if (appartient(
									(int) Math.ceil(rang
											/ (nbNodes / nbRegions)), tab[i]))
								return i;
						}
						return i - 1;

					}

					private int dyLargeCross(int unitee, int nbNodes, int rang,
							int nbRegions) {
						int tab[][] = { { 2 }, { 1, 3, 5 }, { 4 } };
						int i = 0;
						for (i = 0; i < 3; i++) {
							if (appartient(
									(int) Math.ceil(rang
											/ (nbNodes / nbRegions)), tab[i]))
								return i;
						}
						return i - 1;

					}

					private int dyS(int unitee, int nbNodes, int rang,
							int nbRegions) {
						int tab[][] = { { 1, 5, 8 }, { 2 }, { 3, 6, 9 },
								{ 10 }, { 4, 7, 11 } };
						int i = 0;
						for (i = 0; i < 5; i++) {
							if (appartient(
									(int) Math.ceil(rang
											/ (nbNodes / nbRegions)), tab[i]))
								return i;
						}
						return i - 1;

					}

					private int dyThinCross(int unitee, int nbNodes, int rang,
							int nbRegions) {
						int tab[][] = { { 1, 6, 10, 12, 16 }, { 2, 7, 13, 17 },
								{ 3, 18 }, { 4, 8, 14, 19 },
								{ 5, 9, 11, 15, 20 }

						};
						int i = 0;
						for (i = 0; i < 5; i++) {
							if (appartient(
									(int) Math.ceil(rang
											/ (nbNodes / nbRegions)), tab[i]))
								return i;
						}
						return i - 1;

					}

					private int dyThinH(int unitee, int nbNodes, int rang,
							int nbRegions) {
						int tab[][] = { { 1, 8, 10, 16, 22, 24 },
								{ 2, 11, 17, 25 }, { 3, 12, 18, 26 },
								{ 4, 27 }, { 5, 13, 19, 28 },
								{ 6, 14, 20, 29 }, { 7, 9, 15, 21, 23, 30 } };
						int i = 0;
						for (i = 0; i < 7; i++) {
							if (appartient(
									(int) Math.ceil(rang
											/ (nbNodes / nbRegions)), tab[i]))
								return i;
						}
						return i - 1;

					}

					private int dx(int unitee, int nbNodes, int rang,
							int indexTopology, int nbRegions) {
						switch (indexTopology) {
						case 0:
							return dxThinH(unitee, nbNodes, rang, nbRegions);
						case 1:
							return dxThinCross(unitee, nbNodes, rang, nbRegions);// ok
						case 2:
							return dxS(unitee, nbNodes, rang, nbRegions);
						case 3:
							return dxLargeCross(unitee, nbNodes, rang,
									nbRegions);
						case 4:
							return dxL(unitee, nbNodes, rang, nbRegions);
						case 5:
							return dxLargeH(unitee, nbNodes, rang, nbRegions);
						default:
							return -1;

						}

					}

					/*
					 * private int dxRectangle(int unitee, int nbNodes, int
					 * rang, int nbRegions, int d1, int d2) { return( (int)
					 * Math.ceil(rang/(nbNodes/nbRegions))%d1) ; }
					 */

					private int dxLargeH(int unitee, int nbNodes, int rang,
							int nbRegions) {
						int tab[][] = { { 1, 2, 3, 4, 5 }, { 6 }, { 7 },
								{ 8, 9, 10, 11, 12 }

						};
						int i = 0;
						for (i = 0; i < tab.length; i++) {
							if (appartient(
									(int) Math.ceil(rang
											/ (nbNodes / nbRegions)), tab[i]))
								return i;
						}
						return (i - 1);

					}

					private int dxL(int unitee, int nbNodes, int rang,
							int nbRegions) {
						int tab[][] = { { 1, 2, 3 }, { 4 }

						};
						int i = 0;
						for (i = 0; i < 2; i++) {
							if (appartient(
									(int) Math.ceil(rang
											/ (nbNodes / nbRegions)), tab[i]))
								return i;
						}
						return (i - 1);
					}

					private int dxLargeCross(int unitee, int nbNodes, int rang,
							int nbRegions) {
						int tab[][] = { { 1 }, { 2, 3, 4 }, { 5 } };
						int i = 0;
						for (i = 0; i < 3; i++) {
							if (appartient(
									(int) Math.ceil(rang
											/ (nbNodes / nbRegions)), tab[i]))
								return i;
						}
						return (i - 1);
					}

					private int dxS(int unitee, int nbNodes, int rang,
							int nbRegions) {
						int tab[][] = { { 1, 2, 3, 4 }, { 5, 6, 7 },
								{ 8, 9, 10, 11 }, };
						int i = 0;
						for (i = 0; i < 3; i++) {
							if (appartient(
									(int) Math.ceil(rang
											/ (nbNodes / nbRegions)), tab[i]))
								return i;
						}
						return (i - 1);
					}

					private int dxThinCross(int unitee, int nbNodes, int rang,
							int nbRegions) {
						int tab[][] = { { 1, 2, 3, 4, 5 }, { 6, 7, 8, 9 },
								{ 10, 11 }, { 12, 13, 14, 15 },
								{ 16, 17, 18, 19, 20 } };
						int i = 0;
						for (i = 0; i < 5; i++) {
							if (appartient(
									(int) Math.ceil(rang
											/ (nbNodes / nbRegions)), tab[i]))
								return i;
						}
						return (i - 1);
					}

					private int dxThinH(int unitee, int nbNodes, int rang,
							int nbRegions) {
						int tab[][] = { { 1, 2, 3, 4, 5, 6, 7 }, { 8, 9 },
								{ 10, 11, 12, 13, 14, 15 },
								{ 16, 17, 18, 19, 20, 21 }, { 22, 23 },
								{ 24, 25, 26, 27, 28, 29, 30 } };
						int i = 0;
						for (i = 0; i < 6; i++) {
							if (appartient(
									(int) Math.ceil(rang
											/ (nbNodes / nbRegions)), tab[i]))
								return i;
						}
						return (i - 1);

					}

					// i,x,nbNodes,unitee,indexTopology
					private int positinnerX(int i, int x, int nbNodes,int unitee, int indexTopology) {
						switch (indexTopology) {
						case 0:
							return positinnerXThinH(i, x, unitee, nbNodes);
						case 1:
							return positinnerXThinCross(i, x, unitee, nbNodes);// ok
						case 2:
							return positinnerXS(i, x, unitee, nbNodes);
						case 3:
							return positinnerXLargeCross(i, x, unitee, nbNodes);
						case 4:
							return positinnerXL(i, x, unitee, nbNodes);
						case 5:
							return positinnerXLargeH(i, x, unitee, nbNodes);
						default:
							return -1;

						}

					}

					private int positinnerXLargeH(int i, int x, int unitee,
							int nbNodes) {
						// TODO Auto-generated method stub
						return 0;
					}

					private int positinnerXL(int i, int x, int unitee,
							int nbNodes) {
						// TODO Auto-generated method stub
						return 0;
					}

					private int positinnerXLargeCross(int i, int x, int unitee,
							int nbNodes) {
						// TODO Auto-generated method stub
						return 0;
					}

					private int positinnerXS(int i, int x, int unitee,
							int nbNodes) {
						// TODO Auto-generated method stub
						return 0;
					}

					private int positinnerXThinCross(int i, int x, int unitee,
							int nbNodes) {
						// TODO Auto-generated method stub
						return 0;
					}

					private int positinnerY(int i, int x, int nbNodes,
							int unitee, int indexTopology) {
						switch (indexTopology) {
						case 0:
							return positinnerYThinH(i, x, unitee, nbNodes);
						case 1:
							return positinnerYThinCross(i, x, unitee, nbNodes);// ok
						case 2:
							return positinnerYS(i, x, unitee, nbNodes);
						case 3:
							return positinnerYLargeCross(i, x, unitee, nbNodes);
						case 4:
							return positinnerYL(i, x, unitee, nbNodes);
						case 5:
							return positinnerYLargeH(i, x, unitee, nbNodes);
						default:
							return -1;

						}

					}

					private int positinnerYL(int i, int x, int unitee,
							int nbNodes) {
						// TODO Auto-generated method stub
						return 0;
					}

					private int positinnerYS(int i, int x, int unitee,
							int nbNodes) {
						// TODO Auto-generated method stub
						return 0;
					}

					private int positinnerYLargeCross(int i, int x, int unitee,
							int nbNodes) {
						// TODO Auto-generated method stub
						return 0;
					}

					private int positinnerYLargeH(int i, int x, int unitee,
							int nbNodes) {
						// TODO Auto-generated method stub
						return 0;
					}

					private int positinnerYThinCross(int i, int x, int unitee,
							int nbNodes) {
						// TODO Auto-generated method stub
						return 0;
					}

					private int positinnerYThinH(int i, int x, int unitee,
							int nbNodes) {
						int tab[][] = { { 1, 8, 10, 16, 22, 24 },
								{ 2, 11, 17, 25 }, { 3, 12, 18, 26 },
								{ 4, 27 }, { 5, 14, 20, 29 },
								{ 6, 14, 20, 29 }, { 7, 9, 15, 21, 23, 30 } };
						/*
						 * int tab[][] = null; int tab1[]={1,8,10,16,22,24}; int
						 * tab2[]={2,11,17,25}; int tab3[]={3,12,18,26}; int
						 * tab4[]={4,27}; int tab5[]={5,14,20,29}; int
						 * tab6[]={6,14,20,29}; int tab67[]={7,9,15,21,23,30};
						 * //int tab7[]={}; //tab[]=tab; tab[0]=tab1;
						 * tab[1]=tab2; tab[2]=tab3; tab[3]=tab4; tab[4]=tab5;
						 * tab[5]=tab6;
						 */
						for (int ii = 0; ii < 7; ii++)
							if (ii > (i * (nbNodes / 30)))
								return deplace(x, ii, unitee);
						return 0;
					}

					private int positinnerXThinH(int i, int x, int unitee,
							int nbNodes) {
						int tab[][] = { 
								{ 1, 2, 3, 4, 6, 7 }, 
								{ 8, 9 },
								{ 10, 11, 12, 13, 14, 15 },
								{ 16, 17, 18, 19, 20, 21 }, 
								{ 22, 23 },
								{ 24, 25, 26, 27, 28, 29, 30 }
								};
						/*
						 * int tab[0]={1,2,3,4,6,7}; int tab[1]={8,9}; int
						 * tab[2]={10,11,12,13,14,15}; int
						 * tab[3]={16,17,18,19,20,21}; int tab[4]={22,23}; int
						 * tab[5]={24,25,26,27,28,29,30};
						 */
						// int tab7[]={};
						// tab[]=tab;
						// tab[0]=tab1;
						// tab[1]=tab2;
						// tab[2]=tab3;
						// tab[3]=tab4;
						// tab[4]=tab5;
						// tab[5]=tab6;
						for (int ii = 0; ii < 6; ii++)
							// if((nbNodes/30)==ii)
							if (ii > (i * (nbNodes / 30)))
								return deplace(x, ii, unitee);
						// return deplace(x,ii,unitee);
						return 0;
					}

					private int deplace(int x, int deplacement, int unitee) {
						return (x + (deplacement * unitee));
					}

					private boolean appartient(int i, int[] tab) {
						for (int t : tab) {
							if ((i + 1) == t) {
								System.out.println(i + " appartient ");
								return true;
							}
							// else
							// System.out.println(i+" appartient passssss ");
						}
						return false;
					}

					private int newXVertex(int i, int nbNodes, int unitee) {
						// TODO Auto-generated method stub
						return 0;
					}

					private boolean appartient(int x, int y, int unite,
							int topologyIndex) {

						switch (topologyIndex) {
						case 0:
							return appartientThinH(x, y, unite);
						case 1:
							return appartientThinCross(x, y, unite);// ok
						case 2:
							return appartientS(x, y, unite);
						case 3:
							return appartientLargeCross(x, y, unite);
						case 4:
							return appartientL(x, y, unite);
						case 5:
							return appartientLargeH(x, y, unite);
						default:
							return false;

						}

					}

					// /////////////////////////////////////////////////////////////////////
					// private boolean genereate_LargeCross(int maxRange, int
					// nbNodes, int
					// densite) {
					// // TODO Auto-generated method stub
					// return true;
					// }
					//
					// private boolean genereate_L(int maxRange, int nbNodes,
					// int densite) {
					// // TODO Auto-generated method stub
					// return true;
					// }
					//
					// private boolean genereate_H(int maxRange, int nbNodes,
					// int densite) {
					// // TODO Auto-generated method stub
					// return true;
					// }
					//
					// private boolean genereate_S(int maxRange, int nbNodes,
					// int densite) {
					// // TODO Auto-generated method stub
					// return true;
					// }
					//
					// private boolean genereate_ThinH(int maxRange, int
					// nbNodes, int densite) {
					//
					// return true;
					// }

					// ///////////////////////////////////////////////////////////
					private boolean appartient(int x, int bornInf, int bornSup) {
						if ((x >= bornInf) && (x <= bornSup))
							return true;
						return false;
					}

					// /////////////////////////////////////////////////////
					private boolean appartientLargeH(int x, int y, int unite) {
						if ((appartient(x, 1 * unite, 2 * unite) || appartient(
								x, 3 * unite, 4 * unite))
								&& (appartient(y, 1 * unite, 4 * unite)))
							return true;
						if ((appartient(x, 2 * unite, 3 * unite))
								&& (appartient(y, 2 * unite, 3 * unite)))
							return true;
						return false;
					}

					// ///////////////////////////////////////////////////////
					private boolean appartientL(int x, int y, int unite) {
						if ((appartient(x, 1 * unite, 2 * unite))
								&& (appartient(y, 1 * unite, 4 * unite)))
							return true;
						if ((appartient(x, 2 * unite, 3 * unite))
								&& (appartient(y, 3 * unite, 4 * unite)))
							return true;
						return false;
					}

					// /////////////////////////////////////////////////////////////////
					private boolean appartientLargeCross(int x, int y, int unite) {
						if ((appartient(x, 1 * unite, 2 * unite) || appartient(
								x, 3 * unite, 4 * unite))
								&& (appartient(y, 2 * unite, 3 * unite)))
							return true;
						if ((appartient(x, 2 * unite, 3 * unite))
								&& (appartient(y, 1 * unite, 4 * unite)))
							return true;
						return false;
					}

					// ///////////////////////////////////////////////////////////////
					private boolean appartientS(int x, int y, int unite) {
						if ((appartient(x, unite, 2 * unite) || appartient(x,
								5 * unite, 6 * unite))
								&& (appartient(y, 1 * unite, 8 * unite)))
							return true;
						if ((appartient(x, 2 * unite, 3 * unite))
								&& (appartient(y, unite, 2 * unite)
										|| appartient(y, 5 * unite, 6 * unite) || appartient(
											y, 7 * unite, 8 * unite)))
							return true;
						if ((appartient(x, 3 * unite, 4 * unite))
								&& (appartient(y, unite, 2 * unite)
										|| appartient(y, 3 * unite, 4 * unite)
										|| appartient(y, 5 * unite, 6 * unite) || appartient(
											y, 7 * unite, 8 * unite)))
							return true;
						if ((appartient(x, 4 * unite, 5 * unite))
								&& (appartient(y, unite, 2 * unite)
										|| appartient(y, 3 * unite, 4 * unite) || appartient(
											y, 7 * unite, 8 * unite)))
							return true;
						return false;
					}

					// ////////////////////////////////////////////////////////////
					private boolean appartientThinCross(int x, int y, int unite) {
						if ((appartient(x, unite, 2 * unite) || appartient(x,
								5 * unite, 6 * unite))
								&& appartient(y, unite, 6 * unite))
							return true;
						if ((appartient(x, 2 * unite, 3 * unite) || appartient(
								x, 4 * unite, 5 * unite))
								&& (appartient(y, unite, 3 * unite) || appartient(
										y, 4 * unite, 6 * unite)))
							return true;

						if ((appartient(x, 3 * unite, 4 * unite))
								&& (appartient(y, unite, 2 * unite) || appartient(
										y, 5 * unite, 6 * unite)))
							return true;

						return false;
					}

					// /////////////////////////////////////////////////////////////////
					private boolean appartientThinH(int x, int y, int unite) {
						if ((appartient(x, unite, 2 * unite) || appartient(x,
								6 * unite, 7 * unite))
								&& appartient(y, unite, 9 * unite))
							return true;
						if ((appartient(x, 2 * unite, 3 * unite) || appartient(
								x, 5 * unite, 6 * unite))
								&& (appartient(y, unite, 2 * unite) || appartient(
										y, 8 * unite, 9 * unite)))
							return true;
						if ((appartient(x, 3 * unite, 5 * unite))
								&& (appartient(y, unite, 5 * unite) || appartient(
										y, 6 * unite, 9 * unite)))
							return true;

						return false;
					}

				});

	}

}
