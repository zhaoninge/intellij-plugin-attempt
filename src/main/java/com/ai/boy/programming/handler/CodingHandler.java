package com.ai.boy.programming.handler;

import com.ai.boy.programming.service.CodeFunctionService;
import com.ai.boy.programming.setting.AppSettingsState;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.codeInsight.hint.HintManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static cn.hutool.json.JSONUtil.toJsonStr;

/**
 * @author zhaoning
 * @date 2024/04/05
 * @desc
 */
public class CodingHandler extends EditorActionHandler {

    private static final Logger LOGGER = Logger.getInstance(CodingHandler.class.getName());

    private final EditorActionHandler originalHandler;

    public CodingHandler(EditorActionHandler originalHandler) {
        this.originalHandler = originalHandler;
    }

    @Override
    protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        try {
            Project project = editor.getProject();
            Document document = editor.getDocument();
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
            caret = null == caret ? editor.getCaretModel().getPrimaryCaret() : caret;
            int line = caret.getLogicalPosition().line;

            if (isCodingCommand(document, line)) {
                if (StringUtils.isBlank(AppSettingsState.getInstance().getSavedApiKey())) {
                    Messages.showErrorDialog("位置：\nPreferences → Tools → Programming Boy", "请先配置API key");
                    return;
                }

                HintManager.getInstance().showInformationHint(editor, "begin thinking, wait for a moment ……");

                String linesContent = readLinesForward(document, line - 1);

                ApplicationManager.getApplication().executeOnPooledThread(() -> {
                    String codedText = CodeFunctionService.getInstance().coding(linesContent);
                    replace(project, document, psiFile, line, codedText);
                });

            } else {
                originalHandler.execute(editor, caret, dataContext);
            }
        } catch (Exception e) {
            LOGGER.error("coding error, caret: " + toJsonStr(caret), e);
        }
    }

    private boolean isCodingCommand(Document document, int line) {
        return readLine(document, line)
                .trim()
                .startsWith("/code");
    }

    private String readLine(Document document, int line) {
        int lineStartOffset = document.getLineStartOffset(line);
        int lineEndOffset = document.getLineEndOffset(line);
        return document.getText(new TextRange(lineStartOffset, lineEndOffset));
    }

    private String readLinesForward(Document document, int line) {
        int beginLine = line;
        while (true) {
            String preLineContent = readLine(document, beginLine);
            // 读到空行，或遇到文件头，扎到行块分割
            if (preLineContent.trim().isEmpty() || beginLine <= 0) {
                TextRange textRange = new TextRange(document.getLineStartOffset(beginLine),
                        document.getLineEndOffset(line));
                return document.getText(textRange);
            }
            --beginLine;
        }
    }

    private void replace(Project project, Document document, PsiFile psiFile, int line, String text) {
        int lineStartOffset = document.getLineStartOffset(line);
        int lineEndOffset = document.getLineEndOffset(line);
        WriteCommandAction.runWriteCommandAction(project, () -> {
            document.replaceString(lineStartOffset, lineEndOffset, text);
            // 格式化新增代码
            format(psiFile, lineStartOffset, lineEndOffset + text.length());
        });
    }

    private void format(PsiFile psiFile, int startOffset, int endOffset) {
        TextRange textRange = new TextRange(startOffset, endOffset);
        ReformatCodeProcessor reformatCodeProcessor = new ReformatCodeProcessor(psiFile,
                new TextRange[]{textRange});
        reformatCodeProcessor.run();
    }
}
