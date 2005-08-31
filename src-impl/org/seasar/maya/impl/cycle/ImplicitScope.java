/*
 * Copyright (c) 2004-2005 the Seasar Foundation and the Others.
 * 
 * Licensed under the Seasar Software License, v1.1 (aka "the License"); you may
 * not use this file except in compliance with the License which accompanies
 * this distribution, and is available at
 * 
 *     http://www.seasar.org/SEASAR-LICENSE.TXT
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.seasar.maya.impl.cycle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.seasar.maya.cycle.AttributeScope;
import org.seasar.maya.cycle.ServiceCycle;
import org.seasar.maya.impl.cycle.implicit.AttributeScopeMap;
import org.seasar.maya.impl.cycle.implicit.HeaderMap;
import org.seasar.maya.impl.cycle.implicit.HeaderValuesMap;
import org.seasar.maya.impl.cycle.implicit.ImplicitObjectResolver;
import org.seasar.maya.impl.cycle.implicit.ParamMap;
import org.seasar.maya.impl.cycle.implicit.ParamValuesMap;
import org.seasar.maya.impl.cycle.implicit.ServiceCycleResolver;
import org.seasar.maya.impl.util.StringUtil;

/**
 * @author Masataka Kurihara (Gluegent, Inc.)
 */
public class ImplicitScope implements AttributeScope {

    public static final String SERVICE_CYCLE = "serviceCycle";
    public static final String PARAM = "param";
    public static final String PARAM_VALUES = "paramValues";
    public static final String HEADER = "header";
    public static final String HEADER_VALUES = "headerValues";
    public static final String APPLICATION = "application";
    public static final String SESSION = "session";
    public static final String REQUEST = "request";
    
	private static Map _resolverMap;
    static {
    	_resolverMap = new HashMap();
    	_resolverMap.put(SERVICE_CYCLE , new ServiceCycleResolver());
    	_resolverMap.put(PARAM , ParamMap.RESOLVER);
    	_resolverMap.put(PARAM_VALUES , ParamValuesMap.RESOLVER);
    	_resolverMap.put(HEADER , HeaderMap.RESOLVER);
    	_resolverMap.put(HEADER_VALUES , HeaderValuesMap.RESOLVER);
        _resolverMap.put(APPLICATION, AttributeScopeMap.RESOLVER_APPLICATION);
        _resolverMap.put(SESSION, AttributeScopeMap.RESOLVER_SESSION);
        _resolverMap.put(REQUEST, AttributeScopeMap.RESOLVER_REQUEST);
    }
    
    private Map _instanceMap = new HashMap();
	
	public String getScopeName() {
		return ServiceCycle.SCOPE_IMPLICIT;
	}

	public Iterator iterateAttributeNames() {
		return _resolverMap.keySet().iterator();
	}

    protected ImplicitObjectResolver getResolver(String name) {
        if(StringUtil.isEmpty(name)) {
            return null;
        }
        return (ImplicitObjectResolver)_resolverMap.get(name);
    }
    
    public boolean hasAttribute(String name) {
		return _resolverMap.containsKey(name);
	}

	public Object getAttribute(String name) {
		if(StringUtil.isEmpty(name)) {
			return null;
		}
        Object object = _instanceMap.get(name);
        if(object == null) {
            ImplicitObjectResolver resolver = getResolver(name);
            if(resolver != null) {
                object = resolver.resolve();
                _instanceMap.put(name, object);
            }
        }
        return object;
	}

    public boolean isAttributeWritable() {
		return false;
	}
	
	public void setAttribute(String name, Object attribute) {
		throw new ScopeNotWritableException(getScopeName());
	}

    public void removeAttribute(String name) {
        throw new ScopeNotWritableException(getScopeName());
    }
	
}
