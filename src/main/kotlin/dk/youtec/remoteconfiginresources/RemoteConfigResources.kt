package dk.youtec.remoteconfiginresources

import android.content.Context
import android.content.res.Resources
import android.support.annotation.*
import android.text.TextUtils
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import java.util.*

/**
 * Resources that overrides values by getting them from remote config.
 */
open class RemoteConfigResources(context: Context, val res: Resources) : Resources(res.assets, res.displayMetrics, res.configuration) {

    private val firebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        //Build defaults
        val defaults = HashMap<String, Any>()
        defaults.putAll(extractStrings(Class.forName(context.packageName + ".R\$string")))
        defaults.putAll(extractBools(Class.forName(context.packageName + ".R\$bool")))
        defaults.putAll(extractInts(Class.forName(context.packageName + ".R\$integer")))
        defaults.putAll(extractColors(Class.forName(context.packageName + ".R\$color")))
        firebaseRemoteConfig.setDefaults(defaults)

        fetch()
    }

    fun fetch(cacheExpirationSeconds: Long = 43200L) {
        // cacheExpirationSeconds is set to cacheExpiration here, indicating that any previously
        // fetched and cached config would be considered expired because it would have been fetched
        // more than cacheExpiration seconds ago. Thus the next fetch would go to the server unless
        // throttling is in progress. The default expiration duration is 43200 (12 hours).

        firebaseRemoteConfig.fetch(cacheExpirationSeconds)
                .addOnCompleteListener({ task ->
                    if (task.isSuccessful) {
                        firebaseRemoteConfig.activateFetched()
                    }
                })
    }

    @Throws(Resources.NotFoundException::class)
    override fun getText(@StringRes id: Int): CharSequence {
        val resourceEntryName = getResourceEntryName(id)

        val value = firebaseRemoteConfig.getString(resourceEntryName)
        return value
    }

    @Throws(Resources.NotFoundException::class)
    override fun getBoolean(@BoolRes id: Int): Boolean {
        val resourceEntryName = getResourceEntryName(id)

        val boolValue = firebaseRemoteConfig.getBoolean(resourceEntryName)
        return boolValue
    }

    @Throws(Resources.NotFoundException::class)
    override fun getColor(@ColorRes id: Int): Int {
        val resourceEntryName = getResourceEntryName(id)

        val value = firebaseRemoteConfig.getLong(resourceEntryName)
        return value.toInt()
    }

    @Throws(Resources.NotFoundException::class)
    override fun getInteger(@IntegerRes id: Int): Int {
        val resourceEntryName = getResourceEntryName(id)

        val value = firebaseRemoteConfig.getLong(resourceEntryName)
        return value.toInt()
    }

    protected fun extractStrings(classToInspect: Class<*>): Map<out String, Any> {
        val allFields = classToInspect.declaredFields
        val strings = HashMap<String, Any>(allFields.size)
        for (field in allFields) {
            val name = field.name
            if (!TextUtils.isEmpty(name)) {
                val id = field.get(null) as Number?
                if (id != null) {
                    try {
                        strings.put(name, res.getString(id.toInt()))
                    } catch (e: NotFoundException) {
                        Log.w(TAG, e.message, e)
                    }
                }
            }
        }
        return strings
    }

    fun extractBools(classToInspect: Class<*>): Map<out String, Boolean> {
        val allFields = classToInspect.declaredFields
        val map = HashMap<String, Boolean>(allFields.size)
        for (field in allFields) {
            val name = field.name
            if (!TextUtils.isEmpty(name)) {
                val id = field.get(null) as Number?
                if (id != null) {
                    try {
                        map.put(name, res.getBoolean(id.toInt()))
                    } catch (e: NotFoundException) {
                        Log.w(TAG, e.message, e)
                    }
                }
            }
        }
        return map
    }

    fun extractInts(classToInspect: Class<*>): Map<out String, Int> {
        val allFields = classToInspect.declaredFields
        val map = HashMap<String, Int>(allFields.size)
        for (field in allFields) {
            val name = field.name
            if (!TextUtils.isEmpty(name)) {
                val id = field.get(null) as Number?
                if (id != null) {
                    try {
                        map.put(name, res.getInteger(id.toInt()))
                    } catch (e: NotFoundException) {
                        Log.w(TAG, e.message, e)
                    }
                }
            }
        }
        return map
    }

    fun extractColors(classToInspect: Class<*>): Map<out String, Int> {
        val allFields = classToInspect.declaredFields
        val map = HashMap<String, Int>(allFields.size)
        for (field in allFields) {
            val name = field.name
            if (!TextUtils.isEmpty(name)) {
                val id = field.get(null) as Number?
                if (id != null) {
                    try {
                        map.put(name, res.getColor(id.toInt()))
                    } catch (e: NotFoundException) {
                        Log.w(TAG, e.message, e)
                    }
                }
            }
        }
        return map
    }

    companion object {
        private val TAG = RemoteConfigResources::class.java.simpleName
    }
}