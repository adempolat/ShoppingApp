package com.adempolat.eterationshoppingapp.usecases

import com.adempolat.eterationshoppingapp.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideLoadProductsUseCase(repository: ProductRepository): LoadProductsUseCase {
        return LoadProductsUseCase(repository)
    }

    @Provides
    fun provideFilterProductsUseCase(): FilterProductsUseCase {
        return FilterProductsUseCase()
    }

    @Provides
    fun provideSortProductsUseCase(): SortProductsUseCase {
        return SortProductsUseCase()
    }
}

