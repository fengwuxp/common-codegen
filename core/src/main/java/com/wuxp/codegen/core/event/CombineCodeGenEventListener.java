package com.wuxp.codegen.core.event;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CombineCodeGenEventListener implements CodeGenEventListener {


    private final List<CodeGenEventListener> eventListeners;

    public CombineCodeGenEventListener(List<CodeGenEventListener> eventListeners) {
        this.eventListeners = eventListeners;
    }

    @Override
    public void onApplicationEvent(CodeGenEvent event) {
        eventListeners.forEach(codeGenEventListener -> codeGenEventListener.onApplicationEvent(event));
    }

    @Override
    public Set<CommonCodeGenClassMeta> getEventCodeGenMetas() {
        return eventListeners.stream()
                .map(CodeGenEventListener::getEventCodeGenMetas)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream).collect(Collectors.toSet());
    }
}
