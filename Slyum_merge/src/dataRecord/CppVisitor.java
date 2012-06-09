package dataRecord;

public class CppVisitor implements ElementVisitor
{	
	private Keyword currentAccess = Keyword.PACKAGE; 
	
	public CppVisitor()
	{
	}
	
	@Override
	public String visit(PackageStmt ps)
	{
		return "namespace "+ ps.getPackageName();
	}

	@Override
	public String visit(ImportStmt importStmt)
	{
		return "#import <"+importStmt.getPackageName()+">";
	}

	@Override
	public String visit(Comment comment)
	{
		return comment.toString();
	}

	@Override
	public String visit(Type header)
	{
		return null;
	}

	@Override
	public String visit(EnumType enumType)
	{
		return enumType.toString();
	}

	@Override
	// actually there is no interface on C++
	public String visit(InterfaceType it)
	{
		String tmp = "class ";
		if (!it.getImplList().isEmpty())
		{
			tmp += " : ";
			for (Implementable ex : it.getImplList())
			{
				if (ex.getClass() == APIinterface.class)
					tmp += ((APIinterface)ex).getElementType();
				else
					tmp += ex.getClass().getSimpleName();
				if (!ex.equals(it.getImplList().get(it.getImplList().size() - 1)))
					tmp += ", ";
			}
		}
		tmp += "\n";
		for (int i = 0; i < Declaration.depth; i++)
		{
			tmp += "\t";
		}
		tmp += "{";
		Declaration.depth++;
		for (Element e : it.getElements())
		{
			tmp += "\n";
			for (int i = 0; i < Declaration.depth; i++)
			{
				tmp += "\t";
			}
			tmp += e.accept(this);
		}
		Declaration.depth--;
		tmp += "\n";
		for (int i = 0; i < Declaration.depth; i++)
		{
			tmp += "\t";
		}

		tmp += "}";

		return tmp;
	}

	@Override
	public String visit(Constructor constructorHeader)
	{
		return null;
	}

	@Override
	public String visit(Method method)
	{
		String s = "";
		
		if (!method.getAccess().equals(currentAccess))
		{
			currentAccess = method.getAccess();
			s += method.getAccess() + " : \n\t";
		}
		s += "\t";

		if (method.isStatic())
			s += "static ";
		if (method.isAbstract())
			s += "virtual ";
		s += method.getReturnType();
		s += " ";
		s += method.getName();
		s += method.ParamToString();
		//s += throwClausesToString();
		if (method.isFinal())
			s += "const ";

		if (method.isAbstract())
			s+= method.methodBody;
		else
		{	
			String tabs = "";
			for (int i = 0; i < Declaration.depth; i++)
			{
				tabs += "\t";
			}
			s += "\n\t"+ tabs + method.methodBody;
		}

		return s;
	}

	@Override
	public String visit(CompilationUnit compilationUnit)
	{
		String tmp = "";

		for (Element e : compilationUnit.getElements())
		{
			tmp += e.accept(this);
		}

		return tmp;
	}

	@Override
	public String visit(Attribute attr)
	{
		String tmp = "";
		
		if (!attr.getAccess().equals(currentAccess))
		{
			currentAccess = attr.getAccess();
			tmp += attr.getAccess() + " : \n\t";
		}
		tmp += "\t";
		
		if (attr.isStatic())
			tmp += "static ";
		tmp += attr.type.getElementType();
		if (attr.isFinal())
			tmp += "const ";
		tmp += " ";
		tmp += attr.name;

		if (!attr.getValue().isEmpty())
			tmp += " =" + attr.getValue();

		return tmp;
	}

	@Override
	public String visit(Parametre parametre)
	{
		String tmp = "";

		if (parametre.isFinal())
			tmp += "const";
		
		tmp += parametre.type.getElementType();
		tmp += " ";
		tmp += parametre.name;
		
		if (!parametre.getValue().isEmpty())
			tmp += " =" + parametre.getValue();

		return tmp;
	}

	@Override
	public String visit(InterfaceField interfaceField)
	{
		return null;
	}
	
	@Override
	public String visit(ClassType ct)
	{
		
		String tmp ="";

		tmp += " ";
		if (ct.isAbstract())
			tmp += "virtual ";
		if (ct.isStatic())
			tmp += "static ";
		tmp += "class " + ct.getName() + " ";
		if (!ct.getExtendList().isEmpty() || !ct.getImplList().isEmpty())
		{
			tmp += ": ";
			for (Extendable ex : ct.getExtendList())
			{
				if (ex.getClass() == APIclass.class)
				{
					tmp += "public ";
					tmp += ((APIclass)ex).getElementType();
				}
				else
				{
					tmp += ((ClassType) ex).getAccess() + " ";
					tmp += ((ClassType) ex).getName();
				}
				if (!ex.equals(ct.getExtendList().get(ct.getExtendList().size() - 1)) || !ct.getImplList().isEmpty())
					tmp += ", ";
			}
			for (Implementable ex : ct.getImplList())
			{
				if (ex.getClass() == APIinterface.class)
					tmp += ((APIinterface)ex).getElementType();
				else
					tmp += ((InterfaceType) ex).getName();
				if (!ex.equals(ct.getImplList().get(ct.getImplList().size() - 1)))
					tmp += ", ";
			}
			
			tmp += " ";
		}
		tmp += "\n";
		for (int i = 0; i < Declaration.depth; i++)
		{
			tmp += "\t";
		}
		tmp += "{";
		Declaration.depth++;
		for (Element e : ct.getElements())
		{
			tmp += "\n";
			for (int i = 0; i < Declaration.depth; i++)
			{
				tmp += "\t";
			}
			tmp += e.accept(this);
		}
		Declaration.depth--;
		tmp += "\n";
		for (int i = 0; i < Declaration.depth; i++)
		{
			tmp += "\t";
		}
		tmp += "}";
		

		currentAccess = Keyword.PACKAGE;
		return tmp;
	}

}
