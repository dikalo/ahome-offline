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

import com.ait.toolkit.core.client.CSSUtil;
import com.ait.toolkit.core.client.Function;
import com.ait.toolkit.offline.client.resources.OfflineResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.user.client.DOM;

/**
 * Offline class.
 */
public class Offline {

	private static final String VERSION = "0.7.2";
	private static final String OFFLINE_RESSOURCES_FOLDER = GWT.getModuleBaseURL() + "ahomeOffline/";
	private static final String ERROR_MESSAGE = "Offline class used without Offline.js";
	private static final String CSS_LANGUAGE_ID = "ahome-offline-lang";
	private static final String CSS_LANGUAGE_INDICATOR_ID = "ahome-offline-indicator-lang";
	private static final String CSS_THEME_ID = "ahome-offline-theme";
	private static final String CSS_THEME_INDICATOR_ID = "ahome-offline-indicator-theme";
	private static final String OFFLINE_THEMES_FOLDER = OFFLINE_RESSOURCES_FOLDER + VERSION + "/themes/";

	static {
		if (isLoaded()) {
			setTheme(OfflineTheme.DEFAULT);
			setLanguage(OfflineLanguage.ENGLISH);
		} else {
			load();
			setTheme(OfflineTheme.DEFAULT);
			setLanguage(OfflineLanguage.ENGLISH);
		}

		setReconnectDelays(45, 10);
		setCheckOnLoad(true);
	}

	private Offline() {

	}

	/**
	 * Loads the offline library. You normally never have to do this manually
	 */
	public static void load() {
		if (!isLoaded()) {
			OfflineResources resources = GWT.create(OfflineResources.class);
			ScriptInjector.fromString(resources.js().getText()).setWindow(ScriptInjector.TOP_WINDOW).inject();
		}
	}

	/**
	 * Sets the themes of the offline overlay
	 * 
	 * @param theme
	 *            , the theme to use
	 */
	public static void setTheme(OfflineTheme theme) {
		String cssTheme = OFFLINE_THEMES_FOLDER + "offline-theme-" + theme.name().toLowerCase() + ".css";
		// String cssIndicatorTheme = RESOURCE_FOLDER + "offline-theme-"
		// + theme.name().toLowerCase() + "-indicator.css";

		if (isRessourceInject(CSS_THEME_ID)) {
			updateStyle(CSS_THEME_ID, cssTheme);
		} else {
			CSSUtil.injectStyleSheet(cssTheme, CSS_THEME_ID);
		}

		/*
		 * if (isRessourceInject(CSS_THEME_INDICATOR_ID)) { updateStyle(CSS_THEME_INDICATOR_ID, cssIndicatorTheme); } else { CssUtil.injectStyleSheet(cssIndicatorTheme,
		 * CSS_THEME_INDICATOR_ID); }
		 */

	}

	/**
	 * Sets the language of the offline overlay
	 * 
	 * @param theme
	 *            , the language to use
	 */
	public static void setLanguage(OfflineLanguage language) {
		String offlineLang = OFFLINE_THEMES_FOLDER + "offline-language-" + language.name().toLowerCase() + ".css";
		if (isRessourceInject(CSS_LANGUAGE_ID)) {
			updateStyle(CSS_THEME_ID, offlineLang);
		} else {
			CSSUtil.injectStyleSheet(offlineLang, CSS_LANGUAGE_ID);
		}

	}

	/** Should we check the connection status immediately on page load. */
	public static native void setCheckOnLoad(boolean value)/*-{
		$wnd.Offline.options.checkOnLoad = value;
	}-*/;

	/** Should we monitor AJAX requests to help decide if we have a connection. */
	public static native void setInterceptsRequests(boolean value)/*-{
		$wnd.Offline.options.interceptRequests = value;
	}-*/;

	/**
	 * Should we store and attempt to remake requests which fail while the connection is down.
	 */
	public static native void setRequests(boolean value)/*-{
		$wnd.Offline.options.requests = value;
	}-*/;

	/**
	 * Should we automatically retest periodically when the connection is down (set to false to disable).
	 */
	public static native void setReconnect(boolean value)/*-{
		$wnd.Offline.options.reconnect = value;
	}-*/;

	/**
	 * Should we automatically retest periodically when the connection is down.
	 * 
	 * @param initial
	 *            , How many seconds should we wait before rechecking.
	 * @param retryIn
	 *            , How long should we wait between retries.
	 */
	public static native void setReconnectDelays(double initial, double retryIn)/*-{
		$wnd.Offline.options.reconnect = {
			initialDelay : initial,
			delay : retryIn
		};
	}-*/;

	/**
	 * Check the current status of the connection.
	 */
	public static native void check()/*-{
		$wnd.Offline.check();
	}-*/;

	/**
	 * Check the current status of the connection.
	 */
	public static native String getState()/*-{
		return $wnd.Offline.state;
	}-*/;

	/**
	 * Set how the offline status should be checked
	 * 
	 * @param check
	 *            , the check strategy method to be used
	 */
	public static native void setChecks(OfflineCheck check)/*-{
		var peer = check.@com.ait.toolkit.core.client.JsObject::getJsObj()();
		$wnd.Offline.options.checks = peer;
	}-*/;

	/**
	 * Removes Offline resources from the DOM.
	 */
	public static void removeRessources() {
		if (isRessourceInject(CSS_LANGUAGE_ID)) {
			DOM.getElementById(CSS_LANGUAGE_ID).removeFromParent();
		}
		if (isRessourceInject(CSS_THEME_ID)) {
			DOM.getElementById(CSS_THEME_ID).removeFromParent();
		}
		if (isRessourceInject(CSS_THEME_INDICATOR_ID)) {
			DOM.getElementById(CSS_THEME_INDICATOR_ID).removeFromParent();
		}

	}

	/**
	 * The connection has gone from down to up
	 * 
	 * @param handler
	 *            , the handler that will handler this event
	 */
	public static EventRegistration addUpHandler(Function handler) {
		return addHandler("up", handler);
	}

	/**
	 * The connection has gone from up to down
	 * 
	 * @param handler
	 *            , the handler that will handler this event
	 */
	public static EventRegistration addDownHandler(Function handler) {
		return addHandler("down", handler);
	}

	/**
	 * A connection test has succeeded, fired even if the connection was already up
	 * 
	 * @param handler
	 *            , the handler that will handler this event
	 */
	public static EventRegistration addConfirmedUpHandler(Function handler) {
		return addHandler("confirmed-up", handler);
	}

	/**
	 * A connection test has failed, fired even if the connection was already down
	 * 
	 * @param handler
	 *            , the handler that will handler this event
	 */
	public static EventRegistration addConfirmedDownHandler(Function handler) {
		return addHandler("confirmed-down", handler);
	}

	/**
	 * Convenient method to set the offline status to up
	 */
	public static native void up()/*-{
		if (!$wnd.Offline.options.checks) {
			$wnd.Offline.options.checks = new $wnd.Object();
		}
		$wnd.Offline.options.checks.active = 'up';
	}-*/;

	/**
	 * Convenient method to set the offline status to down
	 */
	public static native void down()/*-{
		if (!$wnd.Offline.options.checks) {
			$wnd.Offline.options.checks = new $wnd.Object();
		}
		$wnd.Offline.options.checks.active = 'down';
	}-*/;

	/**
	 * We are testing the connection
	 * 
	 * @param handler
	 *            , the handler that will handler this event
	 */
	public static EventRegistration addCheckingHandler(Function handler) {
		return addHandler("checking", handler);
	}

	/**
	 * We are beginning the reconnect process
	 * 
	 * @param handler
	 *            , the handler that will handler this event
	 */
	public static EventRegistration addReconnectStartedHandler(Function handler) {
		return addHandler("reconnect-started", handler);
	}

	/**
	 * We are done attempting to reconnect
	 * 
	 * @param handler
	 *            , the handler that will handler this event
	 */
	public static EventRegistration addReconnectStopHandler(Function handler) {
		return addHandler("reconnect-stop", handler);
	}

	/**
	 * Fired every second during a reconnect attempt, when a check is not happening
	 * 
	 * @param handler
	 *            , the handler that will handler this event
	 */
	public static EventRegistration addReconnectTickler(Function handler) {
		return addHandler("reconnect-tick", handler);
	}

	/**
	 * We are reconnecting now
	 * 
	 * @param handler
	 *            , the handler that will handler this event
	 */
	public static EventRegistration addReconnectingHandler(Function handler) {
		return addHandler("reconnect-reconnecting", handler);
	}

	/**
	 * Return the underlying Offline.js version
	 * 
	 * @return
	 */
	public static String getVersion() {
		return VERSION;
	}

	/**
	 * A reconnect check attempt failed
	 * 
	 * @param handler
	 *            , the handler that will handler this event
	 */
	public static EventRegistration addReconnectFailureHandler(Function handler) {
		return addHandler("reconnect-failure", handler);
	}

	/**
	 * Any pending requests have been remade
	 * 
	 * @param handler
	 *            , the handler that will handler this event
	 */
	public static EventRegistration addRequestFlushHandler(Function handler) {
		return addHandler("request-flush", handler);
	}

	/**
	 * A new request is being held
	 * 
	 * @param handler
	 *            , the handler that will handler this event
	 */
	public static EventRegistration addRequestHoldHandler(Function handler) {
		return addHandler("request-hold", handler);
	}

	private static native EventRegistration addHandler(String event, Function handler)/*-{
		var fn = function() {
			handler.@com.ait.toolkit.core.client.Function::execute()();
		};
		$wnd.Offline.on(event, fn);
		return @com.ait.toolkit.offline.client.EventRegistration::new(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(event,fn);
	}-*/;

	public static native boolean isLoaded()/*-{
		if (typeof $wnd.Offline === "undefined" || $wnd.Offline === null) {
			return false;
		}
		return true;
	}-*/;

	private static boolean isRessourceInject(String resourceId) {
		return DOM.getElementById(resourceId) != null;
	}

	private static void updateStyle(String targetId, String newStyleSheet) {
		DOM.getElementById(targetId).setPropertyString("href", newStyleSheet);
	}

}
