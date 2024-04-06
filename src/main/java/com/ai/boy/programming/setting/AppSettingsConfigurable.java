// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.ai.boy.programming.setting;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Provides controller functionality for application settings.
 */
final class AppSettingsConfigurable implements Configurable {

  private AppSettingsComponent settingsComponent;

  // A default constructor with no arguments is required because this implementation
  // is registered in an applicationConfigurable EP

  @Nls(capitalization = Nls.Capitalization.Title)
  @Override
  public String getDisplayName() {
    return "Programming Boy";
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return settingsComponent.getPreferredFocusedComponent();
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    settingsComponent = new AppSettingsComponent();
    return settingsComponent.getPanel();
  }

  @Override
  public boolean isModified() {
    AppSettingsState settings = AppSettingsState.getInstance();
      return !settingsComponent.getMoonshotApiKey().equals(settings.moonshotApiKey);
  }

  @Override
  public void apply() {
    AppSettingsState settings = AppSettingsState.getInstance();
    settings.moonshotApiKey = settingsComponent.getMoonshotApiKey();
  }

  @Override
  public void reset() {
    AppSettingsState settings = AppSettingsState.getInstance();
    settingsComponent.setMoonshotApiKey(settings.moonshotApiKey);
  }

  @Override
  public void disposeUIResources() {
    settingsComponent = null;
  }

}
