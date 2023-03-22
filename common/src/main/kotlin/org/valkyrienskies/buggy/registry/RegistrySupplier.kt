package org.valkyrienskies.buggy.registry

interface RegistrySupplier<T> {

    val name: String
    fun get(): T

}