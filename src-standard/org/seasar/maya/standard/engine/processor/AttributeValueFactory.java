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
package org.seasar.maya.standard.engine.processor;

import org.seasar.maya.cycle.AttributeScope;
import org.seasar.maya.cycle.ServiceCycle;
import org.seasar.maya.impl.util.CycleUtil;
import org.seasar.maya.impl.util.StringUtil;

/**
 * @author maruo_syunsuke
 */
public class AttributeValueFactory {
    
    public static AttributeValue create(String name){
        if( StringUtil.isEmpty(name)){
            return new AttributeValue_Null();
        }
        return new AttributeValue_Basic(name);
    }
    
    public static AttributeValue create(String name, String scope){
        if( StringUtil.isEmpty(name)){
            return new AttributeValue_Null();
        }
        return new AttributeValue_Basic(name, scope);
    }

    public static AttributeValue createForceOutputer(String name){
        if( StringUtil.isEmpty(name)){
            return new AttributeValue_Output();
        }
        return new AttributeValue_Basic(name);
    }
    
    public static AttributeValue createForceOutputer(String name, String scope){
        if( StringUtil.isEmpty(name)){
            return new AttributeValue_Output();
        }
        return new AttributeValue_Basic(name, scope);
    }
}

class AttributeValue_Basic implements AttributeValue {
    
    private static final long serialVersionUID = 5161583383272232824L;
    
    private String _name ;
    private String _scope ;
    
    public AttributeValue_Basic(String name){
        this(name, ServiceCycle.SCOPE_PAGE);
    }
    
    public AttributeValue_Basic(String name, String scope){
        _name  = name ;
        _scope = scope ;
    }
    
    public String getName() {
        return _name;
    }
    
    public void setValue(Object value) {
        ServiceCycle cycle = CycleUtil.getServiceCycle();
        AttributeScope scope = cycle.getAttributeScope(_scope);
        scope.setAttribute(_name, value);
    }
    
    public Object getValue(){
        ServiceCycle cycle = CycleUtil.getServiceCycle();
        return cycle.getAttribute( _name );
    }
    
    public void remove(){
        ServiceCycle cycle = CycleUtil.getServiceCycle();
        cycle.removeAttribute(_name);
    }
    
}

class AttributeValue_Null implements AttributeValue {
    
    private static final long serialVersionUID = 6535995392396158657L;
    
    public void setValue(Object value){
    }
    
    public Object getValue(){
        return null ;
    }
    
    public void remove(){
    }
    
    public String getName() {
        return null;
    }

}

class AttributeValue_Output extends AttributeValue_Null{
    public void setValue(Object value){
        CycleUtil.getServiceCycle().getResponse().write(value.toString());
    }
}