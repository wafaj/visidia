package visidia.examples.algo.Mazurkiewicz;


import visidia.misc.*;

import java.util.*;
import visidia.simulation.process.algorithm.Algorithm;
import visidia.simulation.process.edgestate.SyncState;
import visidia.simulation.process.messages.*;

public class Mazurkiewicz extends Algorithm {
    final int starCenter=-1;
    final int notInTheStar=-2;
    
    
    static int synchroNumber=0;
    static int messagesNumber=0;
    static String[] nodes={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    
    static MessageType synchronization = new MessageType("synchronization", false, java.awt.Color.blue);
    
    static MessageType labels = new MessageType("labels", true);
    static MessageType labels2 = new MessageType("EndMsg", true, java.awt.Color.magenta);
    static MessageType labels3 = new MessageType("NewId", true, java.awt.Color.blue);
    
    public Collection<MessageType> getMessageTypeList(){
        Collection<MessageType> typesList = new LinkedList<MessageType>();
        typesList.add(synchronization);
        typesList.add(labels);
        typesList.add(labels2);
        typesList.add(labels3);
        return typesList;
    }
    
    public void init(){
        
        int graphS = getNetSize(); /* The number of vertices in the graph */
        int synchro;
        boolean run = true; /* Stop criterion */
        boolean finishedNode[];
        int lastName;
        Knowledge node=new Knowledge();
        
        putProperty("myId",0,visidia.simulation.SimulationConstants.PropertyStatus.DISPLAYED);
        node.initial(graphS);
        
        finishedNode=new boolean[getArity()];
        for (int i=0;i<getArity();i++) {
            finishedNode[i]=false;
        }
        
        while(run){
        	
            synchro=starSynchro(finishedNode);
            if( synchro==starCenter ){
                
                for (int door=0;door<getArity();door++) {
                    if (!finishedNode[door])
                        receiveKnowledge(node,door);
                }
                lastName=node.myName();
                
                if ((node.myName() == 0) || (node.maxSet(node.neighbourNode(node.myName()),node.neighbour()))){
                    node.changeName(node.max()+1);
                    putProperty("label",nodes[node.myName()%26]);
                }
                
                Vector nameVector=new Vector(2);
                nameVector.add(new Integer(node.myName()));
                nameVector.add(new Integer(lastName));
                
               
                sendAll(new VectorMessage(nameVector,labels3));
                try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                messagesNumber+=getArity();
                putProperty("myId",new Integer(node.myName()));
                
                Vector vec=addVector(node.myName(),node.neighbour());
                
                node.changeKnowledge(vec);
                
                
                broadcastKnowledge(node);
                
                changeTable(node);
                if ( node.endKnowledge(graphS) ) {
                    run=false;
                }
                
                breakSynchro();
                
            }
            else {
                if ( synchro != notInTheStar) {
                    
                    sendKnowledge(node,synchro);
                    
                    Message newName = receiveFrom(synchro);
                    node.changeNeighbours(((VectorMessage)newName).data());
                    putProperty("My Neighbours", ((Vector) node.neighbour()).toString());
                    receiveKnowledge(node,synchro);
                    changeTable(node);
                }
                
            }
            
            
        }
        
        sendAll(new IntegerMessage(new Integer(-1),synchronization));
        System.out.println("Nombre de starSynchro = "+synchroNumber+"   Nombre de messages = "+messagesNumber);
        
    }
    
    public int starSynchro(boolean finishedNode[]){
        
        int arite = getArity() ;
        int[] answer = new int[arite] ;
        
        /*random */
        int choosenNumber = Math.abs(SynchronizedRandom.nextInt());
        
        /*Send to all neighbours */
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                sendTo(i,new IntegerMessage(new Integer(choosenNumber),synchronization));
            }
        }
        /*receive all numbers from neighbours */
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                Message msg = receiveFrom(i);
                answer[i]= ((IntegerMessage)msg).value();
                if (answer[i]==-1)
                    finishedNode[i]=true;
            }
        }
        
        /*get the max */
        int max = choosenNumber;
        for( int i=0;i < arite ; i++){
            if (! finishedNode[i]) {
                if( answer[i] >= max )
                    max = answer[i];
            }
        }
        
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                sendTo(i,new IntegerMessage(new Integer(max),synchronization));
            }
        }
        /*get alla answers from neighbours */
        for( int i = 0; i < arite; i++){
            if (! finishedNode[i]) {
                Message msg = receiveFrom(i);
                answer[i]= ((IntegerMessage)msg).value();
            }
        }
        
        /*get the max */
        max =choosenNumber;
        for( int i=0;i < arite ; i++){
            if( answer[i] >= max )
                max = answer[i];
        }
        
        if (choosenNumber >= max) {
            for( int door = 0; door < arite; door++){
                if (! finishedNode[door])
                    setDoorState(new SyncState(true),door);
            }
            
            for( int i = 0; i < arite; i++){
                if (! finishedNode[i]) {
                    sendTo(i,new IntegerMessage(new Integer(1),synchronization));
                }
            }
            
            for (int i=0;i<arite;i++) {
                if (! finishedNode[i]) {
                    Message msg=receiveFrom(i);
                }
            }
            synchroNumber++;
            return starCenter;
        }
        else {
            int inTheStar=notInTheStar;
            
            for( int i = 0; i < arite; i++){
                if (! finishedNode[i]) {
                    sendTo(i,new IntegerMessage(new Integer(0),synchronization));
                }
            }
            
            for (int i=0; i<arite;i++) {
                if (! finishedNode[i]) {
                    Message msg=receiveFrom(i);
                    if  (((IntegerMessage)msg).value() == 1) {
                        inTheStar=i;
                    }
                }
            }
            return inTheStar;
            
        }
    }
    
    
    public void breakSynchro() {
    	for( int door = 0; door < getArity(); door++){
    		setDoorState(new  SyncState(false),door);
        }
    }
    
    private void receiveKnowledge(Knowledge node,int door) {
        
        VectorMessage vm=(VectorMessage) receiveFrom(door);
        Vector<?> data =vm.data();
        while (((Integer)data.elementAt(0)).intValue() !=-1) {
            node.changeKnowledge(data);
            ((Integer)data.elementAt(0)).intValue();
            vm=(VectorMessage) receiveFrom(door);
            data = vm.data();
        }
    }
    
    private void broadcastKnowledge(Knowledge node) {
        Vector vec = new Vector();
        for (int i=1;i<=node.max();i++) {
            if ((node.neighbourNode(i))!=null) {
                vec=addVector(i,node.neighbourNode(i));
                try {
        			Thread.sleep(1000);
        		} catch (InterruptedException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		sendAll(new VectorMessage((Vector)vec.clone(),labels));
                vec.clear();
                messagesNumber+=getArity();
            }
        }
        try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        vec.add(new Integer(-1));
        vec.add(new Integer(-1));
        sendAll(new VectorMessage((Vector)vec.clone(),labels2));
    }
    
    private void sendKnowledge(Knowledge node,int synchro) {
        Vector vec = new Vector();
        for (int i=1;i<=node.max();i++) {
            if ((node.neighbourNode(i))!=null) {
                vec=addVector(i,node.neighbourNode(i));
                try {
        			Thread.sleep(100);
        		} catch (InterruptedException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
                sendTo(synchro,new VectorMessage((Vector)vec.clone(),labels));
                vec.clear();
                messagesNumber++;
            }
        }
        try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        vec.add(new Integer(-1));
        vec.add(new Integer(-1));
        sendTo(synchro,new VectorMessage((Vector)vec.clone(),labels2));
        
    }
    
    private void changeTable(Knowledge node) {
        for (int i=1;i<=node.max();i++) {
            if ((node.neighbourNode(i))!=null) {
                Integer nodeI=new Integer(i);
                putProperty(nodeI.toString(), ((Vector)node.neighbourNode(i)).toString());
            }
        }
    }
    
    private Vector addVector(int num, Vector vec){
        Vector intVec=new Vector();
        
        intVec.add(new Integer(num));
        if (vec!=null) {
            for (int i=0;i<vec.size();i++)
                intVec.add((Integer)vec.elementAt(i));
            
        }
        return intVec;
        
    }
    
    
    
    public Object clone(){
        return new Mazurkiewicz();
    }
}
