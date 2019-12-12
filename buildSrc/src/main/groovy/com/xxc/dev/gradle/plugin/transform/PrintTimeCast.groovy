package com.xxc.dev.gradle.plugin.transform

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

class PrintTimeCast extends Transform {

    Project mProject

    PrintTimeCast(Project project) {
        mProject = project
    }

    @Override
    String getName() {
        return getClass().getSimpleName()
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
//        return TransformManager.EMPTY_SCOPES
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    Set<? super QualifiedContent.Scope> getReferencedScopes() {
//        return QualifiedContent.Scope.PROJECT
        return super.getReferencedScopes()
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
//        for (TransformInput input : invocation.getInputs) {
//            input.getJarInputs().parallelStream().each { jarInput ->
//                File src = jarInput.getFile()
//                JarFile jarFile = new JarFile(src)
//                Enumeration<JarEntry> entries = jarFile.entries()
//                while (entries.hasMoreElements()) {
//                    JarEntry entry = entries.nextElement()
//                    println "JarEntry >>> " + entry.name
//                }
//            }
//        }
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        super.transform(context, inputs, referencedInputs, outputProvider, isIncremental)
        TransformInjectImpl.init(mProject)
        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                //注入代码
                TransformInjectImpl.inject(directoryInput.file, mProject)
                def dest = outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }

            input.jarInputs.each { JarInput jarInput ->
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }

                def dest = outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
//                println "JarName >>> " + jarName
                FileUtils.copyFile(jarInput.file, dest)
            }
        }
        System.err.println(">>>>>>>>>  " + TransformInjectImpl.APP_R_CLASS.absolutePath)
    }
}
