package io.trieulh.challenge.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.trieulh.challenge.ui.screen.orchard.update.OrchardUpdateUiStateManager
import io.trieulh.challenge.ui.screen.orchard.update.OrchardUpdateUiStateManagerImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class PresenterModule {

    @Binds
    abstract fun bindOrchardUpdateUiStateManager(orchardUpdateUiStateManager: OrchardUpdateUiStateManagerImpl): OrchardUpdateUiStateManager
}