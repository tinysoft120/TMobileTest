package com.tinysoft.tmobiletest

import androidx.room.Room
import com.tinysoft.tmobiletest.db.CardDatabase
import com.tinysoft.tmobiletest.network.provideOkHttp
import com.tinysoft.tmobiletest.network.provideSearchApiService
import com.tinysoft.tmobiletest.repository.Repository
import com.tinysoft.tmobiletest.repository.RepositoryImpl
import com.tinysoft.tmobiletest.ui.homelist.HomeListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    factory {
        provideOkHttp()
    }
    single {
        provideSearchApiService(get())
    }
}

private var dataModule = module {
    factory {
        get<CardDatabase>().cardDao()
    }

    single {
        Room.databaseBuilder(androidContext(), CardDatabase::class.java, "card.db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        RepositoryImpl(get(), get())
    } bind Repository::class
}

private var viewModules = module {
    viewModel {
        HomeListViewModel(get())
    }
}

val appModules = listOf(networkModule, dataModule, viewModules)