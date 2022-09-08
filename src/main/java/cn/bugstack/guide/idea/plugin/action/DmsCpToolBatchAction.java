package cn.bugstack.guide.idea.plugin.action;

import cn.bugstack.guide.idea.plugin.domain.service.IProjectGenerator;
import cn.bugstack.guide.idea.plugin.domain.service.impl.ProjectGeneratorImpl;
import cn.bugstack.guide.idea.plugin.ui.DmsCpToolUI;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/6 15:20
 * @Version 1.0
 */
public class DmsCpToolBatchAction extends AnAction {

    private IProjectGenerator projectGenerator = new ProjectGeneratorImpl();

    public DmsCpToolBatchAction() {
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        String classPath = psiFile.getVirtualFile().getPath();

        DmsCpToolUI cpToolUI = new DmsCpToolUI(project,psiFile,1);
        //是否允许用户通过拖拽的方式扩大或缩小你的表单框，我这里定义为true，表示允许
        cpToolUI.setResizable(true);
        cpToolUI.show();
    }
}