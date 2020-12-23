package com.burton.plugin.servicePublish.util;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.search.EverythingGlobalScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiTreeUtil;

/*********************************
 * <p> 文件名称: PsiUtil
 * <p> 模块名称：com.burton.plugin.servicePublish.util
 * <p> 功能说明: 类操作工具类
 * <p> 开发人员：burton
 * <p> 开发时间：2020/12/16
 * <p> 修改记录：程序版本   修改日期    修改人员   修改单号   修改说明
 **********************************/
public class PsiUtil {
    public static String getFileExtension(DataContext dataContext) {
        VirtualFile file = DataKeys.VIRTUAL_FILE.getData(dataContext);
        return file == null ? null : file.getExtension();
    }

    private PsiElement getPsiElement(AnActionEvent e) {
        PsiFile psiFile = (PsiFile) e.getData(LangDataKeys.PSI_FILE);
        Editor editor = (Editor) e.getData(PlatformDataKeys.EDITOR);
        if (psiFile != null && editor != null) {
            int offset = editor.getCaretModel().getOffset();
            return psiFile.findElementAt(offset);
        } else {
            e.getPresentation().setEnabled(false);
            return null;
        }
    }

    private PsiClass getPsiMethodFromContext(AnActionEvent e) {
        PsiElement elementAt = this.getPsiElement(e);
        return elementAt == null ? null : (PsiClass) PsiTreeUtil.getParentOfType(elementAt, PsiClass.class);
    }

    public static PsiClass getPsiClassByName(String className, AnActionEvent e) {
        PsiClass[] psiClasses = PsiShortNamesCache.getInstance(e.getProject()).getClassesByName(className, new EverythingGlobalScope(e.getProject()));
        return psiClasses[0];
    }

    //psiFile转psiClass
    public static PsiClass getPsiClass(PsiFile psiFile) {
        String fullName = psiFile.getName();
        String className = fullName.split("\\.")[0];
        PsiClass[] psiClasses = PsiShortNamesCache.getInstance(psiFile.getProject()).getClassesByName(className, new EverythingGlobalScope(psiFile.getProject()));
        return psiClasses[0];
    }

    //psiFile转psiClass2
    public static PsiClass getPsiClass2(PsiFile psiFile) {
        PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
        final PsiClass[] classes = psiJavaFile.getClasses();
        return classes[0];
    }
}
