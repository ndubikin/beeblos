// Public global variables
var MAX_REQUEST_SIZE = 10485760;
var MAX_AREA = 10000 * 10000;

// URLs for save and export
var EXPORT_URL = '/export';
var SAVE_URL = '/bee_bpm_web/rest/wf/save';
var OPEN_URL = '/open';
var RESOURCES_PATH = 'resources';
var RESOURCE_BASE = RESOURCES_PATH + '/grapheditor';
var STENCIL_PATH = 'stencils';
var IMAGE_PATH = 'images';
var STYLE_PATH = 'styles';
var CSS_PATH = 'styles';
var OPEN_FORM = 'open.html';

// Specifies connection mode for touch devices (at least one should be true)
var tapAndHoldStartsConnection = true;
var showConnectorImg = true;

// Parses URL parameters. Supported parameters are:
// - lang=xy: Specifies the language of the user interface.
// - touch=1: Enables a touch-style user interface.
// - storage=local: Enables HTML5 local storage.
var urlParams = (function(url)
{
	var result = new Object();
	var idx = url.lastIndexOf('?');

	if (idx > 0)
	{
		var params = url.substring(idx + 1).split('&');
		
		for (var i = 0; i < params.length; i++)
		{
			idx = params[i].indexOf('=');
			
			if (idx > 0)
			{
				result[params[i].substring(0, idx)] = params[i].substring(idx + 1);
			}
		}
	}
	
	return result;
})(window.location.href);

// Sets the base path, the UI language via URL param and configures the
// supported languages to avoid 404s. The loading of all core language
// resources is disabled as all required resources are in grapheditor.
// properties. Note that in this example the loading of two resource
// files (the special bundle and the default bundle) is disabled to
// save a GET request. This requires that all resources be present in
// each properties file since only one file is loaded.
mxLoadResources = false;
mxBasePath = '../../../src';
mxLanguage = urlParams['lang'];
mxLanguages = ['de'];
