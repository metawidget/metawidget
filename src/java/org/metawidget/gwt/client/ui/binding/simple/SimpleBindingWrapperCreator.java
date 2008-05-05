package org.metawidget.gwt.client.ui.binding.simple;

import java.io.PrintWriter;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class SimpleBindingWrapperCreator
{
	private TreeLogger			mLogger;

	private GeneratorContext	mContext;

	private TypeOracle			mTypeOracle;

	private String				mTypeName;

	public SimpleBindingWrapperCreator( TreeLogger logger, GeneratorContext context, String typeName )
	{
		mLogger = logger;
		mContext = context;
		mTypeOracle = context.getTypeOracle();
		mTypeName = typeName;
	}

	/**
	 * Crée le code du wrapper correspondant à l'objet demandé. Puis inscrit la classe wrapper dans
	 * le treeLogger.
	 *
	 * @return nom complet du wrapper
	 */
	public String createWrapper()
	{
		try
		{
			JClassType classType = mTypeOracle.getType( mTypeName );
			SourceWriter source = getSourceWriter( classType );
			// Si le wrapper existe déjà, getSourceWriter renvoie null
			// Il n'est donc pas nécessaire de créer cette classe
			if ( source == null )
				return classType.getParameterizedQualifiedSourceName() + "Wrapper";

			JMethod[] methods = classType.getMethods();
			String simpleName = classType.getSimpleSourceName();
			source.indent();
			source.println( "private " + simpleName + " contenu;" );
			source.println();
			source.println( "public void setContent(BusinessObject contenu) {" );
			source.indent();
			source.println( "contenu = (" + simpleName + ") contenu;" );
			source.outdent();
			source.println( "}" );
			source.println();
			source.println( "public BusinessObject getContent() {" );
			source.indent();
			source.println( "return contenu;" );
			source.outdent();
			source.println( "}" );
			source.println();
			// create links with content object getters and setters
			for ( int i = 0; i < methods.length; i++ )
			{
				JMethod methode = methods[i];
				source.println( methode.getReadableDeclaration() + " {" );
				source.indent();
				if ( methode.getName().startsWith( "set" ) )
				{ // setter
					JParameter parameter = methode.getParameters()[0];
					source.println( "contenu." + methode.getName() + "(" + parameter.getName() + ");" );
				}
				else
				{ // getter
					source.println( "return contenu." + methode.getName() + "();" );
				}
				source.outdent();
				source.println( "}" );
				source.println();
			}
			// create the getAttribute method
			source.println( "public String getAttribute(String attr) {" );
			source.indent();

			for ( int i = 0; i < methods.length; i++ )
			{
				String methodName = methods[i].getName();
				JParameter[] methodParameters = methods[i].getParameters();
				JType returnType = methods[i].getReturnType();
				if ( methodName.startsWith( "get" ) & methodParameters.length == 0 )
				{
					source.println( "if (attr.equals(\"" + methodName.substring( 3 ).toLowerCase() + "\")) {" );
					source.indent();
					source.println( "return " + castToString( returnType, "" + methodName + "()" ) + ";" );
					source.outdent();
					source.print( "} else " );
				}
			}
			source.println( "{" );
			source.indent();
			source.println( "return null;" );
			source.outdent();
			source.println( "}" );
			source.outdent();
			source.println( "}" );
			source.println();
			// create the set attribute method
			source.println( "public void setAttribute(String attr, String value) {" );
			source.indent();
			for ( int i = 0; i < methods.length; i++ )
			{
				JMethod methode = methods[i];
				if ( methode.getName().startsWith( "set" ) & methode.getParameters().length == 1 )
				{
					JType paramType = methode.getParameters()[0].getType();
					source.println( "if (attr.equals(\"" + methode.getName().substring( 3 ).toLowerCase() + "\")) { " );
					source.indent();
					source.println( "" + methode.getName() + "(" + castFromString( paramType, "value" ) + ");" );
					source.outdent();
					source.print( "} else " );
				}
			}
			source.println( "{" );
			source.println( "}" );
			source.outdent();
			source.println( "}" );
			source.commit( mLogger );
			return classType.getParameterizedQualifiedSourceName() + "Wrapper";
		}
		catch ( NotFoundException e )
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * SourceWriter instantiation. Return null if the resource already exist.
	 *
	 * @return sourceWriter
	 */
	public SourceWriter getSourceWriter( JClassType classType )
	{
		String packageName = classType.getPackage().getName();
		String simpleName = classType.getSimpleSourceName() + "Wrapper";
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory( packageName, simpleName );
		composer.setSuperclass( classType.getName() );
		composer.addImplementedInterface( "com.zenika.tutorial.gwt.client.Wrapper" );
		composer.addImport( "com.zenika.tutorial.gwt.client.BusinessObject" );
		PrintWriter printWriter = mContext.tryCreate( mLogger, packageName, simpleName );
		if ( printWriter == null )
			return null;

		SourceWriter sw = composer.createSourceWriter( mContext, printWriter );
		return sw;
	}

	public String castToString( JType type, String value )
	{
		if ( type.getSimpleSourceName().equals( "String" ) )
		{
			return value;
		}
		else if ( type.getSimpleSourceName().equals( "int" ) )
		{
			return "Integer.toString(" + value + ")";
		}
		else if ( type.getSimpleSourceName().equals( "byte" ) )
		{
			return "Byte.toString(" + value + ")";
		}
		else if ( type.getSimpleSourceName().equals( "short" ) )
		{
			return "Short.toString(" + value + ")";
		}
		else if ( type.getSimpleSourceName().equals( "long" ) )
		{
			return "Long.toString(" + value + ")";
		}
		else if ( type.getSimpleSourceName().equals( "float" ) )
		{
			return "Float.toString(" + value + ")";
		}
		else if ( type.getSimpleSourceName().equals( "double" ) )
		{
			return "Double.toString(" + value + ")";
		}
		else if ( type.getSimpleSourceName().equals( "boolean" ) )
		{
			return "Boolean.toString(" + value + ")";
		}
		else if ( type.getSimpleSourceName().equals( "char" ) )
		{
			return "Character.toString(" + value + ")";
		}
		else
		{
			return "type not considered for the moment";
		}
	}

	public String castFromString( JType type, String value )
	{
		if ( type.getSimpleSourceName().equals( "String" ) )
		{
			return value;
		}
		else if ( type.getSimpleSourceName().equals( "int" ) )
		{
			return "Integer.parseInt(" + value + ")";
		}
		else if ( type.getSimpleSourceName().equals( "byte" ) )
		{
			return "Byte.parseByte(" + value + ")";
		}
		else if ( type.getSimpleSourceName().equals( "short" ) )
		{
			return "Short.parseShort(" + value + ")";
		}
		else if ( type.getSimpleSourceName().equals( "long" ) )
		{
			return "Long.parseLong(" + value + ")";
		}
		else if ( type.getSimpleSourceName().equals( "float" ) )
		{
			return "Float.parseFloat(" + value + ")";
		}
		else if ( type.getSimpleSourceName().equals( "double" ) )
		{
			return "Double.parseDouble(" + value + ")";
		}
		else if ( type.getSimpleSourceName().equals( "boolean" ) )
		{
			return "Boolean.parseBoolean(" + value + ")";
		}
		else if ( type.getSimpleSourceName().equals( "char" ) )
		{
			return value + ".charAt(0)";
		}
		else
		{
			return null;
		}
	}
}
