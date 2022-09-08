package cn.bugstack.guide.idea.plugin.ui;

import cn.bugstack.guide.idea.plugin.infrastructure.utils.FileUtil;
import cn.bugstack.guide.idea.plugin.infrastructure.utils.MyBundle;
import cn.bugstack.guide.idea.plugin.module.FileChooserComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class DmsCpToolUI extends DialogWrapper {
    private JPanel contentPane;
    private JList listShow;
    private JTextField textFieldProject;
    private JTextField textFieldSvn;
    private JLabel labProject;
    private JLabel labSvn;
    private JPanel Settings;
    private JLabel labFile;
    private JTextField textFieldIniFile;
    private JButton btnSelect;
    private JButton btnFileSelect;

    private DefaultListModel listModel;

    private CustomOKAction okAction;
    private DialogWrapperExitAction exitAction;

    private Project project;
    private PsiFile psiFile;

    private int projectType = 0;

    public DmsCpToolUI() {
        super(true);
        init();
    }

    public DmsCpToolUI(@Nullable Project project) {
        super(true);
        init();
    }

    public DmsCpToolUI(@Nullable Project project, @Nullable PsiFile psiFile, int projectType) {
        super(true);
        init();
        this.project = project;
        this.psiFile = psiFile;
        this.projectType = projectType;
        initOther();
    }

    /**
     * initOther
     */
    public void initOther(){

        MyBundle myBundle = MyBundle.getInstance();
        String iniFilePath = myBundle.getValue("INIFILEPATH");
        if (null != iniFilePath && !"".equals(iniFilePath)) {
            this.textFieldIniFile.setText(iniFilePath);
            List<String> datas = FileUtil.queryData(iniFilePath);
            this.listShow.setListData(datas.toArray());
            this.textFieldSvn.setText(datas.get(projectType));
        }

        this.textFieldProject.setText(project.getBasePath());
        this.listShow.setListData(
                new String[]{project.toString(),
                        project.getProjectFilePath(),
                        psiFile.getVirtualFile().getPath()});

        //SVN选择
        this.btnSelect.addActionListener(e -> {
            FileChooserComponent component = FileChooserComponent.getInstance(project);
            VirtualFile baseDir = project.getBaseDir();
            VirtualFile virtualFile = component.showFolderSelectionDialog("选择SVN目录", baseDir, baseDir);
            if (null != virtualFile) {
                this.textFieldSvn.setText(virtualFile.getPath());
            }
        });

        //ini文件选择
        this.btnFileSelect.addActionListener(e -> {
            FileChooserComponent component = FileChooserComponent.getInstance(project);
            VirtualFile baseDir = project.getBaseDir();
            VirtualFile virtualFile = component.showFileSelectionDialog("选择配置文件", baseDir, baseDir);
            if(virtualFile != null) {
                //Messages.showMessageDialog(virtualFile.getName(), "获取到的文件名称", Messages.getInformationIcon());
                String fileName = virtualFile.getPath();
                this.textFieldIniFile.setText(fileName);
                List<String> datas = FileUtil.queryData(fileName);
                this.listShow.setListData(datas.toArray());
                this.textFieldSvn.setText(datas.get(projectType));
            } else {
                Messages.showMessageDialog("文件名称为空", "文件名称为空", Messages.getInformationIcon());
            }
        });
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        setModal(true);
        return contentPane;
    }

    /**
     * 校验数据
     * @return 通过必须返回null，不通过返回一个 ValidationInfo 信息
     */
    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        //String text = inputTextField.getText();
        //if(StringUtils.isNotBlank(text)) {
        //    return null;
        //} else {
        //    return new ValidationInfo("校验不通过");
        //}

        return null;
    }

    /**
     * doCopy
     */
    public void doCopy(){
        try {
            String srcFileFullPath = psiFile.getVirtualFile().getPath();
            String srcFileName = psiFile.getVirtualFile().getName();

            int idx = srcFileFullPath.indexOf(Constant.PROPATH);
            String srcFilePath = srcFileFullPath.substring(idx + Constant.PROPATH.length());
            idx = srcFilePath.indexOf(srcFileName);
            String srcPath = srcFilePath.substring(0, idx - 1);

            String desPath = this.textFieldSvn.getText() + "/" + Constant.APPATH + "/" + srcPath;
            String desFileFullPath = desPath + "/" + srcFileName;
            int iRet = FileUtil.makeUploadDir(desPath);
            if (iRet == 0) {
                iRet = FileUtil.copyFile(srcFileFullPath, desFileFullPath);
                if (iRet == 0) {
                    Messages.showMessageDialog(srcFileName + "复制成功","复制成功", Messages.getInformationIcon());
                }
            } else {
                Messages.showMessageDialog("创建路径出错!","复制出错", Messages.getInformationIcon());
            }
        } catch (IOException e) {
            Messages.showMessageDialog(e.getMessage(),"复制出错", Messages.getInformationIcon());
        }
    }

    /**
     * 覆盖默认的ok/cancel按钮
     * @return
     */
    @NotNull
    @Override
    protected Action[] createActions() {
        exitAction = new DialogWrapperExitAction("Cancel", CANCEL_EXIT_CODE);
        okAction = new CustomOKAction();
        // 设置默认的焦点按钮
        okAction.putValue(DialogWrapper.DEFAULT_ACTION, true);
        return new Action[]{okAction,exitAction};
    }

    /**
     * 自定义 ok Action
     */
    protected class CustomOKAction extends DialogWrapperAction {

        protected CustomOKAction() {
            super("OK");
        }

        @Override
        protected void doAction(ActionEvent e) {
            doCopy();
        }
    }
}
