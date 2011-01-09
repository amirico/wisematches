package wisematches.server.core.sessions.impl;

import org.springframework.asm.*;
import wisematches.server.core.sessions.ImplementationBeanType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
class MocksGenerator {
    private String name;
    private ClassWriter classWriter;

    private static int interfaceIndex;
    static AsmClassLoader classLoader = new AsmClassLoader();

    void generateInterface(Class... inherits) {
        final String[] ints = new String[inherits.length];
        for (int i = 0; i < inherits.length; i++) {
            final Class inherit = inherits[i];
            ints[i] = inherit.getName().replaceAll("\\.", "/");
        }

        name = "wisematches.server.core.sessions.impl.MockInterface" + (interfaceIndex++);
        final String asmName = name.replaceAll("\\.", "/");

        classWriter = new ClassWriter(true);
        classWriter.visit(Opcodes.V1_6,
                Opcodes.ACC_PUBLIC + Opcodes.ACC_INTERFACE + Opcodes.ACC_ABSTRACT,
                asmName, null, "java/lang/Object", ints);
    }

    void generateClass(boolean isAbstract, Class superClass, Class... inherits) {
        final String[] ints = new String[inherits.length];
        for (int i = 0; i < inherits.length; i++) {
            final Class inherit = inherits[i];
            ints[i] = inherit.getName().replaceAll("\\.", "/");
        }

        name = "wisematches.server.core.sessions.impl.MockClass" + (interfaceIndex++);
        final String asmName = name.replaceAll("\\.", "/");

        classWriter = new ClassWriter(true);
        classWriter.visit(Opcodes.V1_6,
                Opcodes.ACC_PUBLIC + (isAbstract ? Opcodes.ACC_ABSTRACT : 0),
                asmName, null,
                superClass.getName().replaceAll("\\.", "/"),
                ints);

/*
        //init body
        if (!isAbstract) {
            final MethodVisitor mv = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);

            mv.visitCode();
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
*/
    }

    public void addImplementationAnnotation() {
        final Type annType = Type.getType(ImplementationBeanType.class);

        final AnnotationVisitor annotationVisitor = classWriter.visitAnnotation(annType.getDescriptor(), true);
        annotationVisitor.visit("value", "wisematches.server.core.sessions.impl.MockClass" + (interfaceIndex));
        annotationVisitor.visitEnd();
    }

    public void addImplementationMethod(String name, Class returnType, Class... parameters) {
        final String signature = Type.getMethodDescriptor(Type.getType(returnType), converToTypes(parameters));
        final MethodVisitor mv = classWriter.visitMethod(Opcodes.ACC_PUBLIC, name, signature, null, null);
        mv.visitCode();
        mv.visitInsn(Opcodes.RETURN);
        mv.visitEnd();
    }

    public void addConstructor(int modifier, Class... types) {
        final Type type = Type.getType(Void.TYPE);
        final String s = Type.getMethodDescriptor(type, converToTypes(types));

        final MethodVisitor mv = classWriter.visitMethod(modifier, "<init>", s, null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    void addInterfaceMethod(String name, Class returnType, Class... parameters) {
        final Type[] params = converToTypes(parameters);
        String signature = Type.getMethodDescriptor(Type.getType(returnType), params);
        classWriter.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, name, signature, null, null);
    }


    private Type[] converToTypes(Class... parameters) {
        final Type[] params = new Type[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            params[i] = Type.getType(parameters[i]);
        }
        return params;
    }

    Class toClass() {
        classWriter.visitEnd();

        try {
            classLoader.addPredefinedClass(name, classWriter.toByteArray());
            final Class<?> aClass = classLoader.loadClass(name);

            return aClass;
        } catch (ClassNotFoundException e) {
            throw new AssertionError("Class '" + name + "' was added to ClassLoader but can't be loaded???");
        }
    }

    static class AsmClassLoader extends ClassLoader {
        private final Map<String, byte[]> asmClasses = new HashMap<String, byte[]>();

        public void addPredefinedClass(String name, byte[] classes) {
            asmClasses.put(name, classes);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            final byte[] bytes = asmClasses.get(name);
            if (bytes != null) {
                return super.defineClass(name, bytes, 0, bytes.length);
            }
            return super.findClass(name);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }
}