package dk.youtec.remoteconfiginresources

import android.app.ListActivity
import android.content.res.Resources

open class RemoteConfigListActivity : ListActivity() {

    private var resources: Resources? = null

    override fun getResources(): Resources {
        if (resources == null) {
            resources = RemoteConfigResources(this, super.getResources())
        }
        return resources!!
    }
}
