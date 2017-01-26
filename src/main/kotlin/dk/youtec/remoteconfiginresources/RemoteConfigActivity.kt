package dk.youtec.remoteconfiginresources

import android.app.Activity
import android.content.res.Resources

open class RemoteConfigActivity : Activity() {

    private var resources: Resources? = null

    override fun getResources(): Resources {
        if (resources == null) {
            resources = RemoteConfigResources(this, super.getResources())
        }
        return resources!!
    }
}
