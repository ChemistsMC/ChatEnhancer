package com.chemistsmc.chatenhancer

/**
 * Class to manage our [ChatModule]'s.
 */
class ModuleManager(private val plugin: ChatEnhancer) {

    private val chatModules = mutableSetOf<ChatModule>()

    /**
     * Load a chat module for use.
     *
     * @param module The module to load
     */
    fun loadModule(module: ChatModule) {
        chatModules.add(module)
        plugin.logger.info("Added module '${module.javaClass.simpleName}'")
    }

    /**
     * Get all of the currently loaded modules.
     *
     * @return A Set of the loaded modules
     */
    fun getModules(): Set<ChatModule> = chatModules
}
