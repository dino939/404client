package com.denger.client.another.retransform.types;

import b.a;
import com.denger.client.another.retransform.AbstractTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import static b.a.getClassBytes;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;

public class reWrite extends AbstractTransformer {
    private final Class<?> target, hook;
    private final int id;

    public reWrite(Class<?> target, Class<?> hook, int methodId) {
        this.target = target;
        this.hook = hook;
        id = methodId;
    }


    @Override
    public void transform() {
        String targetName = target.getName().replace(".", "/");
        ClassNode targetNode = bytesToClassNode(getClassBytes(targetName));
        ClassNode hookNode = bytesToClassNode(getClassBytes(hook.getName().replace(".", "/")));
        targetNode.methods.stream().filter(methodNode -> equals(methodNode, hookNode.methods.get(id))).forEach(methodNode -> {
            AbstractInsnNode toNeedDel = null;
            for (AbstractInsnNode insnList : methodNode.instructions){
                if (insnList.getOpcode() == Opcodes.NEW){
                    toNeedDel = insnList;
                }
                if (insnList instanceof MethodInsnNode){
                    MethodInsnNode we = (MethodInsnNode) insnList;
                    if (we.getOpcode() == INVOKESPECIAL){
                        we.owner = "com/denger/client/another/hooks/ClientPlayerEntityHook";
                    }
                }
            }
            InsnList insnList = new InsnList();
            insnList.add(new TypeInsnNode(Opcodes.NEW, "com/denger/client/another/hooks/ClientPlayerEntityHook"));
            methodNode.instructions.insert(toNeedDel, insnList);
            methodNode.instructions.remove(toNeedDel);
        });
        byte[] clazz = convert(targetNode);
        a.redefineClass(targetName, clazz);
    }

}
