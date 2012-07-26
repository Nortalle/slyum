
package dataRecord.elements;

import java.util.LinkedList;
import java.util.List;

import dataRecord.Keyword;
import dataRecord.elementType.APIclass;
import dataRecord.elementType.APIinterface;
import dataRecord.elementType.ClassKind;
import dataRecord.elementType.InterfaceKind;
import dataRecord.io.ElementVisitor;

/**
 * This class represent a class in an object-oriented language
 * 
 * visit http://docs.oracle.com/javase/tutorial/java/concepts/class.html
 * for a good explaination of "Class"
 * 
 * A class can inherit from an other class and can implement from many other classes
 * 
 * @author Fabrizio Beretta Piccoli
 * @version 2.0 | 2-lug-2012
 * @see Type
 * @see ClassKind
 */
public class ClassType extends Type implements ClassKind
{
	private boolean isFinal = false;
	private boolean isAbstract = false;
	private List<ClassKind> extendList = new LinkedList<ClassKind>();

	/**
	 * creates a class with a name and a access modifier
	 * 
	 * @param name
	 * @param access
	 */
	public ClassType(String name, Keyword access)
	{
		super(name, access, Keyword.CLASS);
	}
	
	/**
	 * creates a class with a name and a access modifier
	 * an id can be set 
	 * 
	 * @param name
	 * @param access
	 * @param id
	 */
	public ClassType(String name, Keyword access, int id)
	{
		super(name, access, Keyword.CLASS, id);
	}
	
	/**
	 * creates a clone of the class given in argument
	 * 
	 * @param c	the class to clone
	 */
	public ClassType(ClassType c)
	{
		super(c.getName(), c.getAccess(), Keyword.CLASS);
		extendList = c.getExtendList();
		implList = c.getImplList();
		setStatic(c.isStatic());
		setAbstract(c.isAbstract);
		setFinal(c.isFinal);
		setGeneric(c.getGeneric());
		setElements(c.getElements());
		setID(c.getID());
	}

	/**
	 * this method will be called by the writer to know
	 * how to write this object.
	 * 
	 * @see ElementVisitor
	 * 
	 */
	@Override
	public String accept(ElementVisitor v)
	{
		return v.visit(this);
	}

	public boolean isFinal()
	{
		return isFinal;
	}

	public void setFinal(boolean isFinal)
	{
		this.isFinal = isFinal;
	}

	public List<ClassKind> getExtendList()
	{
		return extendList;
	}

	public void setExtendList(List<ClassKind> extendList)
	{
		this.extendList = extendList;
	}
	
	public boolean isAbstract()
	{
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract)
	{
		this.isAbstract = isAbstract;
	}

	@Override
	public String getElementType()
	{
		return getName();
	}
	
	
	/**
	 * for debug only
	 * 
	 * give a String representation of the object
	 */
	public String toString()
	{
		String tmp = getAccess().toString();

		tmp += " ";
		if (isFinal)
			tmp += "final ";
		if (isAbstract())
			tmp += "abstract ";
		if (isStatic())
			tmp += "static ";
		tmp += "class " + getName() + " ";
		if (!extendList.isEmpty())
		{
			tmp += "extends ";
			for (ClassKind ex : extendList)
			{
				if (ex.getClass() == APIclass.class)
					tmp += ((APIclass)ex).getElementType();
				else
					tmp += ((ClassType) ex).getName();
				if (!ex.equals(extendList.get(extendList.size() - 1)))
					tmp += ", ";
			}
			tmp += " ";
		}
		if (!implList.isEmpty())
		{
			tmp += "implements ";
			for (InterfaceKind ex : implList)
			{
				if (ex.getClass() == APIinterface.class)
					tmp += ((APIinterface)ex).getElementType();
				else
					tmp += ((InterfaceType) ex).getName();
				if (!ex.equals(implList.get(implList.size() - 1)))
					tmp += ", ";
			}
		}
		tmp += "\n";
		for (int i = 0; i < getDepth(); i++)
		{
			tmp += "\t";
		}
		tmp += "{";
		setDepth(getDepth() + 1);
		for (Element e : getElements())
		{
			tmp += "\n";
			for (int i = 0; i < getDepth(); i++)
			{
				tmp += "\t";
			}
			tmp += e.toString();
		}
		setDepth(getDepth() - 1);
		tmp += "\n";
		for (int i = 0; i < getDepth(); i++)
		{
			tmp += "\t";
		}
		tmp += "}";

		return tmp;
	}

}