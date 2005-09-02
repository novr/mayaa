/*
 * Copyright (c) 2004-2005 the Seasar Foundation and the Others.
 * 
 * Licensed under the Seasar Software License, v1.1 (aka "the License");
 * you may not use this file except in compliance with the License which 
 * accompanies this distribution, and is available at
 * 
 *     http://www.seasar.org/SEASAR-LICENSE.TXT
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.seasar.maya.impl.cycle.web;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.seasar.maya.cycle.AttributeScope;
import org.seasar.maya.impl.util.collection.EnumerationIterator;

/**
 * @author Masataka Kurihara (Gluegent, Inc)
 */
public class HeaderValuesScope implements AttributeScope {

	private HttpServletRequest _request;
	
	public HeaderValuesScope(HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException();
		}
		_request = request;
	}

	public String getScopeName() {
		return "headerValues";
	}

	public Iterator iterateAttributeNames() {
		return EnumerationIterator.getInstance(_request.getHeaderNames());
	}

	public boolean hasAttribute(String name) {
		for(Iterator it = iterateAttributeNames(); it.hasNext(); ) {
			String headerName = (String)it.next();
			if(headerName.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public Object getAttribute(String name) {
        if(hasAttribute(name)) {
            return _request.getHeaders(name);
        }
        return UNDEFINED;
	}

	public boolean isAttributeWritable() {
		return false;
	}

	public void setAttribute(String name, Object attribute) {
	}

	public void removeAttribute(String name) {
	}

}