package com.denger.client.utils.asm;

import org.objectweb.asm.ClassWriter;

public class FuckyClassWriter extends ClassWriter {
    public FuckyClassWriter(int flags) {
        super(flags);
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        System.out.println(type1+":"+type2);
        try {
            return super.getCommonSuperClass(type1, type2);
        }catch (TypeNotPresentException ex)
        {
            return "java/lang/Object";
        }
    }
}
