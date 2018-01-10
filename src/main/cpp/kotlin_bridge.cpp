#include <iostream>
#include <jvmti.h>

#include "sampleagent_api.h"

JNIEXPORT jint JNICALL Agent_OnLoad(JavaVM *vm, char *options, void *reserved) {
    std::cout << "Begin onload" << std::endl;
    std::cout << "Creating symbols" << std::endl;
    sampleagent_ExportedSymbols* temp = sampleagent_symbols();
    std::cout << "Created symbols" << std::endl;
    temp->kotlin.root.sampleagent.test();
    std::cout << "Ran function" << std::endl;
    return 0;
    
    // return sampleagent_symbols()->kotlin.root.sampleagent.agentOnLoad(
    //     (sampleagent_kref_kotlinx_cinterop_CPointer) { vm },
    //     (sampleagent_kref_kotlinx_cinterop_CPointer) { options },
    //     (sampleagent_kref_kotlinx_cinterop_CPointer) { reserved });
}