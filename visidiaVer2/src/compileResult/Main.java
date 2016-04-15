package compileResult;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Main {
	public static void main(String[] args) {
		File f = new File("./resultSim");
		ArrayList<String> listFichiers= 		getFilesList(f);
		
		
		ArrayList<String> listAlgo =getListAlgo();
		ArrayList<Integer> listSize = getNotRepeatedInt(listFichiers,1);
		
		for (Iterator iterator = listAlgo.iterator(); iterator.hasNext();) {
			String currentAlgo=(String) iterator.next();
			for (Iterator iterator2 = listSize.iterator(); iterator2.hasNext();) {
				Integer currentSize = (Integer) iterator2.next();
				ArrayList<String> tempFilesList= getTempList(currentAlgo , currentSize,listFichiers);
				System.out.println("Traitement de fichiers suivants en cours ... ");
				for (Iterator iterator3 = tempFilesList.iterator(); iterator3.hasNext();) {
					String tempfile = (String) iterator3.next();
					System.out.println(tempfile);
				}
				fixStatSynthesis(tempFilesList);
				System.out.println("");
				
				
				
				
			}
			
		}
		for (int j = 0; j < listFichiers.size(); j++) {
			;//System.out.println(listFichiers.get(j));
			
		}		
		for (int j = 0; j < listSize.size(); j++) {
			;//System.out.println(listSize.get(j));
			
		}
		for (Iterator iterator = listSize.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();
			
		}
		for (int i = 0; i < listFichiers.size(); i++) {
			for (Iterator iterator = listSize.iterator(); iterator.hasNext();) {
				Integer integer = (Integer) iterator.next();
				
			}
			
		}

		
		
		
	}

	private static void fixStatSynthesis(ArrayList<String> tempFilesList) {
		ArrayList<SimulationSynthesis> lsitSimSynthese =new ArrayList<SimulationSynthesis>();
		for (int i = 0; i < 9; i++) {
			lsitSimSynthese.add(new SimulationSynthesis(0,i+1) );	
		}
		
		int nbline=0;
		for (Iterator iterator = tempFilesList.iterator(); iterator.hasNext();) {
			String current = (String) iterator.next();
			//ouvrir et lire ligne par ligne le ficier 
			try {
				BufferedReader fichier  = new BufferedReader(new FileReader("./resultSim/"+current));
				String ligne;
				try {
					while ((ligne = fichier.readLine()) != null) {
						int indexLine=indexSim(ligne);
						lsitSimSynthese.get(indexLine).update(ligne);
						
					    // System.out.println(" 119 detected "+indexLine);					 
					     //System.out.println(ligne);
					     
					     nbline++;
					  }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			      try {
					fichier.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		for (Iterator iterator = lsitSimSynthese.iterator(); iterator.hasNext();) {
			SimulationSynthesis simulationSynthesis = (SimulationSynthesis) iterator.next();
			System.out.println(simulationSynthesis);
			
		}
		
		System.out.println(nbline);
	}

	private static int indexSim(String ligne) {
		return (extractIntFromStringAtPos(ligne,2)-1);
	}

	private static ArrayList<String> getTempList(String algo, Integer size, ArrayList<String> listFichiers) {
		ArrayList<String> list= new ArrayList<String>();
		for (Iterator iterator = listFichiers.iterator(); iterator.hasNext();) {
			String line = (String) iterator.next();
			if(line.contains(algo)&& (size.equals(extractIntFromStringAtPos(line,1)))){
				list.add(line);
			}
			
		}
		return list;
	}

	private static ArrayList<String> getListAlgo() {
		ArrayList<String> list= new ArrayList<String>();
		String[]tableauAlgos={"LSM_pts","LSM_Walk_pts", "RED_pts","SDC_pts"};
		for (int i = 0; i < tableauAlgos.length; i++) {
			list.add(tableauAlgos[i]);
		}
		return list;
	}

	private static ArrayList<Integer> getNotRepeatedInt(ArrayList<String> listFichiers, int pos) {
		ArrayList<Integer> listSize = new ArrayList<Integer>();
		for (int j = 0; j < listFichiers.size(); j++) {
			Integer currentInt=	extractIntFromStringAtPos(listFichiers.get(j),pos);
			if(currentInt!= 0 && !listSize.contains(currentInt))
				listSize.add(currentInt);
		}
		return listSize;
	}

	private static ArrayList<String> getFilesList(File f) {
		String[]tableauAlgos={"LSM_pts","LSM_Walk_pts", "RED_pts","SDC_pts"};
		String[] tableauFichiers;
		ArrayList<Integer> listSize = new ArrayList<Integer>();
		
		
		ArrayList<String> listFichiers=new ArrayList<String>();
		int sizeG=0;
		int versionG=0;
		
		//StatObj
		
		//System.out.println("size of integer in java  = "+ Integer.SIZE);
		
		
//		System.out.println("sizeG"+sizeG+"versionG"+versionG);

		int i;
		tableauFichiers = f.list();
		for (int j = 0; j < tableauFichiers.length; j++) {
			listFichiers.add(tableauFichiers[j]);
			
		}
		for (int j = 0; j < listFichiers.size(); j++) {
	;//		System.out.println(listFichiers.get(j));
			
		}
		Collections.sort(listFichiers);
		System.out.println("");
		for (int j = 0; j < listFichiers.size(); j++) {
			;//System.out.println(listFichiers.get(j));
			
		}

		
		//Collections.sort(listefichiers);
		//Collections.sort(listefichiers);	
		for (i = 0; i < tableauFichiers.length; i++) {
			String current = tableauFichiers[i];
			sizeG=extractIntFromStringAtPos(current,1);
			versionG=extractIntFromStringAtPos(current,2);
			//System.out.println(current);
			update(sizeG,versionG,current);
			//System.out.println("-----> sizeG"+sizeG+"versionG"+versionG);
			try {
				BufferedReader fichier  = new BufferedReader(new FileReader("./resultSim/"+current));
				String ligne;
				try {
					while ((ligne = fichier.readLine()) != null) {
					     ;// System.out.println(ligne);
					  }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			      try {
					fichier.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return listFichiers;

	}

	private static int extractIntFromStringAtPos(String line, int n) {
		Pattern pattern = Pattern.compile ("\\d+");
		String target = line.toString();
		Matcher matcher = pattern.matcher (target);
		
		int currentInt=0;
		for (int j = 0; j < n; j++) {
			if(matcher.find())
				currentInt =(int)Integer.parseInt(matcher.group());
			else
				return 0;
			
		}
		return currentInt;

	}

	private static int getVersionG(String line) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static int getSizeG(String line) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static void update(int sizeG, int versionG, String line) {
		
		Pattern pattern = Pattern.compile ("\\d+");
		String target = line.toString();
		Matcher matcher = pattern.matcher (target);
		
		if(matcher.find())
			sizeG=(int)Integer.parseInt(matcher.group());		
		if(matcher.find())
			versionG=(int)Integer.parseInt(matcher.group());


		
	}

}