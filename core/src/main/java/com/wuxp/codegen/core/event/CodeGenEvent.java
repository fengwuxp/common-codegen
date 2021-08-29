package com.wuxp.codegen.core.event;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class CodeGenEvent extends ApplicationEvent {

    private static final long serialVersionUID = -3007080900619401132L;

    @Getter
    private final CodeGenEventStatus status;

    public CodeGenEvent(CommonCodeGenClassMeta source) {
        this(source, CodeGenEventStatus.SCAN_CODEGEN);
    }

    public CodeGenEvent(CommonCodeGenClassMeta source, CodeGenEventStatus status) {
        super(source);
        this.status = status;
    }

    @Override
    public CommonCodeGenClassMeta getSource() {
        return (CommonCodeGenClassMeta) super.getSource();
    }

    public enum CodeGenEventStatus {

        SCAN_CODEGEN,

        SCAN_CODEGEN_DONE,

        EVENT_CODEGEN,

        COMPLETED
    }
}
