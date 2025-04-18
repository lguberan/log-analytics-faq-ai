import jpype
import jpype.imports
import os
from jpype.types import *

jar_path = "lib/h2-2.3.232.jar"

print(f"🔍 JAR exists: {os.path.isfile(jar_path)}")

# Démarre la JVM avec le JAR en classpath
jpype.startJVM(classpath=[jar_path])

try:
    Driver = jpype.JClass("org.h2.Driver")
    print("✅ org.h2.Driver loaded successfully.")
except Exception as e:
    print("❌ Error loading org.h2.Driver:", e)

jpype.shutdownJVM()
