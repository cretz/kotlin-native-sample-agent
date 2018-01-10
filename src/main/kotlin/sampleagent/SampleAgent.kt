package sampleagent

import jvmti.*
import kotlinx.cinterop.*

fun test() {
    println("Inside of Kotlin test function")
}

// fun agentOnLoad(vm: CValuesRef<JavaVMVar>?, options: CValuesRef<ByteVar>?, reserved: CValuesRef<*>?): jint {
fun agentOnLoad(vm: CPointer<JavaVMVar>?, options: CPointer<ByteVar>?, reserved: COpaquePointer?): jint {
    println("Inside of agent onload in Kotlin")
    return 0
}