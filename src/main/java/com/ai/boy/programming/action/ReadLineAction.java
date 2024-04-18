package com.ai.boy.programming.action;

import com.ai.boy.programming.service.CodeFunctionService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

public class ReadLineAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        // 从光标位置开始向上读取行，直到遇到空行或文件头

        Editor editor = event.getRequiredData(PlatformDataKeys.EDITOR);
        Document document = editor.getDocument();
        PsiFile psiFile = PsiManager.getInstance(event.getProject())
                .findFile(FileDocumentManager.getInstance().getFile(document));
        if (psiFile == null) {
            return;
        }

        int currentOffset = editor.getCaretModel().getCurrentCaret().getOffset();
        int curLineNum = document.getLineNumber(currentOffset);
        while (true) {
            // 向上读一行
            --curLineNum;
            int preLineStartOffset = document.getLineStartOffset(curLineNum);
            int preLineEndOffset = document.getLineEndOffset(curLineNum);
            String preLineContent = document.getText(new TextRange(preLineStartOffset, preLineEndOffset));

            // 读到空行，或遇到文件头
            if (preLineContent.trim().isEmpty() || curLineNum <= 0) {
                // 将读到的文本块写入光标处
                String linesContent = document.getText(new TextRange(preLineStartOffset, currentOffset));

                String function = CodeFunctionService.getInstance().coding(linesContent);

                WriteCommandAction.runWriteCommandAction(event.getProject(), () -> {
                    document.insertString(currentOffset, function);
                });
                break;
            }
        }
    }
}
