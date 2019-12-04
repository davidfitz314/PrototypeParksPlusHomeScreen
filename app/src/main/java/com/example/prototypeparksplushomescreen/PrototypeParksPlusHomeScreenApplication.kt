package com.example.prototypeparksplushomescreen

import android.app.Application
import com.facebook.stetho.Stetho

class PrototypeParksPlusHomeScreenApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Setup Stetho
        Stetho.initialize(
            Stetho.newInitializerBuilder(this).enableDumpapp(Stetho.defaultDumperPluginsProvider(this)).enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)).build())

    }
}