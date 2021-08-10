package com.wuxp.codegen.core.event;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class CodeGenEvent extends ApplicationEvent {

    private static final long serialVersionUID = -3007080900619401132L;

    @Getter
    private final boolean done;

    public CodeGenEvent(CommonCodeGenClassMeta source) {
        this(source, false);
    }

    public CodeGenEvent(CommonCodeGenClassMeta source, boolean done) {
        super(source);
        this.done = done;
    }

    @Override
    public CommonCodeGenClassMeta getSource() {
        return (CommonCodeGenClassMeta) super.getSource();
    }
}
