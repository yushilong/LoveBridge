
/**
 * Copyright 2013 Mani Selvaraj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lovebridge.library.volley.ex;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.lovebridge.library.volley.AuthFailureError;
import com.lovebridge.library.volley.Response.ErrorListener;
import com.lovebridge.library.volley.Response.Listener;
import com.lovebridge.library.volley.toolbox.JsonObjectRequest;

/**
 * Custom Implementation of com.android.volley.toolbox.JsonObjectRequest 
 * to handle the headers.
 * @author Mani Selvaraj
 *
 */
public class YARJsonRequest extends JsonObjectRequest{

	private Map<String, String> headers = new HashMap<String, String>();
	private Priority priority = null;
	
	public YARJsonRequest(int method, String url, JSONObject jsonRequest,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
		// TODO Auto-generated constructor stub
	}

    public YARJsonRequest(String url, JSONObject jsonRequest, Listener<JSONObject> listener,
            ErrorListener errorListener) {
    	super(url,jsonRequest,listener,errorListener);
    }
    
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return headers;
	}
	
	public void setHeader(String title, String content) {
		 headers.put(title, content);
	}
	
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	/*
	 * If prioirty set use it,else returned NORMAL
	 * @see com.android.volley.Request#getPriority()
	 */
    public Priority getPriority() {
    	if( this.priority != null) {
    		return priority;
    	} else {
    		return Priority.NORMAL;	
    	}
    }
    
    public void setHeaders()
    {
        //        heads.put(key, value);
    }
}
