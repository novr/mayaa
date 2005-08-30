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
package org.seasar.maya.impl.builder.injection;

import java.util.ArrayList;
import java.util.List;

import org.seasar.maya.builder.injection.InjectionChain;
import org.seasar.maya.builder.injection.InjectionResolver;
import org.seasar.maya.engine.specification.SpecificationNode;
import org.seasar.maya.impl.provider.UnsupportedParameterException;

/**
 * @author Masataka Kurihara (Gluegent, Inc.)
 */
public class CompositeInjectionResolver implements InjectionResolver {
    
	private List _resolvers = new ArrayList();
	
    /**
     * ���]���o��ǉ�����B
     * @param resolver �ǉ����������]���o�B
     */
    public void add(InjectionResolver resolver) {
        if(resolver == null) {
            throw new IllegalArgumentException();
        }
        synchronized (_resolvers) {
            _resolvers.add(resolver);
        }
    }
    
    public SpecificationNode getNode(SpecificationNode original, InjectionChain chain) {
        if(original == null || chain == null) {
            throw new IllegalArgumentException();
        }
        if(_resolvers.size() > 0) {
            InjectionChainImpl first = new InjectionChainImpl(chain);
            return first.getNode(original);
        }
        return chain.getNode(original);
    }

    private class InjectionChainImpl implements InjectionChain {
        
        private int _index;
        private InjectionChain _external;
        
        public InjectionChainImpl(InjectionChain external) {
            if (external == null) {
                throw new IllegalArgumentException();
            }
            _external = external;
        }
        
        public SpecificationNode getNode(SpecificationNode original) {
            if(original == null) {
                throw new IllegalArgumentException();
            }
            if(_index < _resolvers.size()) {
                InjectionResolver resolver = (InjectionResolver)_resolvers.get(_index);
                _index++;
                InjectionChain chain;
                if(_index == _resolvers.size()) {
                    chain = _external;
                } else {
                    chain = this;
                }
                return resolver.getNode(original, chain);
            }
            throw new IndexOutOfBoundsException();
        }

    }
    
    public void setParameter(String name, String value) {
        throw new UnsupportedParameterException(name);
    }

}