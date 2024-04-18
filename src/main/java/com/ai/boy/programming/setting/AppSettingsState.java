// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.ai.boy.programming.setting;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "com.ai.boy.programming.setting.AppSettingsState",
        storages = @Storage("programmingBoy.setting.xml")
)
@Service
@Data
public final class AppSettingsState implements PersistentStateComponent<AppSettingsState> {

    private String provider;
    private Map<String, String> modelMap;

    public static AppSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(AppSettingsState.class);
    }

    public void setModel(String provider, String model) {
        if (null == this.modelMap) {
            this.modelMap = new HashMap<>();
        }
        modelMap.put(provider, model);
    }

    public String getModel(String provider) {
        return null != modelMap ? modelMap.get(provider) : null;
    }

    public String getSavedModel() {
        return getModel(this.provider);
    }

    public void setApiKey(String provider, String apiKey) {
        saveCredential(provider, apiKey);
    }

    public String getApiKey(String provider) {
        return getCredential(provider);
    }

    public String getSavedApiKey() {
        return getApiKey(this.provider);
    }

    @Override
    public @NotNull AppSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AppSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    // region 敏感数据处理
    private static CredentialAttributes createCredentialAttributes(String key) {
        return new CredentialAttributes(CredentialAttributesKt.generateServiceName("ProgrammingBoy", key));
    }

    private void saveCredential(String key, String password) {
        CredentialAttributes attributes = createCredentialAttributes(key);
        Credentials credentials = new Credentials("username", password);
        PasswordSafe.getInstance().set(attributes, credentials);
    }

    private String getCredential(String key) {
        CredentialAttributes attributes = createCredentialAttributes(key);
        return PasswordSafe.getInstance().getPassword(attributes);
    }
    // endregion
}
