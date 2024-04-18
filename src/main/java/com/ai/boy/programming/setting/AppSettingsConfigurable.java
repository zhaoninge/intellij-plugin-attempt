// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.ai.boy.programming.setting;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static com.ai.boy.programming.factory.ModelFactory.getModelList;
import static com.ai.boy.programming.factory.ModelFactory.getProviderList;

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
        return settingsComponent.getProviderCB();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        settingsComponent = new AppSettingsComponent();
        return settingsComponent.getMainPanel();
    }

    @Override
    public boolean isModified() {
        AppSettingsState settings = AppSettingsState.getInstance();
        String selectedProvider = getSelectedProvider();
        return !selectedProvider.equals(settings.getProvider())
                || !getSelectedModel().equals(settings.getModel(selectedProvider))
                || !settingsComponent.getApiKey().equals(settings.getApiKey(selectedProvider));
    }

    @Override
    public void apply() {
        String selectedProvider = getSelectedProvider();
        String selectedmodel = getModelList(selectedProvider).get(settingsComponent.getModelCB().getSelectedIndex());

        AppSettingsState settingsState = AppSettingsState.getInstance();
        settingsState.setProvider(selectedProvider);
        settingsState.setModel(selectedProvider, selectedmodel);
        settingsState.setApiKey(selectedProvider, settingsComponent.getApiKey());
    }

    @Override
    public void reset() {
        AppSettingsState settings = AppSettingsState.getInstance();
        settingsComponent.initProvider(getProviderList(), settings.getProvider());
        initModelAndApiKey();
        addProviderCBListener();
    }

    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }

    private String getSelectedProvider() {
        return getProviderList().get(settingsComponent.getProviderCB().getSelectedIndex());
    }

    private String getSelectedModel() {
        String selectedProvider = getSelectedProvider();
        return getSelectedModel(selectedProvider);
    }

    private String getSelectedModel(String selectedProvider) {
        return getModelList(selectedProvider).get(settingsComponent.getModelCB().getSelectedIndex());
    }

    private void addProviderCBListener() {
        settingsComponent.getProviderCB().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                initModelAndApiKey();
            }
        });
    }

    private void initModelAndApiKey() {
        // 包含了默认选择的逻辑
        AppSettingsState settings = AppSettingsState.getInstance();
        String selectedProvider = getSelectedProvider();
        settingsComponent.initAndSelectModel(getModelList(selectedProvider), settings.getModel(selectedProvider));
        settingsComponent.setApiKey(settings.getApiKey(selectedProvider));
    }
}
