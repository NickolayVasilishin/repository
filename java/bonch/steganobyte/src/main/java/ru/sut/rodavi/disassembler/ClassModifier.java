package ru.sut.rodavi.disassembler;

/**
 * Created by nikolay on 6/13/17.
 */

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClassModifier {

    public static class ModifierMethodWriter extends MethodVisitor {

        private String methodName;

        public ModifierMethodWriter(int api, MethodVisitor mv, String methodName) {
            super(api, mv);
            this.methodName = methodName;
        }

        //This is the point we insert the code. Note that the instructions are added right after
        //the visitCode method of the super class. This ordering is very important.
        @Override
        public void visitCode() {
            super.visitCode();
            if (!methodName.equals("<init>")) {

                mv.visitLdcInsn("//FORMAT//secretMessage");
                mv.visitVarInsn(Opcodes.ASTORE, 1);

                super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                super.visitVarInsn(Opcodes.ALOAD, 1);
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");

            }
        }
    }

    //Our class modifier class visitor. It delegate all calls to the super class
    //Only makes sure that it returns our MethodVisitor for every method
    public static class ModifierClassWriter extends ClassVisitor {
        private int api;

        public ModifierClassWriter(int api, ClassWriter cv) {
            super(api, cv);
            this.api = api;
        }

        @Override
        public void visit(int i, int i1, String s, String s1, String s2, String[] strings) {
            super.visit(i, i1, s, s1, s2, strings);
            visitField(Opcodes.ACC_STATIC, "message", "Ljava/lang/String;", null, null).visitEnd();
            visitField(Opcodes.ACC_STATIC, "inside", "Ljava/lang/String;", null, null).visitEnd();
            visitField(Opcodes.ACC_STATIC, "fields", "Ljava/lang/String;", null, null).visitEnd();

        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc,
                                         String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            ModifierMethodWriter mvw = new ModifierMethodWriter(api, mv, name);
            return mvw;
        }
    }



    public static void main(String[] args) throws IOException {
        InputStream in = ClassModifier.class.getResourceAsStream("/ru/sut/rodavi/SimpleClass.class");
        ClassReader classReader = new ClassReader(in);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        //Wrap the ClassWriter with our custom ClassVisitor
        ModifierClassWriter mcw = new ModifierClassWriter(Opcodes.ASM4, cw);
        classReader.accept(mcw, 0);

        //Write the output to a class file
        File outputDir = new File("out/ru/sut/rodavi/");
        outputDir.mkdirs();
        DataOutputStream dout = new DataOutputStream(new FileOutputStream(new File(outputDir, "SimpleClass.class")));
        dout.write(cw.toByteArray());
    }

}