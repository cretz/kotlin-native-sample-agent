#include <iostream>
#include <jvmti.h>

#include "sampleagent_api.h"

JNIEXPORT jint JNICALL Agent_OnLoad(JavaVM *vm, char *options, void *reserved) {
    return sampleagent_symbols()->kotlin.root.sampleagent.agentOnLoad(
        (uintptr_t) vm,
        (uintptr_t) options,
        (uintptr_t) reserved);
}