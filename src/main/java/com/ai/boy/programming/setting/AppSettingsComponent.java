// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.ai.boy.programming.setting;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.util.List;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
@Data
public class AppSettingsComponent {

    private final JPanel mainPanel;
    private final JComboBox<String> providerCB = new JComboBox();
    private final JComboBox<String> modelCB = new JComboBox();
    private final JBTextField apiKeyTF = new JBTextField();

    public AppSettingsComponent() {
        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("模型提供方: "), providerCB, 1, false)
                .addLabeledComponent(new JBLabel("模型版本: "), modelCB, 1, false)
                .addLabeledComponent(new JBLabel("API Key: "), apiKeyTF, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public void initProvider(List<String> providerList, String providerSelected) {
        providerList.forEach(providerCB::addItem);

        if (StringUtils.isNotBlank(providerSelected)) {
            this.providerCB.setSelectedIndex(providerList.indexOf(providerSelected));
        }
    }

    public void initAndSelectModel(List<String> modelList, String model) {
        modelCB.removeAllItems();
        modelList.forEach(this.modelCB::addItem);
        if (StringUtils.isNotBlank(model)) {
            this.modelCB.setSelectedIndex(modelList.indexOf(model));
        }
    }

    public void setApiKey(String text) {
        apiKeyTF.setText(text);
    }

    public String getApiKey() {
        return apiKeyTF.getText();
    }
}
