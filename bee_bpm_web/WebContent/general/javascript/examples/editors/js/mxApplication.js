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
				
				// dml - creamos una session para el editor NO FUNCIONA BIEN
				var session = editor.connect('/bee_bpm_web/rest/wf/InicialiceSession/69',
						'/bee_bpm_web/rest/wf/Poll','/bee_bpm_web/rest/wf/Notify',
						'/bee_bpm_web/rest/wf/getSpObjectList',onChange());

				// dml 20130709 - como ya puse la llamada en el oncomplete ya no hace falta el timeout, 
				// ya abrirá el editor una vez creado el fichero para leer
				//openProcessXmlMapTmp(editor);
				setTimeout(function(){openProcessXmlMapTmpAndProperties(editor)},1000);
				
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
				
				// Shows the application				
				mxUtils.hideSplash(1000);
			}
		}
		catch (e)
		{
			mxUtils.hideSplash(1000);

			// Shows an error message if the editor cannot start
			mxUtils.alert('Cannot start application: '+e.message);
			throw e; // for debugging
		}

		return editor;
		
	}
	
	// Opens the previously saved xml map
	function openProcessXmlMapTmpAndProperties(editor)
	{
		editor.open("/bee_bpm_web/processXmlMapTmp.xml");
		
		// With this we open the properties of the WProcessDef
		editor.execute('showFixProperties', editor.graph.getSelectionCell());
	};
		
	function onChange()
	{
		console.log("ESTO CAMBIAAA");
	};

}
