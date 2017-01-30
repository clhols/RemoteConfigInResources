# RemoteConfigInResources
This library makes your Android resources configurable in Firebase Remote Config.
 
For example if your existing code is calling
  
        getResources().getInteger(R.integer.the_answer)
        
this library enables your to set "the_answer" in Firebase Remote Config and the changed value will be reflected in the app.

String, integer, boolean and color resources are supported.

##Using

Add the following to your app/build.gradle dependencies:

        compile 'dk.youtec:remoteconfiginresources:0.0.1'

Then you can either extend one of the RemoteConfig activities:

        public class MainActivity extends RemoteConfigAppCompatActivity
        public class MainActivity extends RemoteConfigActivity
        
Or you can override the getResources method in your activity:

    private RemoteConfigResources mResources;
    
    @Override
    public Resources getResources() {
        if (mResources == null) {
            mResources = new RemoteConfigResources(this, super.getResources());
        }
        return mResources;
    }
    
Now when you call getResources in the activity, any request for string, int, bool or color will use values from Remote Config.

Remember that Firebase caches the remote config vaules, but you can change that as you normally would.

See: https://firebase.google.com/docs/remote-config/android
 
## Limitations
- Resources used in layouts will not get values from Remote Config.
- A color hex value (#FFFF4081) must be converted to an integer value (4294918273) before inserted into Remote Config.
- Only string, integer, boolean and color resources are supported.