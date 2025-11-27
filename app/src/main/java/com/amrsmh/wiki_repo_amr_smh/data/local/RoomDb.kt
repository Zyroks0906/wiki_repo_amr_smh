package com.amrsmh.wiki_repo_amr_smh.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.amrsmh.wiki_repo_amr_smh.data.local.dao.LootDao
import com.amrsmh.wiki_repo_amr_smh.data.local.dao.MonsterDao
import com.amrsmh.wiki_repo_amr_smh.data.local.dao.ShopItemDao
import com.amrsmh.wiki_repo_amr_smh.data.local.entities.LootEntity
import com.amrsmh.wiki_repo_amr_smh.data.local.entities.MonsterEntity
import com.amrsmh.wiki_repo_amr_smh.data.local.entities.ShopItemEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        LootEntity::class,
        MonsterEntity::class,
        ShopItemEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class RoomDb : RoomDatabase() {
    abstract fun lootDao(): LootDao
    abstract fun monsterDao(): MonsterDao
    abstract fun shopItemDao(): ShopItemDao

    companion object {
        @Volatile private var INSTANCE: RoomDb? = null

        fun getInstance(context: Context): RoomDb {
            return INSTANCE ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDb::class.java,
                    "repo_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    populateDatabase(database)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = inst
                inst
            }
        }

        private suspend fun populateDatabase(db: RoomDb) {
            val lootDao = db.lootDao()
            val monsterDao = db.monsterDao()
            val shopDao = db.shopItemDao()

            // ============ DATOS DE BOTÍN (15 objetos) ============
            lootDao.insert(LootEntity(
                name = "Caja de Suministros Militar",
                location = "Genérico",
                value = 150,
                weight = "Normal",
                notes = "Contiene munición y vendajes básicos",
                createdAt = System.currentTimeMillis() - 1000000,
                isFavorite = false
            ))

            lootDao.insert(LootEntity(
                name = "Cristal Arcano Brillante",
                location = "Mágico",
                value = 520,
                weight = "Ligero",
                notes = "Emite un resplandor azulado. Alta energía mágica detectada",
                createdAt = System.currentTimeMillis() - 900000,
                isFavorite = true
            ))

            lootDao.insert(LootEntity(
                name = "Retrato del Conde",
                location = "Mansión",
                value = 340,
                weight = "Normal",
                notes = "Pintura al óleo del siglo XVIII. Muy valioso para coleccionistas",
                createdAt = System.currentTimeMillis() - 800000,
                isFavorite = false
            ))

            lootDao.insert(LootEntity(
                name = "Equipo de Supervivencia Ártico",
                location = "Ártico",
                value = 280,
                weight = "Pesado",
                notes = "Incluye ropa térmica, piolet y cuerdas de escalada",
                createdAt = System.currentTimeMillis() - 700000,
                isFavorite = true
            ))

            lootDao.insert(LootEntity(
                name = "Vaso Canopo Egipcio",
                location = "Museo",
                value = 680,
                weight = "Normal",
                notes = "Artefacto de la dinastía XVIII. Extremadamente valioso",
                createdAt = System.currentTimeMillis() - 600000,
                isFavorite = true
            ))

            lootDao.insert(LootEntity(
                name = "Botiquín de Emergencia",
                location = "Genérico",
                value = 95,
                weight = "Ligero",
                notes = "Vendajes, antibióticos y analgésicos",
                createdAt = System.currentTimeMillis() - 500000,
                isFavorite = false
            ))

            lootDao.insert(LootEntity(
                name = "Tomo de Hechizos Antiguos",
                location = "Mágico",
                value = 780,
                weight = "Normal",
                notes = "Libro encuadernado en cuero. Contiene rituales olvidados",
                createdAt = System.currentTimeMillis() - 400000,
                isFavorite = true
            ))

            lootDao.insert(LootEntity(
                name = "Candelabro de Plata",
                location = "Mansión",
                value = 420,
                weight = "Pesado",
                notes = "Plata maciza del siglo XIX. Excelente estado",
                createdAt = System.currentTimeMillis() - 300000,
                isFavorite = false
            ))

            lootDao.insert(LootEntity(
                name = "Brújula de Explorador",
                location = "Ártico",
                value = 110,
                weight = "Ligero",
                notes = "Brújula de precisión. Funcional",
                createdAt = System.currentTimeMillis() - 200000,
                isFavorite = false
            ))

            lootDao.insert(LootEntity(
                name = "Sarcófago Miniatura",
                location = "Museo",
                value = 890,
                weight = "Muy Pesado",
                notes = "Réplica detallada en oro. Posiblemente ceremonial",
                createdAt = System.currentTimeMillis() - 100000,
                isFavorite = true
            ))

            lootDao.insert(LootEntity(
                name = "Munición Calibre .45",
                location = "Genérico",
                value = 60,
                weight = "Normal",
                notes = "Caja de 50 balas. Buen estado",
                createdAt = System.currentTimeMillis() - 90000,
                isFavorite = false
            ))

            lootDao.insert(LootEntity(
                name = "Orbe de Poder",
                location = "Mágico",
                value = 950,
                weight = "Ligero",
                notes = "Esfera de cristal con energía pulsante en su interior",
                createdAt = System.currentTimeMillis() - 80000,
                isFavorite = true
            ))

            lootDao.insert(LootEntity(
                name = "Juego de Té de Porcelana",
                location = "Mansión",
                value = 260,
                weight = "Normal",
                notes = "Porcelana china. Set completo para 6 personas",
                createdAt = System.currentTimeMillis() - 70000,
                isFavorite = false
            ))

            lootDao.insert(LootEntity(
                name = "Diario del Explorador",
                location = "Ártico",
                value = 180,
                weight = "Ligero",
                notes = "Contiene mapas y rutas de expediciones anteriores",
                createdAt = System.currentTimeMillis() - 60000,
                isFavorite = false
            ))

            lootDao.insert(LootEntity(
                name = "Máscara Funeraria de Oro",
                location = "Museo",
                value = 1200,
                weight = "Pesado",
                notes = "Oro de 18 quilates. Pieza extremadamente rara",
                createdAt = System.currentTimeMillis() - 50000,
                isFavorite = true
            ))

            // ============ DATOS DE MONSTRUOS (12 monstruos) ============
            monsterDao.insert(MonsterEntity(
                name = "Rondador",
                danger = 1,
                detection = "Audio",
                notes = "Lento pero resistente. Mantener distancia y evitar ruidos fuertes",
                createdAt = System.currentTimeMillis() - 1000000,
                isFavorite = false
            ))

            monsterDao.insert(MonsterEntity(
                name = "Acechador de Sombras",
                danger = 2,
                detection = "Visión",
                notes = "Se mueve en grupos de 2-3. Vulnerable a la luz directa. Evitar zonas oscuras",
                createdAt = System.currentTimeMillis() - 900000,
                isFavorite = false
            ))

            monsterDao.insert(MonsterEntity(
                name = "Gritón",
                danger = 3,
                detection = "Audio",
                notes = "EXTREMADAMENTE PELIGROSO. Su grito alerta a todos los enemigos cercanos. Eliminar con prioridad máxima",
                createdAt = System.currentTimeMillis() - 800000,
                isFavorite = true
            ))

            monsterDao.insert(MonsterEntity(
                name = "Espectro de Hielo",
                danger = 2,
                detection = "Visión",
                notes = "Habita zonas árticas. Vulnerable al fuego. Se camufla con nieve",
                createdAt = System.currentTimeMillis() - 700000,
                isFavorite = true
            ))

            monsterDao.insert(MonsterEntity(
                name = "Guardián de la Mansión",
                danger = 2,
                detection = "Audio",
                notes = "Patrulla los pasillos. Ruta predecible. Se puede evadir fácilmente",
                createdAt = System.currentTimeMillis() - 600000,
                isFavorite = false
            ))

            monsterDao.insert(MonsterEntity(
                name = "Devorador",
                danger = 3,
                detection = "Audio",
                notes = "Altamente agresivo. Ataca en cuanto detecta presencia. No intentar enfrentamiento directo",
                createdAt = System.currentTimeMillis() - 500000,
                isFavorite = true
            ))

            monsterDao.insert(MonsterEntity(
                name = "Rastrero",
                danger = 1,
                detection = "Visión",
                notes = "Se arrastra por el suelo. Lento y fácil de evadir. Poca amenaza",
                createdAt = System.currentTimeMillis() - 400000,
                isFavorite = false
            ))

            monsterDao.insert(MonsterEntity(
                name = "Hechicero Oscuro",
                danger = 3,
                detection = "Visión",
                notes = "Lanza proyectiles mágicos. Mantener cobertura. Requiere estrategia de equipo",
                createdAt = System.currentTimeMillis() - 300000,
                isFavorite = true
            ))

            monsterDao.insert(MonsterEntity(
                name = "Momia Guardiana",
                danger = 2,
                detection = "Audio",
                notes = "Protege artefactos del museo. Movimiento errático. Usar señuelos sonoros",
                createdAt = System.currentTimeMillis() - 200000,
                isFavorite = false
            ))

            monsterDao.insert(MonsterEntity(
                name = "Centinela",
                danger = 1,
                detection = "Visión",
                notes = "Vigilancia estática. Campo de visión limitado. Fácil de evitar",
                createdAt = System.currentTimeMillis() - 100000,
                isFavorite = false
            ))

            monsterDao.insert(MonsterEntity(
                name = "Bestia Polar",
                danger = 3,
                detection = "Audio",
                notes = "Depredador del ártico. Muy rápido. Huir es la mejor opción. No puede entrar en edificios",
                createdAt = System.currentTimeMillis() - 90000,
                isFavorite = true
            ))

            monsterDao.insert(MonsterEntity(
                name = "Aparición Espectral",
                danger = 2,
                detection = "Visión",
                notes = "Fantasma de la mansión. Atraviesa paredes. Vulnerable a luz ultravioleta",
                createdAt = System.currentTimeMillis() - 80000,
                isFavorite = false
            ))

            // ============ DATOS DE TIENDA (20 artículos) ============
            // ARMAS
            shopDao.insert(ShopItemEntity(
                name = "Rifle de Asalto Mk-II",
                price = 850,
                description = "Arma automática de alta precisión. 30 balas por cargador. Cadencia de 700 RPM",
                category = "Arma",
                createdAt = System.currentTimeMillis() - 1000000,
                isFavorite = true
            ))

            shopDao.insert(ShopItemEntity(
                name = "Escopeta de Combate",
                price = 620,
                description = "Gran poder de parada a corta distancia. 8 cartuchos. Ideal para espacios cerrados",
                category = "Arma",
                createdAt = System.currentTimeMillis() - 950000,
                isFavorite = false
            ))

            shopDao.insert(ShopItemEntity(
                name = "Pistola Semiautomática",
                price = 320,
                description = "Arma de apoyo confiable. 15 balas. Compacta y ligera",
                category = "Arma",
                createdAt = System.currentTimeMillis() - 900000,
                isFavorite = false
            ))

            shopDao.insert(ShopItemEntity(
                name = "Rifle de Francotirador",
                price = 1200,
                description = "Precisión extrema a larga distancia. Mira telescópica 8x. Silenciador integrado",
                category = "Arma",
                createdAt = System.currentTimeMillis() - 850000,
                isFavorite = true
            ))

            shopDao.insert(ShopItemEntity(
                name = "Granada de Fragmentación",
                price = 300,
                description = "Explosivo de área. Radio de 5 metros. Usar con extrema precaución",
                category = "Arma",
                createdAt = System.currentTimeMillis() - 800000,
                isFavorite = false
            ))

            shopDao.insert(ShopItemEntity(
                name = "Lanzallamas Portátil",
                price = 980,
                description = "Arma incendiaria. Efectivo contra grupos. Combustible para 30 segundos",
                category = "Arma",
                createdAt = System.currentTimeMillis() - 750000,
                isFavorite = true
            ))

            shopDao.insert(ShopItemEntity(
                name = "Ballesta Táctica",
                price = 450,
                description = "Silenciosa y letal. 12 virotes. Recargable",
                category = "Arma",
                createdAt = System.currentTimeMillis() - 700000,
                isFavorite = false
            ))

            // CONSUMIBLES
            shopDao.insert(ShopItemEntity(
                name = "Kit Médico Avanzado",
                price = 200,
                description = "Cura heridas graves. Restaura 80% de salud. Incluye antibióticos y morfina",
                category = "Consumible",
                createdAt = System.currentTimeMillis() - 650000,
                isFavorite = false
            ))

            shopDao.insert(ShopItemEntity(
                name = "Vendaje de Combate",
                price = 45,
                description = "Cura heridas leves. Detiene sangrado. Uso rápido",
                category = "Consumible",
                createdAt = System.currentTimeMillis() - 600000,
                isFavorite = false
            ))

            shopDao.insert(ShopItemEntity(
                name = "Raciones de Emergencia",
                price = 50,
                description = "Comida concentrada de alto valor nutricional. Restaura stamina",
                category = "Consumible",
                createdAt = System.currentTimeMillis() - 550000,
                isFavorite = false
            ))

            shopDao.insert(ShopItemEntity(
                name = "Estimulante de Adrenalina",
                price = 180,
                description = "Aumenta velocidad y reflejos temporalmente. Duración: 60 segundos",
                category = "Consumible",
                createdAt = System.currentTimeMillis() - 500000,
                isFavorite = true
            ))

            shopDao.insert(ShopItemEntity(
                name = "Antídoto Universal",
                price = 120,
                description = "Neutraliza venenos y toxinas. Cura envenenamiento",
                category = "Consumible",
                createdAt = System.currentTimeMillis() - 450000,
                isFavorite = false
            ))

            shopDao.insert(ShopItemEntity(
                name = "Bebida Energética Militar",
                price = 35,
                description = "Restaura stamina rápidamente. Reduce fatiga",
                category = "Consumible",
                createdAt = System.currentTimeMillis() - 400000,
                isFavorite = false
            ))

            // UTILIDAD
            shopDao.insert(ShopItemEntity(
                name = "Linterna Táctica",
                price = 120,
                description = "Iluminación potente. Batería de larga duración. Modo estroboscópico incluido",
                category = "Utilidad",
                createdAt = System.currentTimeMillis() - 350000,
                isFavorite = false
            ))

            shopDao.insert(ShopItemEntity(
                name = "Detector de Movimiento",
                price = 280,
                description = "Detecta enemigos en un radio de 20 metros. Alerta sonora y visual",
                category = "Utilidad",
                createdAt = System.currentTimeMillis() - 300000,
                isFavorite = true
            ))

            shopDao.insert(ShopItemEntity(
                name = "Kit de Lockpicking",
                price = 150,
                description = "Abre cerraduras mecánicas. 15 ganzúas de diferentes tipos",
                category = "Utilidad",
                createdAt = System.currentTimeMillis() - 250000,
                isFavorite = false
            ))

            shopDao.insert(ShopItemEntity(
                name = "Chaleco Antibalas Nivel III",
                price = 450,
                description = "Protección balística. Reduce daño por proyectiles en 60%",
                category = "Utilidad",
                createdAt = System.currentTimeMillis() - 200000,
                isFavorite = true
            ))

            shopDao.insert(ShopItemEntity(
                name = "Radio de Comunicación",
                price = 180,
                description = "Comunicación con el equipo. Alcance de 5 km. Encriptación básica",
                category = "Utilidad",
                createdAt = System.currentTimeMillis() - 150000,
                isFavorite = false
            ))

            shopDao.insert(ShopItemEntity(
                name = "Visión Nocturna",
                price = 520,
                description = "Permite ver en la oscuridad total. Autonomía de 8 horas",
                category = "Utilidad",
                createdAt = System.currentTimeMillis() - 100000,
                isFavorite = true
            ))

            shopDao.insert(ShopItemEntity(
                name = "Cuerda de Escalada",
                price = 85,
                description = "50 metros de cuerda resistente. Soporta hasta 200kg. Incluye mosquetones",
                category = "Utilidad",
                createdAt = System.currentTimeMillis() - 50000,
                isFavorite = false
            ))

            shopDao.insert(ShopItemEntity(
                name = "EMP Portátila",
                price = 680,
                description = "Desactiva dispositivos electrónicos. Radio de 10 metros. Un solo uso",
                category = "Utilidad",
                createdAt = System.currentTimeMillis() - 25000,
                isFavorite = true
            ))
        }
    }
}
