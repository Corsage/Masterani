package edu.jc.corsage.masterani.Sayonara.Collection

/**
 * TODO: Extensively test so I can get all the provider types...
 */

enum class Provider(val id: Int) {
    MASTERANI(0),
    MP4UPLOAD(1),
    GDRIVE(12),
    OPENLOAD(15),
    STREAMANGO(18),
    STREAMMOE(19);

    companion object {
        fun getId(provider: Provider): Int {
            return provider.id
        }
    }
}