package dk.youtec.remoteconfiginresources

import android.content.res.Resources
import android.support.v7.app.AppCompatActivity

open class RemoteConfigAppCompatActivity : AppCompatActivity() {

    private var resources: Resources? = null

    override fun getResources(): Resources {
        if (resources == null) {
            resources = RemoteConfigResources(this, super.getResources())
        }
        return resources!!
    }
}
