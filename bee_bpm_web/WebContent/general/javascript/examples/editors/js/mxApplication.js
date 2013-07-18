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
				
				// dml - creamos una session para el editor NO FUNCIONA BIEN
				var session = editor.connect('/bee_bpm_web/rest/wf/InicialiceSession/69',
						'/bee_bpm_web/rest/wf/Poll','/bee_bpm_web/rest/wf/Notify',onChange());

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
		
		// With this we open the properties of the WProcessDef
		editor.execute('showFixProperties', editor.graph.getSelectionCell());
	};
		
	function onChange()
	{
		console.log("ESTO CAMBIAAA");
	};

}
