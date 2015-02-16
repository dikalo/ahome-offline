/*
 Copyright (c) 2014 Ahomé Innovation Technologies. All rights reserved.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.ait.toolkit.offline.client;

import com.ait.toolkit.core.client.JsoHelper;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * By default, Offline makes an XHR request to load your /favicon.ico to check
 * the connection. If you don't have such a file, it will 404 in the console,
 * but otherwise work fine (even a 404 means the connection is up). You can
 * change the URL it hits (an endpoint which will respond with a quick 204 is
 * perfect)
 * <p>
 * 
 * 
 * Make sure that the URL you check has the same origin as your page (the
 * connection method, domain and port all must be the same), or you will run
 * into CORS issues. You can add Access-Control headers to the endpoint to fix
 * it on modern browsers, but it will still cause issues on IE9 and below.
 * 
 * If you do want to run tests on a different domain, try the {@link ImageCheck}
 * method. It loads an image, which are allowed to cross domains.
 * <p>
 * The one cavet is that with the image method, we can't distinguish a 404 from
 * a genuine connection issue, so any error at all will appear to Offline as a
 * connection issue.
 * 
 */
public class XhrCheck extends OfflineCheck {

	private JavaScriptObject xhr;

	public XhrCheck(String url) {
		jsObj = JsoHelper.createObject();
		xhr = JsoHelper.createObject();
		JsoHelper.setAttribute(xhr, "url", url);
		JsoHelper.setAttribute(jsObj, "xhr", xhr);
	}

}
