package sampleagent

import jvmti.*
import kotlinx.cinterop.*

@Suppress("UNUSED_PARAMETER")
fun agentOnLoad(vmPtr: Long, optionsPtr: Long, reservedPtr: Long): jint =
    printSystemPropertyNames(vmPtr)?.let {
        print("Error: $it")
        JNI_ERR
    } ?: JNI_OK

fun printSystemPropertyNames(vmPtr: Long): String? = memScoped {
    val vm = vmPtr.toCPointer<JavaVMVar>()?.pointed ?: return "No VM found"
    val vmFns = vm.value?.pointed ?: return "No VM functions found"
    val getEnvFn = vmFns.GetEnv ?: return "No GetEnv function found"

    val jvmtiEnvPtr = alloc<CPointerVar<jvmtiEnvVar>>()
    val envResult = getEnvFn(vm.ptr, jvmtiEnvPtr.ptr.reinterpret(), JVMTI_VERSION)
    if (envResult != JNI_OK) return "GetEnv failed with code $envResult"
    
    val jvmtiFns = jvmtiEnvPtr.pointed?.value?.pointed ?: return "No JVMTI functions found"
    val deallocFn = jvmtiFns.Deallocate ?: return "No Deallocate function found"
    val propsFn = jvmtiFns.GetSystemProperties ?: return "No GetSystemProperties function found"

    val count = alloc<jintVar>()
    val propNames = alloc<CPointerVar<CPointerVar<ByteVar>>>()
    val propErr = propsFn(jvmtiEnvPtr.value, count.ptr, propNames.ptr)
    if (propErr != JVMTI_ERROR_NONE) return "GetSystemProperties failed with code $propErr"
    try {
        println("Displaying ${count.value} property names:")
        for (i in 0 until count.value) {
            // TODO: yeah, yeah, this doesn't follow JNI's modified UTF-8...I'm lazy for now
            println(propNames.value?.get(i)?.toKString() ?: return "Failed getting property $i")
        }
    } finally {
        // Gotta dealloc the props array
        val deallocErr = deallocFn(jvmtiEnvPtr.value, propNames.value?.reinterpret())
        if (deallocErr != JVMTI_ERROR_NONE) return "Deallocate failed with code $deallocErr"
    }
    null
}
