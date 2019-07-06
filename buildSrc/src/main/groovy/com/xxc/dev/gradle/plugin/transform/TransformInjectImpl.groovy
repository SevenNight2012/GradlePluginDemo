package com.xxc.dev.gradle.plugin.transform

import com.android.build.gradle.AppExtension
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.apache.http.util.TextUtils
import org.gradle.api.Project

import java.util.regex.Pattern

class TransformInjectImpl {

    static String APPLICATION_ID

    private static final ClassPool POOL = ClassPool.getDefault()
    private static String RELATIVE_APPLICATION_R_CLASS_PATH
    public static File APP_R_CLASS

    static void init(Project project) {
        AppExtension app = project.extensions.getByType(AppExtension)
        APPLICATION_ID = app.defaultConfig.applicationId
        //project.android.bootClasspath 加入android.jar，不然找不到android相关的所有类
        POOL.appendClassPath(project.android.bootClasspath[0].toString())
        //引入android.os.Bundle包，因为onCreate方法参数有Bundle
        POOL.importPackage("android.os.Bundle")
        POOL.importPackage("android.util.Log")
        POOL.importPackage("android.view.View")
        POOL.importPackage("com.xxc.dev.gradle.plugin.ViewUtils")

        String dir = APPLICATION_ID.replaceAll("\\.", File.separator)
        println "应用applicationId >> " + APPLICATION_ID
        RELATIVE_APPLICATION_R_CLASS_PATH = dir + File.separator + "R.class"

        println "R文件 >> " + RELATIVE_APPLICATION_R_CLASS_PATH
        String R = RELATIVE_APPLICATION_R_CLASS_PATH.substring(0, RELATIVE_APPLICATION_R_CLASS_PATH.length() - 6)
        println "R >>>>> " + R
    }

    static void inject(File file, Project project) {
        println file.absolutePath
        //将当前路径加入类池,不然找不到这个类
        POOL.appendClassPath(file.absolutePath)
        boolean leftSlash = File.separator == '/'

        String root = file.absolutePath
        if (!root.endsWith(File.separator))
            root += File.separator

        //如果是文件件的话遍历所有的文件
        file.eachFileRecurse { File itemFile ->
            def path = itemFile.absolutePath.replaceAll(root, "")
            if (!leftSlash) {
                path = path.replaceAll("\\\\", "/")
            }
            if (shouldTransform(path, itemFile) && path.endsWith(".class")) {
                def classPath = path.replaceAll("/", ".").replaceAll(".class", "")
                println ">>>>  " + classPath
                CtClass ctClass = POOL.get(classPath)

                ctClass.declaredMethods.each { CtMethod method ->
                    boolean hasCastAnnotation = method.hasAnnotation("com.xxc.dev.gradle.plugin.annotation.Cast")
                    boolean hasSmoothClick = method.hasAnnotation("com.xxc.dev.gradle.plugin.annotation.SmoothClick")
                    if (hasCastAnnotation) {
                        method.addLocalVariable("start", CtClass.longType)
                        method.insertBefore("long start = System.currentTimeMillis();")
                        def name = ctClass.simpleName
                        println "ClassName >> " + name
                        method.insertAfter("Log.d(\"" + name + "\", \"" + method.name + " 耗时 >> \"+(System.currentTimeMillis() - start));")
                    } else if (hasSmoothClick) {
//                        第一种方式，将业务逻辑抽离到对应的工具类或者Controller中，直接调用即可，代码少，不容易出错，推荐使用
                        method.addLocalVariable("tag", POOL.get("java.lang.Object"))
                        method.addLocalVariable("result", POOL.get("java.lang.Object"))
                        method.insertBefore("{\n" +
                                "            Object result = null;\n" +
                                "            \n" +
                                "            if (ViewUtils.isSmoothClick(\$1)){\n" +
                                "                \$1.setTag(R.id.smooth_click_tag, (\$w) System.currentTimeMillis());\n" +
                                "            }else{\n" +
                                "                return (\$r)result;\n" +
                                "            }\n" +
                                "        } ")

//                        第二种方式，将业务逻辑提前在对应的类中写好，然后将相关的代码拷贝出来粘贴至此即可，
//                        此种方式不易调试，且容易出错，另外不好修改，若业务逻辑改变，此处代码也需要修改调整，不推荐
//                        但是在学习理解之初，建议多使用此种方式
//                        method.addLocalVariable("lastTime", POOL.get("java.lang.Object"))
//                        method.addLocalVariable("during", CtClass.longType)
//
//                        method.insertBefore("{\n" +
//                                "            Object lastTime = \$1.getTag(R.id.smooth_click_tag);\n" +
//                                "            System.out.println(\"last >> \" + lastTime);\n" +
//                                "            if (null == lastTime) {\n" +
//                                "                v.setTag(R.id.smooth_click_tag, (\$w) System.currentTimeMillis());\n" +
//                                "            } else {\n" +
//                                "                long during = System.currentTimeMillis() - Long.parseLong(String.valueOf(lastTime));\n" +
//                                "                System.out.println(\"耗时 >> \" + during);\n" +
//                                "                if (during <= 500) {\n" +
//                                "                    return;\n" +
//                                "                } else {\n" +
//                                "                    \$1.setTag(R.id.smooth_click_tag, (\$w) System.currentTimeMillis());\n" +
//                                "                }\n" +
//                                "            }\n" +
//                                "        }")
                    }
                }
                ctClass.writeFile(file.absolutePath)
                ctClass.detach()
            }
        }

    }

    static boolean shouldTransform(String path, File file) {
        if (TextUtils.isEmpty(path)) {
            return false
        }
        Pattern androidSupport = Pattern.compile("android/support.*")
        if (androidSupport.matcher(path).matches()) {
            return false
        }
        Pattern androidR = Pattern.compile("^.*R\\.class.*\$")
        if (androidR.matcher(path).matches()) {
            if (path.endsWith(RELATIVE_APPLICATION_R_CLASS_PATH)) {
                APP_R_CLASS = file
                String R = RELATIVE_APPLICATION_R_CLASS_PATH.substring(0, RELATIVE_APPLICATION_R_CLASS_PATH.length() - 6)
                POOL.importPackage(R.replaceAll(File.separator, "."))
            }
            return false
        }
        Pattern androidR$ = Pattern.compile("^.*R\\\$.*\\.class.*\$")
        if (androidR$.matcher(path).matches()) {
            return false
        }
        return true
    }

}
