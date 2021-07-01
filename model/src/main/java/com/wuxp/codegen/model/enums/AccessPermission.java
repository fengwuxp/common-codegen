package com.wuxp.codegen.model.enums;

import java.lang.reflect.Modifier;

/**
 * 访问权限控制
 *
 * @author wuxp
 */
public enum AccessPermission {


    PRIVATE("private"),

    PUBLIC("public"),

    PROTECTED("protected"),

    DEFAULT("");

    private final String value;

    AccessPermission(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isPublic() {
        return PUBLIC.equals(this);
    }

    public boolean isPrivate() {
        return PRIVATE.equals(this);
    }

    public static AccessPermission valueOfModifiers(int modifiers) {
        if (Modifier.isPrivate(modifiers)) {
            return AccessPermission.PRIVATE;
        } else if (Modifier.isProtected(modifiers)) {
            return AccessPermission.PROTECTED;
        } else if (Modifier.isPublic(modifiers)) {
            return AccessPermission.PUBLIC;
        } else {
            return AccessPermission.DEFAULT;
        }
    }
}
