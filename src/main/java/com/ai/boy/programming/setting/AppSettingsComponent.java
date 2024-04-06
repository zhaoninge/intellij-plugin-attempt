// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.ai.boy.programming.setting;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class AppSettingsComponent {

  private final JPanel mainPanel;
  private final JBTextField moonshotApiKey = new JBTextField();

  public AppSettingsComponent() {
    mainPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent(new JBLabel("Moonshot API Key: "), moonshotApiKey, 1, false)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();
  }

  public JPanel getPanel() {
    return mainPanel;
  }

  public JComponent getPreferredFocusedComponent() {
    return moonshotApiKey;
  }

  @NotNull
  public String getMoonshotApiKey() {
    return moonshotApiKey.getText();
  }

  public void setMoonshotApiKey(@NotNull String newText) {
    moonshotApiKey.setText(newText);
  }


}
