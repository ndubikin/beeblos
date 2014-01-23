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
	function mxApplication(config, requestContextPath, currentProcessId, xmlMap)
	{
		
		try
		{
			if (!mxClient.isBrowserSupported())
			{
				mxUtils.error('Browser is not supported!', 200, false);
			}
			else
			{
				
				// dml 20140123
				var initWS = requestContextPath + '/rest/wf/InicialiceSession/' + currentProcessId;
				var pollWS = requestContextPath + '/rest/wf/Poll';
				var notifyWS = requestContextPath + '/rest/wf/Notify';
				var notifyWithResponseWS = requestContextPath + '/rest/wf/NotifyWithResponse';
				var getSPObjectListWS = requestContextPath + '/rest/wf/getSpObjectList';
				
				var node = mxUtils.load(config).getDocumentElement();
				var editor = new mxEditor(node);
				
				// dml - creamos una session para el editor
				// dml 20140123 - NOTA: el initWS TODAVIA NO FUNCIONA BIEN
				var session = editor.connect(initWS, pollWS, notifyWS, notifyWithResponseWS, getSPObjectListWS);

				// dml 20130709 - como ya puse la llamada en el oncomplete ya no hace falta el timeout, 
				// ya abrirá el editor una vez creado el fichero para leer
				loadXMLMapEditor(editor, xmlMap);
				//setTimeout(function(){loadXMLMapEditor(editor, xmlMap)},1700);
				
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
				mxUtils.hideSplash(0);
			}
		}
		catch (e)
		{
			mxUtils.hideSplash(2300);

			// Shows an error message if the editor cannot start
			mxUtils.alert('Cannot start application: '+e.message);
			throw e; // for debugging
		}

		return editor;
		
	}
	
	// Opens the previously saved xml map
	function loadXMLMapEditor(editor, xmlMap)
	{
		var doc = mxUtils.parseXml(xmlMap);
		var node = doc.documentElement;
		editor.readGraphModel(node);
		
		// With this we open the properties of the WProcessDef
		editor.execute('showFixProperties', editor.graph.getSelectionCell());
	};
		

	/**
	 * Constructs a new show application (note that this returns an mxEditor
	 * instance).
	 */
	function mxViewXmlMapApplication(config, xmlMap)
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
				
				// dml 20130709 - como ya puse la llamada en el oncomplete ya no hace falta el timeout, 
				// ya abrirá el editor una vez creado el fichero para leer
				loadAndShowXMLMapEditor(editor, xmlMap);
				//setTimeout(function(){loadAndShowXMLMapEditor(editor, xmlMap)},1700);
								

				// Shows the application				
				mxUtils.hideSplash(0);
			}
		}
		catch (e)
		{
			mxUtils.hideSplash(2300);

			// Shows an error message if the editor cannot start
			mxUtils.alert('Cannot start application: '+e.message);
			throw e; // for debugging
		}

		return editor;
		
	}
	
	// Opens the previously saved xml map
	function loadAndShowXMLMapEditor(editor, xmlMap)
	{
		var doc = mxUtils.parseXml(xmlMap);
		var node = doc.documentElement;
		editor.readGraphModel(node);
		showMap(editor.graph, "Workflow show");
	};

	// Show the xml map
	function showMap(graph, mapTitle)
	{
		var x0 = 50;
		var y0 = 50;
		
		var doc = window.document;
		
		var bounds = graph.getGraphBounds();
		var dx = -bounds.x + x0;
		var dy = -bounds.y + y0;

		// Needs a special way of creating the page so that no click is required
		// to refresh the contents after the external CSS styles have been loaded.
		// To avoid a click or programmatic refresh, the styleSheets[].cssText
		// property is copied over from the original document.
		if (mxClient.IS_IE)
		{
			var html = '<html>';
			html += '<head>';

			var base = document.getElementsByTagName('base');
			
			for (var i = 0; i < base.length; i++)
			{
				html += base[i].outerHTML;
			}

			html += '<style>';

			// Copies the stylesheets without having to load them again
			for (var i = 0; i < document.styleSheets.length; i++)
			{
				try
				{
					html += document.styleSheets(i).cssText;
				}
				catch (e)
				{
					// ignore security exception
				}
			}

			html += '</style>';

			html += '</head>';
			html += '<body>';
			
			// Copies the contents of the graph container
			html += graph.container.innerHTML;
			
			html += '</body>';
			html += '<html>';

			doc.writeln(html);
			doc.close();

			// Makes sure the inner container is on the top, left
		    var node = doc.body.getElementsByTagName('DIV')[0];
		    
		    if (node != null)
		    {
			    node.style.position = 'absolute';
			    node.style.left = dx + 'px';
			    node.style.top = dy + 'px';
		    }
		}
		else
		{
			doc.writeln('<html');
			doc.writeln('<head>');
			
			var base = document.getElementsByTagName('base');
			
			for (var i=0; i<base.length; i++)
			{
				doc.writeln(mxUtils.getOuterHtml(base[i]));
			}
			
			var links = document.getElementsByTagName('link');
			
			for (var i=0; i<links.length; i++)
			{
				doc.writeln(mxUtils.getOuterHtml(links[i]));
			}
	
			var styles = document.getElementsByTagName('style');
			
			for (var i=0; i<styles.length; i++)
			{
				doc.writeln(mxUtils.getOuterHtml(styles[i]));
			}

			doc.writeln('</head>');
			doc.writeln('</html>');
			doc.close();
			
			// Workaround for FF2 which has no body element in a document where
			// the body has been added using document.write.
			if (doc.body == null)
			{
				doc.documentElement.appendChild(doc.createElement('body'));
			}
			
			// Workaround for missing scrollbars in FF
			doc.body.style.overflow = 'auto';
			
			var node = graph.container.firstChild;
			
			while (node != null)
			{
				var clone = node.cloneNode(true);
				doc.body.appendChild(clone);
				node = node.nextSibling;
			}

			// Shifts negative coordinates into visible space
			var node = doc.getElementsByTagName('g')[0];

			if (node != null)
			{
				node.setAttribute('transform', 'translate(' + dx + ',' + dy + ')');
		    	
		    	// Updates the size of the SVG container
		    	var root = node.ownerSVGElement;
				root.setAttribute('width', bounds.width + Math.max(bounds.x, 0) + 3);
				root.setAttribute('height', bounds.height + Math.max(bounds.y, 0) + 3);
			}
		}
	
		mxUtils.removeCursors(doc.body);
	
		doc.title = mapTitle;

		return doc;
	}
	
	function onChange()
	{
		console.log("Cambio.");
	};

}