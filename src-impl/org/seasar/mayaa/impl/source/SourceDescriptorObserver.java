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
package org.seasar.mayaa.impl.source;

import org.seasar.mayaa.source.SourceDescriptor;

/**
 * @author Taro Kato (Gluegent, Inc.)
 */
public interface SourceDescriptorObserver {

    /**
     * �o�^����Ă���SourceDescriptor��ʒm����B
     * 
     * @param sourceDescriptor
     *            �o�^����Ă���SourceDescriptor
     * @return ���̓o�^SourceDescriptor�̒ʒm���󂯂����ꍇ�� true��Ԃ����ƁB �������I���������ꍇ�� false ��Ԃ����ƁB
     */
    boolean nextSourceDescriptor(SourceDescriptor sourceDescriptor);

}