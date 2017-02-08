package dk.youtec.remoteconfiginresources

import android.content.res.Resources
import android.support.v4.app.FragmentActivity

open class RemoteConfigFragmentActivity : FragmentActivity() {

    private var resources: Resources? = null

    override fun getResources(): Resources {
        if (resources == null) {
            resources = RemoteConfigResources(this, super.getResources())
        }
        return resources!!
    }
}
