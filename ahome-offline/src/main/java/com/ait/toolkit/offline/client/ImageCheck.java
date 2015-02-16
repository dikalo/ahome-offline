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
 * If you want to run offilne tests on a different domain, try the image method.
 * It loads an image, which are allowed to cross domains.
 * <p>
 * The one cavet is that with the image method, we can't distinguish a 404 from
 * a genuine connection issue, so any error at all will appear to Offline as a
 * connection issue.
 * 
 */
public class ImageCheck extends OfflineCheck {

	private JavaScriptObject image;

	public ImageCheck(String url) {
		jsObj = JsoHelper.createObject();
		image = JsoHelper.createObject();
		JsoHelper.setAttribute(image, "url", url);
		JsoHelper.setAttribute(jsObj, "image", image);
		this.setActive(OfflineState.IMAGE);
	}

}
