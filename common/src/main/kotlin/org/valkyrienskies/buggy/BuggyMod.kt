package org.valkyrienskies.buggy

import org.valkyrienskies.core.impl.config.VSConfigClass


object BuggyMod {
    const val MOD_ID = "vs_buggy"

    @JvmStatic
    fun init() {
        BuggyBlocks.register()
        BuggyBlockEntities.register()
        BuggyItems.register()
        BuggyScreens.register()
        BuggyEntities.register()
        BuggyWeights.register()
        //BuggyCommands.register()
        VSConfigClass.registerConfig("vs_buggy", BuggyConfig::class.java)
    }

    @JvmStatic
    fun initClient() {
        BuggyClientScreens.register()
    }
}
