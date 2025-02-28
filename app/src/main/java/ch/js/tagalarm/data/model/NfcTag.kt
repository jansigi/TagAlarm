package ch.js.tagalarm.data.model

data class NfcTag(
    val serialNumber: String,
    val name: String = "NFC Tag",
) {
    override fun toString(): String = "$name ($serialNumber)"

}
