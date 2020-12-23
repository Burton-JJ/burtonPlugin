package com.burton.plugin.servicePublish.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.xml.XmlFile;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/*********************************
 * <p> 文件名称: ServicePublishUtil

 * <p> 模块名称：com.burton.plugin.servicepublish.utils
 * <p> 功能说明: 服务发布工具类
 * <p> 开发人员：burton
 * <p> 开发时间：2020/8/28
 * <p> 修改记录：程序版本   修改日期    修改人员   修改单号   修改说明
 **********************************/
public class ServicePublishUtil {

    /**
     * 获取接口实现类名称
     * 接口名 + impl
     *
     * @param serviceName
     * @return
     */
    private static String getServiceImplName(String serviceName) {
        char[] chars = serviceName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars) + "Impl";
    }


    /**
     * xml格式化
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String formatXml(String str) throws Exception {
        Document document = null;
        document = DocumentHelper.parseText(str);
        // 格式化输出格式
        OutputFormat format = OutputFormat.createPrettyPrint();
        //format.setEncoding("gb2312");
        StringWriter writer = new StringWriter();
        // 格式化输出流
        XMLWriter xmlWriter = new XMLWriter(writer, format);
        // 将document写入到输出流
        xmlWriter.write(document);
        xmlWriter.close();
        return writer.toString();
    }


    /**
     * 生成xml文件到指定路径
     *
     * @param filePath
     * @param fileName
     * @param formatedXmlContent
     * @param e
     */
    public static void generateXmlToDir(String filePath, String fileName, String formatedXmlContent, AnActionEvent e) {
        //创建Xml文件
        XmlFile psiFile = (XmlFile) PsiFileFactory.getInstance(e.getProject()).createFileFromText(fileName, StdFileTypes.XML, formatedXmlContent);
        File ioFile = new File(filePath);
        VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(ioFile);
        //virtualFile.refresh(false, true)

        PsiDirectory psiDirectory = PsiDirectoryFactory.getInstance(e.getProject()).createDirectory(virtualFile);
        psiDirectory.add(psiFile);
    }

    /**
     * 获取Service的服务发布xml文件内容
     *
     * @param interfaceMethodInfoList
     * @return
     */
    public static StringBuilder getServiceXml(List<TbspInterfaceMethodInfo> interfaceMethodInfoList) {
        StringBuilder stringBuilder = new StringBuilder();
        String serviceName = interfaceMethodInfoList.get(0).getServiceName().substring(interfaceMethodInfoList.get(0).getServiceName().lastIndexOf(".") + 1);
        stringBuilder.append("<service-components>");
        stringBuilder.append("<service-component type=\"").append(interfaceMethodInfoList.get(0).getServiceName()).append("\" bean=\"").append(getServiceImplName(serviceName)).append("\">");
        //stringBuilder.append(System.lineSeparator());
        for (TbspInterfaceMethodInfo tbspInterfaceMethodInfo : interfaceMethodInfoList) {
            stringBuilder.append("<service-method local-name=\"").append(tbspInterfaceMethodInfo.getMethodName()).append("\" service-id=\"")
                    .append(tbspInterfaceMethodInfo.getServiceName()).append(".").append(tbspInterfaceMethodInfo.getMethodName()).append("\" method-name=\"")
                    .append(tbspInterfaceMethodInfo.getMethodName()).append("\" cacheable=\"false\" category=\"").append(tbspInterfaceMethodInfo.getServicePackageName())
                    .append("\">").append("<service-parameters>").append("<service-parameter name=\"req\" type=\"").append(tbspInterfaceMethodInfo.getParamName())
                    .append("\" required=\"false\" is-array=\"false\"/>").append("</service-parameters>").append("<service-result name=\"")
                    .append(tbspInterfaceMethodInfo.getMethodName()).append("Result").append("\" required=\"false\" is-array=\"false\" type=\"")
                    .append(tbspInterfaceMethodInfo.getReturnName()).append("\"/>").append("</service-method>");
        }
        stringBuilder.append("</service-component>").append("</service-components>");
        return stringBuilder;
    }

    /**
     * 获取ServiceWrraper的服务发布xml文件内容
     *
     * @param interfaceMethodInfoList
     * @return
     */
    public static StringBuilder getServiceWrraperXml(List<TbspInterfaceMethodInfo> interfaceMethodInfoList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<method-configs>");
        //stringBuilder.append(System.lineSeparator());
        for (TbspInterfaceMethodInfo tbspInterfaceMethodInfo : interfaceMethodInfoList) {
            stringBuilder.append("<method-config service-id=\"").append(tbspInterfaceMethodInfo.getServiceName()).append(".").append(tbspInterfaceMethodInfo.getMethodName())
                    .append("\" type=\"").append(tbspInterfaceMethodInfo.getServiceName()).append("\" method-name=\"").append(tbspInterfaceMethodInfo.getMethodName()).append("\">");
            stringBuilder.append(System.lineSeparator());
            stringBuilder.append("<parameter-type type=\"").append(tbspInterfaceMethodInfo.getParamName()).append("\"/>");
            //stringBuilder.append(System.lineSeparator());
            stringBuilder.append("</method-config>");
            //stringBuilder.append(System.lineSeparator());
        }
        stringBuilder.append("</method-configs>");
        return stringBuilder;
    }

    /**
     * 创建ServiceWrapper文件
     * @param interfaceMethodInfoList
     * @param filePath
     * @param e
     * @throws Exception
     */
    public static void generateServiceWrapper(List<TbspInterfaceMethodInfo> interfaceMethodInfoList, String filePath, AnActionEvent e) throws Exception {
        if (interfaceMethodInfoList != null && interfaceMethodInfoList.size() > 0) {
            String serviceName = interfaceMethodInfoList.get(0).getServiceName().substring(interfaceMethodInfoList.get(0).getServiceName().lastIndexOf(".") + 1);
            String fileName = serviceName + ".servicewrapper.new.xml";
            StringBuilder serviceWrraperXml = getServiceWrraperXml(interfaceMethodInfoList);
            String formatedXml = formatXml(serviceWrraperXml.toString());
            generateXmlToDir(filePath, fileName, formatedXml, e);
        }
    }

    /**
     * 创建Service文件
     * @param interfaceMethodInfoList
     * @param filePath
     * @param e
     * @throws Exception
     */
    public static void generateService(List<TbspInterfaceMethodInfo> interfaceMethodInfoList, String filePath, AnActionEvent e) throws Exception {
        if (interfaceMethodInfoList != null && interfaceMethodInfoList.size() > 0) {
            String serviceName = interfaceMethodInfoList.get(0).getServiceName().substring(interfaceMethodInfoList.get(0).getServiceName().lastIndexOf(".") + 1);
            String fileName = serviceName + ".service.new.xml";
            StringBuilder serviceXml = getServiceXml(interfaceMethodInfoList);
            String formatedXml = formatXml(serviceXml.toString());
            generateXmlToDir(filePath, fileName, formatedXml, e);
        }
    }

    /**
     * 添加需要扫描的jar包
     *
     * @param jarPath
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws MalformedURLException
     */
    public static void addUrl(File jarPath) throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, MalformedURLException {

        //URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        //ClassLoader.getSystemClassLoader();
//        ClassLoader classLoader = ServicePublishUtil.class.getClassLoader();
        URL url = jarPath.toURI().toURL();

//        if (!(classLoader instanceof URLClassLoader)) {
//            try {
//                Field field = classLoader.getClass().getDeclaredField("ucp");
//                field.setAccessible(true);
//                Object ucp = field.get(classLoader);
//
//                System.out.println("DEB: invoke method!");
//                Method method = ucp.getClass().getDeclaredMethod("addURL", URL.class);
//                method.setAccessible(true);
//
//                method.invoke(ucp, url);
//            } catch (Exception exception) {
//                exception.printStackTrace();
//                throw new IllegalStateException(exception.getMessage(), exception);
//            }
//        }



        // 反射获取类加载器中的addURL方法，并将需要加载类的jar路径
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
//        URL url = jarPath.toURI().toURL();
        // 把当前jar的路径加入到类加载器需要扫描的路径
        method.invoke(urlClassLoader, url);
    }

}
