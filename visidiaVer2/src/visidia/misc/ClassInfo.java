package visidia.misc;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Vector;

/**
 * This class allows to retrieve the package a compiled Java class belongs to.  
 */
public class ClassInfo
{
	/**
	 * @param aPath the path of the class file
	 * @return the package or null if no package are defined for this class
	 * @throws Exception
	 */
	public static String getPackage(String aPath) throws Exception
	{
		DataInputStream dis = null;
		try
		{
			dis = new DataInputStream(new FileInputStream(aPath));

			// magic
			int magic = dis.readInt();
			if (magic != 0xcafebabe)
			{
				throw new Exception("Not a class file");
			}

			// major minor
			dis.readShort();
			dis.readShort();

			// cp count
			int cpCount = dis.readShort();

			Vector pool = readConstantPool(dis, cpCount);      

			// access flags
			dis.readShort();

			// this class
			int thisClass = dis.readShort();
			thisClass = ((Integer) pool.get(thisClass)).intValue();

			String name = (String) pool.get(thisClass);
			int index = name.lastIndexOf('/');
			if (-1 == index)
			{
				return null;
			}
			else
			{
				return name.substring(0, index);
			}
		}
		finally
		{
			try
			{
				dis.close();
			}
			catch (Exception e)
			{}
		}
	}  

	/**
	 * Read the constant pool of a class file and store the results in a Vector
	 * @param anInput the DataInput to use to read the file
	 * @param aCount the size of the constant pool + 1 as defined by the class format
	 * @return a Vector containing the constant pool element, the first element is null.
	 * @throws Exception
	 */
	private static Vector readConstantPool(DataInput anInput, int aCount) throws Exception
	{
		Vector pool = new Vector(aCount);
		pool.add(null);

		for (int ind = 0; ind < (aCount - 1); ind++)
		{
			char tag = (char) anInput.readByte();
			switch (tag)
			{
			case 1:
			{
				int len = anInput.readShort();
				byte buf[] = new byte[len];
				anInput.readFully(buf);
				pool.add(new String(buf));
				break;
			}
			case 3:
			case 4:
				pool.add(new Integer(anInput.readInt()));
				break;
			case 5:
			case 6:
			{
				int tab[] = new int[2];
				tab[0] = anInput.readInt();
				tab[1] = anInput.readInt();
				pool.add(tab);
				pool.add(null);
				ind++;
				break;
			}
			case 7:
			case 8:
				pool.add(new Integer(anInput.readShort()));
				break;
			case 9:
			case 10:
			case 11:
			case 12:
			{
				int tab[] = new int[2];
				tab[0] = anInput.readShort();
				tab[1] = anInput.readShort();
				pool.add(tab);
				break;
			}
			default:
				throw new Exception("Invalid constant pool entry tag: " + (int) tag);
			}
		}

		return pool;
	}

}