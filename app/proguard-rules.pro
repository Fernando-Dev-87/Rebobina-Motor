# Regras de Proteção e Ofuscação Rebobina motor

# Preserva informações de linha para relatórios de erro (opcional, mas útil)
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Proteção para Room Database
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public <init>(...);
}
-keep class androidx.room.BoundDb { *; }
-keep class * extends androidx.room.RoomDatabase

# Proteção para Modelos de Dados (Essencial para não quebrar a lógica do app)
-keep class com.example.domain.model.** { *; }
-keep class com.example.data.datasource.** { *; }

# Proteção para Moshi (JSON)
-keep class com.squareup.moshi.** { *; }
-keepclassmembers class * {
    @com.squareup.moshi.Json *;
}

# Proteção para Compose
-keep class androidx.compose.material.icons.** { *; }

# Ofuscação agressiva para o restante do código
-repackageclasses 'com.example.hidden'
-allowaccessmodification
-optimizationpasses 5
