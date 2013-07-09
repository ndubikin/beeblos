/*
 * $Id: mxApplication.js,v 1.1 2012/11/15 13:26:50 gaudenz Exp $
 * Copyright (c) 2006-2010, JGraph Ltd
 *
 * Defines the startup sequence of the application.
 *
 */
{

	/**
	 * Constructs a new application (note that this returns an mxEditor
	 * instance).
	 */
	function mxApplication(config)
	{
		console.log("----------------------------> EMPIEZA <---------------------------------");
		var hideSplash = function()
		{
			// Fades-out the splash screen
			var splash = document.getElementById('splash');
			
			if (splash != null)
			{
				try
				{
					mxEvent.release(splash);
					mxEffects.fadeOut(splash, 1000, false);
				}
				catch (e)
				{
					splash.parentNode.removeChild(splash);
				}
			}
		};
		
		try
		{
			if (!mxClient.isBrowserSupported())
			{
				mxUtils.error('Browser is not supported!', 200, false);
			}
			else
			{
				
				var node = mxUtils.load(config).getDocumentElement();
				var editor = new mxEditor(node);

// ESTO DEBERIA CARGAR COMO VARIABLE PERO NO SE LEE BIEN DESDE EL BEAN EL XMLSTRING CUANDO SE LE PASA
// COMO PARAMETRO EL #{wProcessDefQueryBean.xmlMapTmp} ya que no es pagina de jsf
//				console.log("----------------------------> ANTES DE CARGAR EL NODO XMLSTRING... <---------------------------------");
//				console.log(xmlString);
//				setTimeout(function(){readXmlString(editor,xmlString)},1000);
//				console.log("----------------------------> CARGADO XMLSTRING ... <---------------------------------");
//				console.log(xmlString);

				// dml 20130709 - como ya puse la llamada en el oncomplete ya no hace falta el timeout, 
				// ya abrirá el editor una vez creado el fichero para leer
				//openProcessXmlMapTmp(editor);
				setTimeout(function(){openProcessXmlMapTmp(editor)},1000);
				
				// Updates the window title after opening new files
				var title = document.title;
				var funct = function(sender)
				{
					document.title = title + ' - ' + sender.getTitle();
				};
				
				editor.addListener(mxEvent.OPEN, funct);
				
				// Prints the current root in the window title if the
				// current root of the graph changes (drilling).
				editor.addListener(mxEvent.ROOT, funct);
				funct(editor);
				
				// Displays version in statusbar
				editor.setStatus('mxGraph '+mxClient.VERSION);

/*				
				console.log("----------------------------> ANTES DE CREAR EL GRAFO ... <---------------------------------");

				var graph = new mxGraph(container);

				console.log("----------------------------> GRAFO CREADO... <---------------------------------");
				
				// Load cells and layouts the graph
				graph.getModel().beginUpdate();
				try
				{	
					console.log("----------------------------> ANTES PARSE <---------------------------------");
					// Loads the custom file format (TXT file)
					//parse(graph, 'fileio.txt');
	
					console.log("----------------------------> DESPUES PARSE <---------------------------------");
					// Loads the mxGraph file format (XML file)
					read(graph, '../dmlExample.xml');
										
					console.log("----------------------------> DESPUES READ <---------------------------------");
					// Gets the default parent for inserting new cells. This
					// is normally the first child of the root (ie. layer 0).
					console.log("----------------------------> DEFAULT PARENT <---------------------------------");
					console.log("----------------------------> " + graph.getDefaultParent() + " <---------------------------------");
					var parent = graph.getDefaultParent();
					console.log("----------------------------> END DEFAULT PARENT <---------------------------------");

					var layout = new mxFastOrganicLayout(graph);
					// Executes the layout
					layout.execute(parent);
				}
				finally
				{
					// Updates the display
					graph.getModel().endUpdate();
				}
*/
				
				// Shows the application				
				hideSplash();
			}
		}
		catch (e)
		{
			hideSplash();

			// Shows an error message if the editor cannot start
			mxUtils.alert('Cannot start application: '+e.message);
			throw e; // for debugging
		}
								
		return editor;
	}
	
	// Opens the previously saved xml map
	function openProcessXmlMapTmp(editor)
	{
		editor.open("/bee_bpm_web/processXmlMapTmp.xml");
	};

	// Opens the previously saved xml map
	function readXmlString(editor,xmlString)
	{
		var doc = mxUtils.parseXML(xmlString);
		node = doc.documentElement;
		editor.readGraphModel(node);
	};

}
