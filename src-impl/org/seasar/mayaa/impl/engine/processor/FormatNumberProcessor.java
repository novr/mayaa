/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.mayaa.impl.engine.processor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.seasar.mayaa.cycle.ServiceCycle;
import org.seasar.mayaa.engine.Page;
import org.seasar.mayaa.engine.processor.ProcessStatus;
import org.seasar.mayaa.engine.processor.ProcessorProperty;
import org.seasar.mayaa.impl.cycle.CycleUtil;
import org.seasar.mayaa.impl.util.ObjectUtil;
import org.seasar.mayaa.impl.util.collection.AbstractSoftReferencePool;

/**
 * @author Koji Suga (Gluegent, Inc.)
 */
public class FormatNumberProcessor extends TemplateProcessorSupport {

    private static final long serialVersionUID = 7899970766673369995L;

    private ProcessorProperty _value;
    private String _pattern;
    private static Map _formatPools = new HashMap();

    public void initialize() {
        if (_pattern == null) {
            _pattern = new DecimalFormat().toPattern();
        }
    }

    // MLD property, expectedClass=java.lang.String
    public void setValue(ProcessorProperty value) {
        _value = value;
    }

    public void setPattern(String pattern) {
        _pattern = pattern;
    }

    public ProcessStatus doStartProcess(Page topLevelPage) {
        if(_value != null) {
            ServiceCycle cycle = CycleUtil.getServiceCycle();
            cycle.getResponse().write(format(_value));
        }
        return ProcessStatus.SKIP_BODY;
    }

    private String format(ProcessorProperty property) {
        Object result = property.getValue().execute(null);
        if (result != null) {
            if (result instanceof Number || result instanceof String) {
                NumberFormatPool pool = getFormatPool();

                NumberFormat formatter = pool.borrowFormat();
                String formattedValue =
                    formatter.format(ObjectUtil.numberValue(result, null));

                pool.returnFormat(formatter);
                return formattedValue;
            }

            throw new IllegalArgumentException(
                    "argument type mismatch: " + result.getClass().getName());
        }
        return "";
    }

    protected NumberFormatPool getFormatPool() {
        synchronized (_formatPools) {
            NumberFormatPool pool = (NumberFormatPool)_formatPools.get(_pattern);
            if(pool == null) {
                pool = new NumberFormatPool(_pattern);
                _formatPools.put(_pattern, pool);
            }
            return pool;
        }
    }

    protected class NumberFormatPool extends AbstractSoftReferencePool {

        private static final long serialVersionUID = -4295432835558317767L;

        private String _formatPattern;

        public NumberFormatPool(String formatPattern) {
            if (formatPattern == null) {
                throw new IllegalArgumentException();
            }
            _formatPattern = formatPattern;
        }

        protected Object createObject() {
            return new DecimalFormat(_formatPattern);
        }

        protected boolean validateObject(Object object) {
            return object instanceof NumberFormat;
        }

        public NumberFormat borrowFormat() {
            return (NumberFormat) borrowObject();
        }

        public void returnFormat(NumberFormat format) {
            if (format != null) {
                returnObject(format);
            }
        }

    }

}