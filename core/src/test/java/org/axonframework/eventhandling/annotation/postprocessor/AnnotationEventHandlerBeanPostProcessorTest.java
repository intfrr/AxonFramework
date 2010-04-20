/*
 * Copyright (c) 2010. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.eventhandling.annotation.postprocessor;

import net.sf.cglib.proxy.Enhancer;
import org.axonframework.domain.DomainEvent;
import org.axonframework.domain.Event;
import org.axonframework.eventhandling.EventListener;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Allard Buijze
 */
public class AnnotationEventHandlerBeanPostProcessorTest {

    private AnnotationEventListenerBeanPostProcessor testSubject;

    @Before
    public void setUp() {
        testSubject = spy(new AnnotationEventListenerBeanPostProcessor());
    }

    @Test
    public void testPostProcessBean_AlreadyHandlerIsNotEnhanced() {
        RealEventListener eventHandler = new RealEventListener();
        Object actualResult = testSubject.postProcessAfterInitialization(eventHandler, "beanName");
        assertFalse(Enhancer.isEnhanced(actualResult.getClass()));
        assertSame(eventHandler, actualResult);
    }

    @Test
    public void testPostProcessBean_PlainObjectIsIgnored() {
        NotAnEventHandler eventHandler = new NotAnEventHandler();
        Object actualResult = testSubject.postProcessAfterInitialization(eventHandler, "beanName");
        assertFalse(Enhancer.isEnhanced(actualResult.getClass()));
        assertSame(eventHandler, actualResult);
    }

    public static class NotAnEventHandler {

    }

    public static class SimpleEventHandler {

        @EventHandler
        public void handleEvent(DomainEvent event) {
            // not relevant
        }
    }

    public static class RealEventListener implements EventListener {

        @Override
        public void handle(Event event) {
            // not relevant
        }

        @EventHandler
        public void handleEvent(Event event) {

        }
    }
}