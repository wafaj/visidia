package visidia.examples.algo.clone_solutions;

public class NullVectorExceotion extends Exception{  
	/**
	 * 
	 */
	private static final long serialVersionUID = 4864639999916626730L;
	/** 
	* Crée une nouvelle instance de NombreNonValideException 
	*/  
	public NullVectorExceotion() {}  
	/** 
	* Crée une nouvelle instance de NombreNonValideException 
	* @param message Le message détaillant exception 
	*/  
	public NullVectorExceotion(String message) {  
		super(message); 
	}  
	/** 
	* Crée une nouvelle instance de NombreNonValideException 
	* @param cause L'exception à l'origine de cette exception 
	*/  
	public NullVectorExceotion(Throwable cause) {  
		super(cause); 
	}  
	/** 
	* Crée une nouvelle instance de NombreNonValideException 
	* @param message Le message détaillant exception 
	* @param cause L'exception à l'origine de cette exception 
	*/  
	public NullVectorExceotion(String message, Throwable cause) {  
		super(message, cause); 
	} 
}